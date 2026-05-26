package api.bookstore.order_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException ex){
        String message = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        return ResponseEntity.badRequest().body(Map.of(
                "message", message,
                "status", String.valueOf(HttpStatus.BAD_REQUEST.value())
        ));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, String>> handleException(Exception ex){
        String message = ex.getMessage() == null ? "Internal Server Error" : ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", message,
                "status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ));
    }
}
