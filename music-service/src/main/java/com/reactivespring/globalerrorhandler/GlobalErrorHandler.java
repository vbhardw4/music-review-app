package com.reactivespring.globalerrorhandler;

import com.reactivespring.exceptions.MusicInfoClientException;
import com.reactivespring.exceptions.MusicInfoServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {

    /**
     *
     * @param mice
     * @return
     *
     * Every time we encounter mice, this response entity would be returned.
     */
    @ExceptionHandler(MusicInfoClientException.class)
    public ResponseEntity<String> handleClientException(MusicInfoClientException mice) {
        log.error("Exception caught in handleClientException "+mice);
        return ResponseEntity.status(mice.getStatusCode()).body(mice.getMessage());
    }

    @ExceptionHandler(MusicInfoServerException.class)
    public ResponseEntity<String> handleServerException(MusicInfoServerException mise) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mise.getMessage());
    }
}
