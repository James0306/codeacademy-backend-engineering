package com.diningreview.dining_review_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.diningreview.dining_review_api.model.ReviewStatus;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false, updatable = false)
    private Long restaurantId;

    //Optional Scores
    private Integer peanutScore;
    private Integer eggScore;
    private Integer dairyScore;

    //Optional comment
    private String comment;

    //Enum for Review Status. The @Enumerated tells JPA to take it as a String
    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

    public Review (String name, Long restaurantId){
        this.name = name;
        this.restaurantId = restaurantId;
    }

}
