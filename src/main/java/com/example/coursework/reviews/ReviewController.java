package com.example.coursework.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Метод добавления отзыва к стажировке
     *
     * @param internship_id id стажировки
     * @param review        отзыв
     */
    @PostMapping
    @PreAuthorize("hasAuthority('review:add')")
    public void addReview(
            @RequestParam("internship_id") Long internship_id,
            @RequestBody Review review
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            reviewService.addReview(username, internship_id, review);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User must login"
            );
        }
    }

    @DeleteMapping(path = "/{review_id}")
    public void deleteReview(
            @PathVariable(name = "review_id") Long review
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            reviewService.deleteReview(username, review);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User must login"
            );
        }
    }

    /**
     * Метод редактирования отзыва
     *
     * @param review отзыв
     */
    @PutMapping(path = "/{review_id}")
    @PreAuthorize("hasAuthority('review:edit')")
    public void updateReview(
            @PathVariable(name = "review_id") Long review_id,
            @RequestBody Review review
    ) {
        // добавить изменение по score
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            reviewService.updateReviewText(username, review, review_id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User must login"
            );
        }
    }

    /**
     * Метод получения отзывов о стажировке
     *
     * @param internship_id id стажировки
     * @return массив отзывов
     */
    @GetMapping(path = "/internship/{internship_id}")
    public Set<Review> internshipReviews(
            @PathVariable Long internship_id
    ) {
        return reviewService.getInternshipReviews(internship_id);
    }

    /**
     * Метод получения отзывов, оставленных конкретным пользователем
     *
     * @param user_id id пользователя
     * @return отзывы, оставленные пользователем
     */
    @GetMapping(path = "/user/{user_id}")
    public Set<Review> userReviews(
            @PathVariable Integer user_id
    ) {
        // нужный пользователь + администратор
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return reviewService.getUserReviews(user_id, username);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
    }

}
