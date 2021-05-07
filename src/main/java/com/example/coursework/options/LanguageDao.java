package com.example.coursework.options;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class LanguageDao {
    Set<String> languages = new LinkedHashSet<>();

    public void addLanguage(String language) {
        languages.add(language);
    }
}
