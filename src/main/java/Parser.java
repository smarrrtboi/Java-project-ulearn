import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parser {
    public static ArrayList<Building> getBuildingsFromCSV() throws IOException {
        var buildings = new ArrayList<Building>();
        var flag = true;
        List<String[]> readLines = null;
        try (var reader = new CSVReader(new FileReader("База Санкт-Петербурга.csv"))){
            readLines = reader.readAll();
        } catch (CsvException e) {
            e.printStackTrace();
        }
        for (String[] line : readLines)
        {
            if (flag){
                flag = false;
                continue;
            }
            var floorNumber = Objects.equals(line[4], "") ||
                    Objects.equals(line[4], "Малоэтажные") ||
                    Objects.equals(line[4], "Многоэтажные")
                    ? 0 : Integer.parseInt(line[4].split("-")[0]);
            buildings.add(new Building(line[0], line[1], line[2], line[3], floorNumber, Integer.parseInt(line[5]),
                    line[6], Integer.parseInt(line[7]), line[8]));
        }
        return buildings;
    }
}
