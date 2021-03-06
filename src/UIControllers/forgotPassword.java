package UIControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

public class forgotPassword {
	/**
	 * Starts the login page.
	 * @param actionEvent Action event object
	 * @throws IOException Exception thrown when input or output stream not initialized
	 */
	public void showLoginPage(ActionEvent actionEvent) throws IOException {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Check your e-mail for the new password.");
		alert.showAndWait();

		Parent newscene = FXMLLoader.load(getClass().getResource("entryPage.fxml"));
		Main.primaryStage.setScene(new Scene(newscene, 600, 400));
		Main.primaryStage.show();
	}
}
