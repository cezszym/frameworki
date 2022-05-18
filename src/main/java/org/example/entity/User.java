package org.example.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @NotNull
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

    @NotNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Flat> flats;

    @NotNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @NotNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

    @NotNull
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Review> reviews;
}