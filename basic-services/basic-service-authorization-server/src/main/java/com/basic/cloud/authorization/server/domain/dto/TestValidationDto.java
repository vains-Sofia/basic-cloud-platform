package com.basic.cloud.authorization.server.domain.dto;

import com.basic.cloud.data.validation.annotation.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * 测试入参校验类
 *
 * @author vains
 */
@Data
@Schema(name = "测试入参校验类", description = "测试入参校验类")
public class TestValidationDto {

    @Phone
    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotEmpty
    private List<String> list;

    @NotNull
    @Range(min = 1, max = 100)
    private Integer age;

}
