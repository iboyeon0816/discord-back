package np2024.discord.repository;

import np2024.discord.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionRepository {

    private static final Map<String, User> store = new ConcurrentHashMap<>();

    public void save(String sessionId, String username) {
        User user = new User(username);
        store.put(sessionId, user);
    }

    public User findById(String sessionId) {
        return store.get(sessionId);
    }

    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public User delete(String sessionId) {
        User user = store.get(sessionId);
        store.remove(sessionId);
        return user;
    }

    public boolean exists(String username) {
        for (User user : store.values()) {
            if (username.equals(user.getName())) {
                return true;
            }
        }
        return false;
    }
}
