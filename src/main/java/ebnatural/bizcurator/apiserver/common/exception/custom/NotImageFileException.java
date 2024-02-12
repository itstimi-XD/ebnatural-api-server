package ebnatural.bizcurator.apiserver.common.exception.custom;

public class NotImageFileException extends CustomException {

    public NotImageFileException() {
        super(ErrorCode.NOT_IMAGE_FILE);
    }
}
