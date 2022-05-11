package org.example.dto;
import lombok.Data;

@Data
public class SignUpDto {
    private String email;
    private String nick;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String password;
}
