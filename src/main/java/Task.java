import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Task {
    private ArrayList<Integer> floors = new ArrayList<>();
    private DB dbHandler = DB.getInstance();

    public Task() throws SQLException {
        floors = dbHandler.getAllFloors();
    }

    public void createBarChart() throws SQLException {
        var dataset = new DefaultCategoryDataset();
        for (var floor : floors){
            dataset.setValue(dbHandler.findNumberOfHousesByFloor(floor), floor, "Этаж");
            System.out.println(floor + " " + dbHandler.findNumberOfHousesByFloor(floor));
        }
        var chart = ChartFactory.createBarChart(
                "Кол-во домов по этажам",
                null,
                "Количество домов",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);
        chart.setBackgroundPaint(Color.MAGENTA);
        chart.getTitle().setPaint(Color.green);
        var plot = chart.getCategoryPlot();
        var br = (BarRenderer) plot.getRenderer();
        br.setItemMargin(0);
        var frame = new JFrame("Количество домов по этажам");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        var cp = new ChartPanel(chart);
        frame.add(cp);
        frame.pack();
        frame.setVisible(true);
    }

    public void printHighwayHouses() throws SQLException {
        var houses = dbHandler.findHousesOnHighwayByPrefix(9881);
        for (var house : houses)
            System.out.println(house);
    }

    public void printAveragePrefixOfUnis() throws SQLException {
        var prefixes = dbHandler.findPrefixesOfUniversities();
        System.out.println(prefixes.stream().reduce(0, Integer::sum) / prefixes.size());
    }
}
