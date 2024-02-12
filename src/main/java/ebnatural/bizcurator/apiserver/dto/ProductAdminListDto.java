package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProductAdminListDto {
    private Long id;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("product_name")
    private String name;

    @JsonProperty("regular_price")
    private int regularPrice;

    @JsonProperty("discount_rate")
    private int discountRate;

    @JsonProperty("sale_price")
    private int salePrice;

    @JsonProperty("manufacturer_name")
    private String manufacturerName;

    @QueryProjection
    public ProductAdminListDto(Long id, String categoryName, String manufacturerName, String name, int regularPrice, int discountRate) {
        this.id = id;
        this.categoryName = categoryName;
        this.name = name;
        this.regularPrice = regularPrice;
        this.discountRate = discountRate;
        this.salePrice = regularPrice * (100 - discountRate) / 100;
        this.manufacturerName = manufacturerName;

    }

}
