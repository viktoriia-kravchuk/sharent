package com.viktoria.sharent.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="country")
    private String country;
    @Column (name="region")
    private String region;
    @Column (name="city")
    private String city;
    @Column (name="postcode")
    private String postcode;
    @Column(name="street_name")
    private String streetName;
    @Column(name="street_number")
    private String streetNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "user_id")
    User user;

}
