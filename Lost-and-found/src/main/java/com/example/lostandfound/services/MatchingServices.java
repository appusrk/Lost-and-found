package com.example.lostandfound.services;

import com.example.lostandfound.model.Found_items;
import com.example.lostandfound.model.Lost_items;
import com.example.lostandfound.model.Match_history;
import com.example.lostandfound.repository.FoundItemRepository;
import com.example.lostandfound.repository.LostItemRepository;
import com.example.lostandfound.repository.MatchingHistoryRepository;

import org.springframework.stereotype.Service;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchingServices {

    private final LostItemRepository lostRepo;
    private final FoundItemRepository foundRepo;
    private final MatchingHistoryRepository matchHistoryRepo;
    private final NotificationService notificationService;
    private final AIService aiService;

    public MatchingServices(LostItemRepository lostRepo,
                            FoundItemRepository foundRepo,
                            MatchingHistoryRepository matchHistoryRepo,
                            NotificationService notificationService,
                            AIService aiService) {
        this.lostRepo = lostRepo;
        this.foundRepo = foundRepo;
        this.matchHistoryRepo = matchHistoryRepo;
        this.notificationService = notificationService;
        this.aiService = aiService;
    }

    // ---------------- HELPER FUNCTIONS ----------------

    private boolean isNameSimilar(String name1, String name2) {
    if (name1 == null || name2 == null) return false;

    // Convert to lowercase and split into tokens
    String[] tokens1 = name1.toLowerCase().split("\\s+");
    String[] tokens2 = name2.toLowerCase().split("\\s+");

    // Optional: ignore common words like "the", "at", "in"
    Set<String> stopWords = new HashSet<>(Arrays.asList("the", "at", "in", "on", "and", "&"));
    
    LevenshteinDistance ld = new LevenshteinDistance();
    int matches = 0;

    for (String t1 : tokens1) {
        if (stopWords.contains(t1)) continue; // ignore stop words
        for (String t2 : tokens2) {
            if (stopWords.contains(t2)) continue;
            
            // Exact match or fuzzy match (distance <=1)
            if (t1.equals(t2) || ld.apply(t1, t2) <= 1) {
                matches++;
                break; // move to next t1 token after first match
            }
        }
    }

    double similarity = (2.0 * matches) / (tokens1.length + tokens2.length);
    return similarity >= 0.5; // threshold: 50% words match
}
private boolean isLocSimilar(String name1, String name2) {
    if (name1 == null || name2 == null) return false;

    // Convert to lowercase and split into tokens
    String[] tokens1 = name1.toLowerCase().split("\\s+");
    String[] tokens2 = name2.toLowerCase().split("\\s+");

    // Optional: ignore common words like "the", "at", "in"
    Set<String> stopWords = new HashSet<>(Arrays.asList("the", "at", "in", "on", "and", "&"));
    
    LevenshteinDistance ld = new LevenshteinDistance();
    int matches = 0;

    for (String t1 : tokens1) {
        if (stopWords.contains(t1)) continue; // ignore stop words
        for (String t2 : tokens2) {
            if (stopWords.contains(t2)) continue;
            
            // Exact match or fuzzy match (distance <=1)
            if (t1.equals(t2) || ld.apply(t1, t2) <= 1) {
                matches++;
                break; // move to next t1 token after first match
            }
        }
    }

    double similarity = (2.0 * matches) / (tokens1.length + tokens2.length);
    return similarity >= 0.5; // threshold: 50% words match
}


    private boolean isDescriptionSimilar(String d1, String d2) {
        if (d1 == null || d2 == null) return false;
        d1 = d1.toLowerCase();
        d2 = d2.toLowerCase();
        int distance = LevenshteinDistance.getDefaultInstance().apply(d1, d2);
        int maxLen = Math.max(d1.length(), d2.length());
        return ((double) distance / maxLen) <= 0.4;
    }

    private int hamdis(String h1, String h2) {
        int dis = 0;
        int len = Math.min(h1.length(), h2.length());
        for (int i = 0; i < len; i++) {
            if (h1.charAt(i) != h2.charAt(i)) dis++;
        }
        return dis + Math.abs(h1.length() - h2.length());
    }

    private boolean isImageSimilar(Found_items found, Lost_items lost) {
        if (found.getImageEmbedding() != null && lost.getImageEmbedding() != null) {
            float[] vec1 = aiService.getImageEmbedding(found.getImageUrl());
            float[] vec2 = aiService.getImageEmbedding(lost.getImageUrl());
            double similarity = aiService.cosineSimilarity(vec1, vec2);
            return similarity >= 0.85;
        }
        if (found.getImageHash() != null && lost.getImageHash() != null) {
            int distance = hamdis(found.getImageHash(), lost.getImageHash());
            return distance <= 8;
        }
        return false;
    }

    // ---------------- MAIN LOGIC ----------------

    public void findMatchesForLost(Lost_items lost) {

        List<Found_items> matches = foundRepo.findAll()
                .stream()
                .filter(f -> {
                    boolean nameMatch = isNameSimilar(f.getItemName(), lost.getItemName());
                    boolean descMatch = isDescriptionSimilar(f.getDescription(), lost.getDescription());
                    boolean imageMatch = isImageSimilar(f, lost);

                    boolean basicMatch = nameMatch && descMatch;
                    boolean imageCondition = imageMatch || lost.getImageUrl() == null || f.getImageUrl() == null;

                    return (basicMatch || imageCondition)
                            && isLocSimilar(f.getLocation(),lost.getLocation());
                })
                .collect(Collectors.toList());

        for (Found_items found : matches) {

            Match_history existing = matchHistoryRepo.findByLostItemAndFoundItem(lost, found)
                    .orElse(null);

            if (existing == null) {
                // First time match → create row
                existing = new Match_history();
                existing.setLostItem(lost);
                existing.setFoundItem(found);
                existing.setUser(lost.getUser());
                existing.setLostContact(lost.getEmail());
                existing.setFoundContact(found.getEmail());
                existing.setNotificationSent(false); // default
                matchHistoryRepo.save(existing);
            }

            // If notification already sent, skip it
            if (existing.isNotificationSent()) {
                continue;
            }

            // Send notification ONCE
            String subject = "Possible Match Found!";
            String body = "Hi!\n\nWe found a possible match for your lost item: "
                    + lost.getItemName()
                    + "\nDescription: " + lost.getDescription()
                    + "\nLocation: " + lost.getLocation()
                    + "\nContact the other user: " + found.getEmail()
                    + "\n\nFindify Team";

            notificationService.sendEmail(lost.getEmail(), subject, body);
            notificationService.sendEmail(found.getEmail(), subject, body);

            existing.setNotificationSent(true);
            matchHistoryRepo.save(existing);
        }
    }

    public void findMatchesForFound(Found_items found) {

        List<Lost_items> matches = lostRepo.findAll()
                .stream()
                .filter(l -> {
                    boolean nameMatch = isNameSimilar(l.getItemName(), found.getItemName());
                    boolean descMatch = isDescriptionSimilar(l.getDescription(), found.getDescription());
                    boolean imageMatch = isImageSimilar(found, l);

                    boolean basicMatch = nameMatch && descMatch;
                    boolean imageCondition = imageMatch || found.getImageUrl() == null || l.getImageUrl() == null;

                    return (basicMatch || imageCondition)
                            && isLocSimilar(l.getLocation(),found.getLocation());
                })
                .collect(Collectors.toList());

        for (Lost_items lost : matches) {

            Match_history existing = matchHistoryRepo.findByLostItemAndFoundItem(lost, found)
                    .orElse(null);

            if (existing == null) {
                existing = new Match_history();
                existing.setLostItem(lost);
                existing.setFoundItem(found);
                existing.setUser(lost.getUser());
                existing.setLostContact(lost.getEmail());
                existing.setFoundContact(found.getEmail());
                existing.setNotificationSent(false);
                matchHistoryRepo.save(existing);
            }

            if (existing.isNotificationSent()) {
                continue;
            }

            String subject = "Possible Match Found!";
            String body = "Hi!\n\nWe found a possible match for your found item: "
                    + found.getItemName()
                    + "\nDescription: " + found.getDescription()
                    + "\nLocation: " + found.getLocation()
                    + "\nContact the other user: " + lost.getEmail()
                    + "\n\nFindify Team";

            notificationService.sendEmail(found.getEmail(), subject, body);
            notificationService.sendEmail(lost.getEmail(), subject, body);

            existing.setNotificationSent(true);
            matchHistoryRepo.save(existing);
        }
    }
}
