import bagel.util.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * A utility class that provides methods to read and write files.
 */
public class IOUtils {
    /***
     * Read a properties file and return a Properties object
     * @param configFile: the path to the properties file
     * @return: Properties object
     */
    public static Properties readPropertiesFile(String configFile) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(configFile));
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return appProps;
    }

    public static Point parseCoords(String coords) {
        String[] coordinates = coords.split(",");

        return new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
    }

    public static Point parseInitialMultipleCoords(String coords) {
        String[] pairs= coords.split(";");
        for (String pair: pairs) {
            String[] numbers = pair.split(",");
            double firstNum = Double.parseDouble(numbers[0]);
            double secondNum = Double.parseDouble(numbers[1]);
            return new Point(firstNum, secondNum);
        }

        return new Point(1, 2);

    }
    public static ArrayList<Point> parseMultipleCoords(String coords) {
        String[] pairs= coords.split(";");
        ArrayList<Point> points = new ArrayList<>();
        for (String pair: pairs) {
            String[] numbers = pair.split(",");
            double firstNum = Double.parseDouble(numbers[0]);
            double secondNum = Double.parseDouble(numbers[1]);
            points.add(new Point(firstNum, secondNum));

        }

        return points;

    }
}
