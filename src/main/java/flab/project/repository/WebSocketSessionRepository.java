package flab.project.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.domain.ServerInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

@Repository
@RequiredArgsConstructor
public class WebSocketSessionRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Map<Long, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap();
    public void saveUserSession(Long userId, WebSocketSession webSocketSession, ServerInfo serverInfo) {
        try {
            String key = "user:" + userId;
            webSocketSessionMap.put(userId, webSocketSession);
            redisTemplate.opsForHash().put(key, "serverInfo", objectMapper.writeValueAsString(serverInfo));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUserSession(Long userId) {
        String key = "user:" + userId;
        webSocketSessionMap.remove(userId);
        redisTemplate.delete(key);
    }

    public WebSocketSession getWebSocketSession(Long userId) {
        return webSocketSessionMap.get(userId);
    }
}
