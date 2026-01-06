package com.company.interview.entity;


import jakarta.persistence.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating;

    private String comments;

    @OneToOne
    @JoinColumn(name = "interview_id", unique = true)
    private Interview interview;

    public Feedback() {
    }

    public Feedback(Integer rating, String comments, Interview interview) {
        this.rating = rating;
        this.comments = comments;
        this.interview = interview;
    }

    public Long getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }
}
