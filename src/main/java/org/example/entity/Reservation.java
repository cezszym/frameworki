package org.example.entity;


import com.sun.istack.NotNull;
import lombok.*;
import org.example.other.ReservationStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {
    @Id
    @NotNull
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

    @NotNull
    private Date start;
    @NotNull
    private Date end;
    @NotNull
    private ReservationStatus status;
    @NotNull
    private Date created;
}