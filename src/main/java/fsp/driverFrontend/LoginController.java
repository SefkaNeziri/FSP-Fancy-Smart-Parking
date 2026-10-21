
package fsp.driverFrontend;

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

  // Poziva se klikom na Login dugme.
  @FXML
  private void handleLogin(ActionEvent event) {

    String email = emailField.getText().trim();
    String password = passwordField.getText();

    // Osnovna provera unetih podataka.
    if (email.isEmpty() || password.isEmpty()) {
      showMessage("Please enter your email and password.");
      return;
    }

    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      showMessage("Please enter a valid email address.");
      return;
    }

    // DEMO: ne proveravamo sifru na serveru.
    // Samo pamtimo koji je email korisnik uneo.
    DemoData.currentDriverEmail = email;

    // Otvaramo glavni ekran aplikacije.
    Navigator.goTo(event, "dashboard.fxml");
  }

  // Otvara ekran za registraciju.
  @FXML
  private void handleRegister(ActionEvent event) {
    Navigator.goTo(event, "register.fxml");
  }

  // Prikazuje upozorenje u posebnom prozoru.
  private void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Fancy Smart Parking");
    alert.setHeaderText("Login");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
