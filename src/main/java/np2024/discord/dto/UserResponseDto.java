package np2024.discord.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import np2024.discord.dto.enums.EventType;

public class UserResponseDto {

    @Getter
    @AllArgsConstructor
    public static class ConnectionResultDto {
        private String username;
        private EventType eventType;
    }

    @Getter
    @AllArgsConstructor
    public static class GetResultDto {
        private String username;
    }
}
