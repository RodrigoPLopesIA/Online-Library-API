package br.com.rodrigo.onlinelibraryapi.exceptions;

import java.util.IllegalFormatConversionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiErrorHandler {

        @ExceptionHandler(IllegalFormatConversionException.class)
        public ResponseEntity<ErrorMessage> illegalFormatConversionException(IllegalFormatConversionException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
        }

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ResponseEntity<ErrorMessage> maxUploadSizeExceededException(MaxUploadSizeExceededException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ErrorMessage> unauthorizedException(UnauthorizedException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.UNAUTHORIZED, ex.getMessage()));
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorMessage> badCredentialsException(BadCredentialsException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                        HttpServletRequest request, BindingResult result) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid fields!",
                                                result));
        }

        @ExceptionHandler(UniqueViolationException.class)
        public ResponseEntity<ErrorMessage> dataIntegrityViolationException(UniqueViolationException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorMessage> illegalArgumentException(IllegalArgumentException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
        }
}
