package org.example.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String email;

    @NotNull
    private String nick;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String password;
}