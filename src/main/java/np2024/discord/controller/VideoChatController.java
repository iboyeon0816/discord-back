package np2024.discord.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import np2024.discord.dto.VideoRequestDto.JoinDto;
import org.json.simple.JSONObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Validated
@Controller
@RequiredArgsConstructor
public class VideoChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/channels/{channelId}/video/conn")
    @SendTo("/topic/channels/{channelId}/video")
    public JoinDto joinVideoChannel(@DestinationVariable Long channelId,
                                    @Payload @Valid JoinDto request) {
        return request;
    }

    @MessageMapping("/offer")
    public void deliverOffer(@Payload JSONObject ob) {
        messagingTemplate.convertAndSend("/topic/offer/" + ob.get("receiver"), ob);
    }

    @MessageMapping("/answer")
    public void deliverAnswer(@Payload JSONObject ob) {
        messagingTemplate.convertAndSend("/topic/answer/" + ob.get("receiver"), ob);
    }

    @MessageMapping("/candidate")
    public void deliverCandidate(@Payload JSONObject ob) {
        messagingTemplate.convertAndSend("/topic/candidate/" + ob.get("receiver"), ob);
    }
}
