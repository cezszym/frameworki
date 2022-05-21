package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.Reservation;
import org.example.other.ReservationStatus;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ReservationDTO {

    private UUID id;
    private PostDTO postDTO;
    private UserDTO userDTO;
    private Date start;
    private Date end;
    private ReservationStatus status;
    private Date created;


    public static ReservationDTO fromEntity(Reservation reservation){
        return new ReservationDTO(reservation.getId(), PostDTO.fromEntity(reservation.getPost()),
                UserDTO.fromEntity(reservation.getUser()), reservation.getStart(), reservation.getEnd(),
                reservation.getStatus(), reservation.getCreated());
    }
}
