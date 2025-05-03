package me.springboot_todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "아이디는 필수 입력값입니다", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]{2,20}$",
            message = "아이디는 2-20자의 영문, 숫자, 특수문자만 사용 가능합니다"
    )
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "비밀번호는 필수 입력값입니다", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]{8,20}$",
            message = "비밀번호는 8-20자의 영문, 숫자, 특수문자만 사용 가능합니다"
    )
    private String password;

    @NotBlank(message = "닉네임은 필수 입력값입니다", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 2-10자의 한글, 영문, 숫자만 사용 가능합니다"
    )
    private String nickname;
}
