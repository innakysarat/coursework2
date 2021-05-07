package com.example.coursework.options;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class CountryDao {
    Set<String> countries = new LinkedHashSet<>();

    public void addCountry(String country) {
        countries.add(country);
    }
}
