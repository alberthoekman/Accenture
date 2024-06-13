package Accenture.Assessment.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/** Class that handles eexceptions globally. */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles CustomException.
   *
   * @return A response to send to the client.
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handleCustomException(CustomException ex, WebRequest request) {
    logger.error(ex.getLogMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
  }

  /**
   * Handles any other exception than CustomException.
   *
   * @return A response to send to the client.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
    logger.error("An unexpected error ocurred: ", ex);
    return new ResponseEntity<>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
