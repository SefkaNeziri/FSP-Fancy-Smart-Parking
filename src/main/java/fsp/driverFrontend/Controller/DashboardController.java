
package fsp.driverFrontend.Controller;

import fsp.driverFrontend.DemoData;
import fsp.driverFrontend.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

  @FXML
  private Label welcomeLabel;

  // initialize() is automatically called when the FXML file is loaded.
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

  // Logs out the user and returns to the Login screen.
  @FXML
  private void handleLogout(ActionEvent event) {
    DemoData.logout();
    Navigator.goTo(event, "login.fxml");
  }
}
