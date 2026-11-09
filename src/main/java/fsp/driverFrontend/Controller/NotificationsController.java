
package fsp.driverFrontend.Controller;

import fsp.driverFrontend.DemoData;
import fsp.driverFrontend.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class NotificationsController {

  @FXML
  private ListView<String> notificationList;

  // Displays only the messages that belong to this driver.
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
