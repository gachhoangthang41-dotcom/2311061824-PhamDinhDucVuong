package vn.edu.crs.registrationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>>
    handleNotFound(
            NoSuchElementException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        Map.of(
                                "message",
                                exception.getMessage()
                        )
                );
    }

    /**
     * 409
     *
     * Ví dụ:
     * - đã đăng ký môn
     * - hết chỗ
     * - đã hủy trước đó
     * - course-service không kết nối được
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>>
    handleConflict(
            IllegalStateException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        Map.of(
                                "message",
                                exception.getMessage()
                        )
                );
    }

    /**
     * Validation 400
     */
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleValidation(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> errors =
                new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(
                        error ->
                                errors.put(
                                        error.getField(),
                                        error.getDefaultMessage()
                                )
                );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "message",
                "Dữ liệu đầu vào không hợp lệ"
        );

        response.put(
                "errors",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}