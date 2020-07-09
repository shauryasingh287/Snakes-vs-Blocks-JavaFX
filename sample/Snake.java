package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model class to hold Snake information
 */
public class Snake implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;
    private int sz;
    private transient Label sizeLabel;
    private transient Pane mainframe;
    private double xc;
    private double yc;
    private ArrayList<SnakeBall> balls = new ArrayList<SnakeBall>();
    private SnakeBall head;
    private boolean[] powers;
    private long[] powertime;


    /**
     * Getter for sizeLabel
     *
     * @return a <code> Label </code> sizeLabel
     */
    public Label getSizeLabel() {
        return sizeLabel;
    }

    /**
     * Constructor for Snake class
     * @param sz <code> Integer </code>
     * @param mainframe <code> Pane </code>
     * @param xc <code> Double </code>
     * @param yc <code> Double </code>
     */
    Snake(int sz, Pane mainframe, double xc, double yc) {
        this.powers = new boolean[5];
        this.powertime = new long[5];
        this.sizeLabel = new Label(Integer.toString(4));
        this.sizeLabel.setTextFill(Color.WHITE);
        this.sz = sz;
        this.mainframe = mainframe;
        this.xc = xc;
        this.yc = yc;
        double cury = yc;
        for(int i = 0; i < sz; i++) {
            SnakeBall B = new SnakeBall(xc, cury);
            balls.add(B);
            cury += 8;
            mainframe.getChildren().add(B);
        }
        head = balls.get(0);
    }

    /**
     * Function to move right
     * @param dist <code> Double </code>
     */
    public void moveRight(double dist) {
        balls.forEach(s -> s.moveRight(dist));
        head = balls.get(0);
    }

    /**
     * Function to move left
     * @param dist <code> Double </code>
     */
    public void moveLeft(double dist) {
        balls.forEach(s -> s.moveLeft(dist));
        head = balls.get(0);
    }

    /**
     * Getter for size
     * @return an <code> Integer </code> size
     */
    public int getSz() {
        return sz;
    }


    /**
     * Getter for mainframe
     * @return a <code> Pane </code> mainframe
     */
    public Pane getMainframe() {
        return mainframe;
    }

    /**
     * Setter for mainframe
     * @param mainframe <code> Pane </code>
     */
    public void setMainframe(Pane mainframe) {
        this.mainframe = mainframe;
    }

    /**
     * Getter for x-coordinate
     * @return a <code> Double </code> xc
     */
    public double getXc() {
        return head.getTranslateX();
    }

    /**
     * Getter for x-coordinate
     * @return a <code> Double </code> xc
     */
    public double getYc() {
        return head.getTranslateY();
    }


    /**
     * Function to check intersection of Snake
     * @param node <code> Node </code>
     * @return a <code> Boolean </code> to indicate intersection
     */
    public boolean checkIntersection(Node node) {
        return sz > 0 && head.getBoundsInParent().intersects(node.getBoundsInParent());
    }

    /**
     * Function to add Snake balls
     * @param strength <code> Integer </code>
     */
    public void addSnakeBalls(int strength) {
        System.out.println(strength);
        for(int i = 0; i < strength; i++) {
            SnakeBall s = new SnakeBall(head.getTranslateX(), yc + 8*sz);
            if (powers[4]) s.setFill(Color.RED);
            mainframe.getChildren().add(s);
            balls.add(s);
            sz++;
            this.sizeLabel.setText(Integer.toString(sz));
        }
        head = balls.get(0);
    }

    /**
     * Save function to prepare for serialization
     */
    public void save() {
        xc = head.getTranslateX();
        yc = head.getTranslateY();
    }

    /**
     * Function to remove Snake balls
     * @param strength <code> Integer </code>
     * @return an <code> Integer </code> to indicate Snake alive status
     */
    public int removeSnakeBalls(int strength) {
        for(int i = 0; i < strength; i++) {
            mainframe.getChildren().remove(balls.get(sz-1));
            balls.remove(sz-1); sz--;
            this.sizeLabel.setText(Integer.toString(sz));
            if(sz == 0) {
                return -1;
            }
        }
        head = balls.get(0);
        return 0;
    }

    /**
     * Function to move Snake to a location
     * @param v <code> Double </code>
     */
    public void moveTo(double v) {
        balls.forEach(s -> s.moveTo(v));
        head = balls.get(0);
    }

    /**
     * Function to give powerup to snake
     * @param k <code> Integer </code>
     * @param t <code> Long </code>
     */
    public void givePowerup(int k, long t) {
        if (k == 4) {
            this.setFill(Color.RED);
        }
        powers[k] = true;
        powertime[k] = t;
    }


    /**
     * sets Fill Color for indication of shield powerup
     *
     * @param color <code> Color </code>
     */
    public void setFill(Color color) {
        for (SnakeBall snakeBall : balls) {
            snakeBall.setFill(color);
        }
    }

    /**
     * Function to check powerup validity and invalidate upon expiry
     */
    public void reducePowerups() {
        for(int i = 1; i < 5; i++) {
            if(powers[i]) {
                if (System.currentTimeMillis() > powertime[i] + 5000) {
                    System.out.println(System.currentTimeMillis() + " " + powertime[i] + " " + i);
                    powers[i] = false;
                    if (i == 4) this.setFill(Color.ORANGE);
                }
            }
        }
    }

    /**
     * Function to remove powerup
     * @param k <code> Integer </code>
     */
    public void endPowerup(int k) {
        powers[k] = false;
        powertime[k] = 0;
    }

    /**
     * Function to check active powerup
     * @param i <code> Integer </code>
     * @return <code> Boolean </code> indicating presence of powerup
     */
    public boolean havePowerup(int i) {
        return powers[i];
    }

    /**
     * Function to resurrect Snake
     * @param mainframe <code> Pane </code>
     */
    public void resurrect(Pane mainframe) {
        this.mainframe = mainframe;
        balls.clear();
        double cury = yc;
        for(int i = 0; i < sz; i++) {
            SnakeBall B = new SnakeBall(xc, cury);
            balls.add(B);
            cury += 8;
            mainframe.getChildren().add(B);
        }
        sizeLabel = new Label(Integer.toString(sz));
        sizeLabel.setTextFill(Color.WHITE);
    }
}
