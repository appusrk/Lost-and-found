package com.example.lostandfound.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_history")
public class Match_history {

    public Match_history() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lost_item_id")
    private Lost_items lostItem;

    @ManyToOne
    @JoinColumn(name = "found_item_id")
    private Found_items foundItem;

    @Column(name = "lost_contact")
    private String lostContact;

    @Column(name = "found_contact")
    private String foundContact;
    
    @ManyToOne
    @JoinColumn(name = "USN", referencedColumnName = "USN", nullable = false)
    private Users user;

    @Column(name = "notification_sent")
    private boolean notificationSent = false;

    public boolean isNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

    // Getters and setters

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

    public String getLostContact() {
        return lostContact;
    }

    public void setLostContact(String lostContact) {
        this.lostContact = lostContact;
    }

    public String getFoundContact() {
        return foundContact;
    }

    public void setFoundContact(String foundContact) {
        this.foundContact = foundContact;
    }

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
    

}
