package com.example.coursework.internships;

import com.example.coursework.options.*;
import com.example.coursework.student.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("internships")
public class InternshipController {
    private final InternshipService internshipService;
    private final InternshipRepository internshipRepository;

    @Autowired
    public InternshipController(InternshipService internshipService,
                                InternshipRepository internshipRepository) {
        this.internshipService = internshipService;
        this.internshipRepository = internshipRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('course:write')")
    public void addInternship(@RequestBody Internship internship,
                              @RequestParam("organization_id") Long organization_id) {
        internshipService.addInternship(organization_id, internship);
    }

    @CrossOrigin
    @GetMapping
    public List<Internship> getInternships() {
        return internshipService.getInternships();
    }

    @GetMapping(path = "/{internship_id}")
    public Internship getInternship(@PathVariable Long internship_id) {
        return internshipService.getInternship(internship_id);
    }

    @PutMapping(path = "/check/{internship_id}")
    @PreAuthorize("hasAuthority('course:check')")
    public void checkInternship(@PathVariable Long internship_id,
                                @RequestParam("check") boolean isChecked
    ) {
        internshipService.checkInternship(internship_id, isChecked);
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
    @PreAuthorize("hasAuthority('course:write')")
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
            @RequestParam(value = "username") String username,
            @RequestParam(value = "internship_name") String internship_name
    ) {
        internshipService.addFavourites(username, internship_name);
    }

    @DeleteMapping("/favourites")
    public void deleteFavourites(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "internship_name") String internship_name
    ) {
        internshipService.deleteFavourites(username, internship_name);
    }

    @GetMapping("/favourites")
    public Set<User> getUsersFavourites(
            @RequestParam(value = "internship_name") String internship_name
    ) {
        return internshipService.getUsersFavourites(internship_name);
    }

    @PostMapping(
            path = "{internship_id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('course:write')")
    public void uploadInternshipImage(@PathVariable("internship_id") Long internship_id,
                                      @RequestParam("image") MultipartFile file) {
        internshipService.uploadInternshipImage(internship_id, file);
    }

    @GetMapping(path = "{internship_id}/image")
    public byte[] downloadInternshipImage(@PathVariable("internship_id") Long internship_id) {
        return internshipService.downloadInternshipImage(internship_id);
    }

    @DeleteMapping(path = "/{internship_id}/image")
    @PreAuthorize("hasAuthority('course:write')")
    public void deleteFile(@PathVariable Long internship_id) {
        internshipService.deleteImage(internship_id);
    }
}
