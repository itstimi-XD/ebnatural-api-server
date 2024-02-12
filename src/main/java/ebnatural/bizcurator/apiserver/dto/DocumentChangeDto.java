package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DocumentChangeDto {

    @NotBlank
    private String category;

    private String productName;

    private String productDetail;

    private int quantity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date desiredEstimateDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date desiredDeliveryDate;

    private int establishYear;

    private String companyIntroduction;

    private String requestContext;

}
