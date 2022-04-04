package org.example.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String contents;
    private Date exposureDate;
    private Integer likes;
    private Integer dislikes;
}