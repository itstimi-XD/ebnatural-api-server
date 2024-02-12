package ebnatural.bizcurator.apiserver.dto;

import lombok.Getter;

@Getter
public class ProductImageDto {
    private Long id;
    private String imgUrl;
    private String repimgYn;
    private Long productId;

    public ProductImageDto(Long id, String imgUrl, String repimgYn, Long productId) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
        this.productId = productId;
    }

    public ProductImageDto(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
