package com.example.coursework.internships;

import com.example.coursework.options.*;
import com.example.coursework.student.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
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
    @CrossOrigin
    @PostMapping
    @PreAuthorize("hasAuthority('course:write')")
    public void addInternship(@RequestBody Internship internship,
                              @RequestParam("organization_id") Long organization_id) {
        // ТОЛЬКО РУКОВОДИТЕЛЬ
        internshipService.addInternship(organization_id, internship);
    }

    @CrossOrigin
    @GetMapping
    public List<Internship> getInternships() {
        return internshipService.getInternships();
    }
    @CrossOrigin
    @GetMapping(path = "/{internship_id}")
    public Internship getInternship(@PathVariable Long internship_id) {
        // любой пользователь, администратор, руководитель только нужный
        return internshipService.getInternship(internship_id);
    }
    @CrossOrigin
    @PutMapping(path = "/check/{internship_id}")
    @PreAuthorize("hasAuthority('course:check')")
    public void checkInternship(@PathVariable Long internship_id,
                                @RequestParam("check") boolean isChecked
    ) {
        internshipService.checkInternship(internship_id, isChecked);
    }
    @CrossOrigin
    @PutMapping(path = "/{internship_id}")
    @PreAuthorize("hasAuthority('course:write')")
    public void updateInternship(@PathVariable Long internship_id,
                                 @RequestBody Internship internship) {
        // добавить проверку на организацию - та ли исправляет
        internshipService.updateInternship(internship_id, internship.getName(), internship.getDescription(),
                internship.getStartDate(), internship.getFinishDate(),
                internship.getPrice(), internship.getCountry(), internship.getLanguage(), internship.getSubject());
    }
    @CrossOrigin
    @DeleteMapping(path = "/{internship_id}")
    @PreAuthorize("hasAuthority('course:write')")
    public void deleteInternship(@PathVariable Long internship_id) {
        // проверка на нужного руководителя
        internshipService.deleteInternship(internship_id);
    }
    @CrossOrigin
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
    @CrossOrigin
    @GetMapping("/subjects")
    public Set<String> findSubjects() {
        return internshipService.findSubjects();
    }
    @CrossOrigin
    @GetMapping("/countries")
    public Set<String> findCountries() {
        return internshipService.findCountries();
    }
    @CrossOrigin
    @GetMapping("/languages")
    public Set<String> findLanguages() {
        return internshipService.findLanguages();
    }
    @CrossOrigin
    @GetMapping("/prices")
    public Set<Integer> findPrices() {
        return internshipService.findPrices();
    }

    @CrossOrigin
    @PostMapping("/favourites")
    public void addFavourites(
            @RequestParam(value = "internship_name") String internship_name
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            internshipService.addFavourites(username, internship_name);
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
    }
    @CrossOrigin
    @DeleteMapping("/favourites")
    public void deleteFavourites(
            @RequestParam(value = "internship_name") String internship_name
    ) {
        // тот самый пользователь
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            internshipService.deleteFavourites(username, internship_name);
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
    }
    @CrossOrigin
    @GetMapping("/favourites")
    public Set<User> getUsersFavourites(
            @RequestParam(value = "internship_name") String internship_name
    ) {
        // админ + нужный руководитель
        return internshipService.getUsersFavourites(internship_name);
    }

    @CrossOrigin
    @GetMapping("/organization")
    public Long getOrganization(
            @RequestParam("internship") String internship_name
    ) {
        // админ + нужный руководитель
        return internshipService.getOrganization(internship_name);
    }

    @PostMapping(
            path = "{internship_id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('course:write')")
    public void uploadInternshipImage(@PathVariable("internship_id") Long internship_id,
                                      @RequestParam("image") MultipartFile file) {
        // нужный руковод
        internshipService.uploadInternshipImage(internship_id, file);
    }

    @GetMapping(path = "{internship_id}/image")
    public byte[] downloadInternshipImage(@PathVariable("internship_id") Long internship_id) {
        return internshipService.downloadInternshipImage(internship_id);
    }

    @DeleteMapping(path = "/{internship_id}/image")
    @PreAuthorize("hasAuthority('course:write')")
    public void deleteFile(@PathVariable Long internship_id) {
        // нужный руковод
        internshipService.deleteImage(internship_id);
    }
}
