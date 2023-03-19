package br.com.felipemenezesdm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.Objects;

import static java.time.Duration.between;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class LogPayload {
    @JsonIgnore
    private String loggerId;

    private String serviceId;

    private String severity;

    private String endPoint;

    private String correlationId;

    private String httpStatus;

    private Integer httpStatusCode;

    private Long duration;

    private String logCode;

    private String message;

    private String codeLine;

    private Object artifact;

    private Long timestamp;

    private String dateTime;

    private Object details;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static LogPayload build() {
        Instant date = Instant.now();
        LogPayload logPayload = new LogPayload();
        logPayload.setTimestamp(date.toEpochMilli());
        logPayload.setDateTime(ofPattern(DATE_TIME_FORMAT).withZone(systemDefault()).format(date));

        return logPayload;
    }

    public LogPayload setLoggerId(String loggerId) {
        this.loggerId = loggerId;
        return this;
    }

    public LogPayload setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public LogPayload setSeverity(String severity) {
        this.severity = severity;
        return this;
    }

    public LogPayload setEndPoint(String endPoint) {
        this.endPoint = endPoint;
        return this;
    }

    public LogPayload setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public LogPayload setHttpStatus(Integer httpStatus) {
        this.httpStatus = HttpStatus.valueOf(httpStatusCode).name();
        this.httpStatusCode = httpStatus;
        return this;
    }

    public LogPayload setHttpStatus(HttpStatus httpStatus) {
        this.setHttpStatus(httpStatus.value());
        return this;
    }

    public LogPayload setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public LogPayload setDuration(Instant startTime, Instant endTime) {
        if(Objects.isNull(endTime)) {
            endTime = Instant.now();
        }

        this.duration = between(startTime, endTime).toMillis();
        return this;
    }

    public LogPayload setLogCode(String logCode) {
        this.logCode = logCode;
        return this;
    }

    public LogPayload setMessage(String message) {
        this.message = message;
        return this;
    }

    public LogPayload setCodeLine(String codeLine) {
        this.codeLine = codeLine;
        return this;
    }

    public LogPayload setArtifact(Object artifact) {
        this.artifact = artifact;
        return this;
    }

    public LogPayload setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public LogPayload setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public LogPayload setDetails(Object details) {
        this.details = details;
        return this;
    }

    public String getLoggerId() {
        return loggerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getSeverity() {
        return severity;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public Long getDuration() {
        return duration;
    }

    public String getLogCode() {
        return logCode;
    }

    public String getMessage() {
        return message;
    }

    public String getCodeLine() {
        return codeLine;
    }

    public Object getArtifact() {
        return artifact;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Object getDetails() {
        return details;
    }
}
