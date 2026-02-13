package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoldMedalRepository extends JpaRepository<GoldMedal, Long> {
    List<GoldMedal> findByCountryOrderByYearAsc(String country);
    List<GoldMedal> findByCountryOrderByYearDesc(String country);

}
