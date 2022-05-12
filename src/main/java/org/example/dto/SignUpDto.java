package org.example.dto;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class SignUpDto {

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    private String nick;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String password;
}
