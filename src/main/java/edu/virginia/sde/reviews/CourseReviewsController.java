package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CourseReviewsController {
    @FXML
    public Label courseTitleLabel;

    @FXML
    public Label instructorLabel;

    @FXML
    public Label averageRatingLabel;

    @FXML
    public ListView<Review> reviewListView;

    @FXML
    public Label messageLabel;

    private Course selectedCourse;
    private ReviewRepository reviewRepo;

    public void setSelectedCourse(Course course) {
        this.selectedCourse = course;
        courseTitleLabel.setText(course.getCatalog() + " - " + course.getName());
        instructorLabel.setText("Instructor: " + course.getInstructor());

        refreshAverageReviewRating();
        refreshReviews();
    }

    private void refreshAverageReviewRating() {
        List<Review> reviews = reviewRepo.getReviewsByCourse(selectedCourse);
        String avgRating = reviews.isEmpty()
                ? "N/A"
                : String.format("%.2f", reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0));

        averageRatingLabel.setText("Rating: "+ avgRating);
    }

    private void refreshReviews() {
        refreshAverageReviewRating();
        reviewListView.getItems().clear();
        List<Review> reviews = reviewRepo.getReviewsByCourse(selectedCourse);
        reviewListView.getItems().setAll(reviews);
    }

    @FXML
    public void initialize() {
        reviewRepo = CourseReviewsApplication.getReviewRepo();

        reviewListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Review review, boolean empty) {
                super.updateItem(review, empty);
                if (empty || review == null) {
                    setText(null);
                } else {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm");

                    StringBuilder text = new StringBuilder();
                    text.append(String.format("Rating: %.2f", (double) review.getRating()));
                    text.append("\nDate: ").append(review.getTimestamp().format(dtf));

                    if (!review.getContent().isEmpty()) {
                        text.append("\nComment: ").append(review.getContent());
                    }

                    setText(text.toString());
                }
            }
        });
    }

    @FXML
    public void handleAddReview(ActionEvent actionEvent) {
        AddReviewDialogController controller = CourseReviewsApplication.showDialog("add-review-dialog.fxml", "Add Review");
        if (controller == null) {
            // #TODO load warning screen
        } else {
            controller.setCourse(selectedCourse);
            controller.setOnReviewAdded(this::refreshReviews);
        }
    }

    @FXML
    public void handleEditReview() {
        Review review = reviewRepo.getReviewByUserAndCourse(CourseReviewsApplication.getLoggedInUser(), selectedCourse);
        if (review == null) {
            messageLabel.setText("You have no reviews in this course.");
            return;
        }

        AddReviewDialogController controller = CourseReviewsApplication.showDialog("add-review-dialog.fxml", "Edit Review");
        if (controller == null) {
            // #TODO show warning screen
        } else {
            controller.setCourse(selectedCourse);
            controller.setEditingReview(review);
            controller.setOnReviewAdded(this::refreshReviews);
        }
    }

    @FXML
    public void handleDeleteReview() {
        Review review = reviewRepo.getReviewByUserAndCourse(CourseReviewsApplication.getLoggedInUser(), selectedCourse);
        if (review == null) {
            messageLabel.setText("You have no reviews in this course.");
            return;
        }

        reviewRepo.deleteReview(review);
        refreshReviews();
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        CourseSearchController controller = CourseReviewsApplication.loadScene("course-search.fxml");
        if (controller == null) {
            // #TODO load warning screen
        }
    }
}
