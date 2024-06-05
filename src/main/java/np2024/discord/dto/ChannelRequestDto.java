package np2024.discord.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ChannelRequestDto {
    @NotEmpty
    private String channelName;
}
