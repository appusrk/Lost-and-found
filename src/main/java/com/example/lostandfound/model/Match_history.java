package com.example.lostandfound.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_history")
public class Match_history {

	
    public Match_history() {
		
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lost_item_id")
    private Lost_items lostItem;

    @ManyToOne
    @JoinColumn(name = "found_item_id")
    private Found_items foundItem;

    @Column(name = "similarity_score")
    private Float similarityScore;

    @Enumerated(EnumType.STRING)
    private Status status = Status.suggested;

    @Column(name = "matched_on")
    private LocalDateTime matchedOn;

    public enum Status {
        suggested, confirmed, rejected
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Lost_items getLostItem() {
		return lostItem;
	}

	public void setLostItem(Lost_items lostItem) {
		this.lostItem = lostItem;
	}

	public Found_items getFoundItem() {
		return foundItem;
	}

	public void setFoundItem(Found_items foundItem) {
		this.foundItem = foundItem;
	}

	public Float getSimilarityScore() {
		return similarityScore;
	}

	public void setSimilarityScore(Float similarityScore) {
		this.similarityScore = similarityScore;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getMatchedOn() {
		return matchedOn;
	}

	public void setMatchedOn(LocalDateTime matchedOn) {
		this.matchedOn = matchedOn;
	}

    
}
