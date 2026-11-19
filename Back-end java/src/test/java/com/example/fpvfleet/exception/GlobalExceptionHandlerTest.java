package com.example.fpvfleet.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest("GET", "/api/v1/drones/99");
    }

    @Test
    void handleNotFound_resourceMissing_returns404WithMessageAndPath() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Дрон з ID 99 не знайдено");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).isEqualTo("Дрон з ID 99 не знайдено");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/drones/99");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    void handleBadRequest_illegalArgument_returns400WithOriginalMessage() {
        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(new IllegalArgumentException("pilotId є обов'язковим"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getMessage()).isEqualTo("pilotId є обов'язковим");
    }

    @Test
    void handleConflict_dataIntegrityViolation_returns409WithoutLeakingSqlDetails() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate entry 'ace' for key 'username'");

        ResponseEntity<ErrorResponse> response = handler.handleConflict(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).doesNotContain("Duplicate entry");
    }

    @Test
    void handleAccessDenied_deniedByPreAuthorize_returns403() {
        ResponseEntity<ErrorResponse> response = handler.handleAccessDenied(new AccessDeniedException("Access Denied"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().getMessage()).contains("Access Denied");
    }

    @Test
    void handleGlobalException_unexpectedError_returns500WithGenericMessage() {
        ResponseEntity<ErrorResponse> response = handler.handleGlobalException(new IllegalStateException("NPE in internals"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMessage()).doesNotContain("NPE");
    }
}
