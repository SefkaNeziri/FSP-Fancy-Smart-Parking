
package fsp.driverFrontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

  @FXML
  private Label welcomeLabel;

  // initialize() se automatski poziva pri ucitavanju FXML-a.
  @FXML
  private void initialize() {
    welcomeLabel.setText(
        "Welcome, " + DemoData.currentDriverEmail
    );
  }

  @FXML
  private void handleSearch(ActionEvent event) {
    Navigator.goTo(event, "parking.fxml");
  }

  @FXML
  private void handleReservations(ActionEvent event) {
    Navigator.goTo(event, "reservations.fxml");
  }

  @FXML
  private void handlePayments(ActionEvent event) {
    Navigator.goTo(event, "payments.fxml");
  }

  @FXML
  private void handleNotifications(ActionEvent event) {
    Navigator.goTo(event, "notifications.fxml");
  }

  // Odjavljuje korisnika i vraca Login ekran.
  @FXML
  private void handleLogout(ActionEvent event) {
    DemoData.logout();
    Navigator.goTo(event, "login.fxml");
  }
}
