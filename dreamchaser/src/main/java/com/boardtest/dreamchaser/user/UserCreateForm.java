package com.boardtest.dreamchaser.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

    @Size(min = 3, max = 12, message = "아이디는 3자 이상 12자 이하로 입력해주세요.")
    @NotEmpty(message = "아이디는 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문과 숫자만 사용 가능합니다.")
    private String username;

    @Size(min = 3, max = 12, message = "비밀번호는 3자 이상 12자 이하로 입력해주세요.")
    @NotEmpty(message = "비밀번호는 필수 항목입니다.")

    // @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{3,12}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함되어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "비밀번호는 영문과 숫자만 사용 가능합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
    private String passwordConfirm;

    @Size(max = 15, message = "닉네임은 15자 이하로 입력해주세요.")
    @NotEmpty(message = "닉네임은 필수 항목입니다.")
    private String nickname;
}
