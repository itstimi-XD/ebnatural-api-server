package ebnatural.bizcurator.apiserver.common.exception.custom;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
