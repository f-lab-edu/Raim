package flab.project.exception;

import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class KakaoException extends RuntimeException {
    private ExceptionCode exceptionCode;

    public KakaoException(ExceptionCode exceptionCode) {
        super(exceptionCode.getExternalMessage());
        this.exceptionCode = exceptionCode;
    }

    public KakaoException(ExceptionCode exceptionCode, Map<String, String> errorMap) {
        super(exceptionCode.getExternalMessage());
        this.exceptionCode = exceptionCode;
        
        logError(exceptionCode.getInternalMessage(), errorMap);
    }

    private  void logError(String internalMessage, Map<String, String> errorMap) {
        log.info(internalMessage);
        for (Map.Entry<String, String> entry: errorMap.entrySet()) {
            log.info("{}: {}", entry.getKey(), entry.getValue());
        }
    }
}
