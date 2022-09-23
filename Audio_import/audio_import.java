import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class FirstGUI extends Application {

    private static Scene scene1, scene2;
    private static byte[] audioBytes;
    private static int dataoffset = 78;
    private static int end = 0;
    private static int frequency = 48000;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Wav frequency");

        Label label1 = new Label("Which wav file do you want to read?");
        Button button1 = new Button("bass_1.wav");
        button1.setOnAction(e -> {
            readWAV("bass_1.wav");
            showScene2(primaryStage);
        });
        Button button2 = new Button("cartoon_2.wav");
        button2.setOnAction(e -> {
            readWAV("cartoon_2.wav");
            showScene2(primaryStage);
        });
        Button button3 = new Button("HP_2.wav");
        button3.setOnAction(e -> {
            readWAV("HP_2.wav");
            showScene2(primaryStage);
        });
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1, button1, button2, button3);
        scene1 = new Scene(layout1, 300, 250);

        //Setting title to the Stage
        primaryStage.setTitle("Bar Chart");


        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

    public static void readWAV(String fileName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(fileName));
            int read;
            byte[] buff = new byte[1024];
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
            out.flush();
            audioBytes = out.toByteArray();
            System.out.println(audioBytes.length);
            for (int i = 0; i < 100; i++) {
                char c = (char)audioBytes[i];
                System.out.println(i + " " + audioBytes[i] + " " + c);
            }
            end = audioBytes.length;
        } catch (Exception e) {
            System.out.println(e);
        }
        //end = audioBytes.length;
    }

    //public static String[] BinToHex ()

    public static void showScene2(Stage primaryStage) {
        //Defining the axes
        List<String> bins1 = new ArrayList<>();
        List<String> bins2 = new ArrayList<>();
        for (int i = dataoffset; i < end; i=i+2) {
            if(i%100!=0){
                continue;
            }
            bins1.add(i+"");
            bins2.add(i+"");
        }

        CategoryAxis xAxis1 = new CategoryAxis();
        xAxis1.setCategories(FXCollections.<String>
                observableArrayList(bins1));
        xAxis1.setLabel("time");
        CategoryAxis xAxis2 = new CategoryAxis();
        xAxis2.setCategories(FXCollections.<String>
                observableArrayList(bins2));
        xAxis2.setLabel("time");

        NumberAxis yAxis1 = new NumberAxis();
        yAxis1.setLabel("amplitude");
        NumberAxis yAxis2 = new NumberAxis();
        yAxis2.setLabel("amplitude");

        //Creating the Bar chart
        BarChart<String, Number> barChart1 = new BarChart<>(xAxis1, yAxis1);
        barChart1.setTitle("Sound frequency");
        BarChart<String, Number> barChart2 = new BarChart<>(xAxis2, yAxis2);
        barChart2.setTitle("Sound frequency");

        //Prepare XYChart.Series objects by setting data
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Left channel");
        ObservableList<XYChart.Data<String,Number>> data1 = series1.getData();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Right channel");
        ObservableList<XYChart.Data<String,Number>> data2 = series2.getData();

        for (int i = dataoffset; i < end; i=i+2) {
            if(i%100 !=0){
                continue;
            }

            data1.add(new XYChart.Data<>(i+"", audioBytes[i+1]));
            data2.add(new XYChart.Data<>(i+"", audioBytes[i+1]));
            if (i%100 == 0)
                System.out.println(i);
        }
        series1.setData(data1);
        series2.setData(data2);

        //Setting the data to bar chart
        //barChart1.getData().addAll(series1, series2);
        //Creating a Group object
        //Group root = new Group(barChart1)

        Label label = new Label("samples : "+ (audioBytes.length-dataoffset)/4 +", frequency: " + frequency);

        barChart1.getData().addAll(series1);
        barChart2.getData().addAll(series2);
        VBox layout = new VBox(20);
        layout.getChildren().addAll(barChart1, barChart2,label);
        //Creating a scene object
        scene2 = new Scene(layout, 800, 600);
        primaryStage.setScene(scene2);
    }
}
