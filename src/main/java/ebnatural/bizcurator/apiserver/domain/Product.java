package ebnatural.bizcurator.apiserver.domain;

import ebnatural.bizcurator.apiserver.dto.request.ProductRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "name", nullable = false)
    @NotBlank(message = "상품명은 필수 입력값입니다.")
    @Size(max = 50, message = "상품명은 최대 50자까지 입력 가능합니다.")
    private String name;

    @Setter
    @Column(name = "regular_price")
    @NotNull(message = "정상 가격은 필수 입력값입니다.")
    @Positive(message = "정상 가격은 양수여야 합니다.")
    private Integer regularPrice;
    
    @Setter
    @Column(name = "min_quantity")
    @NotNull(message = "최소 수량은 필수 입력값입니다.")
    @Positive(message = "최소 수량은 양수여야 합니다.")
    private Integer minQuantity;

    @Setter
    @Column(name = "max_quantity")
    @NotNull(message = "최대 수량은 필수 입력값입니다.")
    @Positive(message = "최대 수량은 양수여야 합니다.")
    private Integer maxQuantity;

    @Setter
    @Column(name = "discount_rate")
    @NotNull(message = "할인율은 필수 입력값입니다.")
    @Positive(message = "할인율은 양수여야 합니다.")
    private Integer discountRate;

    @Column(name = "weekly_clicks")
    @NotNull(message = "주간 클릭수는 초기값이 0입니다.")
    private Integer weeklyClicks;

    @Column(name = "monthly_clicks")
    @NotNull(message = "월간 클릭수는 초기값이 0입니다.")
    private Integer monthlyClicks;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull(message = "카테고리 ID는 필수 입력값입니다.")
    private Category category;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    @NotNull(message = "제조사 ID는 필수 입력값입니다.")
    private Manufacturer manufacturer;

    public void updateImages(String mainImageUrl, String detailImageUrl) {
        for (ProductImage productImage : this.getProductImages()) {
            if ("Y".equals(productImage.getRepimgYn()) && mainImageUrl != null) {
                productImage.updateImageUrl(mainImageUrl);
            } else if ("N".equals(productImage.getRepimgYn()) && detailImageUrl != null) {
                productImage.updateImageUrl(detailImageUrl);
            }
        }
    }

    public void update(ProductRequest productRequest, Category category, Manufacturer manufacturer) {
        this.category = category;
        this.manufacturer = manufacturer;
        this.name = productRequest.getName();
        this.regularPrice = productRequest.getRegularPrice();
        this.minQuantity = productRequest.getMinQuantity();
        this.maxQuantity = productRequest.getMaxQuantity();
        this.discountRate = productRequest.getDiscountRate();
    }



    //fetchType이 LAZY이면 proxy를 읽지 못하는 문제가 있음, 근대 EAGER을 쓰면 무한반복
    @Setter
    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    /**
     *
     * @return 할인율을 포함한 물건 가격 반환
     */
    public int getCostWithDiscount() {
        return this.regularPrice * (100 - this.discountRate)/100;
    }


}
