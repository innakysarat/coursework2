package com.example.coursework.internships;

import com.example.coursework.options.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("internships")
public class InternshipController {
    private final InternshipService internshipService;
    private final InternshipRepository internshipRepository;
    private final SubjectDao subjectDao;
    private final LanguageDao languageDao;
    private final PriceDao priceDao;
    private final CountryDao countryDao;

    @Autowired
    public InternshipController(InternshipService internshipService, InternshipRepository internshipRepository, SubjectDao subjectDao, LanguageDao languageDao, PriceDao priceDao, CountryDao countryDao) {
        this.internshipService = internshipService;
        this.internshipRepository = internshipRepository;
        this.subjectDao = subjectDao;
        this.languageDao = languageDao;
        this.priceDao = priceDao;
        this.countryDao = countryDao;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('course:write')")
    public void addInternship(@RequestBody Internship internship,
                              @RequestParam("organization_id") Long organization_id) {
        internshipService.addInternship(organization_id, internship);
    }

    @GetMapping()
    public List<Internship> getInternships() {
        return internshipService.getInternships();
    }

    @GetMapping(path = "{internship_id}")
    public Internship getInternship(@PathVariable Long internship_id) {
        return internshipService.getInternship(internship_id);
    }

    @PutMapping(path = "check/{internship_id}")
    @PreAuthorize("hasAuthority('course:check')")
    public void checkInternship(@PathVariable Long internship_id) {
        internshipService.checkInternship(internship_id);
    }

    @PutMapping(path = "/{internship_id}")
    @PreAuthorize("hasAuthority('course:write')")
    public void updateInternship(@PathVariable Long internship_id,
                                 @RequestBody Internship internship) {
        // добавить проверку на организацию - та ли исправляет
        internshipService.updateInternship(internship_id, internship.getName(), internship.getDescription(),
                internship.getStartDate(), internship.getFinishDate(),
                internship.getPrice(), internship.getCountry(), internship.getLanguage(), internship.getSubject());
    }

    @DeleteMapping(path = "/{internship_id}")
    public void deleteInternship(@PathVariable Long internship_id) {
        internshipService.deleteInternship(internship_id);
    }

    @GetMapping("/filter")
    public Iterable<Internship> find(@RequestParam(value = "search") String search) {
        InternshipPredicatesBuilder builder = new InternshipPredicatesBuilder();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression exp = builder.build();
        if (exp != null) {
            return internshipRepository.findAll(exp);
        } else {
            return internshipRepository.findAll();
        }
    }

    @GetMapping("/subjects")
    public Set<String> findSubjects() {
        return internshipService.findSubjects();
    }

    @GetMapping("/countries")
    public Set<String> findCountries() {
        return internshipService.findCountries();
    }

    @GetMapping("/languages")
    public Set<String> findLanguages() {
        return internshipService.findLanguages();
    }

    @GetMapping("/prices")
    public Set<Integer> findPrices() {
        return internshipService.findPrices();
    }

    @PostMapping("/favourites")
    public void addFavourites(
            @RequestParam(value="username") String username,
            @RequestParam(value = "internship_name") String internship_name
    ) {
        internshipService.addFavourites(username, internship_name);
    }
}
