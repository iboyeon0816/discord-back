package np2024.discord.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import np2024.discord.domain.Channel;
import np2024.discord.domain.User;
import np2024.discord.dto.MessageResponseDto;
import np2024.discord.dto.enums.EventType;
import np2024.discord.dto.enums.MessageType;
import np2024.discord.repository.ChannelRepository;
import np2024.discord.repository.SessionRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static np2024.discord.dto.UserResponseDto.ConnectionResultDto;

@Slf4j
@Lazy
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

    private final SessionRepository sessionRepository;
    private final ChannelRepository channelRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();

        if (StompCommand.CONNECT.equals(command)) {
            String username = accessor.getFirstNativeHeader("username");
//            printLog(command, username);

            validateNotDuplicated(username);
            sessionRepository.save(sessionId, username);

            sendConnectMessage(username, EventType.CONNECT);
        }
        else if (StompCommand.DISCONNECT.equals(command)) {
            User user = sessionRepository.delete(sessionId);
//            printLog(command, user.getName());

//            if (user.getChannel() != null) {
//                Long channelId = user.getChannel().getId();
//                sendSubscribeMessage(user.getName(), MessageType.UNSUBSCRIBE, "/topic/channels/" + channelId);
//            }

            sendConnectMessage(user.getName(), EventType.DISCONNECT);
        }
//        else if (StompCommand.SUBSCRIBE.equals(command)) {
//            User user = sessionRepository.findById(sessionId);
//            log.info("[{}] {} - {}", command, destination, user.getName());

//            if (destination != null && destination.startsWith("/topic/channels")) {
//                Channel channel = getChannel(destination);
//                user.setChannel(channel);
//                sendSubscribeMessage(user.getName(), MessageType.SUBSCRIBE, destination);
//            }
//        }
//        else if (StompCommand.UNSUBSCRIBE.equals(command)) {
//            User user = sessionRepository.findById(sessionId);
//            printLog(command, user.getName());

//            if (destination != null && destination.startsWith("/topic/channels")) {
//                user.setChannel(null);
//                sendSubscribeMessage(user.getName(), MessageType.UNSUBSCRIBE, destination);
//            }
//        }
        return message;
    }

    private void validateNotDuplicated(String username) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("헤더 값이 전달되지 않았습니다. (username)");
        }
        if (sessionRepository.exists(username)) {
            throw new IllegalArgumentException("중복된 사용자 이름입니다.");
        }
    }

    private void sendConnectMessage(String username, EventType eventType) {
        ConnectionResultDto responseDto = new ConnectionResultDto(username, eventType);
        messagingTemplate.convertAndSend("/topic/users", responseDto);
    }

    private void sendSubscribeMessage(String username, MessageType messageType, String destination) {
        MessageResponseDto responseDto = new MessageResponseDto(username, null, messageType);
        messagingTemplate.convertAndSend(destination, responseDto);
    }

    private Channel getChannel(String destination) {
        Long channelId = Long.parseLong(destination.substring("topic/channels/".length()));
        Channel channel = channelRepository.findById(channelId);

        if (channel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널 ID 입니다.");
        }

        return channel;
    }

    private static void printLog(StompCommand command, String username) {
        log.info("[{}] {}", command, username);
    }
}
