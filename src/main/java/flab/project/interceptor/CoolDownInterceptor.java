package flab.project.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CoolDownInterceptor implements HandlerInterceptor {

    private static final Map<String, Long> coolDownMap = new ConcurrentHashMap<>();
    private static final long COOL_DOWN_TIME = 5000;
    private static final int TOO_MANY_REQUESTS = 429;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        if (coolDownMap.containsKey(sessionId)) {
            Long lastRequestTime = coolDownMap.get(sessionId);
            Long now = System.currentTimeMillis();

            if (now - lastRequestTime < COOL_DOWN_TIME) {
                response.setStatus(TOO_MANY_REQUESTS);
                return false;
            }
        }

        coolDownMap.put(sessionId, System.currentTimeMillis());
        return true;
    }
}
