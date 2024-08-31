package mc.project.online_store.controller.advice;

import mc.project.online_store.exception.FileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileExceptionHandler {
    @ExceptionHandler({FileException.class})
    public ResponseEntity<String> handleFileException(FileException exception) {

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
