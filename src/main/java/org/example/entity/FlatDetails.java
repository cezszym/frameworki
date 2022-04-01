package org.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FlatDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

}