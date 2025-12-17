package com.example.lostandfound.controller;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.UUID;
import java.util.Objects;

import com.example.lostandfound.model.Issues;
import com.example.lostandfound.model.Users;
import com.example.lostandfound.repository.IssueRepository;
import com.example.lostandfound.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Issues createIssue(
            @RequestParam String issue_dept,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam String usn,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException {

        Users user = userRepository.findByUsn(usn);
        if (user == null) throw new RuntimeException("User not found: " + usn);

        Issues issue = new Issues();
        issue.setIssueDept(issue_dept);
        issue.setDescription(description);
        issue.setLocation(location);
        issue.setUser(user);            // ← Correct way to set FK
        issue.setReportedOn(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get("uploads").resolve(fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, image.getBytes());

            issue.setImageUrl(filePath.toString());
            issue.setImageHash(Objects.toString(image.hashCode()));
        }

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

    
