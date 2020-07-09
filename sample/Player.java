package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.*;

/**
 * Model class to hold Player information
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;
    private Snake snake;
    private transient Pane mainframe;
    private int score;
    private transient Label scoreLabel;
    private int coins = 0;
    private transient Label coinsLabel;
    private String username;

    public Player(Pane mainframe, String username) {
        this.username = username;
        this.score = 0;
        this.coins = 0;
        this.coinsLabel = new Label("0");
        this.coinsLabel.setTextFill(Color.WHITE);
        this.scoreLabel = new Label("0");
        this.scoreLabel.setTextFill(Color.WHITE);
        this.snake = new Snake(5, mainframe, width/2, height/2.0);
        this.mainframe = mainframe;
    }

    /**
     * Constructor for Player class
     *
     * @param username <code> String </code>
     */
    public Player(String username) {
        this.username = username;
        this.score = 0;
        this.coins = 0;
        this.coinsLabel = new Label("0");
        this.coinsLabel.setTextFill(Color.WHITE);
        this.scoreLabel = new Label("0");
        this.scoreLabel.setTextFill(Color.WHITE);
//        this.snake = new Snake(5, mainframe, width/2, height/2.0);
    }


    /**
     * Getter for score
     * @return an <code> Integer </code> score
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for snake
     * @return a <code> Snake </code> snake
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * Getter for scoreLabel
     * @return a <code> Label </code> scoreLabel
     */
    public Label getScoreLabel() {
        return scoreLabel;
    }


    /**
     * Setter for snake
     * @param snake <code> Snake </code>
     */
    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    /**
     * Increase player score by factor
     *
     * @param factor <code> Integer </code>
     */
    public void increaseScore(int factor) {
        this.score += factor;
        this.scoreLabel.setText(Integer.toString(this.score));
    }

    /**
     * Getter for coinsLabel
     * @return a <code> Label </code> coinsLabel
     */
    public Label getCoinsLabel() {
        return coinsLabel;
    }

    /**
     * Add coins to Player account
     */
    public void addCoin() {
        this.coins++;
        this.coinsLabel.setText(Integer.toString(this.coins));
    }

    /**
     * Setter for score
     *
     * @param score <code> Integer </code>
     */
    public void setScore(int score) {
        System.out.println("does this happen");
        this.score = score;
        scoreLabel.setText(Integer.toString(score));
    }

    /**
     * Serialize
     */
    public void serialize() {
        String dataFile = this.username;
        dataFile += ".ser";
        ObjectOutputStream writer = null;
        try {
            writer = new ObjectOutputStream(new FileOutputStream(dataFile));
            writer.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Deserialize
     * @param username <code> String </code>
     * @return deserialized <code> Player </code>
     */
    public static Player deserialize(String username) {
        String dataFile = username;
        dataFile += ".ser";
        ObjectInputStream reader = null;
        try {
            reader = new ObjectInputStream(new FileInputStream(dataFile));
            Player player = (Player) reader.readObject();
            player.scoreLabel = new Label(Integer.toString(player.score));
            player.coinsLabel = new Label(Integer.toString(player.coins));
            player.scoreLabel.setTextFill(Color.WHITE);
            player.coinsLabel.setTextFill(Color.WHITE);
            return player;
        } catch (IOException | ClassNotFoundException e) {
            //no player found, take care of this
            e.printStackTrace();
        }
        return null;
    }


    public void setCoins(int coins) {
        this.coins = coins;
        this.coinsLabel.setText(Integer.toString(this.coins));
    }

    /**
     * Restore function for deserialize
     */
    public void restore() {
        scoreLabel = new Label(Integer.toString(score));
        coinsLabel = new Label(Integer.toString(coins));
        coinsLabel.setTextFill(Color.WHITE);
        scoreLabel.setTextFill(Color.WHITE);
    }

    /**
     * Getter for username
     * @return a <code> String </code> username
     */
    public String getUsername() {
        return username;
    }


    public int getCoins() {
        return coins;
    }

    /**
     * Setter for pane
     * @param mainframe <code> Pane </code>
     */
    public void setPane(Pane mainframe) {
        this.mainframe = mainframe;
        this.snake = new Snake(5, mainframe, width / 2, height / 2.0);
//        System.out.println("issue");
    }

}
