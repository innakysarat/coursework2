package com.example.coursework.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping()
    @PreAuthorize("hasAuthority('review:add')")
    public void addReview(
            // @AuthenticationPrincipal User currentUser,
            @RequestParam("internship_id") Long internship_id,
            @RequestBody Review review
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        reviewService.addReview(username, internship_id, review);
    }

    /**
     * Метод редактирования отзыва
     *
     * @param review отзыв
     * @param text   текст отзыва
     */
    @PutMapping(path = "/{review_id}")
    @PreAuthorize("hasAuthority('review:edit')")
    public void updateReviewText(
            @PathVariable(name = "review_id") Review review,
            @RequestParam("text") String text
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (username != null) {
            reviewService.updateReviewText(username, review, text);
        } else {
            throw new IllegalStateException("User must login");
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
    @GetMapping(path = "user/{user_id}")
    public Set<Review> userReviews(
            @PathVariable Integer user_id
    ) {
        return reviewService.getUserReviews(user_id);
    }

}
