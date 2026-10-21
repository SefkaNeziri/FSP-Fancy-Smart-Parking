
package fsp.driverFrontend.Controller;

import fsp.driverFrontend.DemoData;
import fsp.driverFrontend.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ParkingController {

  @FXML
  private ListView<DemoData.ParkingSpot> parkingList;

  @FXML
  private ComboBox<DemoData.ParkingSpot> spotComboBox;

  @FXML
  private DatePicker datePicker;

  @FXML
  private ComboBox<String> timeComboBox;

  @FXML
  private ComboBox<Integer> hoursComboBox;

  @FXML
  private Label priceLabel;

  // Prepares the data when the Parking screen is opened.
  @FXML
  private void initialize() {

    parkingList.getItems().setAll(DemoData.spots);
    spotComboBox.getItems().setAll(DemoData.spots);

    // The user selects the reservation start time.

    for (int hour = 0; hour < 24; hour++) {
      timeComboBox.getItems().add(
          String.format("%02d:00", hour)
      );
    }

    hoursComboBox.getItems().addAll(1, 2, 3, 4, 5, 6);

    datePicker.setValue(LocalDate.now());
    hoursComboBox.setValue(1);

    // Selecting an item from the list automatically updates the ComboBox.
    parkingList.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldSpot, newSpot) -> {
          if (newSpot != null) {
            spotComboBox.setValue(newSpot);
          }
        });

    // When the user changes the parking space or duration,
    // the price is automatically updated.
    spotComboBox.valueProperty().addListener(
        (observable, oldValue, newValue) -> updatePrice()
    );

    hoursComboBox.valueProperty().addListener(
        (observable, oldValue, newValue) -> updatePrice()
    );

    spotComboBox.getSelectionModel().selectFirst();
    updatePrice();
  }

  // Calculates the price based on the selected parking space and duration.
  private void updatePrice() {

    DemoData.ParkingSpot spot = spotComboBox.getValue();
    Integer hours = hoursComboBox.getValue();

    if (spot == null || hours == null) {
      priceLabel.setText("Estimated price: €0.00");
      return;
    }

    double total = spot.pricePerHour * hours;

    priceLabel.setText(
        String.format("Estimated price: €%.2f", total)
    );
  }

  // Creates a new demo reservation.
  @FXML
  private void handleReserve(ActionEvent event) {

    DemoData.ParkingSpot spot = spotComboBox.getValue();
    LocalDate date = datePicker.getValue();
    String time = timeComboBox.getValue();
    Integer hours = hoursComboBox.getValue();

    if (spot == null || date == null
        || time == null || hours == null) {
      showMessage(Alert.AlertType.WARNING,
          "Please select all reservation details.");
      return;
    }

    LocalTime selectedTime = LocalTime.parse(time);
    LocalDateTime start = LocalDateTime.of(date, selectedTime);

    // It is not possible to reserve a time slot in the past.
    if (!start.isAfter(LocalDateTime.now())) {
      showMessage(Alert.AlertType.WARNING,
          "Please select a future date and time.");
      return;
    }

    // Checks whether another demo reservation exists
    // for the same parking space during the selected time slot.
    DemoData.Reservation reservation =
        DemoData.reserve(spot, start, hours);

    if (reservation == null) {
      showMessage(Alert.AlertType.WARNING,
          "This parking spot is already reserved "
              + "for the selected time.");
      return;
    }

    showMessage(Alert.AlertType.INFORMATION,
        "Reservation #" + reservation.id
            + " created successfully in demo mode.");

    Navigator.goTo(event, "reservations.fxml");
  }

  @FXML
  private void handleBack(ActionEvent event) {
    Navigator.goTo(event, "dashboard.fxml");
  }

  private void showMessage(
      Alert.AlertType type,
      String message
  ) {
    Alert alert = new Alert(type);
    alert.setTitle("Fancy Smart Parking");
    alert.setHeaderText("Parking Reservation");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
