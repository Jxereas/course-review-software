package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AddReviewDialogController implements DialogController {
    @FXML
    public Spinner<Integer> ratingSpinner;

    @FXML
    public TextArea commentArea;

    @FXML
    public Label messageLabel;

    private Course course;
    private ReviewRepository reviewRepo;
    private Stage dialogStage;
    private Runnable onReviewAdded;
    private Review editingReview;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setOnReviewAdded(Runnable callback) {
        this.onReviewAdded = callback;
    }

    public void setEditingReview(Review review) {
        this.editingReview = review;
        ratingSpinner.getValueFactory().setValue(review.getRating());
        commentArea.setText(review.getContent());
    }

    @FXML
    public void initialize() {
        ratingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5));
        reviewRepo = CourseReviewsApplication.getReviewRepo();
    }

    @FXML
    public void handleAddReview(ActionEvent actionEvent) {
        int rating = ratingSpinner.getValue();
        String content = commentArea.getText();

        if (editingReview != null) {
            editingReview.setRating(rating);
            editingReview.setContent(content);
            editingReview.setTimestamp(LocalDateTime.now());
            reviewRepo.updateReview(editingReview);
        } else {
            if (reviewRepo.hasUserReviewedCourse(CourseReviewsApplication.getLoggedInUser(), course)) {
                messageLabel.setText("You have already added a review for this course.");
                return;
            }

            Review review = new Review(content, rating, CourseReviewsApplication.getLoggedInUser(), course);
            reviewRepo.createReview(review);
        }

        onReviewAdded.run();
        dialogStage.close();
    }
}
