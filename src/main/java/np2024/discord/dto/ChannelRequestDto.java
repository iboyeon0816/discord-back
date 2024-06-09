package np2024.discord.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class ChannelRequestDto {

    @Getter
    public static class CreateDto {
        @NotEmpty
        private String channelName;
        @NotEmpty
        private String username;
    }

    @Getter
    public static class UpdateDto {
        @NotEmpty
        private String channelName;
    }
}
