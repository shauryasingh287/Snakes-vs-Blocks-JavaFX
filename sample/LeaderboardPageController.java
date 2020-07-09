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
import java.util.ArrayList;

/**
 * Controller class for Leaderboard Page
 */
public class LeaderboardPageController {
    @FXML
    private Button backbutton;
    @FXML
    private AnchorPane mainframe;
    @FXML
    private Label score1;
    @FXML
    private Label name1;
    @FXML
    private Label date1;
    @FXML
    private Label score2;
    @FXML
    private Label name2;
    @FXML
    private Label date2;
    @FXML
    private Label score3;
    @FXML
    private Label name3;
    @FXML
    private Label date3;
    @FXML
    private Label score4;
    @FXML
    private Label name4;
    @FXML
    private Label date4;
    @FXML
    private Label score5;
    @FXML
    private Label name5;
    @FXML
    private Label date5;
    @FXML
    private Label score6;
    @FXML
    private Label name6;
    @FXML
    private Label date6;
    @FXML
    private Label score7;
    @FXML
    private Label name7;
    @FXML
    private Label date7;
    @FXML
    private Label score8;
    @FXML
    private Label name8;
    @FXML
    private Label date8;
    @FXML
    private Label score9;
    @FXML
    private Label name9;
    @FXML
    private Label date9;
    @FXML
    private Label score10;
    @FXML
    private Label name10;
    @FXML
    private Label date10;

    /**
     * Setter for current_player
     *
     * @param current_player <code> Player </code>
     */
    public void setCurrent_player(Player current_player) {
        this.current_player = current_player;
    }

    Player current_player;

    /**
     * Function to go back to main page
     * @param actionEvent <code> ActionEvent </code>
     */
    public void goBackToMainPage(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            Scene sc = new Scene(fxmlLoader.load());
            MainPageController mainPage = fxmlLoader.getController();
            mainPage.setCurrent_player(current_player);
//            Scene sc = new Scene((AnchorPane)FXMLLoader.load(getClass().getResource("Main_Page.fxml")));
            sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
            ((Stage) mainframe.getScene().getWindow()).setScene(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter for leaders
     * @param leaders <code> ArrayList of Players</code>
     * @param dates <code> ArrayList of Strings </code>
     */
    public void setLeaders(ArrayList<Player> leaders, ArrayList<String> dates) {
        name1.setText(leaders.get(0).getUsername());
        name2.setText(leaders.get(1).getUsername());
        name3.setText(leaders.get(2).getUsername());
        name4.setText(leaders.get(3).getUsername());
        name5.setText(leaders.get(4).getUsername());
        name6.setText(leaders.get(5).getUsername());
        name7.setText(leaders.get(6).getUsername());
        name8.setText(leaders.get(7).getUsername());
        name9.setText(leaders.get(8).getUsername());
        name10.setText(leaders.get(9).getUsername());
        score1.setText(Integer.toString(leaders.get(0).getScore()));
        score2.setText(Integer.toString(leaders.get(1).getScore()));
        score3.setText(Integer.toString(leaders.get(2).getScore()));
        score4.setText(Integer.toString(leaders.get(3).getScore()));
        score5.setText(Integer.toString(leaders.get(4).getScore()));
        score6.setText(Integer.toString(leaders.get(5).getScore()));
        score7.setText(Integer.toString(leaders.get(6).getScore()));
        score8.setText(Integer.toString(leaders.get(7).getScore()));
        score9.setText(Integer.toString(leaders.get(8).getScore()));
        score10.setText(Integer.toString(leaders.get(9).getScore()));
        date1.setText(dates.get(0));
        date2.setText(dates.get(1));
        date3.setText(dates.get(2));
        date4.setText(dates.get(3));
        date5.setText(dates.get(4));
        date6.setText(dates.get(5));
        date7.setText(dates.get(6));
        date8.setText(dates.get(7));
        date9.setText(dates.get(8));
        date10.setText(dates.get(9));
        name1.setTextFill(Color.WHITE);
        score1.setTextFill(Color.WHITE);
        date1.setTextFill(Color.WHITE);
        name2.setTextFill(Color.WHITE);
        score2.setTextFill(Color.WHITE);
        date2.setTextFill(Color.WHITE);
        name3.setTextFill(Color.WHITE);
        score3.setTextFill(Color.WHITE);
        date3.setTextFill(Color.WHITE);
        name4.setTextFill(Color.WHITE);
        score4.setTextFill(Color.WHITE);
        date4.setTextFill(Color.WHITE);
        name5.setTextFill(Color.WHITE);
        score5.setTextFill(Color.WHITE);
        date5.setTextFill(Color.WHITE);
        name6.setTextFill(Color.WHITE);
        score6.setTextFill(Color.WHITE);
        date6.setTextFill(Color.WHITE);
        name7.setTextFill(Color.WHITE);
        score7.setTextFill(Color.WHITE);
        date7.setTextFill(Color.WHITE);
        name8.setTextFill(Color.WHITE);
        score8.setTextFill(Color.WHITE);
        date8.setTextFill(Color.WHITE);
        name8.setTextFill(Color.WHITE);
        score9.setTextFill(Color.WHITE);
        date9.setTextFill(Color.WHITE);
        name10.setTextFill(Color.WHITE);
        score10.setTextFill(Color.WHITE);
        date10.setTextFill(Color.WHITE);
    }
}
