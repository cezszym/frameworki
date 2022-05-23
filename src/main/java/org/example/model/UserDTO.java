package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.Flat;
import org.example.entity.Post;
import org.example.entity.Reservation;
import org.example.entity.Review;
import org.example.entity.User;


import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private String email;
    private String nick;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String password;

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(),
                user.getEmail(),
                user.getNick(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhoneNumber(),
                user.getPassword());
    }
}
