package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCourseDialogController implements DialogController {
    @FXML
    public TextField catalogField;

    @FXML
    public TextField nameField;

    @FXML
    public TextField instructorField;

    @FXML
    public Label messageLabel;

    private CourseRepository courseRepository;
    private Runnable onCourseAddedCallback;
    private Stage dialogStage;

    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void setOnCourseAddedCallback(Runnable onCourseAddedCallback) {
        this.onCourseAddedCallback = onCourseAddedCallback;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleAddCourse(ActionEvent actionEvent) {
        String catalog = catalogField.getText().trim();
        String name = nameField.getText().trim();
        String instructor = instructorField.getText().trim();

        if (catalog.isEmpty() || name.isEmpty() || instructor.isEmpty()) {
            messageLabel.setText("All fields are required.");
            return;
        }

        String[] parts = catalog.split("\\s+");
        if (parts.length != 2) {
            messageLabel.setText("Catalog must be in format: ABCD 1234");
            return;
        }

        String mnemonic = parts[0];
        String number = parts[1];

        if (!mnemonic.matches("[A-Za-z]{2,4}")) {
            messageLabel.setText("Mnemonic must be 2-4 letters.");
            return;
        }

        if (!number.matches("\\d{4}")) {
            messageLabel.setText("Course number must be 4 digits.");
            return;
        }

        if (name.length() < 1 || name.length() > 50) {
            messageLabel.setText("Course name must be between 1 and 50 characters.");
            return;
        }

        catalog = mnemonic.toUpperCase() + " " + number;
        Course course = new Course(catalog, name, instructor);
        courseRepository.createCourse(course);
        onCourseAddedCallback.run();
        dialogStage.close();
    }
}
