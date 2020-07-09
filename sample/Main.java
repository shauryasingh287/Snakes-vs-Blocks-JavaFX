package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Model class to hold Game information
 */
public class Main extends Application implements Serializable {
    public transient Pane mainframe;
    private static int debug = 0;
    private final int width = Constants.width;
    private final int height = Constants.height;
    private final int rows = Constants.rows;
    private final int sensitivity = Constants.sensitivity;
    ArrayList<Wall> walls = new ArrayList<Wall>();
    ArrayList<Ball> balls = new ArrayList<Ball>();
    ArrayList<Token> tokens = new ArrayList<Token>();
    ArrayList<Block> blocks = new ArrayList<Block>();

    /**
     * setter for Player
     *
     * @param p <code> Player </code>
     */
    public void setP(Player p) {
        P = p;
    }

    Player P;
    private transient ComboBox<String> gameMenu;
    private transient ToggleButton mute;
    private double t;
    private transient AnimationTimer animationTimer;
    private boolean isRunning;
    private double refreshRate = 2.5;
    private transient ImagePattern img;
    private transient Image volume_on;
    private transient Image volume_off;
    private transient ImageView toggler;
    private boolean wantSound = true;

    /**
     * Main class
     * @param args <code> String[] </code>
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Serialize
     */
    public void serialize() {
        ObjectOutputStream writer = null;
        try {
            P.getSnake().save();
            for(int i = 0; i < blocks.size(); i++) {
                blocks.get(i).store();
            }
            for(int i = 0; i < tokens.size(); i++) {
                tokens.get(i).store();
            }
            for(int i = 0; i < walls.size(); i++) {
                walls.get(i).store();
            }
            for(int i = 0; i < balls.size(); i++) {
                balls.get(i).store();
            }
            String dataFile = P.getUsername();
            dataFile += "database.ser";
            writer = new ObjectOutputStream(new FileOutputStream(dataFile));
            writer.writeObject(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
     * @param dataFile <code> String </code>
     * @return deserialized <code> Main </code>
     */
    public static Main deserialize(String dataFile) {
        ObjectInputStream reader = null;
        Main get_Game = null;
        try {
            reader = new ObjectInputStream(new FileInputStream(dataFile));
            get_Game = (Main) reader.readObject();
            get_Game.mainframe = new Pane();
            get_Game.P.restore();
            get_Game.P.getSnake().resurrect(get_Game.mainframe);
            for(int i = 0; i < get_Game.blocks.size(); i++) {
                get_Game.blocks.get(i).restore();
                get_Game.mainframe.getChildren().addAll(get_Game.blocks.get(i), get_Game.blocks.get(i).getLabel());
            }
            for(int i = 0; i < get_Game.tokens.size(); i++) {
                get_Game.tokens.get(i).restore();
                get_Game.mainframe.getChildren().add(get_Game.tokens.get(i));
            }
            for(int i = 0; i < get_Game.walls.size(); i++) {
                get_Game.walls.get(i).restore();
                get_Game.mainframe.getChildren().add(get_Game.walls.get(i));
            }
            for(int i = 0; i < get_Game.balls.size(); i++) {
                get_Game.balls.get(i).restore();
                get_Game.mainframe.getChildren().addAll(get_Game.balls.get(i), get_Game.balls.get(i).getLabel());
            }

        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        finally {
//            try {
//                reader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return get_Game;
    }

    /**
     * renews Game by re-adding GUI elements
     */
    public void renewGame() {
        mainframe.getChildren().clear();
        P.setSnake(new Snake(4, mainframe, width/2, height/2));
        tokens.clear();
        blocks.clear();
        walls.clear();
        balls.clear();
        gameMenu = new ComboBox<String>();
        HBox hBox = new HBox();
        hBox.setPrefHeight(height / 20);
        hBox.setPrefWidth(width);
        hBox.setStyle("-fx-background-color: #000000");
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.getChildren().add(new Label("Score: "));
        hBox.getChildren().add(P.getScoreLabel());
        hBox.getChildren().add(new Label("Size: "));
        hBox.getChildren().add(P.getSnake().getSizeLabel());
        gameMenu.getItems().add("Pause");
        gameMenu.getItems().add("Restart");
        gameMenu.getItems().add("Exit");
        gameMenu.setPromptText("Menu");
        gameMenu.setPrefWidth(10);
        gameMenu.setOnAction(e -> {
            if(gameMenu.getValue().equals("Pause")) {
                gameMenu.getItems().set(0, "Resume");
                animationTimer.stop();
            } else if(gameMenu.getValue().equals("Resume")) {
                gameMenu.getItems().set(0, "Pause");
                animationTimer.start();
            } else if(gameMenu.getValue().equals("Exit")) {
                animationTimer.stop();
                isRunning = false;
                this.serialize();
                Scene sc = null;
                System.out.println("SHOULD WORK");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
                try {
                    sc = new Scene(fxmlLoader.load());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                MainPageController mainPage = fxmlLoader.getController();
                mainPage.setCurrent_player(P);
                sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
                ((Stage) mainframe.getScene().getWindow()).setScene(sc);
            } else if(gameMenu.getValue().equals("Restart")) {
                renewGame();
                animationTimer.start();
            }
        });
        hBox.getChildren().add(gameMenu);
        mute = new ToggleButton();
        img = new ImagePattern(new Image(getClass().getResourceAsStream("block_burst.png")));
        volume_off = new Image("sample/mute-speakers.png");
        volume_on = new Image("sample/volume-control.png");
        toggler = new ImageView();
        mute.setGraphic(toggler);
        mute.setPrefSize(1, 1);
        toggler.imageProperty().bind(Bindings.when(mute.selectedProperty())
                .then(volume_off)
                .otherwise(volume_on)
        );
        hBox.getChildren().add(mute);
        hBox.getChildren().add(new Label("Coins: "));
        hBox.getChildren().add(P.getCoinsLabel());
        mainframe.getChildren().add(hBox);
    }


    /**
     * Make row of blocks
     * @param pos <code> ArrayList of Integer </code>
     * @param strength <code> ArrayList of Integer </code>
     */
    public void makeRowOfBlocks(ArrayList<Integer> pos, ArrayList<Integer> strength) {
        int cnt = pos.size();
        for (int i = 0; i < cnt; i++) {
            Block b = new Block(pos.get(i) * width / rows, 60, strength.get(i));
            if (intersection(b)) continue;
            blocks.add(b);
            mainframe.getChildren().addAll(b, b.getLabel());
        }
    }

    /**
     * Make a new wall
     * @param pos <code> Integer </code>
     * @param y <code> Double </code>
     * @param height <code> Double </code>
     * @return a <code> Boolean </code> indicating successful addition
     */
    public boolean addWall(int pos, double y, double height) {
        Wall to_add = new Wall(pos * width / rows, y, height);
        if (!intersection(to_add)) {
            walls.add(to_add);
            mainframe.getChildren().addAll(to_add);
            return true;
        }
        return false;
    }

    /**
     * Check for intersection of snake against GUI elements
     * @param to_add <code> Node </code>
     * @return a <code> Boolean </code> indicating intersection
     */
    public boolean intersection(Node to_add) {
        for (int i = 0; i < balls.size(); i++) {
            Bounds b = to_add.getBoundsInParent();
            if (balls.get(i).getBoundsInParent().intersects(b)) {
                return true;
            }
        }
        for (int i = 0; i < walls.size(); i++) {
            Bounds b = to_add.getBoundsInParent();
            if (walls.get(i).getBoundsInParent().intersects(b)) {
                return true;
            }
        }
        for (int i = 0; i < tokens.size(); i++) {
            Bounds b = to_add.getBoundsInParent();
            if (tokens.get(i).getBoundsInParent().intersects(b)) {
                return true;
            }
        }
        for (int i = 0; i < blocks.size(); i++) {
            Bounds b = to_add.getBoundsInParent();
            if (blocks.get(i).getBoundsInParent().intersects(b)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates GUI content after fixed intervals
     */
    public void generateAndRefresh() {
//        System.out.println("check" + " " + debug++);
        if (t > 10) {
            t = 0;
            Random r = new Random(System.currentTimeMillis());
            int num = r.nextInt(rows) + 1;
            ArrayList<Integer> pos = new ArrayList<Integer>();
            ArrayList<Integer> allpos = new ArrayList<Integer>();
            for (int i = 0; i < rows; i++) allpos.add(i);
            for (int i = 0; i < num; i++) {
                int next = r.nextInt(allpos.size());
                pos.add(allpos.get(next));
                allpos.remove(next);
            }
            ArrayList<Integer> strength = new ArrayList<Integer>();

            int smallestBlock = Integer.MAX_VALUE;
            int sumOfStrengths = 0;
            for (int i = 0; i < num; i++) {
                int value = (int) Math.abs(4 * r.nextGaussian()) + 1;
                sumOfStrengths += value;
                smallestBlock = Math.min(smallestBlock, value);
                strength.add(value);
            }
            if (smallestBlock >= P.getSnake().getSz() && pos.size() == rows) {
                pos.remove(pos.size() - 1);
                strength.remove(strength.size() - 1);
            }
            makeRowOfBlocks(pos, strength);
        }
        Random r = new Random(System.currentTimeMillis());
        double model = Math.random();
//        System.out.println(model);
        if (model >= 0.96) {
//            System.err.println("here");
            int wallCount = r.nextInt(3);
            for (int i = 0; i < wallCount; i++) {
                int pos = r.nextInt(rows - 1) + 1;
                int wall_height = 180;
                double pro = Math.random();
                if (pro > 0.5) {
                    wall_height = 60;
                } else if (pro > 0.2) {
                    wall_height = 120;
                }
//                addWall(pos, height/10, width/10 + r.nextInt(width/10));
                addWall(pos, height / 10, wall_height);
            }
            int tokenCount = r.nextInt(3);
            int cnt = 0;
            for (int i = 0; i < tokenCount; i++) {
                double probability = Math.random();
                if (probability > 0.6) {
                    //coins
                    while (!addToken(width / 20 + r.nextInt(width - width / 10), height / 10 + r.nextInt(height / 20), 1)) {
                        if (cnt++ > 10) {
//                            System.err.println("err1");
                            break;
                        }
                    }
//                    while(!addToken(30 + r.nextInt(440), 60 + r.nextInt(30), 1));
                } else if (probability > 0.2) {
                    //balls
                    int ball_strength = (int) (Math.random() * 5) + 1;
                    while (!addBall(width / 20 + r.nextInt(width - width / 10), height / 10 + r.nextInt(height / 20), ball_strength)) {
                        if (cnt++ > 10) {
//                            System.err.println("err2");
                            break;
                        }
                    }
//                    while(!addBall(30 + r.nextInt(440), 60 + r.nextInt(30), 3 + (int)Math.abs(r.nextGaussian())));
                } else if (probability > 0.13) {
                    //magnet
                    while (!addToken(width / 20 + r.nextInt(width - width / 10), height / 10 + r.nextInt(height / 20), 2)) {
                        if (cnt++ > 10) {
//                            System.err.println("err3");
                            break;
                        }
                    }
                } else if (probability > 0.065) {
                    //brickbuster
                    while (!addToken(width / 20 + r.nextInt(width - width / 10), height / 10 + r.nextInt(height / 20), 3)) {
                        if (cnt++ > 10) {
//                            System.err.println("err4");
                            break;
                        }
                    }
//                    while(!addToken(30 + r.nextInt(440), 60 + r.nextInt(30), 3));
                } else {
                    //shield
                    while (!addToken(width / 20 + r.nextInt(width - width / 10), height / 10 + r.nextInt(height / 20), 4)) {
                        if (cnt++ > 10) {
//                            System.err.println("err5");
                            break;
                        }
                    }
//                    while(!addToken(30 + r.nextInt(440), 60 + r.nextInt(30), 4));
                }
            }
        }
//        System.out.println("check" + debug--);
    }

    /**
     * Make new ball
     * @param x
     * @param y
     * @param strength
     * @return a <code> Boolean </code> indicating successful addition
     */
    private boolean addBall(double x, double y, int strength) {
        Ball b = new Ball(x, y, strength);
        if (!intersection(b)) {
            balls.add(b);
            mainframe.getChildren().addAll(b, b.getLabel());
            return true;
        }
        return false;
    }

    /**
     * Make new token
     * @param x <code> Double </code>
     * @param y <code> Double </code>
     * @param type <code> Integer </code>
     * @return a <code> Boolean </code> indicating successful addition
     */
    private boolean addToken(double x, double y, int type) {
        switch (type) {
            case 1:
                Coin c = new Coin(x, y);
                if (!intersection(c)) {
                    tokens.add(c);
                    mainframe.getChildren().addAll(c);
                    return true;
                }
                return false;
            case 2:
                Magnet m = new Magnet(x, y);
                if (!intersection(m)) {
                    tokens.add(m);
                    mainframe.getChildren().addAll(m);
                    return true;
                }
                return false;
            case 3:
                BrickBuster b = new BrickBuster(x, y);
                if (!intersection(b)) {
                    tokens.add(b);
                    mainframe.getChildren().addAll(b);
                    return true;
                }
                return false;
            case 4:
                Shield s = new Shield(x, y);
                if (!intersection(s)) {
                    tokens.add(s);
                    mainframe.getChildren().addAll(s);
                    return true;
                }
                return false;
        }
        return false;
    }

    /**
     * Function to move Snake
     * @param direction <code> Integer </code>
     */
    private void move(int direction) {
        if (direction == 1) {
            //move towards Right
            P.getSnake().moveRight(sensitivity * 5);
            for (Wall wall : walls) {
                if (P.getSnake().checkIntersection(wall)) {
//                    P.getSnake().moveLeft(Math.abs(P.getSnake().getXc() - wall.getTranslateX() - 4));
//                    System.out.println(wall.getTranslateX() + " "  + P.getSnake().getXc() + " left");
//                    P.getSnake().moveLeft(Math.abs(P.getSnake().getXc() - wall.getTranslateX()) - 3);
                    P.getSnake().moveTo(wall.getTranslateX() - 6);
                    return;
                }
            }
        } else {
            P.getSnake().moveLeft(sensitivity * 5);
            for (Wall wall : walls) {
                if (P.getSnake().checkIntersection(wall)) {
//                    System.out.println(wall.getTranslateX() + " "  + P.getSnake().getXc() + " right");
//                    P.getSnake().moveRight(Math.abs(P.getSnake().getXc() - wall.getTranslateX() + 4));
                    P.getSnake().moveTo(wall.getTranslateX() + 8);
                    return;
                }
            }
            //move towards left
        }
    }

    /**
     * Function to assign speed of the game
     * @return a <code> Double </code> gamespeed
     */
    private double assignGameSpeed() {
        double base_speed = 3;
        int snakeLength = P.getSnake().getSz();
        double intended_speed = Math.sqrt(snakeLength) / 2;
        intended_speed *= 2;
        return Math.max(base_speed, intended_speed);
    }


    /**
     * Constructor for Main class
     */
    Main() {
        mainframe = new Pane();
        mainframe.setPrefSize(width, height);
    }

    /**
     * Start for javafx class
     * @param primaryStage <code> Stage </code>
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
//        mainframe = new Pane();
//        mainframe.setPrefSize(width, height);
//        if(P == null) System.out.println("mess up");
//        P.setPane(mainframe);
//        P = new Player(mainframe);
//        renewGame();
        HBox hBox = new HBox();
        hBox.setPrefHeight(height / 20);
        hBox.setPrefWidth(width);
        hBox.setStyle("-fx-background-color: #000000");
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.getChildren().add(new Label("Score: "));
        hBox.getChildren().add(P.getScoreLabel());
        hBox.getChildren().add(new Label("Size: "));
        hBox.getChildren().add(P.getSnake().getSizeLabel());
        gameMenu = new ComboBox<String>();
        gameMenu.getItems().add("Pause");
        gameMenu.getItems().add("Restart");
        gameMenu.getItems().add("Exit");
        gameMenu.setPromptText("Menu");
        gameMenu.setPrefWidth(10);
        gameMenu.setOnAction(e -> {
            if(gameMenu.getValue().equals("Pause")) {
                gameMenu.getItems().set(0, "Resume");
                animationTimer.stop();
            } else if(gameMenu.getValue().equals("Resume")) {
                gameMenu.getItems().set(0, "Pause");
                animationTimer.start();
            } else if(gameMenu.getValue().equals("Exit")) {
                animationTimer.stop();
                isRunning = false;
                this.serialize();
                Scene sc = null;
                System.out.println("SHOULD WORK");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
                try {
                    sc = new Scene(fxmlLoader.load());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                MainPageController mainPage = fxmlLoader.getController();
                mainPage.setCurrent_player(P);
                sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
                ((Stage) mainframe.getScene().getWindow()).setScene(sc);
//                animationTimer.stop();
//                isRunning = false;
//                this.serialize();
//                Scene sc = null;
//                try {
//                    sc = new Scene(FXMLLoader.load(getClass().getResource("PlayPage.fxml")));
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//                sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
//                ((Stage) mainframe.getScene().getWindow()).setScene(sc);
            } else if(gameMenu.getValue().equals("Restart")) {
                renewGame();
                animationTimer.start();
            }
        });
        hBox.getChildren().add(gameMenu);
        mute = new ToggleButton();
        img = new ImagePattern(new Image(getClass().getResourceAsStream("block_burst.png")));
        volume_off = new Image("sample/mute-speakers.png");
        volume_on = new Image("sample/volume-control.png");
        toggler = new ImageView();
        mute.setGraphic(toggler);
        mute.setPrefSize(1, 1);
        toggler.imageProperty().bind(Bindings.when(mute.selectedProperty())
                .then(volume_off)
                .otherwise(volume_on)
        );
        hBox.getChildren().add(mute);
        hBox.getChildren().add(new Label("Coins: "));
        hBox.getChildren().add(P.getCoinsLabel());
        mainframe.getChildren().add(hBox);
        mainframe.setStyle("-fx-background-color: #191970");
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    refreshGUI();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        animationTimer.start();
        isRunning = true;

        Scene scene = new Scene(mainframe);
        scene.setOnKeyPressed(e ->
        {
            if (e.getCode() == KeyCode.D && isRunning)
                move(1);
            if (e.getCode() == KeyCode.A && isRunning)
                move(-1);
            if (e.getCode() == KeyCode.ESCAPE) {
                if (isRunning) {
                    gameMenu.getItems().set(0, "Resume");
                    animationTimer.stop();
                    isRunning = false;
                } else {
                    gameMenu.getItems().set(0, "Pause");
                    animationTimer.start();
                    isRunning = true;
                }
            }
        });
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * refreshes GUI
     * @throws InterruptedException
     */
    private void refreshGUI() throws InterruptedException {
//        System.out.println(walls.size() + " " + blocks.size() + " " + tokens.size() + " " + balls.size());
        Random r = new Random(System.currentTimeMillis());
        refreshRate = Math.max(refreshRate, assignGameSpeed());
//        System.err.println(refreshRate + " " + 60/refreshRate);
        t += 0.04 * refreshRate;
        P.getSnake().reducePowerups();
//        System.out.println("dayum");
        if (t > 3) {
            generateAndRefresh();
        }
        ArrayList<Token> to_be_removedT = new ArrayList<Token>();
        IntStream.range(0, tokens.size()).forEachOrdered(i -> {
            if (P.getSnake().checkIntersection(tokens.get(i))) {
                String mediaFile = "src/sample/TokenSound.mp3";
                Media sound = new Media(new File(mediaFile).toURI().toString());
                MediaPlayer player = new MediaPlayer(sound);
                if (!mute.isSelected())
                    player.play();
                mainframe.getChildren().remove(tokens.get(i));
                Circle expl = new Circle(P.getSnake().getXc() - 10, P.getSnake().getYc(), 20);
                if (tokens.get(i).getTokenKind() == 1) {
                    //Coin
                    P.addCoin();
                    expl.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("action1.png"))));
                }
                if (tokens.get(i).getTokenKind() == 2) {
                    //Magnet
                    expl.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("action2.png"))));
                    for (int j = 0; j < tokens.size(); j++) {
                        if (tokens.get(j).getTokenKind() == 1) {
                            TranslateTransition tr = new TranslateTransition(Duration.millis(1500), tokens.get(j));
//                            tr.setToX(P.getSnake().getXc()-20);
//                            tr.setToY(P.getSnake().getYc());
                            tr.setToX(100);
                            tr.setToY(-20);
                            System.out.println(P.getSnake().getXc() + " " + P.getSnake().getYc());
                            tr.play();
                            Token t = tokens.get(j);
                            tr.setOnFinished(e -> {
                                mainframe.getChildren().remove(t);
                            });
                            P.addCoin();
                        }
                    }
                }
                if (tokens.get(i).getTokenKind() == 3) {
                    //Brickbuster
                    P.getSnake().givePowerup(3, System.currentTimeMillis());
                    expl.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("action3.png"))));
                }
                if (tokens.get(i).getTokenKind() == 4) {
                    //Shield
                    P.getSnake().givePowerup(4, System.currentTimeMillis());
                    expl.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("action4.png"))));
                }
                mainframe.getChildren().add(expl);
                ScaleTransition tr = new ScaleTransition(Duration.millis(100), expl);
                tr.setToX(5);
                tr.setToY(5);
                tr.setOnFinished(e -> {
                    mainframe.getChildren().remove(expl);
                });
                tr.play();
                to_be_removedT.add(tokens.get(i));
            }
        });
        for (Token aTo_be_removedT1 : to_be_removedT) {
            tokens.remove(aTo_be_removedT1);
        }
        ArrayList<Wall> to_be_removedW = new ArrayList<Wall>();
        IntStream.range(0, walls.size()).forEachOrdered(i -> {
            walls.get(i).setTranslateY(walls.get(i).getTranslateY() + refreshRate / 2);
            if (walls.get(i).getTranslateY() > 800) {
                mainframe.getChildren().remove(walls.get(i));
                to_be_removedW.add(walls.get(i));
            }
        });
        for (Wall aTo_be_removedW : to_be_removedW) {
            walls.remove(aTo_be_removedW);
        }
        to_be_removedT.clear();
        IntStream.range(0, tokens.size()).forEachOrdered(i -> {
//            System.out.println(i);
//            tokens.get(i).setTranslateY(tokens.get(i).getTranslateY() + refreshRate/2);
            tokens.get(i).pull(refreshRate / 2);
            if (tokens.get(i).getTranslateY() > 800) {
                mainframe.getChildren().remove(tokens.get(i));
                to_be_removedT.add(tokens.get(i));
            }
        });
        for (Token aTo_be_removedT : to_be_removedT) {
            tokens.remove(aTo_be_removedT);
        }
        ArrayList<Ball> to_be_removed = new ArrayList<Ball>();
        IntStream.range(0, balls.size()).forEachOrdered(i -> {
            balls.get(i).setTranslateY(balls.get(i).getTranslateY() + refreshRate / 2);
            balls.get(i).getLabel().setTranslateY(balls.get(i).getTranslateY() + refreshRate / 2);
            if (balls.get(i).getTranslateY() > 800) {
                mainframe.getChildren().removeAll(balls.get(i), balls.get(i).getLabel());
                to_be_removed.add(balls.get(i));
            }
        });
        for (Ball aTo_be_removed : to_be_removed) {
            balls.remove(aTo_be_removed);
        }
        ArrayList<Block> to_be_removedB = new ArrayList<Block>();
        IntStream.range(0, blocks.size()).forEachOrdered(i -> {
            blocks.get(i).setTranslateY(blocks.get(i).getTranslateY() + refreshRate / 2);
            blocks.get(i).getLabel().setTranslateY(blocks.get(i).getLabel().getTranslateY() + refreshRate / 2);
            if (blocks.get(i).getTranslateY() > 800) {
                mainframe.getChildren().removeAll(blocks.get(i), blocks.get(i).getLabel());
                to_be_removedB.add(blocks.get(i));
            }
        });
        for (Block aTo_be_removedB : to_be_removedB) {
            blocks.remove(aTo_be_removedB);
        }
        if(P.getSnake().havePowerup(3)) {
            for(Block b : blocks) {
                mainframe.getChildren().removeAll(b, b.getLabel());
                P.increaseScore(b.getStrength());
            }
            blocks.clear();
            P.getSnake().endPowerup(3);
        }
        BlockIntersection();
        WallIntersection();
        BallIntersection();
    }

    /**
     * Remove balls after ball intersection
     */
    private void BallIntersection() {
        for (Ball ball : balls) {
            if (P.getSnake().checkIntersection(ball)) {
                P.getSnake().addSnakeBalls(ball.getStrength());
                mainframe.getChildren().removeAll(ball.getLabel(), ball);
                balls.remove(ball);
                return;
            }
        }
    }

    /**
     * Restrict Snake after wall intersection
     */
    private void WallIntersection() {
        for (Wall wall : walls) {
            if (P.getSnake().checkIntersection(wall)) {
                double delta = wall.getTranslateX() - P.getSnake().getXc();
                if (delta > 0) P.getSnake().moveTo(wall.getTranslateX() - 6);
                else P.getSnake().moveTo(wall.getTranslateX() + 8);
                return;
            }
        }
    }

    /**
     * Reduce Block and Snake Strengths following intersection
     * @throws InterruptedException
     */
    private void BlockIntersection() throws InterruptedException {
        int isSnakeDead = 0;
        for (Block block : blocks) {
            if (P.getSnake().checkIntersection(block)) {
                System.out.println("here");
                if (block.getTranslateY() > 185) {
//                    System.out.println(P.getSnake().getXc() + " " + block.getTranslateX() + " " + block.getXc() + "LOL");
                    if (P.getSnake().getXc() > block.getXc() + 50) {
                        P.getSnake().moveTo(block.getXc() + 70);
                    } else {
                        P.getSnake().moveTo(block.getXc() - 10);
                    }
                    break;
//                    return;
                }
                if(block.getStrength() == 1) {
                    P.increaseScore(1);
                    isSnakeDead = P.getSnake().removeSnakeBalls(1);
                    mainframe.getChildren().removeAll(block, block.getLabel());
                    blocks.remove(block);
                    Circle expl = new Circle(P.getSnake().getXc(), P.getSnake().getYc() - 20, 10, img);
                    mainframe.getChildren().add(expl);
                    ScaleTransition explTr = new ScaleTransition(Duration.millis(100), expl);
                    explTr.setToX(5);
                    explTr.setToY(5);
                    explTr.setOnFinished(e -> {
                        mainframe.getChildren().remove(expl);
                    });
                    explTr.play();
                    break;
//                    return;
                }
                if(P.getSnake().havePowerup(4)) {
                    P.increaseScore(block.getStrength());
                    mainframe.getChildren().removeAll(block, block.getLabel());
                    blocks.remove(block);
                    Circle expl = new Circle(P.getSnake().getXc(), P.getSnake().getYc() - 20, 10, img);
                    mainframe.getChildren().add(expl);
                    ScaleTransition explTr = new ScaleTransition(Duration.millis(100), expl);
                    explTr.setToX(5);
                    explTr.setToY(5);
                    explTr.setOnFinished(e -> {
                        mainframe.getChildren().remove(expl);
                    });
                    explTr.play();
                    break;
//                    return;
                }
                pushUp();
                P.increaseScore(1);
                block.decreaseStrength(1);
                isSnakeDead = P.getSnake().removeSnakeBalls(1);
//                return;
                break;
//                if(P.getSnake().getYc() < )
//                System.err.println(P.getSnake().getYc() + " " + block.getTranslateY());
//                if (block.getTranslateY() > 185) {
////                    System.out.println(P.getSnake().getXc() + " " + block.getTranslateX() + " " + block.getXc() + "LOL");
//                    if (P.getSnake().getXc() > block.getXc()) {
//                        P.getSnake().moveTo(block.getXc() + 70);
//                    } else {
//                        P.getSnake().moveTo(block.getXc() - 10);
//                    }
//                    continue;
//                }
//                int cnt = block.getStrength();
//                int initial_strength = block.getStrength();
//                if (P.getSnake().havePowerup(4)) {
//                    cnt = 0;
//                    P.increaseScore(block.getStrength());
//                } else {
//                    int limit = Math.min(initial_strength, P.getSnake().getSz());
//                    for (int i = 0; i < limit; i++) {
//                        if (!P.getSnake().checkIntersection(block)) break;
//                        cnt--;
////                        if (initial_strength > 5)
//                        pushUp();
//                        block.decreaseStrength(1);
//                        P.increaseScore(1);
//                        P.getSnake().removeSnakeBalls(1);
//                    }
//                }
//                if (cnt == 0) {
//                    Circle expl = new Circle(P.getSnake().getXc(), P.getSnake().getYc() - 20, 10);
//                    expl.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("block_burst.png"))));
//                    mainframe.getChildren().add(expl);
//                    ScaleTransition explTr = new ScaleTransition(Duration.millis(100), expl);
//                    explTr.setToX(5);
//                    explTr.setToY(5);
//                    explTr.setOnFinished(e -> {
//                        mainframe.getChildren().remove(expl);
//                    });
//                    explTr.play();
//                    mainframe.getChildren().removeAll(block.getLabel(), block);
//                    blocks.remove(block);
//                    break;
//                }
//                if (P.getSnake().getSz() <= 0) {
//                    return;
//                }
            }
        }
        if (isSnakeDead == -1) {
            P.serialize();
            Leaderboard leaderboard = Leaderboard.deserialize();
            leaderboard.updateLeaderboard(P);
            leaderboard.serialize();
            (new File(P.getUsername() + "database.ser")).delete();
            animationTimer.stop();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ScoreDisplay.fxml"));
            try {
                Scene sc = new Scene(fxmlLoader.load());
                ScoreDisplayController scoreDisplay = fxmlLoader.getController();
                scoreDisplay.setCurrent_player(P);
                scoreDisplay.setScore(P.getScore());
                sc.getStylesheets().add(getClass().getResource("stylize.css").toExternalForm());
//                if(mainframe == null) System.out.println("should not happen");
//                if(mainframe.getScene().getWindow() == null) System.out.println("no");
                ((Stage) mainframe.getScene().getWindow()).setScene(sc);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Bounce effect after block collision
     */
    private void pushUp() {
        int fac = 2;
        ArrayList<Wall> to_be_removedW = new ArrayList<Wall>();
        IntStream.range(0, walls.size()).forEachOrdered(i -> {
            walls.get(i).setTranslateY(walls.get(i).getTranslateY() - fac * refreshRate / 2);
            if (walls.get(i).getTranslateY() > 800) {
                mainframe.getChildren().remove(walls.get(i));
                to_be_removedW.add(walls.get(i));
            }
        });
        for (Wall aTo_be_removedW : to_be_removedW) {
            walls.remove(aTo_be_removedW);
        }
        ArrayList<Token> to_be_removedT = new ArrayList<Token>();
        to_be_removedT.clear();
        IntStream.range(0, tokens.size()).forEachOrdered(i -> {
//            System.out.println(i);
//            tokens.get(i).setTranslateY(tokens.get(i).getTranslateY() + refreshRate/2);
            tokens.get(i).pull(- fac * refreshRate / 2);
            if (tokens.get(i).getTranslateY() > 800) {
                mainframe.getChildren().remove(tokens.get(i));
                to_be_removedT.add(tokens.get(i));
            }
        });
        for (Token aTo_be_removedT : to_be_removedT) {
            tokens.remove(aTo_be_removedT);
        }
        ArrayList<Ball> to_be_removed = new ArrayList<Ball>();
        IntStream.range(0, balls.size()).forEachOrdered(i -> {
            balls.get(i).setTranslateY(balls.get(i).getTranslateY() - fac * refreshRate / 2);
            balls.get(i).getLabel().setTranslateY(balls.get(i).getTranslateY() - fac * refreshRate / 2);
            if (balls.get(i).getTranslateY() > 800) {
                mainframe.getChildren().remove(balls.get(i));
                to_be_removed.add(balls.get(i));
            }
        });
        for (Ball aTo_be_removed : to_be_removed) {
            balls.remove(aTo_be_removed);
        }
        ArrayList<Block> to_be_removedB = new ArrayList<Block>();
        IntStream.range(0, blocks.size()).forEachOrdered(i -> {
            blocks.get(i).setTranslateY(blocks.get(i).getTranslateY() - fac * refreshRate / 2);
            blocks.get(i).getLabel().setTranslateY(blocks.get(i).getLabel().getTranslateY() - fac * refreshRate / 2);
            if (blocks.get(i).getTranslateY() > 800) {
                mainframe.getChildren().remove(blocks.get(i));
                to_be_removedB.add(blocks.get(i));
            }
        });
        for (Block aTo_be_removedB : to_be_removedB) {
            blocks.remove(aTo_be_removedB);
        }
    }
}
