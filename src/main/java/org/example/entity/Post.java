package org.example.entity;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "flat_id")
    private Flat flat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String checkInDate;

    private String checkOutDate;

    private String creationDate;

    private String status;

}