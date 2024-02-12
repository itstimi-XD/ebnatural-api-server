package ebnatural.bizcurator.apiserver.common.exception.custom;


import ebnatural.bizcurator.apiserver.common.exception.custom.ErrorCode;

public class InvalidUsernamePasswordException extends CustomException{

    public InvalidUsernamePasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}