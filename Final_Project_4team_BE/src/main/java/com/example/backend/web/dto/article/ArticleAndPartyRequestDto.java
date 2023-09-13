package com.example.backend.web.dto.article;



import com.example.backend.domain.article.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

//게시글 올리기 폼
@Schema(name = "게시글과 파티 정보 생성", description = "게시글과 파티의 상세 정보를 나타낸다")
public class ArticleAndPartyRequestDto {
    // for article
    @Schema(description = "작성자 아이디", example = "hashedProfile")
    private String profileId;
    @Schema(description = "게시글 제목", example = "TEST title 01")
    private String title;
    @Schema(description = "게시글 내용", example = "TEST content blah blah")
    private String content;
    @Schema(description = "공동 구매 링크", example = "http://coupang.com/product/1")
    private String link;
    @Schema(description = "물품 항목", example = "FOOD")
    private Category category;
    @Schema(description = "사진 리스트")
    private List<String> pic;

    // for party
    //@Schema(description = "파티 만료 기간", example = "2022-02-01-23:59:59")
    private String deadline;
    @Schema(description = "상품 이름", example = "product name")
    private String product;

    @Min(value=0)
    @Schema(description = "상품 총 가격", example = "10000")
    private Integer totalPrice;
    @Min(value=1)
    @Schema(description = "상품 총 개수", example = "24")
    private Integer totalProductCount;
    @Min(value=2)
    @Schema(description = "파티 총 인원", example = "4")
    private Integer totalRecruitMember;
    @Schema(description = "위약금", example = "50")
    private Integer penalty;

    //for memberInfo
    @Min(value=1)
    @Schema(description = "내가 선택한 수량", example = "2")
    private Integer amount;

}
