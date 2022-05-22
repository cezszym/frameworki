package org.example.entity;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "flat")
@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flat {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "flat_details_id")
    private FlatDetail flatDetail;

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