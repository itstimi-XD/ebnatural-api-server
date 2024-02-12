package ebnatural.bizcurator.apiserver.common.exception.custom;

public class ProductNotFoundException extends CustomException {
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}