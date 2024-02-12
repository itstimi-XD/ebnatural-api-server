package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartProductDto {

    private String name; //상품이름
    private Long productId;//상품아이디
    private double discountPrice;//할인가
    private double regularPrice;//정가
    private int quantity;//수량
    private int minimumQuantity;//최소수량
    private String productImageUrl; // 상품이미지

    public CartProductDto(String name, Long productId, double discountPrice, double regularPrice, int quantity, int minimumQuantity, String productImageUrl) {
        this.name = name;
        this.productId = productId;
        this.discountPrice = discountPrice;
        this.regularPrice = regularPrice;
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
        this.productImageUrl = productImageUrl;
    }


}
