
package fsp.driverFrontend.Controller;

import fsp.driverFrontend.DemoData;
import fsp.driverFrontend.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

  @FXML
  private TextField emailField;

  @FXML
  private PasswordField passwordField;

  // Called when the Login button is clicked.
  @FXML
  private void handleLogin(ActionEvent event) {

    String email = emailField.getText().trim();
    String password = passwordField.getText();

    // Basic validation of the entered data.
    if (email.isEmpty() || password.isEmpty()) {
      showMessage("Please enter your email and password.");
      return;
    }

    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      showMessage("Please enter a valid email address.");
      return;
    }

    // DEMO: We do not verify the password on the server.
    // We only store the email address entered by the user.
    DemoData.currentDriverEmail = email;

    // Opens the application's main screen.
    Navigator.goTo(event, "dashboard.fxml");
  }

  // Opens the registration screen.
  @FXML
  private void handleRegister(ActionEvent event) {
    Navigator.goTo(event, "register.fxml");
  }

  // Displays a warning in a separate window.
  private void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Fancy Smart Parking");
    alert.setHeaderText("Login");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
