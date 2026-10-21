
package fsp.driverFrontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class PaymentsController {

  @FXML
  private ComboBox<DemoData.Reservation> reservationComboBox;

  @FXML
  private Label paymentDetailsLabel;

  @FXML
  private void initialize() {

    reservationComboBox.getItems().setAll(
        DemoData.myReservations().stream()
            .filter(r -> !r.cancelled)
            .toList()
    );

    reservationComboBox.valueProperty().addListener(
        (observable, oldValue, newValue) -> updateDetails()
    );

    reservationComboBox.getSelectionModel().selectFirst();
    updateDetails();
  }

  // Prikazuje informacije o izabranoj rezervaciji.
  private void updateDetails() {

    DemoData.Reservation selected =
        reservationComboBox.getValue();

    if (selected == null) {
      paymentDetailsLabel.setText(
          "No active reservations found."
      );
      return;
    }

    String status = selected.paid ? "PAID" : "UNPAID";

    paymentDetailsLabel.setText(
        String.format(
            "Total: €%.2f | Status: %s",
            selected.getTotalPrice(),
            status
        )
    );
  }

  // DEMO: menja status, ali ne izvrsava stvarnu naplatu.
  @FXML
  private void handleSimulatePayment() {

    DemoData.Reservation selected =
        reservationComboBox.getValue();

    if (selected == null) {
      showMessage("Please select a reservation.");
      return;
    }

    if (selected.paid) {
      showMessage("This reservation is already marked as paid.");
      return;
    }

    selected.paid = true;

    DemoData.notifications.add(
        DemoData.currentDriverEmail
            + ": Demo payment completed for reservation #"
            + selected.id + "."
    );

    updateDetails();

    showMessage(
        "Demo payment successful. No money was charged."
    );
  }

  @FXML
  private void handleBack(ActionEvent event) {
    Navigator.goTo(event, "dashboard.fxml");
  }

  private void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Fancy Smart Parking");
    alert.setHeaderText("Payments - Demo Mode");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
