package np2024.discord.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import np2024.discord.dto.enums.EventType;

public class VideoRequestDto {
    @Getter
    public static class JoinDto {
        @NotBlank
        private String username;

        private EventType eventType;
    }

}
