import java.sql.SQLException;

public class main {
    public static void main(String[] args) {
        try {
            var dbHandler = DB.getInstance();
            var task = new Task();
            //dbHandler.fillDB(Parser.getBuildingsFromCSV());
            task.createBarChart();
            System.out.println();
            task.printHighwayHouses();
            System.out.println();
            task.printAveragePrefixOfUnis();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}