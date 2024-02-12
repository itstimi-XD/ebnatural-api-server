package ebnatural.bizcurator.apiserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import ebnatural.bizcurator.apiserver.domain.Category;
import ebnatural.bizcurator.apiserver.domain.Manufacturer;
import ebnatural.bizcurator.apiserver.domain.Product;
import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProductRequest {

    @NotNull(message = "카테고리 ID는 필수 입력값입니다.")
    @Positive(message = "카테고리 ID는 양수여야 합니다.")
    @JsonProperty("category_id")
    private Long categoryId;

    @NotBlank(message = "제조사 이름은 필수 입력값입니다.")
    @Size(max = 50, message = "제조사 이름은 최대 50자까지 입력 가능합니다.")
    @JsonProperty("manufacturer_name")
    private String manufacturerName;

    @NotBlank(message = "상품명은 필수 입력값입니다.")
    @Size(max = 50, message = "상품명은 최대 50자까지 입력 가능합니다.")
    @JsonProperty("product_name")
    private String name;

    @NotNull(message = "정상 가격은 필수 입력값입니다.")
    @Positive(message = "정상 가격은 양수여야 합니다.")
    @JsonProperty("regular_price")
    private Integer regularPrice;

    @NotNull(message = "최소 수량은 필수 입력값입니다.")
    @Positive(message = "최소 수량은 양수여야 합니다.")
    @JsonProperty("min_quantity")
    private Integer minQuantity;

    @NotNull(message = "최대 수량은 필수 입력값입니다.")
    @Positive(message = "최대 수량은 양수여야 합니다.")
    @JsonProperty("max_quantity")
    private Integer maxQuantity;

    @NotNull(message = "할인율은 필수 입력값입니다.")
    @Positive(message = "할인율은 양수여야 합니다.")
    @JsonProperty("discount_rate")
    private Integer discountRate;


    public ProductRequest() {
    }

    public ProductRequest(Long categoryId, String manufacturerName, String name, int regularPrice,
            int minQuantity, int maxQuantity, int discountRate) {
        this.categoryId = categoryId;
        this.manufacturerName = manufacturerName;
        this.name = name;
        this.regularPrice = regularPrice;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.discountRate = discountRate;
    }

    public Product toEntity(Category category, Manufacturer manufacturer) {
        return Product.builder()
                .category(category)
                .manufacturer(manufacturer)
                .name(name)
                .regularPrice(regularPrice)
                .minQuantity(minQuantity)
                .maxQuantity(maxQuantity)
                .discountRate(discountRate)
                .productImages(new ArrayList<>())
                .weeklyClicks(0)
                .monthlyClicks(0)
                .build();
    }

}
