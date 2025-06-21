package com.boardtest.dreamchaser.post;

import com.boardtest.dreamchaser.category.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostForm {

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200)
    private String title;

    @NotNull(message = "카테고리는 필수항목입니다.")
    private Category category;

    @NotNull(message = "별점은 필수항목입니다.")
    private Integer starRating;

    @NotEmpty(message = "한줄평은 필수항목입니다.")
    @Size(max = 50, message = "한줄평은 50자 이내로 작성해주세요.")
    private String summary;

    @NotEmpty(message = "장점은 필수항목입니다.")
    private String pros;

    @NotEmpty(message = "단점은 필수항목입니다.")
    private String cons;

    @NotEmpty(message = "상세 리뷰는 필수항목입니다.")
    private String content;
    private MultipartFile imageFile;
}
