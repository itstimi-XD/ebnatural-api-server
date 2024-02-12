package ebnatural.bizcurator.apiserver.common.exception.custom;


public class InvalidDocumentTypeException extends CustomException{

    public InvalidDocumentTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}