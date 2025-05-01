package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class CourseSearchController {
    @FXML
    private TextField catalogField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField instructorField;

    @FXML
    private ListView<Course> courseListView;

    private CourseRepository courseRepo;
    private ReviewRepository reviewRepo;

    @FXML
    public void initialize() {
        this.courseRepo = CourseReviewsApplication.getCourseRepo();
        this.reviewRepo = CourseReviewsApplication.getReviewRepo();

        courseListView.setCellFactory(listView -> new ListCell<>() {
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    List<Review> reviews = reviewRepo.getReviewsByCourse(course);
                    String avgRating = reviews.isEmpty()
                            ? "N/A"
                            : String.format("%.2f", reviews.stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0));

                    setText(String.format("%s - %s | Instructor: %s | Rating: %s",
                            course.getCatalog(),
                            course.getName(),
                            course.getInstructor(),
                            avgRating));
                }
            }
        });

        refreshCourseList();
    }

    @FXML
    public void handleSearch() {
        refreshCourseList();
    }

    @FXML
    public void handleAddCourse() {
        AddCourseDialogController controller = CourseReviewsApplication.showDialog("add-course-dialog.fxml", "Add Course");

        if (controller != null) {
            controller.setCourseRepository(courseRepo);
            controller.setOnCourseAddedCallback(this::refreshCourseList);
        }
    }

    @FXML
    public void handleCourseClick() {
        Course selected = courseListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            CourseReviewsController controller = CourseReviewsApplication.loadScene("course-reviews.fxml");
            if (controller == null) {
                // #TODO show warning screen
            } else {
                controller.setSelectedCourse(selected);
            }
        }
    }

    @FXML
    public void handleMyReviews() {
        MyReviewsController controller = CourseReviewsApplication.loadScene("my-reviews.fxml");
        if (controller == null) {
            // #TODO show warning screen
        }
    }

    @FXML
    public void handleLogout() {
        CourseReviewsApplication.setLoggedInUser(null);
        LoginController controller = CourseReviewsApplication.loadScene("login.fxml");

        if (controller == null) {
            // #TODO set warning screen
        }
    }

    private void refreshCourseList() {
        courseListView.getItems().clear();

        String catalog = catalogField.getText().trim().toLowerCase();
        String name = nameField.getText().trim().toLowerCase();
        String instructor = instructorField.getText().trim().toLowerCase();

        for (Course course: courseRepo.getAllCourses()) {
            boolean matchesCatalog = catalog.isEmpty() || course.getCatalog().toLowerCase().contains(catalog);
            boolean matchesName = name.isEmpty() || course.getName().toLowerCase().contains(name);
            boolean matchesInstructor = instructor.isEmpty() || course.getInstructor().toLowerCase().contains(instructor);

            if (matchesCatalog && matchesName && matchesInstructor) {
                courseListView.getItems().add(course);
            }
        }
    }
}
