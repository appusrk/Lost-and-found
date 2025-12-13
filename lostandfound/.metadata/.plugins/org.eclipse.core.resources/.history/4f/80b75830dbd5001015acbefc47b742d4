package com.example.lostandfound.controller;

import com.example.lostandfound.model.Issues;
import com.example.lostandfound.model.Users;
import com.example.lostandfound.repository.IssueRepository;
import com.example.lostandfound.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
public class IssueController {

    private final IssueRepository issuesRepository;
    private final UserRepository userRepository;

    public IssueController(IssueRepository issuesRepository, UserRepository userRepository) {
        this.issuesRepository = issuesRepository;
        this.userRepository = userRepository;
    }

    // ✔ Get all issues (for admin or public view)
    @GetMapping
    public List<Issues> getAllIssues() {
        return issuesRepository.findAll();
    }

    // ✔ Create issue
    @PostMapping("/create")
    public Issues createIssue(@RequestBody Issues issue) {

        String usn = issue.getUser().getUsn();  
        Users user = userRepository.findByUsn(usn);

        if (user == null) {
            throw new RuntimeException("User not found: " + usn);
        }

        issue.setUser(user);
        issue.setReportedOn(LocalDateTime.now());

        return issuesRepository.save(issue);
    }

    // ✔ Get issues by USN
    @GetMapping("/user/{usn}")
    public List<Issues> getIssuesByUser(@PathVariable String usn) {
        return issuesRepository.findAll();
    }
    
  
    // ✔ Delete issue
    @DeleteMapping("/{id}")
    public void deleteIssue(@PathVariable int id) {
        issuesRepository.deleteById(id);
    }
}

    
