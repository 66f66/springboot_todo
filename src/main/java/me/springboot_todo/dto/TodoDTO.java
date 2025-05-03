package me.springboot_todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TodoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Size(max = 20, message = "제목은 {max}자 이하입니다.")
    @NotBlank(message = "제목은 필수 입력 값입니다.", groups = ValidationGroups.Create.class)
    private String title;

    @Size(max = 100, message = "설명은 {max}자 이하입니다.")
    private String description;

    private int orderNumber;

    @NotNull(message = "필수 값입니다.", groups = ValidationGroups.Create.class)
    private Boolean completed;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updatedAt;
}
