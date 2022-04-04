package org.example.entity;


import org.example.other.ReservationStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Relacja 'wiele rezerwacji do pojedynczych postów'
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Relacja 'wiele rezerwacji do pojedynczych użytkowników'
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Date startDate;
    private Date endDate;
    private ReservationStatus status;
    private Date createDate;
}