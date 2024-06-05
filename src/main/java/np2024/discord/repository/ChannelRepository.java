package np2024.discord.repository;

import jakarta.annotation.PostConstruct;
import np2024.discord.domain.Channel;
import np2024.discord.dto.ChannelRequestDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ChannelRepository {

    private static final AtomicLong sequence = new AtomicLong(0);
    private static final Map<Long, Channel> store = new ConcurrentHashMap<>();

//    @PostConstruct
    public void addDefaultChannel() {
        Channel channel1 = new Channel(sequence.incrementAndGet(), "channel1");
        store.put(channel1.getId(), channel1);
        Channel channel2 = new Channel(sequence.incrementAndGet(), "channel2");
        store.put(channel2.getId(), channel2);
    }

    public Channel save(ChannelRequestDto request) {
        Channel channel = new Channel(sequence.incrementAndGet(), request.getChannelName());
        store.put(channel.getId(), channel);
        return channel;
    }

    public Channel findById(Long channelId) {
        return store.get(channelId);
    }

    public List<Channel> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long channelId, ChannelRequestDto request) {
        Channel channel = store.get(channelId);
        channel.update(request.getChannelName());
    }

    public void delete(Long channelId) {
        store.remove(channelId);
    }

    public boolean exists(Long channelId) {
        return store.containsKey(channelId);
    }
}
