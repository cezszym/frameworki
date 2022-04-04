package org.example.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "flat_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FlatDetails {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "flat_id")
    private Flat flat;

    @NotNull
    private Boolean kitchen;

    @NotNull
    private Boolean bathroom;

    @NotNull
    private Boolean towels;

    @NotNull
    private Boolean wifi;



}