package ebnatural.bizcurator.apiserver.common.exception;

import ebnatural.bizcurator.apiserver.common.exception.custom.*;
import ebnatural.bizcurator.apiserver.dto.response.ErrorResponse;
import ebnatural.bizcurator.apiserver.dto.response.ResponseStatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotImageFileException.class)
    public ResponseEntity<Object> handleNotImageFileException(NotImageFileException e) {
        log.warn("handleNotImageFileException", e);
        return handleExceptionInternal(e.getErrorCode(), e.getErrorCode().getMessage());
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<Object> handleImageUploadException(ImageUploadException e) {
        log.warn("handleImageUploadException", e);
        return handleExceptionInternal(e.getErrorCode(), e.getErrorCode().getMessage());
    }

    // CategoryNotFoundException 에러 처리
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException e) {
        log.warn("handleCategoryNotFoundException", e);
        return handleExceptionInternal(e.getErrorCode(), e.getErrorCode().getMessage());
    }

    // ProductNotFoundException 에러 처리
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException e) {
        log.warn("handleProductNotFoundException", e);
        return handleExceptionInternal(e.getErrorCode(), e.getErrorCode().getMessage());
    }

    // EntityExistsException 에러 처리
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Object> handleEntityExistException(EntityExistsException e) {
        log.warn("handleEntityExistException", e);
        return handleExceptionInternal(ErrorCode.ENTITY_ALREADY_EXIST, ErrorCode.ENTITY_ALREADY_EXIST.getMessage());
    }

    // EntityNotFoundException 에러 처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("EntityNotFoundException", e);
        return handleExceptionInternal(ErrorCode.ENTITY_NOT_FOUND, ErrorCode.ENTITY_NOT_FOUND.getMessage());
    }

    // IllegalArgumentException 에러 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        return handleExceptionInternal(ErrorCode.ILLEGAL_ARGUMENT, ErrorCode.ILLEGAL_ARGUMENT.getMessage());
    }

    // MethodArgumentTypeMismatchException 에러 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("handleMethodArgumentTypeMismatch", e);
        return handleExceptionInternal(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH, ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.warn("handleDataIntegrityViolation", e);
        return handleExceptionInternal(ErrorCode.DATA_INTEGRITY_VIOLATION, ErrorCode.DATA_INTEGRITY_VIOLATION.getMessage());
    }

    @ExceptionHandler(AlreadyRegisteredUserException.class)
    public ResponseEntity<Object> handleAlreadyRegisteredUserException(AlreadyRegisteredUserException e) {
        log.warn("handleAlreadyRegisteredUserException", e);
        return handleExceptionInternal(ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION, ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION.getMessage());
    }

    @ExceptionHandler(InvalidUsernamePasswordException.class)
    public ResponseEntity<Object> InvalidUsernamePasswordException(InvalidUsernamePasswordException e) {
        log.warn("handleAlreadyRegisteredUserException", e);
        return handleExceptionInternal(ErrorCode.USERNAME_OR_PASSWORD_WRONG, ErrorCode.USERNAME_OR_PASSWORD_WRONG.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ErrorCode.HTTP_MESSAGE_NOT_READABLE, ErrorCode.HTTP_MESSAGE_NOT_READABLE.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ErrorCode.HTTP_MESSAGE_NOT_WRITABLE, ErrorCode.HTTP_MESSAGE_NOT_WRITABLE.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ErrorCode.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("handleMethodArgumentNotValid", ex);
        return handleExceptionInternal(ex);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("handleBindException", ex);
        return handleExceptionInternal(ex);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("handleMissingServletRequestParameter", ex);
        return handleExceptionInternal(ErrorCode.MISSING_SERVLET_REQUEST_PARAMETER, ErrorCode.MISSING_SERVLET_REQUEST_PARAMETER.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("handleMissingPathVariable", ex);
        return handleExceptionInternal(ErrorCode.MISSING_PATH_VARIABLE, ErrorCode.MISSING_PATH_VARIABLE.getMessage());
    }

    // 대부분의 에러 처리
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        return handleExceptionInternal();
    }

    private ResponseEntity<Object> handleExceptionInternal() {
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatusCode())
                .body(makeErrorResponse());
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e) {
        return ResponseEntity.status(ErrorCode.BIND_EXCEPTION.getHttpStatusCode())
                .body(makeErrorResponse(e));
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse() {
        return ErrorResponse.builder()
                .status(ResponseStatusType.ERROR)
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatusCode().value())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .status(ResponseStatusType.ERROR)
                .code(errorCode.getHttpStatusCode().value())
                .message(message)
                .build();
    }

    private ErrorResponse makeErrorResponse(BindException e) {
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .status(ResponseStatusType.ERROR)
                .code(ErrorCode.BIND_EXCEPTION.getHttpStatusCode().value())
                .message(ErrorCode.BIND_EXCEPTION.getMessage())
                .errors(validationErrorList)
                .build();
    }
}
