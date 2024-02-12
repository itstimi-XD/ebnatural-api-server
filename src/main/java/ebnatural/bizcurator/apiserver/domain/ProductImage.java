package ebnatural.bizcurator.apiserver.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "img_url")
    private String imgUrl;

    @Setter
    @Column(name = "repimg_yn")
    private String repimgYn;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static ProductImage createProductImage(Product product, String imgUrl, String repimgYn) {
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImgUrl(imgUrl);
        productImage.setRepimgYn(repimgYn);
        return productImage;
    }

    public void updateImageUrl(String newImageUrl) {
        this.imgUrl = newImageUrl;
    }

}
