package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userRepo.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Success!");
            CourseReviewsApplication.setLoggedInUser(user);

            CourseSearchController controller = CourseReviewsApplication.loadScene("course-search.fxml");
            if (controller == null) {
                // #TODO show warning screen
            }
        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        messageLabel.setTextFill(Color.RED);

        if (username.isEmpty()) {
            messageLabel.setText("Username is required.");
            return;
        }

        if (username.contains(" ")) {
            messageLabel.setText("Username can not contains spaces.");
            return;
        }

        if (password.isEmpty()) {
            messageLabel.setText("Password is required.");
            return;
        }

        if (password.contains(" ")) {
            messageLabel.setText("Password can not contains spaces.");
        }

        if (password.length() < 8) {
            messageLabel.setText("Password must be at least 8 characters.");
            return;
        }


        if (userRepo.getUserByUsername(username) != null) {
            messageLabel.setText("Username already exists.");
            return;
        }


        userRepo.createUser(new User(username, password));
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setText("Account created. You can now log in.");
    }

    @FXML
    public void handleExit(ActionEvent actionEvent) {
        CourseReviewsApplication.exit();
    }
}
