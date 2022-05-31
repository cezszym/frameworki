package org.example.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class SignUpDto {

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email cannot be empty")
    @Schema(example = "john@email.com")
    private String email;
    @Schema(example = "john")
    private String nick;
    @Schema(example = "John")
    private String firstname;
    @Schema(example = "Wild")
    private String lastname;
    @Schema(example = "248 371 119")
    private String phoneNumber;
    @Schema(example = "john")
    private String password;
}
