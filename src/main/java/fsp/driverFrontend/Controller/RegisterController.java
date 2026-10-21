
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

  // Handles the Create Account button click.
  @FXML
  private void handleRegister(ActionEvent event) {

    // Gets the data entered by the user.
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String password = passwordField.getText();
    String confirmation = confirmPasswordField.getText();

    // Checks whether all fields are filled in.
    if (name.isEmpty() || email.isEmpty()
        || password.isEmpty() || confirmation.isEmpty()) {

      showMessage(
          Alert.AlertType.WARNING,
          "Please fill in all fields."
      );
      return;
    }

    // Validates the basic email address format.
    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      showMessage(
          Alert.AlertType.WARNING,
          "Please enter a valid email address."
      );
      return;
    }

    // The password must be at least 8 characters long.
    if (password.length() < 8) {
      showMessage(
          Alert.AlertType.WARNING,
          "Password must contain at least 8 characters."
      );
      return;
    }

    // The password and confirmation password must match.
    if (!password.equals(confirmation)) {
      showMessage(
          Alert.AlertType.WARNING,
          "Passwords do not match."
      );
      return;
    }

    // DEMO: Stores the email as the currently logged-in driver.
    // The backend will create the actual account once it is connected.

    DemoData.currentDriverEmail = email;

    // Informs the user that this is a demo registration.
    showMessage(
        Alert.AlertType.INFORMATION,
        "Registration completed in demo mode. Welcome, " + name + "!"
    );

    // Opens the Dashboard immediately without returning to the Login screen.
    Navigator.goTo(event, "dashboard.fxml");
  }


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
