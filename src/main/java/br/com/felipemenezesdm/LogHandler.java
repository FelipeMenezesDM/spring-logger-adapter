package br.com.felipemenezesdm;

import br.com.felipemenezesdm.enums.SeverityEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.HashMap;

public class LogHandler {
    private final static HashMap<String, Instant> timers = new HashMap<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger("default");

    public LogHandler() {
        throw new IllegalStateException();
    }

    public static void registerLogger(String loggerId) {
        timers.put(loggerId, Instant.now());
    }

    public static void info(String message) {
        logger.info("{}", message);
    }

    public static void info(String message, LogPayload payload) {
        handlerPayload(message, payload, SeverityEnum.INFO);
    }

    public static void warning(String message, LogPayload payload) {
        handlerPayload(message, payload, SeverityEnum.WARNING);
    }

    public static void error(String message, LogPayload payload) {
        handlerPayload(message, payload, SeverityEnum.ERROR);
    }

    private static void handlerPayload(String message, LogPayload payload, SeverityEnum severity) {
        Instant now = Instant.now();
        payload.setServiceId(System.getenv("APP_SERVICE_ID"));
        payload.setSeverity(severity.name());
        payload.setDuration(timers.getOrDefault(payload.getLoggerId(), now), now);

        switch(severity) {
            case ERROR :
                logger.error("{} {}", message, convertToJson(payload));
                break;
            case WARNING:
                logger.warn("{} {}", message, convertToJson(payload));
                break;
            default:
                logger.info("{} {}", message, convertToJson(payload));
                break;
        }
    }

    private static String convertToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object.toString();
        }
    }
}
