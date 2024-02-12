package ebnatural.bizcurator.apiserver.common.exception.custom;


public class CategoryNotFoundException extends CustomException {
    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }
}