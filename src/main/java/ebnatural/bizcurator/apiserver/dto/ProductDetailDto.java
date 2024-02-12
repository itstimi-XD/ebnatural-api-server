package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;

public class ProductDetailDto {
    private Long id;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("product_name")
    private String name;

    @JsonProperty("main_image_id")
    private Long mainImageId;

    @JsonProperty("main_image_url")
    private String mainImageUrl;

    @JsonProperty("detail_image_id")
    private Long detailImageId;

    @JsonProperty("detail_image_url")
    private String detailImageUrl;
    @JsonProperty("regular_price")
    private int regularPrice;

    @JsonProperty("discount_rate")
    private int discountRate;

    @JsonProperty("sale_price")
    private int salePrice;

    @JsonProperty("min_quantity")
    private int minQuantity;

    @QueryProjection
    public ProductDetailDto(Long id, Long categoryId, String name, Long mainImageId, String mainImageUrl, Long detailImageId, String detailImageUrl, int regularPrice, int discountRate, int minQuantity) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.mainImageId = mainImageId;
        this.mainImageUrl = mainImageUrl;
        this.detailImageId = detailImageId;
        this.detailImageUrl = detailImageUrl;
        this.regularPrice = regularPrice;
        this.discountRate = discountRate;
        this.salePrice = regularPrice * (100 - discountRate) / 100;
        this.minQuantity = minQuantity;
    }

}
