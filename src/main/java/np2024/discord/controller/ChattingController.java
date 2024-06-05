package np2024.discord.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import np2024.discord.domain.TextChatMessage;
import np2024.discord.dto.VideoRequestDto.JoinDto;
import np2024.discord.validation.ExistChannel;
import org.json.simple.JSONObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class ChattingController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/channels/{channelId}/text/messages")
    @SendTo("/topic/channels/{channelId}/text")
    public TextChatMessage deliverMessage(@DestinationVariable @ExistChannel Long channelId,
                                          @Payload @Valid TextChatMessage message) {
//        MessageResponseDto responseDto = new MessageResponseDto(message.getUsername(), message.getContent(), MessageType.CHAT);
//        messagingTemplate.convertAndSend("/topic/channels/" + channelId, responseDto);
//        log.info("[TextChat-Message] channelId: {}, sender: {}", channelId, message.getUsername());
        return message;
    }

    @MessageMapping("/channels/{channelId}/video/conn")
    @SendTo("/topic/channels/{channelId}/video")
    public JoinDto joinVideoChannel(@DestinationVariable Long channelId,
                                    @Payload @Valid JoinDto request) {
//        log.info("[VideoChat-Conn] channelId: {}, sender: {}", channelId, request.getUsername());
        return request;
    }

    @MessageMapping("/offer")
    public void deliverOffer(@Payload JSONObject ob) {
//        log.info("[VideoChat-Offer]: {} -> {}", ob.get("sender"), ob.get("receiver"));
        messagingTemplate.convertAndSend("/topic/offer/" + ob.get("receiver"), ob);
    }

    @MessageMapping("/answer")
    public void deliverAnswer(@Payload JSONObject ob) {
//        log.info("[VideoChat-Answer]: {} -> {}", ob.get("sender"), ob.get("receiver"));
        messagingTemplate.convertAndSend("/topic/answer/" + ob.get("receiver"), ob);
    }

    @MessageMapping("/candidate")
    public void deliverCandidate(@Payload JSONObject ob) {
//        log.info("[VideoChat-Candidate]: {} -> {}", ob.get("sender"), ob.get("receiver"));
        messagingTemplate.convertAndSend("/topic/candidate/" + ob.get("receiver"), ob);
    }
}
