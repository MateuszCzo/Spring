package mc.recruitment_task.epidemic_simulation.controller;

import mc.recruitment_task.epidemic_simulation.exception.EpidemicAlgorithmNotFoundException;
import mc.recruitment_task.epidemic_simulation.exception.EpidemicParamsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        exception.getBindingResult()
                .getGlobalErrors()
                .forEach(error -> errors.put(error.getObjectName(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EpidemicParamsNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEpidemicParamsNotFoundException(EpidemicParamsNotFoundException exception) {
        Map<String, String> errors = new HashMap<>();

        errors.put("error", exception.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EpidemicAlgorithmNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEpidemicAlgorithmNotFoundException(EpidemicAlgorithmNotFoundException exception) {
        Map<String, String> errors = new HashMap<>();

        errors.put("error", exception.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
}
