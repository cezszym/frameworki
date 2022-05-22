package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.Flat;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FlatDTO {

    private UUID id;
    private UserDTO user;
    private FlatDetailDTO flatDeatil;
    private String adress;
    private String postCode;
    private String city;
    private String country;
    private float metrage;
    private int numOfRooms;

    public static FlatDTO fromEntity(Flat flat) {
        return new FlatDTO(flat.getId(),
                UserDTO.fromEntity(flat.getUser()),
                FlatDetailDTO.fromEntity(flat.getFlatDetail()),
                flat.getAdress(),
                flat.getPostCode(),
                flat.getCity(),
                flat.getCountry(),
                flat.getMetrage(),
                flat.getNumOfRooms());
    }
}
