package np2024.discord.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ChannelResponseDto {

    @Getter
    @AllArgsConstructor
    public static class CreateResultDto {
        private Long channelId;
    }

    @Getter
    @AllArgsConstructor
    public static class GetResultDto {
        private Long channelId;
        private String channelName;
        private String creatorName;
    }
}
