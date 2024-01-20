import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HSManager {
    private static final String file = "highScores.txt";

    public static void saveHighScore(HighScore highScore) {
        List<HighScore> highScores = loadHighScores();

        highScores.add(highScore);

        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(highScores);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<HighScore> loadHighScores() {
        List<HighScore> highScores = new ArrayList<>();

        try (FileInputStream fileInput = new FileInputStream(file);
             ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {

            Object obj = objectInput.readObject();
            if (obj instanceof List<?> list) {
                for (Object item : list) {
                    if (item instanceof HighScore highScore) {
                        highScores.add(highScore);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        highScores.sort(Comparator.comparingInt(HighScore::getScore).reversed());

        return highScores;
    }
}
