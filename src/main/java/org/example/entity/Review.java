package org.example.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Data
@Table(name = "review")
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String title;

    @NotNull
    private String contents;

    @NotNull
    private Date exposureDate;

    @NotNull
    private Integer likes;

    @NotNull
    private Integer dislikes;
}