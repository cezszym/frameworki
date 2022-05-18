package org.example.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "flat_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FlatDetail {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "flat_id")
    private Flat flat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Boolean kitchen;

    @NotNull
    private Boolean bathroom;

    @NotNull
    private Boolean towels;

    @NotNull
    private Boolean wifi;



}