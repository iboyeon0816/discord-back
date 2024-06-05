package np2024.discord.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import np2024.discord.dto.enums.MessageType;

@Getter
@AllArgsConstructor
public class MessageResponseDto {
    private String username;
    private String content;
    private MessageType messageType;
}
