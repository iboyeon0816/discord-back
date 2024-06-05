package np2024.discord.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Channel {
    private Long id;
    private String name;
    private final List<User> userList = new ArrayList<>();

    public void update(String name) {
        this.name = name;
    }
}
