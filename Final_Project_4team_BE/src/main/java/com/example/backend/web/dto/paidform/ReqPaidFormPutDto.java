package com.example.backend.web.dto.paidform;

import com.example.backend.web.validation.LocalDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "PaidForm 수정 Dto")
public class ReqPaidFormPutDto {

    @Schema(description = "사진")
    private MultipartFile pic;

    @NotEmpty
    @Schema(description = "결제 번호", example = "123asd")
    private String billingNo;

    @LocalDateFormat
    @Schema(description = "배송일자", example = "2021-10-10")
    private String deliveryDate;

    @LocalDateFormat
    @Schema(description = "구매일자", example = "2021-10-10")
    private String receiptDate;
}
