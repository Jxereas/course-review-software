package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;

    @FXML
    public Label messageLabel;

    private UserRepository userRepo;

    public void initialize() {
        userRepo = CourseReviewsApplication.getUserRepo();
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        User user = userRepo.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            messageLabel.setText("Success!");
            CourseReviewsApplication.setLoggedInUser(user);

            CourseSearchController controller = CourseReviewsApplication.loadScene("course-search.fxml");
            if (controller == null) {
                // #TODO show warning screen
            }
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.length() < 8) {
            messageLabel.setText("Username required and password must be at least 8 characters.");
            return;
        }

        if (userRepo.getUserByUsername(username) != null) {
            messageLabel.setText("Username already exists.");
            return;
        }

        userRepo.createUser(new User(username, password));
        messageLabel.setText("Account created. You can now log in.");
    }

    @FXML
    public void handleExit(ActionEvent actionEvent) {
        CourseReviewsApplication.exit();
    }
}
