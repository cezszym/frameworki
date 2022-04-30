package org.example.entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "flat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flat {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "flat_details_id")
    private FlatDetails flatDetails;

    @NotNull
    private String adress;

    @NotNull
    private String postCode;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @NotNull
    private float metrage;

    @NotNull
    private int numOfRooms;


}