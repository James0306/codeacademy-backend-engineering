package com.diningreview.dining_review_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String name;

    private String city;
    private String state;
    private String zipcode;
    private Boolean peanutAllergyInterest;
    private Boolean eggAllergyInterest;
    private Boolean dairyAllergyInterest;
}
