package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyReviewsController {
    @FXML
    public ListView<Review> reviewListView;

    private ReviewRepository reviewRepo;

    private void loadUserReviews() {
        User user = CourseReviewsApplication.getLoggedInUser();
        List<Review> reviews = reviewRepo.getReviewsByUser(user);
        reviewListView.getItems().setAll(reviews);
    }

    @FXML
    public void initialize() {
        reviewRepo = CourseReviewsApplication.getReviewRepo();
        loadUserReviews();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm");

        reviewListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Review review, boolean empty) {
                super.updateItem(review, empty);
                if (empty || review == null) {
                    setText(null);
                } else {
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
    public void handleBack(ActionEvent actionEvent) {
        CourseSearchController controller = CourseReviewsApplication.loadScene("course-search.fxml");
        if (controller == null) {
            // #TODO show warning screen
        }
    }

    @FXML
    public void handleReviewClicked() {
        Review selectedReview = reviewListView.getSelectionModel().getSelectedItem();
        if (selectedReview != null) {
            CourseReviewsController controller = CourseReviewsApplication.loadScene("course-reviews.fxml");
            if (controller == null) {
                // #TODO Add warning screen
            } else {
                controller.setSelectedCourse(selectedReview.getCourse());
            }
        }
    }
}
