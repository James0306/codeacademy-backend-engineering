package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.codecademy.goldmedal.repository.GoldMedalRepository;
import com.codecademy.goldmedal.repository.CountryRepository;


@RestController
@RequestMapping("/countries")
public class GoldMedalController {

    private final GoldMedalRepository goldMedalRepository;
    private final CountryRepository countryRepository;

    public GoldMedalController(GoldMedalRepository goldMedalRepository,
                               CountryRepository countryRepository) {
        this.goldMedalRepository = goldMedalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.equalsIgnoreCase("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.equalsIgnoreCase("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                if(ascendingOrder){
                    medalsList = goldMedalRepository.findByCountryOrderByYearAsc(countryName);
                } else {
                    medalsList = goldMedalRepository.findByCountryOrderByYearDesc(countryName);
                }
                break;
            case "season":
                if(ascendingOrder){
                    medalsList = goldMedalRepository.findByCountryOrderBySeasonAsc(countryName);
                } else {
                    medalsList = goldMedalRepository.findByCountryOrderBySeasonDesc(countryName);
                }
                break;
            case "city":
                if(ascendingOrder){
                    medalsList = goldMedalRepository.findByCountryOrderByCityAsc(countryName);
                } else {
                    medalsList = goldMedalRepository.findByCountryOrderByCityDesc(countryName);
                }
                break;
            case "name":
                if(ascendingOrder){
                    medalsList = goldMedalRepository.findByCountryOrderByNameAsc(countryName);
                } else {
                    medalsList = goldMedalRepository.findByCountryOrderByNameDesc(countryName);
                }
                break;
            case "event":
                if(ascendingOrder){
                    medalsList = goldMedalRepository.findByCountryOrderByEventAsc(countryName);
                } else {
                    medalsList = goldMedalRepository.findByCountryOrderByEventDesc(countryName);
                }
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        Optional<Country> countryOptional = countryRepository.findByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        int goldMedalCount = goldMedalRepository.countByCountry(country.getName());

        List<GoldMedal> summerWins = goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(country.getName(), "Summer");
        var numberSummerWins = !summerWins.isEmpty() ? summerWins.size() : null;
        List<GoldMedal> totalSummerEvents = goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(country.getName(),"Summer");
        var percentageTotalSummerWins = !summerWins.isEmpty() && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents.size() : null;
        var yearFirstSummerWin = !summerWins.isEmpty() ? summerWins.get(0).getYear() : null;

        var winterWins = goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(country.getName(), "Winter");
        var numberWinterWins = !winterWins.isEmpty() ? winterWins.size() : null;
        List<GoldMedal> totalWinterEvents = goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(country.getName(),"Winter");
        var percentageTotalWinterWins = !totalWinterEvents.isEmpty() && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents.size() : null;
        var yearFirstWinterWin = !winterWins.isEmpty() ? winterWins.get(0).getYear() : null;

        int numberEventsWonByFemaleAthletes = goldMedalRepository.countByGender("Female");
        int numberEventsWonByMaleAthletes = goldMedalRepository.countByGender("Male");

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                if(ascendingOrder){
                    countries = countryRepository.findAllByOrderByNameAsc();
                } else {
                    countries = countryRepository.findAllByOrderByNameDesc();
                }
                break;
            case "gdp":
                if(ascendingOrder){
                    countries = countryRepository.findAllByOrderByGdpAsc();
                } else {
                    countries = countryRepository.findAllByOrderByGdpDesc();
                }
                break;
            case "population":
                if(ascendingOrder){
                    countries = countryRepository.findAllByOrderByPopulationAsc();
                } else {
                    countries = countryRepository.findAllByOrderByPopulationDesc();
                }
                break;
            case "medals":
            default:
                countries = countryRepository.findAll();
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            int goldMedalCount = countryRepository.countByName(country.getName());
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
