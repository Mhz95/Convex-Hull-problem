package ConvexHull_Project;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.geom.*;
import java.awt.geom.Point2D.Float;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Convex Hull Algorithm - Programming project
 * 
 * @Authors Alya Alshammari, Munerah H. Alzaidan, Norah Alshahrani, Shams
 *          Alshamasi
 * @Supervisor Prof. M.B. Menai
 * @Copyrights King Saud University CSC 512 Algorithms
 * @since NOV 2019
 */
public class ConvexHullProject extends Application {

	ArrayList<Point2D.Float> visual_soln = new ArrayList<Point2D.Float>();

	// Q1 Input & Output
	ArrayList<Point2D.Float> InputPoints = new ArrayList<Point2D.Float>();
	ArrayList<Point2D.Float> ConvexHull_result = new ArrayList<Point2D.Float>();
	
	// Q2 Input & Output
	Point2D.Float A = new Point2D.Float(-0.04751f, 0.39252f); // source
	Point2D.Float B = new Point2D.Float(1.11922f, 0.81579f); // destination
	ArrayList<Point2D.Float> Shortest_Path_Around = new ArrayList<Point2D.Float>();
	
	boolean switched = false;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		File file = new File("input.txt");
		Scanner sc;
		try {
			sc = new Scanner(file);

			while (sc.hasNextLine()&& sc.hasNext()) { //hasNext() to handle some exceptions
				float x = sc.nextFloat();
				float y = sc.nextFloat();
				Point2D.Float p = new Point2D.Float(x, y);
				InputPoints.add(p);
			}
			// sort inputs according to their x coordinates
			ArrayList<Point2D.Float> SortedInputPoints = MergeSort(InputPoints, InputPoints.size());
			// Checking input size
			switch (InputPoints.size()) {
			case 0:
				System.out.println(" No points found in the figure ");
				break;
			case 1:
				System.out.println(" only one point in the figure cannot form a convexhull");
				break;
			case 2:
				System.out.println(" Only 2 points in the figure can form a single convexhull edge");
				// call visualiser and send InputPoints array
				break;
			default:

				/**
				 * Question 1: Find convex hull
				 */
				
				System.out.println("--------------------------------------------------");
				System.out.println("Question 1 Solution");
				System.out.println("--------------------------------------------------");

				QuickHull obj = new QuickHull();
				ConvexHull_result = obj.quickHull(SortedInputPoints);
				System.out.println("The points in the Convex hull using Quick Hull are: ");
				for (int i = 0; i < ConvexHull_result.size(); i++)
					System.out.println("(" + ConvexHull_result.get(i).x + ", " + ConvexHull_result.get(i).y + ")");

				/**
				 * Question 2: Shortest Path Around between A and B
				 */

				System.out.println("--------------------------------------------------");
				System.out.println("Question 2 Solution");
				System.out.println("--------------------------------------------------");

				Shortest_Path_Around obj2 = new Shortest_Path_Around();

				Shortest_Path_Around = obj2.quickHull(SortedInputPoints, A, B);
				
				System.out.println("The points that forms Shortest Path Around between A and B are: ");
				for (int i = 0; i < Shortest_Path_Around.size(); i++) {
					System.out
							.println("(" + Shortest_Path_Around.get(i).x + ", " + Shortest_Path_Around.get(i).y + ")");
				}

				sc.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Quick Hull Test"); // ??

		ScatterChart scChart = createScatterChart(InputPoints);
		LineChart li = createLineChart();

		Scene scene = new Scene(layerCharts(scChart, li));

		stage.setScene(scene);
		stage.setTitle("CSC 512 - Convex Hull Algorithm");
		stage.show();

	}

	public static ArrayList<Point2D.Float> MergeSort(ArrayList<Point2D.Float> InputPoints, int size) {
		int Size_of_inputPoints = size;
		if (Size_of_inputPoints > 1) {
			int mid = Size_of_inputPoints / 2;
			ArrayList<Point2D.Float> subList1 = new ArrayList<Point2D.Float>();
			ArrayList<Point2D.Float> subList2 = new ArrayList<Point2D.Float>();

			for (int i = 0; i < mid; i++) {
				subList1.add(InputPoints.get(i)); // holds the first half of the input points
			}

			subList1 = MergeSort(subList1, mid);

			for (int i = mid; i < size; i++) {
				subList2.add(InputPoints.get(i)); // holds the first half of the input points
			}

			subList2 = MergeSort(subList2, size - mid);

			InputPoints = Merge(subList1, subList2, mid, size - mid);
		}
		return InputPoints;
	}

	public static ArrayList<Point2D.Float> Merge(ArrayList<Point2D.Float> subList1, ArrayList<Point2D.Float> subList2,
			int n, int m) {
		ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>();
		int i = 0, f = 0, s = 0;
		while (f < n && s < m) {
			if (subList1.get(f).x < subList2.get(s).x) {
				points.add(i, subList1.get(f));
				i++;
				f++;
			} else {
				points.add(i, subList2.get(s));
				i++;
				s++;
			}
		}
		while (f < n) {
			points.add(i, subList1.get(f));
			i++;
			f++;
		}

		while (s < m) {
			points.add(i, subList2.get(s));
			i++;
			s++;
		}
		return points;
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
		final NumberAxis axis = new NumberAxis(-0.2, 1.2, 0.2);

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

	private ScatterChart<Number, Number> createScatterChart(ArrayList<Point2D.Float> points) {
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

		ScatterChart sc = (ScatterChart) charts[0];
		LineChart li = (LineChart) charts[1];

		final VBox vbox = new VBox();
		final HBox hbox = new HBox();

		final Button next = new Button("Next");
		final Button previous = new Button("Previous");

		final RadioButton isQ1 = new RadioButton("Question 1");
		final RadioButton isQ2 = new RadioButton("Question 2");  
		
		hbox.setSpacing(10);
		hbox.getChildren().addAll(next, previous, isQ1, isQ2);

		vbox.getChildren().addAll(li, hbox);
		hbox.setPadding(new Insets(500, 500, 10, 50));
		
		isQ1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switched = true;
				visual_soln = ConvexHull_result;
				li.getData().clear();
				sc.getData().clear();
				XYChart.Series series1 = new XYChart.Series();

				for (int i = 0; i < InputPoints.size(); i++) {
					series1.getData().add(new XYChart.Data(InputPoints.get(i).x, InputPoints.get(i).y));
				}

				sc.getData().addAll(series1);				
				isQ2.setSelected(false);
			}
		});
		isQ2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switched = true;
				visual_soln = Shortest_Path_Around;
				li.getData().clear();
				XYChart.Series series = new XYChart.Series();
				series.getData().add(new XYChart.Data(A.x, A.y));
				series.getData().add(new XYChart.Data(B.x, B.y));
				sc.getData().add(series);
				isQ1.setSelected(false);
			}
		});
		next.setOnAction(new EventHandler<ActionEvent>() {
			int step = 0;

			@Override
			public void handle(ActionEvent e) {

				if (li.getData() == null)
					li.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());

				if(switched) {
					step = 0;
					switched = false;
				}
				
				if (step == 0) {
					System.out.println("First Step In Solution:");
				}

				XYChart.Data firstPoint = new XYChart.Data(visual_soln.get(0).x, visual_soln.get(0).y);
				XYChart.Series series = new XYChart.Series();

				if ((step + 1) < visual_soln.size()) {

					series.getData()
							.add(new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));

					step++;

					series.getData()
							.add(new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));

					System.out.println(
							"X: " + visual_soln.get((step)).x + " Y: " + visual_soln.get((step)).y);

				} else if ((step + 1) == visual_soln.size()) {
					series.getData()
							.add(new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));
					series.getData().add(firstPoint);
					step++;
				} else {
					step = 0;
					li.getData().clear();
				}

				li.getData().add(series);
				li.setLegendVisible(false);
			}
		});

		previous.setOnAction(new EventHandler<ActionEvent>() {
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