package com.diningreview.dining_review_api.repository;

import com.diningreview.dining_review_api.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

    //Check if a restaurant already exists
    boolean existsByName(String name);

    //Fetch the details of a restaurant given unique id
    Optional<Restaurant> findByIdAndZipCode(Long id, String zipCode);

    //Fetch restaurant by zipCode and at least one user submitted score for a given allergy, sorted in descending order
    Optional<Restaurant> findByZipCode
}