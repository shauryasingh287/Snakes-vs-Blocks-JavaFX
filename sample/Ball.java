package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Model class to hold ball information
 */
public class Ball extends Circle implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;
    private transient Label label;
    private int strength;
    private double xc;
    private double yc;

    /**
     * Constructor for ball
     *
     * @param x <code> Double </code>
     * @param y <code> Double </code>
     * @param strength <code> Integer </code>
     */
    Ball(double x, double y, int strength) {
        super(x, y, 8, Color.YELLOW);
        xc = x;
        yc = y;
        this.strength = strength;
        this.label = new Label(Integer.toString(this.strength));
        this.label.setFont(new Font(10));
        this.label.setLayoutX(x-4);
        this.label.setLayoutY(y-6.5);
        this.label.setAlignment(Pos.CENTER);
    }

    /**
     * storing values to prepare for serialization
     */
    public void store() {
        xc = getTranslateX();
        yc = getTranslateY();
    }

    /**
     * getter for strength
     * @return a <code> Integer </code> specifying strength of ball
     */
    public int getStrength() {
        return strength;
    }

    /**
     * getter for label
     * @return a <code> Label </code> of ball
     */
    public Label getLabel() {
        return label;
    }

    /**
     * setter for label
     * @param label <code> Label </code>
     */
    public void setLabel(Label label) {
        this.label = label;
    }

    /**
     * restore function used for deserialization
     */
    public void restore() {
        setTranslateX(xc);
        setTranslateY(yc);
        setRadius(8);
        setFill(Color.YELLOW);
        label = new Label(Integer.toString(strength));
        this.label.setFont(new Font(10));
        this.label.setLayoutX(xc-4);
        this.label.setLayoutY(yc-6.5);
        this.label.setAlignment(Pos.CENTER);
    }
}
