package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductAdminDetailDto {

        private Long id;

        @JsonProperty("category_id")
        private Long categoryId;

        @JsonProperty("manufacturer_name")
        private String manufacturerName;
        @JsonProperty("product_name")
        private String name;

        @JsonProperty("regular_price")
        private int regularPrice;
        @JsonProperty("min_quantity")
        private int minQuantity;

        @JsonProperty("max_quantity")
        private int maxQuantity;
        @JsonProperty("discount_rate")
        private int discountRate;
        @JsonProperty("main_image_url")
        private String mainImageUrl;
        @JsonProperty("detail_image_url")
        private String detailImageUrl;

        public ProductAdminDetailDto(Long id, Long categoryId, String manufacturerName, String name,
                int regularPrice, int minQuantity, int maxQuantity, int discountRate,
                String mainImageUrl,
                String detailImageUrl) {
            this.id = id;
            this.categoryId = categoryId;
            this.manufacturerName = manufacturerName;
            this.name = name;
            this.regularPrice = regularPrice;
            this.minQuantity = minQuantity;
            this.maxQuantity = maxQuantity;
            this.discountRate = discountRate;
            this.mainImageUrl = mainImageUrl;
            this.detailImageUrl = detailImageUrl;
        }
}
