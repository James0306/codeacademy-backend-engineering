package com.diningreview.dining_review_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminReview {

    private Boolean accepted;

    public AdminReview(Boolean accepted){
        this.accepted = accepted;
    }
}
