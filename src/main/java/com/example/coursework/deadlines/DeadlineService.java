package com.example.coursework.deadlines;

import com.example.coursework.internships.Internship;
import com.example.coursework.internships.InternshipRepository;
import com.example.coursework.student.User;
import com.example.coursework.student.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Service
public class DeadlineService {
    private final DeadlineRepository deadlineRepository;
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;

    @Autowired
    public DeadlineService(DeadlineRepository deadlineRepository,
                           UserRepository userRepository,
                           InternshipRepository internshipRepository) {
        this.deadlineRepository = deadlineRepository;
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
    }


    public void addDeadline(String username, Long internship_id, Deadline deadline) {
        User user = userRepository.findByUsername(username);
        Internship internship = internshipRepository.findById(internship_id)
                .orElseThrow(() -> new IllegalStateException("Internship not found"));
        deadline.setUser(user);
        deadline.setInternship(internship);
        user.addDeadline(deadline);
        internship.addDeadline(deadline);
        deadlineRepository.save(deadline);
        userRepository.save(user);
        internshipRepository.save(internship);
    }

    public void deleteDeadline(Long deadline_id, String username) {
        User user = userRepository.findByUsername(username);
        Deadline deadline = deadlineRepository.findById(deadline_id)
                .orElseThrow(() -> new IllegalStateException("Deadline not found"));
        Internship internship = deadline.getInternship();
        deadlineRepository.deleteById(deadline_id);
        user.removeDeadline(deadline);
        internship.removeDeadline(deadline);
        userRepository.save(user);
        internshipRepository.save(internship);
    }

    public Set<Deadline> getUserDeadlines(String username) {
        User user = userRepository.findByUsername(username);
        return user.getDeadlines();
    }

    public void updateDeadline(String username, Long deadline_id, String description,
                               LocalDate finish, LocalDate start, boolean isCompleted) {
        Deadline deadline = deadlineRepository.findById(deadline_id)
                .orElseThrow(() -> new IllegalStateException("Deadline not found"));
        User user = userRepository.findByUsername(username);
        Internship internship = deadline.getInternship();
        internship.removeDeadline(deadline);
        user.removeDeadline(deadline);
        if (description != null &&
                description.length() > 0 &&
                !Objects.equals(deadline.getDescription(), description)) {
            deadline.setDescription(description);
        }
        if (finish != null &&
                !Objects.equals(deadline.getFinish(), finish)) {
            deadline.setFinish(finish);
        }
        if (start != null &&
                !Objects.equals(deadline.getStart(), start)) {
            deadline.setStart(start);
        }
        if (isCompleted != deadline.isCompleted()) {
            deadline.setCompleted(isCompleted);
        }
        user.addDeadline(deadline);
        internship.addDeadline(deadline);
        userRepository.save(user);
        internshipRepository.save(internship);
        deadlineRepository.save(deadline);
    }

    public Deadline getUserDeadline(String username, Long deadline_id) {
        User user = userRepository.findByUsername(username);
        Deadline deadline = deadlineRepository.findById(deadline_id)
                .orElseThrow(() -> new IllegalStateException("Deadline not found"));
        if (Objects.equals(deadline.getUser(), user)) {
            return deadline;
        } else {
            throw new IllegalStateException("Cannot get someone else's deadline");
        }
    }
}
