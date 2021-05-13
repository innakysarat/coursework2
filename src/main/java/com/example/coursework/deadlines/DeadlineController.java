package com.example.coursework.deadlines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("api/deadlines")
public class DeadlineController {
    private final DeadlineService deadlineService;

    @Autowired
    public DeadlineController(DeadlineService deadlineService) {
        this.deadlineService = deadlineService;
    }

    @PostMapping
    public void addDeadline(
            @RequestParam("internship") Long internship_id,
            @RequestBody Deadline deadline) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (username != null) {
            deadlineService.addDeadline(username, internship_id, deadline);
        }
    }

    @DeleteMapping(path = "/{deadline_id}")
    public void deleteDeadline(@PathVariable Long deadline_id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            deadlineService.deleteDeadline(deadline_id, username);
        }
    }

    @GetMapping
    public Set<Deadline> getUserDeadlines() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return deadlineService.getUserDeadlines(username);
        } else {
            throw new IllegalStateException("Username is null");
        }
    }

    @GetMapping(path = "/{deadline_id}")
    public Deadline getUserDeadline(@PathVariable Long deadline_id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return deadlineService.getUserDeadline(username, deadline_id);
        } else {
            throw new IllegalStateException("Username is null");
        }
    }

    @PutMapping(path = "/{deadline_id}")
    public void updateDeadline(@PathVariable Long deadline_id,
                               @RequestBody Deadline deadline) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            deadlineService.updateDeadline(username, deadline_id, deadline.getDescription(),
                    deadline.getFinish(), deadline.getStart(), deadline.isCompleted());
        }
    }
}
