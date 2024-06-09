package np2024.discord.controller;

import jakarta.validation.Valid;
import np2024.discord.domain.TextChatMessage;
import np2024.discord.validation.ExistChannel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Validated
@Controller
public class TextChatController {

    @MessageMapping("/channels/{channelId}/text/messages")
    @SendTo("/topic/channels/{channelId}/text")
    public TextChatMessage deliverMessage(@DestinationVariable @ExistChannel Long channelId,
                                          @Payload @Valid TextChatMessage message) {
        return message;
    }
}
