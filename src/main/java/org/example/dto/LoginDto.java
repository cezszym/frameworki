package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginDto {
    @Schema(example = "john")
    private String nickOrEmail;
    @Schema(example = "john")
    private String password;
}