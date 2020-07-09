package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Main screen after startup
 */
public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private Button signin;
    @FXML
    private Button signup;
    @FXML
    private AnchorPane mainframe;
    private String input_username;
    private Player returning_player;

    /**
     * Function to register new user
     *
     * @param actionEvent <code> ActionEvent </code>
     */
    public void make_account(ActionEvent actionEvent) {
        try {
            Scene sc = new Scene(FXMLLoader.load(getClass().getResource("Make_Account.fxml")));
            sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
            ((Stage) mainframe.getScene().getWindow()).setScene(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to login existing user
     * @param actionEvent <code> ActionEvent </code>
     */
    public void login(ActionEvent actionEvent) {
        if (username.getText() != null)
            input_username = username.getText();
        returning_player = getPlayer();
        if (returning_player == null) return;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_page.fxml"));
            Scene sc = new Scene(fxmlLoader.load());
            MainPageController mainPage = fxmlLoader.getController();
//            returning_player.serialize();
            mainPage.setCurrent_player(returning_player);
            sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
            ((Stage) mainframe.getScene().getWindow()).setScene(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getter for Player
     * @return a <code> Player </code> player
     */
    private Player getPlayer() {
        String dataFile = input_username;
        return Player.deserialize(dataFile);
    }
}
