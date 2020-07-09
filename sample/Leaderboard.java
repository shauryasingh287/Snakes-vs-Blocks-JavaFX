package sample;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Model class to hold Leaderboard information
 */
public class Leaderboard implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * getter for leaders
     * @return <code> ArrayList </code> of <code> Player </code> containing top players
     */
    public ArrayList<Player> getLeaders() {
        return leaders;
    }

    public void setLeaders(ArrayList<Player> leaders) {
        this.leaders = leaders;
    }

    /**
     * getter for dates
     * @return <code> ArrayList </code> of <code> String </code> containing dates
     */
    public ArrayList<String> getDates() {
        return dates;
    }

    private ArrayList<Player> leaders;
    private ArrayList<String> dates;

    /**
     * Constructor for Leaderboard class
     */
    Leaderboard() {
        this.leaders = new ArrayList<>();
        this.dates = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            Player player = new Player("-");
            leaders.add(player);
            dates.add("-");
        }
        System.out.println(dates.size());
    }

    /**
     * Function to update Leaderboard with regard to current player
     * @param current_player <code> Player </code>
     */
    public void updateLeaderboard(Player current_player) {
        for (int i = 0; i < 10; i++) {
            if (current_player.getScore() > leaders.get(i).getScore()) {
                for (int j = 9; j > i; j--) {
                    leaders.set(j, leaders.get(j - 1));
                    System.out.println(dates.size());
                    dates.set(j, dates.get(j - 1));
                }
                leaders.set(i, current_player);
                DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
                LocalDate now = LocalDate.now();
                dates.set(i, now.toString());
                break;
            }
        }
    }

    /**
     * Serialize
     */
    public void serialize() {
        ObjectOutputStream writer = null;
        try {
            writer = new ObjectOutputStream(new FileOutputStream("leaderboard.ser"));
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
     * @return deserialized Leaderboard
     */
    public static Leaderboard deserialize() {
        ObjectInputStream reader = null;
        try {
            reader = new ObjectInputStream(new FileInputStream("leaderboard.ser"));
            return (Leaderboard) reader.readObject();
        } catch (FileNotFoundException e) {
            return new Leaderboard();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (NullPointerException e) {
                return new Leaderboard();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Leaderboard();
    }
}
