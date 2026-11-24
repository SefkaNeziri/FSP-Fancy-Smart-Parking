
package fsp.driverFrontend.Controller;

import fsp.driverFrontend.DemoData;
import fsp.driverFrontend.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

public class ReservationsController {

  @FXML
  private ListView<DemoData.Reservation> reservationList;

  // Loads the current driver's reservations.
  @FXML
  private void initialize() {
    refreshReservations();
  }

  private void refreshReservations() {
    reservationList.getItems().setAll(
        DemoData.myReservations()
    );
  }

  // Cancels the selected reservation.
  @FXML
  private void handleCancel() {

    DemoData.Reservation selected =
        reservationList.getSelectionModel().getSelectedItem();

    if (selected == null) {
      showMessage("Please select a reservation.");
      return;
    }

    if (selected.cancelled) {
      showMessage("This reservation is already cancelled.");
      return;
    }

    if (!selected.start.isAfter(
        java.time.LocalDateTime.now())) {
      showMessage("Past or started reservations "
          + "cannot be cancelled in this demo.");
      return;
    }

    Alert confirmation = new Alert(
        Alert.AlertType.CONFIRMATION
    );

    confirmation.setTitle("Cancel Reservation");
    confirmation.setHeaderText("Confirm cancellation");
    confirmation.setContentText(
        "Do you want to cancel reservation #"
            + selected.id + "?"
    );

    if (confirmation.showAndWait()
        .filter(button -> button == ButtonType.OK)
        .isEmpty()) {
      return;
    }

    // Updates the reservation status only in the local list.
    selected.cancelled = true;

    DemoData.notifications.add(
        DemoData.currentDriverEmail
            + ": Reservation #" + selected.id
            + " was cancelled."
    );

    refreshReservations();
    showMessage("Reservation cancelled in demo mode.");
  }

  @FXML
  private void handleBack(ActionEvent event) {
    Navigator.goTo(event, "dashboard.fxml");
  }

  private void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Fancy Smart Parking");
    alert.setHeaderText("My Reservations");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
