package np2024.discord.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private String name;
    private Channel channel;

    public User(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChannel(Channel channel) {
        if (this.channel != null) {
            this.channel.getUserList().remove(this);
        }
        this.channel = channel;

        if (this.channel != null) {
            this.channel.getUserList().add(this);
        }
    }
}
