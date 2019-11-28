package edu.ksu.algorithms.convexhull;

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

	public static double xPoints[] = { 0.00622, 0.05208, 0.05668, 0.08892, 0.09462, 0.10133, 0.1067, 0.10372, 0.12248,
			0.14415, 0.14647, 0.15026, 0.16889, 0.16964, 0.18846, 0.19974, 0.2078, 0.21485, 0.24201, 0.26569, 0.27135,
			0.28165, 0.29008, 0.29605, 0.30448, 0.31439, 0.31777, 0.34873, 0.34935, 0.34871, 0.3986, 0.39815, 0.4116,
			0.41682, 0.41576, 0.42069, 0.41933, 0.42685, 0.44873, 0.45347, 0.49298, 0.51562, 0.53466, 0.54323, 0.5608,
			0.5764, 0.57627, 0.58084, 0.58841, 0.59543, 0.62421, 0.66625, 0.6648, 0.67247, 0.67998, 0.68418, 0.69529,
			0.69411, 0.70149, 0.72099, 0.72598, 0.74849, 0.75319, 0.751, 0.78812, 0.79874, 0.81084, 0.83687, 0.88026,
			0.87893, 0.88553, 0.89703, 0.89618, 0.90369, 0.90309, 0.90887, 0.92807, 0.94766, 0.9494, 0.9592, 0.9707,
			0.99338, 0.12756, 0.0206, 0.41889, 0.42916, 0.96375, 0.87771, 0.62026, 0.68622, 0.69385, 0.04348, 0.02059,
			0.0028, 0.62035, 0.14088, 0.14723, 0.74983, 0.53064 };
	public static double yPoints[] = { 0.67776, 0.16801, 0.82481, 0.61442, 0.69217, 0.64757, 0.26416, 0.04154, 0.2887,
			0.78791, 0.64127, 1.01297, 0.82789, 0.12763, 0.00756, 0.12677, 1.03616, 0.62739, 1.03847, 0.22171, 0.28649,
			0.05674, 0.62035, 0.79312, 0.38954, 0.02321, 0.25107, 1.03482, 0.28726, 0.06358, 0.7662, 0.60964, 1.00907,
			0.92162, 0.54653, 0.83715, 0.35909, 1.08106, 0.10941, 0.81585, 0.27403, 0.5368, 0.01155, 1.10513, 1.0284,
			0.25503, 0.20907, 0.8572, 0.63017, 1.07868, 0.22738, 0.57804, 0.06451, 0.83966, 1.07604, 0.14425, 0.68472,
			0.26778, 0.94136, 0.58055, 0.40719, 0.14429, 0.83459, 0.06358, 0.12437, 0.0112, 0.41773, 0.42715, 0.77237,
			0.30351, 0.69834, 0.89576, 0.59392, 0.34658, 0, 0.2449, 0.74379, 0.67699, 0.06234, 0.62122, 0.01407,
			0.61187, 0.00563, 0.07176, 0.18678, 0.20978, 0.71297, 0.96026, 1.03214, 0.89988, 0.89412, 0.94301, 0.96601,
			0.99189, 1.0609, 1.1069, 0.64684, 0.84237, 0.01425};

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		ScatterChart sc = createScatterChart();
		LineChart li = createLineChart();

		Scene scene = new Scene(layerCharts(sc, li));

		stage.setScene(scene);
		stage.setTitle("CSC 512 - Convex Hull Algorithm");
		stage.show();
	}

	private NumberAxis createYaxis() {
		final NumberAxis axis = new NumberAxis(0, 1.15, 0.1);

		axis.setPrefWidth(35);
		axis.setMinorTickCount(20);

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

	private ScatterChart<Number, Number> createScatterChart() {
		final ScatterChart<Number, Number> chart = new ScatterChart<>(createXaxis(), createYaxis());

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("All points");

		for (int i = 0; i < xPoints.length; i++) {
			series1.getData().add(new XYChart.Data(xPoints[i], yPoints[i]));
		}

		chart.getData().addAll(series1);
		chart.setPrefSize(500, 400);

		return chart;
	}

	private LineChart<Number, Number> createLineChart() {
		final LineChart<Number, Number> chart = new LineChart<>(createXaxis(), createYaxis());

		chart.setCreateSymbols(false);

		XYChart.Series series1 = new XYChart.Series();
		series1.getData().add(new XYChart.Data(xPoints[4], yPoints[4]));
		series1.getData().add(new XYChart.Data(xPoints[20], yPoints[20]));
		series1.setName("Step 1");
		chart.getData().addAll(series1);

		XYChart.Series series2 = new XYChart.Series();
		series2.getData().add(new XYChart.Data(xPoints[12], yPoints[12]));
		series2.getData().add(new XYChart.Data(xPoints[18], yPoints[18]));
		series2.setName("Step 2");
		chart.getData().addAll(series2);
		chart.setPrefSize(500, 400);

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

		hbox.setSpacing(10);
		hbox.getChildren().addAll(add, remove);

		vbox.getChildren().addAll(li, hbox);
		hbox.setPadding(new Insets(500, 500, 10, 50));

		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (li.getData() == null)
					li.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());

				for (int i = 0; i < 2; i++) {
					XYChart.Series series = new XYChart.Series();
					series.setName("Step " + (li.getData().size() + 1));
					series.getData().add(new XYChart.Data(xPoints[17 + i], yPoints[17 + i]));
					series.getData().add(new XYChart.Data(xPoints[27 + i], yPoints[27 + i]));
					li.getData().add(series);
					li.setPrefSize(500, 400);

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