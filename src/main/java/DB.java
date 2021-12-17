import org.sqlite.JDBC;

import java.sql.*;
import java.util.*;

public class DB {

    private static final String con = "jdbc:sqlite:Buildings.sqlite";
    private static DB instance = null;

    public static synchronized DB getInstance() throws SQLException {
        if (instance == null)
            instance = new DB();
        return instance;
    }

    private Connection connection;

    private DB() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(con);
    }

    public void addBuilding(Building building){
        try {
            var buildingStmt = this.connection.prepareStatement("insert into Buildings(number, address," +
                    " snapshot, appellation, 'floors count', " +
                    " 'building type', id, 'construction year') values (?,?,?,?,?,?,?,?)");
            buildingStmt.setObject(1, building.number);
            buildingStmt.setObject(2, building.address);
            buildingStmt.setObject(3, building.snapshot);
            buildingStmt.setObject(4, building.appellation);
            buildingStmt.setObject(5, building.floorsCount);
            buildingStmt.setObject(6, building.type);
            buildingStmt.setObject(7, building.id);
            buildingStmt.setObject(8, building.constructionYear);
            buildingStmt.execute();

            var prefixStmt = this.connection.prepareStatement("insert into Prefixes(number, 'prefix code') " +
                    "VALUES (?, ?)");
            prefixStmt.setObject(1, building.number);
            prefixStmt.setObject(2, building.prefixCode);
            prefixStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void fillDB(List<Building> buildings) {
        for (var building : buildings)
            addBuilding(building);
    }

    public ArrayList<Integer> getAllFloors() throws SQLException {
        var floors = new ArrayList<Integer>();
        var request = "select distinct \"Floors count\"\n" +
                "from Buildings\n" +
                "where \"Floors count\" > 0\n" +
                "order by \"Floors count\"";
        var stmt = connection.createStatement();
        var results = stmt.executeQuery(request);
        while (results.next())
            floors.add(results.getInt(1));
        return floors;
    }

    public int findNumberOfHousesByFloor(int floor) throws SQLException {
        var request = "select COUNT(Buildings.Number)\n" +
                "from Buildings, Prefixes\n" +
                "where \"Floors count\" = ?\n" +
                "and Buildings.Number = Prefixes.Number";
        var stmt = connection.prepareStatement(request);
        stmt.setObject(1, floor);
        var result = stmt.executeQuery();
        return result.getInt(1);
    }

    public List<String> findHousesOnHighwayByPrefix(int prefix) throws SQLException {
        var houses = new ArrayList<String>();
        var request = "select Buildings.Number\n" +
                "from Buildings, Prefixes\n" +
                "where Address like '%Шлиссельбургское шоссе%' " +
                "and Buildings.Number = Prefixes.Number and \"prefix code\" == ?";
        var stmt = connection.prepareStatement(request);
        stmt.setObject(1, prefix);
        var results = stmt.executeQuery();
        while (results.next())
            houses.add(results.getString(1));
        return houses;
    }

    public List<Integer> findPrefixesOfUniversities() throws SQLException {
        var prefixes = new ArrayList<Integer>();
        var request = "select \"prefix code\"\n" +
                "from Buildings, Prefixes\n" +
                "where Appellation like '%университет%' and \"Floors count\" > 5 and \"Construction year\" not null\n" +
                "and Buildings.Number = Prefixes.Number";
        var stmt = connection.createStatement();
        var results = stmt.executeQuery(request);
        while (results.next())
            prefixes.add(results.getInt(1));
        return prefixes;
    }

}