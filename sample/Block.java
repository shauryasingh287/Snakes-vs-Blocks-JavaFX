package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Model class to hold block information
 */
public class Block extends Rectangle implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;
    private int strength;
    private transient Label label;
    double xc;
    double yc;
    private transient static ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.FUCHSIA, Color.GREENYELLOW, Color.SKYBLUE, Color.RED, Color.TURQUOISE));

    /**
     * Constructor for block class
     *
     * @param xc <code> Double </code>
     * @param yc <code> Double </code>
     * @param strength <code> Integer </code>
     */
    public Block(double xc, double yc, int strength) {
        super(xc, yc, 60, 60);
        this.xc = xc;
        this.yc = yc;
        this.setArcHeight(20.0);
        this.setArcWidth(20.0);
        this.strength = strength;
        label = new Label(Integer.toString(strength));
        this.label.setTextFill(Color.BLACK);
        this.label.setLayoutX(xc  + 25);
        this.label.setLayoutY(yc  + 20);
        this.label.setAlignment(Pos.CENTER);
        this.setFill(colors.get(ThreadLocalRandom.current().nextInt(5)));
    }

    /**
     * storing values to prepare for serialization
     */
    public void store() {
//        xc = getTranslateX();
        yc = getTranslateY();
    }

    /**
     * getter for strength
     * @return strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * getter for label
     * @return label
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
     * getter for x-coordinate
     * @return xc
     */
    public double getXc() {
        return xc;
    }

    /**
     * decreases strength of block
     * @param factor <code> Integer </code>
     */
    public void decreaseStrength(int factor) {
        strength -= factor;
        this.label.setText(Integer.toString(strength));
    }

    /**
     * restore function used for deserialization
     */
    public void restore() {
        System.out.println(xc + " " + yc);
        setTranslateX(xc);
        setTranslateY(yc);
        setHeight(60);
        setWidth(60);
        setArcHeight(20);
        setArcWidth(20);
        label = new Label(Integer.toString(strength));
        colors = new ArrayList<Color>(Arrays.asList(Color.FUCHSIA, Color.GREENYELLOW, Color.SKYBLUE, Color.RED, Color.TURQUOISE));
        this.label.setTextFill(Color.BLACK);
        this.label.setLayoutX(xc  + 25);
        this.label.setLayoutY(yc  + 20);
        this.label.setAlignment(Pos.CENTER);
        setFill(colors.get(ThreadLocalRandom.current().nextInt(5)));
    }
}
