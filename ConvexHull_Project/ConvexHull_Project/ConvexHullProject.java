package ConvexHull_Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Convex Hull Algorithm - Programming project
 * 
 * @Authors Munerah H. Alzaidan, Shams Alshamasi, Norah Alshahrani, Alya
 *          Alshammari
 * @Supervisor Prof. M.B. Menai
 * @Copyrights King Saud University CSC 512 Algorithms
 * @since NOV 2019
 */
public class ConvexHullProject extends Application {

	ArrayList<Point> points = new ArrayList<Point>();
	List<XYChart.Data<Number, Number>> solution = new ArrayList<>();

	public boolean isShuffled = false;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		File file = new File("./InputPoints.txt");

		Scanner sc;
		try {
			sc = new Scanner(file);
			int index = 0;

			while (sc.hasNextLine()) {

				float x = sc.nextFloat();
				float y = sc.nextFloat();

				Point p = new Point(x, y);
				System.out.println("(" + p.x + ", " + p.y + ")");

				points.add(index, p);
				index++;
			}

			QuickHull qh = new QuickHull();
			ArrayList<Point> p = qh.quickHull(points);
			System.out.println("The points in the Convex hull using Quick Hull are: ");
			for (int i = 0; i < p.size(); i++) {
				solution.add(new XYChart.Data(p.get(i).x, p.get(i).y));
				System.out.println("(" + p.get(i).x + ", " + p.get(i).y + ")");

			}

			sc.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Quick Hull Test");

		ScatterChart scChart = createScatterChart(points);
		LineChart li = createLineChart();

		Scene scene = new Scene(layerCharts(scChart, li));

		stage.setScene(scene);
		stage.setTitle("CSC 512 - Convex Hull Algorithm");
		stage.show();
	}

	private NumberAxis createYaxis() {
		final NumberAxis axis = new NumberAxis(0, 1.15, 0.1);

		axis.setPrefWidth(35);
		axis.setMinorTickCount(10);

		axis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(axis) {
			@Override
			public String toString(Number object) {
				return String.format("%.1f", object.floatValue());
			}
		});

		return axis;
	}

	private NumberAxis createXaxis() {
		final NumberAxis axis = new NumberAxis(0, 1, 0.2);

		axis.setPrefWidth(35);
		axis.setMinorTickCount(10);

		axis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(axis) {
			@Override
			public String toString(Number object) {
				return String.format("%.1f", object.floatValue());
			}
		});

		return axis;
	}

	private ScatterChart<Number, Number> createScatterChart(ArrayList<Point> points) {
		final ScatterChart<Number, Number> chart = new ScatterChart<>(createXaxis(), createYaxis());

		XYChart.Series series1 = new XYChart.Series();

		for (int i = 0; i < points.size(); i++) {
			series1.getData().add(new XYChart.Data(points.get(i).x, points.get(i).y));
		}

		chart.getData().addAll(series1);
		chart.setMaxSize(500, 400);
		chart.setLegendVisible(false);

		return chart;
	}

	private LineChart<Number, Number> createLineChart() {
		final LineChart<Number, Number> chart = new LineChart<>(createXaxis(), createYaxis());

		chart.setCreateSymbols(false);
		chart.setMaxSize(500, 400);
		chart.setLegendVisible(false);

		return chart;
	}

	private void setDefaultChartProperties(final XYChart<Number, Number> chart) {
		chart.setLegendVisible(false);
		chart.setAnimated(false);
	}

	private StackPane layerCharts(final XYChart<Number, Number>... charts) {
		for (int i = 1; i < charts.length; i++) {
			configureOverlayChart(charts[i]);
		}

		StackPane stackpane = new StackPane();

		LineChart li = (LineChart) charts[1];

		final VBox vbox = new VBox();
		final HBox hbox = new HBox();

		final Button add = new Button("Next");
		final Button remove = new Button("Previous");
		final Button shuffle = new Button("Shuffle");

		hbox.setSpacing(10);
		hbox.getChildren().addAll(add, remove, shuffle);

		vbox.getChildren().addAll(li, hbox);
		hbox.setPadding(new Insets(500, 500, 10, 50));

		add.setOnAction(new EventHandler<ActionEvent>() {
			int step = 0;

			@Override
			public void handle(ActionEvent e) {

				if (li.getData() == null)
					li.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());

				if (isShuffled) {
					step = 0;
					isShuffled = false;
				}
				
				XYChart.Data firstPoint = solution.get(0);

				if ((step + 1) < solution.size()) {
					XYChart.Series series = new XYChart.Series();
					System.out.println("Normal: "+step);

					series.getData().add(solution.get(step));
			
					
					step++;
					
					if ((step + 1) == solution.size()) {
						series.getData().add(firstPoint);

					} else {
						series.getData().add(solution.get(step));
					}
					
					System.out.println("X: "+ solution.get((step)).getXValue() + " Y: "+ solution.get((step)).getYValue());
					
					li.getData().add(series);
					li.setLegendVisible(false);

				} else {
					step = 0;
					li.getData().clear();
				}
			}
		});

		remove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!li.getData().isEmpty())
					li.getData().remove((int) (Math.random() * (li.getData().size() - 1)));
			}
		});

		shuffle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				isShuffled = true;
				Collections.shuffle(points);
				// Find solution again
				QuickHull qh = new QuickHull();
				ArrayList<Point> p = qh.quickHull(points);
				System.out.println("-------- The solution after shuffle: ");
				solution.clear();
				for (int i = 0; i < p.size(); i++) {
					solution.add(new XYChart.Data(p.get(i).x, p.get(i).y));
					System.out.println("(" + p.get(i).x + ", " + p.get(i).y + ")");

				}
				li.getData().clear();

			}
		});

		stackpane.getChildren().addAll(charts);
		stackpane.getChildren().add(vbox);

		return stackpane;
	}

	private void configureOverlayChart(final XYChart<Number, Number> chart) {
		chart.setAlternativeRowFillVisible(false);
		chart.setAlternativeColumnFillVisible(false);
		chart.setHorizontalGridLinesVisible(false);
		chart.setVerticalGridLinesVisible(false);
		chart.getXAxis().setVisible(false);
		chart.getYAxis().setVisible(false);

		chart.getStylesheets().addAll(getClass().getResource("overlay-chart.css").toExternalForm());
	}
}