package ebnatural.bizcurator.apiserver.common.exception.custom;

public class ImageUploadException extends CustomException {

    public ImageUploadException() {
        super(ErrorCode.IMAGE_UPLOAD_ERROR);
    }
}
