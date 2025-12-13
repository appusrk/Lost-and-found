package com.example.lostandfound.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
public class Issues { 
	public Issues() {}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "issue_dept", nullable = false)
    private String issueDept;

    private String location;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "reported_contact")
    private String reportedContact;
    
    @Column(name = "image_hash")
    private String imageHash;  
    
    @Column(name = "reported_on")
    private LocalDateTime reportedOn =LocalDateTime.now(); 
    
    @Column(name = "status")
    private boolean status;
    
    
    public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	@ManyToOne
    @JoinColumn(name = "USN", referencedColumnName = "USN", nullable = false)
    private Users user;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIssueDept() {
		return issueDept;
	}

	public void setIssueDept(String issueName) {
		this.issueDept = issueName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getReportedContact() {
		return reportedContact;
	}

	public void setReportedContact(String reportedContact) {
		this.reportedContact = reportedContact;
	}

	public String getImageHash() {
		return imageHash;
	}

	public void setImageHash(String imageHash) {
		this.imageHash = imageHash;
	}

	public LocalDateTime getReportedOn() {
		return reportedOn;
	}

	public void setReportedOn(LocalDateTime reportedOn) {
		this.reportedOn = reportedOn;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
}
