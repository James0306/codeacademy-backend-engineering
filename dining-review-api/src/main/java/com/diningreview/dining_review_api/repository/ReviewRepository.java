package com.diningreview.dining_review_api.repository;

import com.diningreview.dining_review_api.model.Review;
import com.diningreview.dining_review_api.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>{

    //Get all pending reviews
    List<Review> findByStatus(ReviewStatus status);

    // Get all approved reviews for a specific restaurant
    List<Review> findByRestaurantIdAndStatus(Long restaurantId, ReviewStatus status);
}