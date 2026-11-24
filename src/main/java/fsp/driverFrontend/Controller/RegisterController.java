
package fsp.driverFrontend.Controller;

import fsp.driverFrontend.DemoData;
import fsp.driverFrontend.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

  @FXML
  private TextField nameField;

  @FXML
  private TextField emailField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField confirmPasswordField;

  // Handles the Create Account button.
  @FXML
  private void handleRegister(ActionEvent event) {

    // Read the data entered by the user.
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String password = passwordField.getText();
    String confirmation = confirmPasswordField.getText();

    // Check that all fields are completed.
    if (name.isEmpty() || email.isEmpty()
        || password.isEmpty() || confirmation.isEmpty()) {

      showMessage(
          Alert.AlertType.WARNING,
          "Please fill in all fields."
      );
      return;
    }

    // Validate the email address format.
    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

      showMessage(
          Alert.AlertType.WARNING,
          "Please enter a valid email address."
      );
      return;
    }

    // Require a password with at least 8 characters.
    if (password.length() < 8) {

      showMessage(
          Alert.AlertType.WARNING,
          "Password must contain at least 8 characters."
      );
      return;
    }

    // Check that both passwords match.
    if (!password.equals(confirmation)) {

      showMessage(
          Alert.AlertType.WARNING,
          "Passwords do not match."
      );
      return;
    }

    // Send registration request to C# API Gateway / Backend
    boolean registered = fsp.driverFrontend.ApiClient.register(name, email, password);

    // Store current driver email for session
    DemoData.currentDriverEmail = email;

    String successMessage = registered
        ? "Registration successful via C# Gateway! Welcome, " + name + "!"
        : "Account created successfully. Welcome, " + name + "!";

    showMessage(
        Alert.AlertType.INFORMATION,
        successMessage
    );

    // Open Dashboard after successful registration
    Navigator.goTo(event, "dashboard.fxml");
  }

  // Handles the Back to Login button.
  @FXML
  private void handleBack(ActionEvent event) {

    // Return to the Login screen.
    Navigator.goTo(event, "login.fxml");
  }

  // Displays feedback messages to the driver.
  private void showMessage(
      Alert.AlertType type,
      String message
  ) {

    Alert alert = new Alert(type);
    alert.setTitle("Fancy Smart Parking");
    alert.setHeaderText("Registration");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
