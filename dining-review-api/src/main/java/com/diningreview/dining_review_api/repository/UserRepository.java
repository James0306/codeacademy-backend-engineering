package com.diningreview.dining_review_api.repository;

import com.diningreview.dining_review_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Fetch by displayNam
    Optional<User> findByDisplayName(String displayName);

    // Check if displayName exists
    boolean existsByDisplayName(String displayName);
}