package ebnatural.bizcurator.apiserver.common.exception.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    HTTP_MESSAGE_NOT_WRITABLE(HttpStatus.BAD_REQUEST, "Http message not writable"),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "Http message not readable"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "HTTP Method not allowed"),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "Illegal argument"),
    MISSING_SERVLET_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "Missing servlet request parameter"),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "Missing path variable"),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "Method argument type mismatch"),
    BIND_EXCEPTION(HttpStatus.BAD_REQUEST, "Bind exception"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST, "Data integrity violation"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category not found"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found"),
    IMAGE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Image upload error"),
    NOT_IMAGE_FILE(HttpStatus.BAD_REQUEST, "Not image file"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "entity not exists"),
    ENTITY_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "entity already exists"),
    ALREADY_REGISTERED_USER_EXCEPTION(HttpStatus.CONFLICT, "username already exists"),
    JWT_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료됐습니다."),
    JWT_WRONG(HttpStatus.BAD_REQUEST, "토큰값이 올바르지 않습니다."),
    AUTHENTICATION_WRONG(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    AUTHORIZATION_WRONG(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "Access Token 의 잘못된 계정정보입니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR ,"서버 오류로 정상적으로 요청을 처리할 수 없습니다."),
    USERNAME_OR_PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    INVALID_DOCUMENT_TYPE_EXCEPTION(HttpStatus.BAD_REQUEST, "요청서의 종류를 올바르게 선택해주세요."),
    PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    CERTIFICATION_NUMBER_WRONG(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    TERMS_OF_SERVICE_AGREEMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "필수 약관 동의가 완료되지 않았습니다.")
    ;

    private final HttpStatus httpStatusCode;
    private final String message;
}

