package np2024.discord.interceptor;

import lombok.RequiredArgsConstructor;
import np2024.discord.domain.User;
import np2024.discord.dto.enums.EventType;
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

@Lazy
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

    private final SessionRepository sessionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();

        if (StompCommand.CONNECT.equals(command)) {
            String username = accessor.getFirstNativeHeader("username");
            validateNotDuplicated(username);

            sessionRepository.save(sessionId, username);
            sendConnectMessage(username, EventType.CONNECT);
        }
        else if (StompCommand.DISCONNECT.equals(command)) {
            User user = sessionRepository.delete(sessionId);
            sendConnectMessage(user.getName(), EventType.DISCONNECT);
        }
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

}
