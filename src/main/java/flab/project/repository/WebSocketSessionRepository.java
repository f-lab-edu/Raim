package flab.project.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.domain.ServerInfo;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

@Repository
@RequiredArgsConstructor
public class WebSocketSessionRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Map<Long, ConcurrentWebSocketSessionDecorator> webSocketSessionMap = new ConcurrentHashMap();
    public void saveUserSession(Long userId, WebSocketSession webSocketSession, ServerInfo serverInfo) {
        try {
            String key = "user:" + userId;
            webSocketSessionMap.put(userId, new ConcurrentWebSocketSessionDecorator(webSocketSession, 1000, 1024 * 1024));
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

    public boolean containUserSession(Long userId) {
        String key = "user:" + userId;
        return redisTemplate.opsForHash().hasKey(key, "serverInfo");
    }

    public ServerInfo getUserSession(Long userId) {
        try {
            String key = "user:" + userId;
            String info = (String) redisTemplate.opsForHash().get(key, "serverInfo");

            return objectMapper.readValue(info, ServerInfo.class);
        } catch (IOException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }
    }

    public ConcurrentWebSocketSessionDecorator getWebSocketSession(Long userId) {
        return webSocketSessionMap.get(userId);
    }
}
