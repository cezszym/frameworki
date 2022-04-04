package org.example.entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.other.ReservationStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Relacja 'wiele rezerwacji do pojedynczych postów'
    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Relacja 'wiele rezerwacji do pojedynczych użytkowników'
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private ReservationStatus status;
    @NotNull
    private Date createDate;
}