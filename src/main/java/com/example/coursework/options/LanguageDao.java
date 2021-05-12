package com.example.coursework.options;

import org.springframework.stereotype.Component;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class LanguageDao {
   private final Set<String> languages;

    public LanguageDao() {
        languages = new LinkedHashSet<>();
    }

    public void addLanguage(String language) {
        languages.add(language);
    }

    public Set<String> getLanguages() {
        return languages;
    }
}
