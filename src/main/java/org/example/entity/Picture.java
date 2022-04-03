package org.example.entity;


import javax.persistence.*;
import java.util.UUID;

@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "flat_id")
    private Flat flat;

    private String path;
    private String description;

    public UUID getId() {
        return id;
    }

    public Flat getFlat() {
        return flat;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }
}