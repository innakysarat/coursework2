package com.example.coursework.options;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class CountryDao {
    private final Set<String> countries;

    public CountryDao() {
        countries = new LinkedHashSet<>();
    }

    public Set<String> getCountries() {
        return countries;
    }

    public void addCountry(String country) {
        countries.add(country);
    }
}
