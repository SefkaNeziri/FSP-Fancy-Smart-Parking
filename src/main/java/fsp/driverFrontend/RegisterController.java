
package fsp.driverFrontend;

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

  // Obradjuje klik na Create Account.

  @FXML
  private void handleRegister(ActionEvent event) {

    // Uzimamo podatke koje je korisnik uneo.
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String password = passwordField.getText();
    String confirmation = confirmPasswordField.getText();

    // Proveravamo da li su sva polja popunjena.
    if (name.isEmpty() || email.isEmpty()
        || password.isEmpty() || confirmation.isEmpty()) {

      showMessage(
          Alert.AlertType.WARNING,
          "Please fill in all fields."
      );
      return;
    }

    // Proveravamo osnovni format email adrese.
    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
      showMessage(
          Alert.AlertType.WARNING,
          "Please enter a valid email address."
      );
      return;
    }

    // Lozinka mora imati najmanje 8 karaktera.
    if (password.length() < 8) {
      showMessage(
          Alert.AlertType.WARNING,
          "Password must contain at least 8 characters."
      );
      return;
    }

    // Lozinka i potvrda lozinke moraju biti iste.
    if (!password.equals(confirmation)) {
      showMessage(
          Alert.AlertType.WARNING,
          "Passwords do not match."
      );
      return;
    }

    // DEMO: cuvamo email kao trenutno prijavljenog vozaca.
    // Pravi nalog ce kreirati backend kada ga povezemo.
    DemoData.currentDriverEmail = email;

    // Obavestavamo korisnika da je ovo demo registracija.
    showMessage(
        Alert.AlertType.INFORMATION,
        "Registration completed in demo mode. Welcome, " + name + "!"
    );

    // Odmah otvaramo Dashboard, bez povratka na Login.
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
