package org.example.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "post_id")
    private UUID post;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private UUID user;

    @NotNull
    private String contents;

    @NotNull
    private Date exposureDate;

    @NotNull
    private Integer likes;

    @NotNull
    private Integer dislikes;
}