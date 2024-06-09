package np2024.discord.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Channel {
    private Long id;
    private String name;
    private String creatorName;

    public void update(String name) {
        this.name = name;
    }
}
