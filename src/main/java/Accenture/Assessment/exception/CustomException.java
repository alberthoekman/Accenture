package Accenture.Assessment.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception that additionally holds a HttpStatus and an alternative message for the logger.
 */
public class CustomException extends RuntimeException {
  private HttpStatus status;
  private String logMessage;

  public CustomException(String message, HttpStatus status) {
    super(message);
    this.status = status;
    this.logMessage = message;
  }

  public CustomException(String message, HttpStatus status, String logMessage) {
    super(message);
    this.status = status;
    this.logMessage = logMessage;
  }

  public String getLogMessage() {
    return logMessage;
  }

  public void setLogMessage(String logMessage) {
    this.logMessage = logMessage;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }
}
