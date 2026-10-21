
package fsp.driverFrontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class NotificationsController {

  @FXML
  private ListView<String> notificationList;

  // Prikazuje samo poruke koje pripadaju ovom vozacu.
  @FXML
  private void initialize() {

    String prefix = DemoData.currentDriverEmail + ":";

    notificationList.getItems().setAll(
        DemoData.notifications.stream()
            .filter(message -> message.startsWith(prefix))
            .toList()
    );

    if (notificationList.getItems().isEmpty()) {
      notificationList.getItems().add(
          "No notifications yet."
      );
    }
  }

  @FXML
  private void handleBack(ActionEvent event) {
    Navigator.goTo(event, "dashboard.fxml");
  }
}
