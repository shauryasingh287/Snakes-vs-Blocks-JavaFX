package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for MakeAccount Page
 */
public class MakeAccountController {

    @FXML
    private Button signupbutton;
    @FXML
    private TextField username_tf;
    private String username;

    /**
     * Function to make new account
     *
     * @param actionEvent <code> ActionEvent </code>
     */
    public void makeNewAccount(ActionEvent actionEvent) {
        if (username_tf.getText() != null)
            username = username_tf.getText();
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
        Scene sc = null;
        try {
            sc = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Player P = new Player(username);
        MainPageController mainPage = fxmlLoader.getController();
        P.serialize();
        System.out.println("check");
        mainPage.setCurrent_player(P);
        sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(sc);
        stage.show();
    }
}