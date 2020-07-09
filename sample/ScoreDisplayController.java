package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for ScoreDisplay Page
 */
public class ScoreDisplayController {
    @FXML
    public Button continueButton;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button newGameButton;
    @FXML
    private AnchorPane mainframe;
    @FXML
    private Label scoreLabel;
    private Main runningGame;
    private Player current_player;


    /**
     * Setter for score
     * @param score <code> Integer </code>
     */
    public void setScore(int score) {
        scoreLabel.setText((Integer.toString(score)));
        scoreLabel.setTextFill(Color.WHITE);
    }

    /**
     * Function to start new Game
     * @param actionEvent <code> ActionEvent </code>
     */
    public void startNewGame(ActionEvent actionEvent) {
        try {
//            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
//            runningGame = new Main();
//            System.out.println(runningGame.blocks.size());
//            runningGame.start(new Stage());
//            Stage stage = new Stage();
//            runningGame = new Main();
//            current_player.setPane(runningGame.mainframe);
//            runningGame.setP(current_player);
//            runningGame.start(stage);
//            runningGame.serialize();
            System.out.println(1);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            Scene sc = new Scene(fxmlLoader.load());
            MainPageController mainPage = fxmlLoader.getController();
            mainPage.setCurrent_player(current_player);
            mainPage.setContinueBonus(false);
            sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
//            Stage stage = new Stage();
//            stage.setScene(sc);
//            stage.show();
            ((Stage) mainframe.getScene().getWindow()).setScene(sc);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to return to Main Menu
     * @param actionEvent <code> ActionEvent </code>
     */
    public void goToMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            Scene sc = new Scene(fxmlLoader.load());
            MainPageController mainPage = fxmlLoader.getController();
            mainPage.setCurrent_player(current_player);
            sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
            ((Stage) mainframe.getScene().getWindow()).setScene(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Setter for current_player
     * @param current_player <code> Player </code>
     */
    public void setCurrent_player(Player current_player) {
        this.current_player = current_player;
    }

    /**
     * Function to continue game for 20 coins
     *
     * @param actionEvent <code> ActionEvent </code>
     */
    public void continueGame(ActionEvent actionEvent) {
        if (current_player.getCoins() < 20) return;
        current_player.setCoins(current_player.getCoins() - 20);
        try {
//            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
//            runningGame = new Main();
//            System.out.println(runningGame.blocks.size());
//            runningGame.start(new Stage());
//            Stage stage = new Stage();
//            runningGame = new Main();
//            current_player.setPane(runningGame.mainframe);
//            runningGame.setP(current_player);
//            runningGame.start(stage);
//            runningGame.serialize();
            System.out.println(1);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            Scene sc = new Scene(fxmlLoader.load());
            MainPageController mainPage = fxmlLoader.getController();
            mainPage.setCurrent_player(current_player);
            mainPage.setContinueBonus(true);
            sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
//            Stage stage = new Stage();
//            stage.setScene(sc);
//            stage.show();
            ((Stage) mainframe.getScene().getWindow()).setScene(sc);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
