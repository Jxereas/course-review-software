package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseReviewsApplication extends Application {
    private static final UserRepository userRepo = new UserRepository();
    private static final CourseRepository courseRepo = new CourseRepository();
    private static final ReviewRepository reviewRepo = new ReviewRepository();
    private static User loggedInUser;
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("Course Reviews");

        LoginController controller = loadScene("login.fxml");
        if (controller == null) {
            // #TODO show warning screen
        }

        stage.show();
    }

    public static <T> T loadScene(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewsApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
            return fxmlLoader.getController();
        } catch (IOException e) {
            System.err.println("Couldn't load " + fxmlPath);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static <T extends DialogController> T showDialog(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewsApplication.class.getResource(fxmlPath));

            Stage dialog = new Stage();
            dialog.setTitle(title);
            dialog.initOwner(primaryStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setScene(new Scene (fxmlLoader.load()));

            T controller = fxmlLoader.getController();
            controller.setDialogStage(dialog);

            dialog.show();

            return controller;
        } catch (IOException e) {
            System.err.println("Couldn't load " + fxmlPath);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static CourseRepository getCourseRepo() {
        return courseRepo;
    }

    public static UserRepository getUserRepo() {
        return userRepo;
    }

    public static ReviewRepository getReviewRepo() {
        return reviewRepo;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        CourseReviewsApplication.loggedInUser = loggedInUser;
    }

    public static void exit() {
        primaryStage.close();
    }
}
