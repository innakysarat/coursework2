package com.example.coursework.reviews;

import com.example.coursework.internships.Internship;
import com.example.coursework.internships.InternshipRepository;
import com.example.coursework.student.UserRepository;
import com.example.coursework.student.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         UserRepository userRepository,
                         InternshipRepository internshipRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
    }

    public void addReview(String username, Long internship_id, Review review) {
        Optional<Internship> internshipOptional = internshipRepository.findById(internship_id);
        Internship internship;
        if (internshipOptional.isPresent()) {
            internship = internshipOptional.get();
            User user = userRepository.findByUsername(username);
            user.addReview(review); // добавляю пользователю его отзыв в коллекцию
            internship.addReview(review); // добавляю к стажировке ещё один отзыв
            review.setAuthor(user); // устанавливаю автора отзыва
            review.setInternship(internship); // устанавливаю стажировку
            reviewRepository.save(review); // сохраняю
        }
    }

    public void updateReviewText(String username, Review review, Long review_id) {
        Review review_check = reviewRepository.findById(review_id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Review not found"
                ));
        User user = userRepository.findByUsername(username);
        if (review_check.getAuthor().equals(user)) {
            if (!org.thymeleaf.util.StringUtils.isEmpty(review.getTextcomment())) {
                review_check.setTextcomment(review.getTextcomment());
            }
          /*  if (review_check.getScore() != review.getScore()) {
                review.setScore(review.getScore());
            }*/
            reviewRepository.save(review_check);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot update someone else's review"
            );
        }
    }

    public Set<Review> getUserReviews(Integer user_id, String username) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));
        Set<Review> reviewSet;
        User user_byUsername = userRepository.findByUsername(username);
        if (!user_byUsername.equals(user)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User cannot get someone else's reviews"
            );
        } else {
            reviewSet = user.getReviews();
            return reviewSet;
        }
    }

    public Set<Review> getInternshipReviews(Long internship_id) {
        Optional<Internship> internshipOptional = internshipRepository.findById(internship_id);
        Internship internship;
        Set<Review> reviewSet = new HashSet<>();
        if (internshipOptional.isPresent()) {
            internship = internshipOptional.get();
            reviewSet = internship.getReviews();
        }
        return reviewSet;
    }

    // ПОЧИНИТЬ !!!!
    public void deleteReview(String username, Long review_id) {
        User user = userRepository.findByUsername(username);
        Review review = reviewRepository.findById(review_id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Review not found"));
        if (review.getAuthor().equals(user)) {
            reviewRepository.deleteById(review_id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot delete someone else's review"
            );
        }
    }
}
