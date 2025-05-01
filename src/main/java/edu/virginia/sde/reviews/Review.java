package edu.virginia.sde.reviews;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "REVIEWS")
public class Review {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "RATING", nullable = false)
    private int rating;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private Course course;

    public Review() {

    }

    public Review(String content, int rating, User user, Course course) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.course = course;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return String.format("%s (%d/5): %s", user != null ? user.getUsername() : "Anonymous", rating, content);
    }
}
