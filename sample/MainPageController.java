package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for Main Page
 */
public class MainPageController {

    @FXML
    private Button leaderboardbutton;
    @FXML
    private Button startbutton;
    @FXML
    private AnchorPane mainframe;
    private Player current_player;
    private Main current_game;

    public Boolean getContinueBonus() {
        return continueBonus;
    }

    public void setContinueBonus(Boolean continueBonus) {
        this.continueBonus = continueBonus;
    }

    private Boolean continueBonus = false;


    /**
     * Function to start new Game
     * @param actionEvent <code> ActionEvent </code>
     */
    public void startGame(ActionEvent actionEvent) {
        try {
            System.out.println(2);
            String dataFile = current_player.getUsername();
            dataFile += "database.ser";
            current_game = Main.deserialize(dataFile);
//            current_game = new Main();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlayPage.fxml"));
            Scene sc = new Scene(fxmlLoader.load());
            PlayPageController playPage = fxmlLoader.getController();
            playPage.setCurrent_player(current_player);
            playPage.setContinueBonus(continueBonus);
            if (current_game == null) System.out.println("confirm");
            if (current_game != null) playPage.setRunningGame(current_game);
            sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
            ((Stage)mainframe.getScene().getWindow()).setScene(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to redirect to leaderboard
     * @param actionEvent <code> ActionEvent </code>
     */
    public void goToLeaderboard(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LeaderboardPage.fxml"));
            Scene sc = new Scene(fxmlLoader.load());
            LeaderboardPageController leaderboardpage = fxmlLoader.getController();
            Leaderboard leaderboard = Leaderboard.deserialize();

            leaderboardpage.setLeaders(leaderboard.getLeaders(), leaderboard.getDates());

            leaderboardpage.setCurrent_player(current_player);
//            Scene sc = new Scene((AnchorPane) FXMLLoader.load(getClass().getResource("LeaderboardPage.fxml")));
            sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
            ((Stage)mainframe.getScene().getWindow()).setScene(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter for current player
     * @param current_player <code> Player </code>
     */
    public void setCurrent_player(Player current_player) {
        this.current_player = current_player;
    }
}
