package org.example.entity;


import org.example.other.ReservationStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID postId;
    private UUID userId;
    private Date startDate;
    private Date endDate;
    private ReservationStatus status;
    private Date createDate;
}