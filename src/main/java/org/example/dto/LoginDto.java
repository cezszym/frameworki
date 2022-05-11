package org.example.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String nickOrEmail;
    private String password;
}