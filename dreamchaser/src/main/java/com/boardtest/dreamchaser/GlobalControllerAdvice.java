package com.boardtest.dreamchaser;

import com.boardtest.dreamchaser.category.Category;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    // 이 클래스의 모든 메소드는 모든 컨트롤러에 적용됩니다.
    @ModelAttribute("groupedCategories")
    public Map<String, List<Category>> addGroupedCategories() {
        // 모든 Category 값을 가져와 mainCategory를 기준으로 그룹화
        return Arrays.stream(Category.values())
                .collect(Collectors.groupingBy(Category::getMainCategory));
    }
}