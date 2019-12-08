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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

	/**
	 * Input variables
	 */
	ArrayList<Point2D.Float> InputPoints = new ArrayList<Point2D.Float>();
	ArrayList<Point2D.Float> SortedInputPointsForQ1 = new ArrayList<Point2D.Float>();
	ArrayList<Point2D.Float> SortedInputPointsForQ2 = new ArrayList<Point2D.Float>();

	/**
	 * Output variables
	 */
	ArrayList<Point2D.Float> ConvexHull_result = new ArrayList<Point2D.Float>();
	ArrayList<Point2D.Float> Shortest_Path_Around = new ArrayList<Point2D.Float>();

	/**
	 * Points A and B for Q2
	 */

	Point2D.Float A = new Point2D.Float(-0.04751f, 0.39252f); // source
	Point2D.Float B = new Point2D.Float(1.11922f, 0.81579f); // destination

	/**
	 * To be shown in the visualizer
	 */
	ArrayList<Point2D.Float> visual_soln = new ArrayList<Point2D.Float>();

	/**
	 * witch Qs in the visualizer
	 */
	boolean switched = false;
	boolean fileUpdated = false;
	int q = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		/**
		 * Read Input points from file
		 */
		File file = new File("./input.txt");
		Scanner sc;
		try {
			sc = new Scanner(file);

			while (sc.hasNextLine() && sc.hasNext()) {
				// hasNext() to handle some exceptions
				float x = sc.nextFloat();
				float y = sc.nextFloat();
				Point2D.Float p = new Point2D.Float(x, y);
				InputPoints.add(p);
			}

			sc.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Initialize Charts and add them to scene
		 */
		ScatterChart scChart = createScatterChart(InputPoints);
		LineChart li = createLineChart();

		Scene scene = new Scene(layerCharts(scChart, li), 600, 500);

		stage.setScene(scene);
		stage.setTitle("CSC 512 - Convex Hull Algorithm");
		stage.show();

	}

	/**
	 * Reset All Arrays for visualization
	 */
	public void reset() {

		SortedInputPointsForQ1.clear();
		SortedInputPointsForQ2.clear();
		Shortest_Path_Around.clear();
		ConvexHull_result.clear();
		visual_soln.clear();
	}

	public boolean updateInputPoints(String filepath) {
		/**
		 * Read Input points from file
		 */
		if (filepath != null && !filepath.isEmpty()) {
			InputPoints.clear();
			File file = new File(filepath);
			Scanner sc;
			try {
				sc = new Scanner(file);

				while (sc.hasNextLine() && sc.hasNext()) {
					// hasNext() to handle some exceptions
					float x = sc.nextFloat();
					float y = sc.nextFloat();
					Point2D.Float p = new Point2D.Float(x, y);
					InputPoints.add(p);
				}

				sc.close();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * To stack charts as layers
	 */
	private VBox layerCharts(final XYChart<Number, Number>... charts) {
		/**
		 * Style Charts
		 */
		for (int i = 0; i < charts.length; i++) {
			configureOverlayChart(charts[i]);
		}

		final ScatterChart sc = (ScatterChart) charts[0];
		final LineChart li = (LineChart) charts[1];
		final ArrayList<Point2D.Float[]> steps = new ArrayList<Point2D.Float[]>();
		final XYChart.Series solnPoints = new XYChart.Series();

		/**
		 * Add GUI components to scene
		 */
		StackPane stackpaneChart = new StackPane();

		final HBox hboxTop = new HBox();
		final HBox hboxMiddle = new HBox();
		final HBox hboxBottom = new HBox();

		final Button next = new Button("Next");
		final Button update = new Button("Update");

		final RadioButton isQ1 = new RadioButton("Question 1");
		final RadioButton isQ2 = new RadioButton("Question 2");

		final Label label = new Label();
		label.setFont(new Font("Arial", 18));
		label.setTextFill(Color.web("#0076a3"));
		label.setWrapText(true);
		label.setPrefWidth(300);

		final Label fileLabel = new Label("Enter points file path: ");
		final TextField inputFilePath = new TextField();

		hboxTop.setSpacing(10);
		hboxTop.setPadding(new Insets(15, 20, 10, 10));
		hboxTop.getChildren().addAll(fileLabel, inputFilePath, update);

		StackPane stackpaneTop = new StackPane();
		stackpaneTop.getChildren().addAll(hboxTop);

		hboxMiddle.setSpacing(10);
		hboxMiddle.setPadding(new Insets(15, 20, 10, 10));
		hboxMiddle.getChildren().addAll(isQ1, isQ2, label);

		StackPane stackpaneControlsTop = new StackPane();
		stackpaneControlsTop.getChildren().addAll(hboxMiddle);

		hboxBottom.setSpacing(10);
		hboxBottom.setPadding(new Insets(15, 20, 10, 10));
		hboxBottom.getChildren().addAll(next);

		StackPane stackpaneControlsBottom = new StackPane();
		stackpaneControlsBottom.getChildren().addAll(hboxBottom);
		/**
		 * To handle update input points
		 */
		update.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (updateInputPoints(inputFilePath.getText())) {
					label.setText("Input points updated !");

					reset();
					fileUpdated = true;
					switched = true;
					q = 0;
					isQ1.setSelected(false);
					isQ2.setSelected(false);

					li.getData().clear();
					sc.getData().clear();
					solnPoints.getData().clear();
					// Draw points
					XYChart.Series series1 = new XYChart.Series();
					for (int i = 0; i < InputPoints.size(); i++) {
						series1.getData().add(new XYChart.Data(InputPoints.get(i).x, InputPoints.get(i).y));

					}
					sc.getData().addAll(series1);
				}

			}
		});

		/**
		 * To handle Switch to Q1 in the visualizer
		 */
		isQ1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				label.setText("");

				// Reset All
				reset();
				steps.clear();

				// Find convexHull
				ArrayList<Point2D.Float[]> soln = solveQuestion1();
				if (soln != null) {
					steps.addAll(soln);
				}

				// Clear charts
				li.getData().clear();
				sc.getData().clear();

				// Draw points
				XYChart.Series series1 = new XYChart.Series();
				for (int i = 0; i < InputPoints.size(); i++) {
					series1.getData().add(new XYChart.Data(InputPoints.get(i).x, InputPoints.get(i).y));

				}
				sc.getData().addAll(series1);

				// Toggle radio button
				switched = true;
				q = 1;
				isQ2.setSelected(false);
			}
		});

		/**
		 * To handle Switch to Q2 in the visualizer
		 */
		isQ2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				label.setText("");

				// Reset All
				reset();

				// Find shortest path around
				solveQuestion2();

				// Clear charts
				li.getData().clear();
				sc.getData().clear();

				// Add 2 points
				XYChart.Series series1 = new XYChart.Series();
				for (int i = 0; i < InputPoints.size(); i++) {
					series1.getData().add(new XYChart.Data(InputPoints.get(i).x, InputPoints.get(i).y));

				}
				sc.getData().addAll(series1);

				XYChart.Series series = new XYChart.Series();
				series.getData().add(new XYChart.Data(A.x, A.y));
				series.getData().add(new XYChart.Data(B.x, B.y));
				sc.getData().add(series);

				// Toggle radio button
				switched = true;
				q = 2;
				isQ1.setSelected(false);
			}
		});

		/**
		 * To handle (Next) button in the visualizer
		 */
		next.setOnAction(new EventHandler<ActionEvent>() {
			int step = 0;
			int triStep = 0;

			@Override
			public void handle(ActionEvent e) {

				switch (InputPoints.size()) {
				case 0:
					System.out.println("No points found in the figure");
					label.setText("No points found in the figure");
					break;
				case 1:
					System.out.println("Only one point in the figure cannot form a convexhull");
					label.setText("Only one point in the figure cannot form a convexhull");
					break;
				case 2:
					System.out.println("Only 2 points in the figure can form a single convexhull edge");
					label.setText("Only 2 points in the figure can form a single convexhull edge");
					if (q == 1) {
						solnPoints.getData().clear();
						XYChart.Series twopoints = new XYChart.Series();
						twopoints.getData().add(new XYChart.Data(InputPoints.get(0).x, InputPoints.get(0).y));
						twopoints.getData().add(new XYChart.Data(InputPoints.get(1).x, InputPoints.get(1).y));
						solnPoints.getData().add(new XYChart.Data(InputPoints.get(0).x, InputPoints.get(0).y));
						solnPoints.getData().add(new XYChart.Data(InputPoints.get(1).x, InputPoints.get(1).y));
						li.getData().add(twopoints);
						li.setLegendVisible(false);
						if (!sc.getData().contains(solnPoints)) {
							sc.getData().add(solnPoints);
						}
					} else if (q == 2) {
						solnPoints.getData().clear();
						XYChart.Series twopoints2 = new XYChart.Series();
						twopoints2.getData().add(new XYChart.Data(visual_soln.get(0).x, visual_soln.get(0).y));
						twopoints2.getData().add(new XYChart.Data(visual_soln.get(1).x, visual_soln.get(1).y));
						solnPoints.getData().add(new XYChart.Data(visual_soln.get(0).x, visual_soln.get(0).y));
						solnPoints.getData().add(new XYChart.Data(visual_soln.get(1).x, visual_soln.get(1).y));
						li.getData().add(twopoints2);
						li.setLegendVisible(false);
						if (!sc.getData().contains(solnPoints)) {
							sc.getData().add(solnPoints);
						}
					}
					break;
				default:
					if (q == 1) {
						if (fileUpdated) {
							triStep = 0;
							fileUpdated = false;
						}
						if (steps.size() == 0) {
							System.out.println("Please input more points !");
							label.setText("Please input more points !");
						} else {
							visual_soln.clear();
							if (triStep < steps.size()) {

								Point2D.Float[] stepArr = steps.get(triStep);

								if (stepArr.length > 1) {
									visual_soln.add(stepArr[2]);
									visual_soln.add(stepArr[1]);
									visual_soln.add(stepArr[0]);

									label.setText("Step : " + (step + 1) + " | TriStep: " + (triStep + 1));

									// Reset on Qs switch
									if (switched) {
										step = 0;
										switched = false;
									}

									// Logs the start of the solution
									if (step == 0) {
										System.out.println("--------------------------------------------------");
										System.out.println("Visual Solution | TriStep " + (triStep + 1));
										System.out.println("--------------------------------------------------");
									}

									// Store the first point to be able to connect the last point
									XYChart.Data firstPoint = new XYChart.Data(visual_soln.get(0).x,
											visual_soln.get(0).y);
									XYChart.Series series = new XYChart.Series();

									if ((step + 1) < visual_soln.size()) {

										// Line start point
										series.getData().add(
												new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));
										solnPoints.getData().add(
												new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));

										System.out.println("Line " + (step + 1));
										System.out.println(
												"X: " + visual_soln.get((step)).x + " Y: " + visual_soln.get((step)).y);

										step++;

										// Line end point
										series.getData().add(
												new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));
										solnPoints.getData().add(
												new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));

										System.out.println(
												"X: " + visual_soln.get((step)).x + " Y: " + visual_soln.get((step)).y);

									} else if ((step + 1) == visual_soln.size()) {
										// If it is the last line

										// Line start point
										series.getData().add(
												new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));
										solnPoints.getData().add(
												new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));

										System.out.println("Line " + (step + 1));
										System.out.println(
												"X: " + visual_soln.get((step)).x + " Y: " + visual_soln.get((step)).y);

										// Line end point
										series.getData().add(firstPoint);
										System.out.println(
												"X: " + firstPoint.getXValue() + " Y: " + firstPoint.getYValue());

										step++;
									} else {
										if (!sc.getData().contains(solnPoints)) {
											sc.getData().add(solnPoints);
										}

										// Reset on steps finish
										step = 0;
										li.getData().clear();
										triStep++;
									}

									li.getData().add(series);
									li.setLegendVisible(false);
								} else {
									visual_soln.add(stepArr[0]);
									solnPoints.getData()
											.add(new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));

									if (!sc.getData().contains(solnPoints)) {
										sc.getData().add(solnPoints);
									}
									step = 0;
									triStep++;
								}

							} else if (triStep == steps.size()) {
								label.setText("Question 1 solved !");

								visual_soln.addAll(ConvexHull_result);

								XYChart.Data firstPoint = new XYChart.Data(visual_soln.get(0).x, visual_soln.get(0).y);
								int k = 0;
								while (k < visual_soln.size()) {
									XYChart.Series seriesAll = new XYChart.Series();

									if ((k + 1) < visual_soln.size()) {

										seriesAll.getData()
												.add(new XYChart.Data(visual_soln.get(k).x, visual_soln.get(k).y));
										k++;

										seriesAll.getData()
												.add(new XYChart.Data(visual_soln.get(k).x, visual_soln.get(k).y));

									} else if ((k + 1) == visual_soln.size()) {
										seriesAll.getData()
												.add(new XYChart.Data(visual_soln.get(k).x, visual_soln.get(k).y));
										seriesAll.getData().add(firstPoint);
										k++;
									}
									li.getData().add(seriesAll);
									li.setLegendVisible(false);
								}

								solnPoints.getData().clear();
								triStep++;
							} else {
								triStep = 0;
								step = 0;
								li.getData().clear();
							}

						}
					} else if (q == 2) {

						// Reset on Qs switch
						if (switched) {
							step = 0;
							switched = false;
						}

						label.setText("Step: " + (step + 1));

						// Logs the start of the solution
						if (step == 0) {
							System.out.println("--------------------------------------------------");
							System.out.println("Visual Solution | Question 2");
							System.out.println("--------------------------------------------------");
						}

						// Store the first point to be able to connect the last point
						XYChart.Data firstPoint = new XYChart.Data(visual_soln.get(0).x, visual_soln.get(0).y);
						XYChart.Series series = new XYChart.Series();

						if ((step + 1) < visual_soln.size()) {

							// Line start point
							series.getData().add(new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));
							solnPoints.getData()
									.add(new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));

							System.out.println("Line " + (step + 1));
							System.out.println("X: " + visual_soln.get((step)).x + " Y: " + visual_soln.get((step)).y);

							step++;

							// Line end point
							series.getData().add(new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));
							solnPoints.getData()
									.add(new XYChart.Data(visual_soln.get(step).x, visual_soln.get(step).y));

							System.out.println("X: " + visual_soln.get((step)).x + " Y: " + visual_soln.get((step)).y);

						} else if ((step + 1) == visual_soln.size()) {
							// If it is the last line

							// Here if we want to show the line links A---B

							step++;
						} else {
							if (!sc.getData().contains(solnPoints)) {
								sc.getData().add(solnPoints);
							}

							// Reset on steps finish
							step = 0;
							li.getData().clear();
							label.setText("Question 2 solved !");
							solnPoints.getData().clear();
						}

						li.getData().add(series);
						li.setLegendVisible(false);

					} else {
						System.out.println("Please select a question !");
						label.setText("Please select a question !");
					}

				}

			}
		});

		VBox vbox = new VBox();

		stackpaneChart.getChildren().addAll(charts);
		vbox.getChildren().addAll(stackpaneTop, stackpaneChart, stackpaneControlsTop, stackpaneControlsBottom);

		return vbox;
	}

	/**
	 * Question 1: Find convex hull
	 */
	public ArrayList<Point2D.Float[]> solveQuestion1() {

		System.out.println("--------------------------------------------------");
		System.out.println("Question 1 Solution");
		System.out.println("--------------------------------------------------");

		if (InputPoints.size() > 2) {
			/**
			 * Sort points using MergeSort
			 */
			SortedInputPointsForQ1 = MergeSort(InputPoints, InputPoints.size());

			QuickHull obj = new QuickHull();
			ConvexHull_result = obj.quickHull(SortedInputPointsForQ1);

			System.out.println("The points in the Convex hull using Quick Hull are: ");
			for (int i = 0; i < ConvexHull_result.size(); i++) {
				System.out.println("(" + ConvexHull_result.get(i).x + ", " + ConvexHull_result.get(i).y + ")");
			}

			return obj.steps;
		}
		return null;
	}

	/**
	 * Question 2: Shortest Path Around between A and B
	 */
	public void solveQuestion2() {

		System.out.println("--------------------------------------------------");
		System.out.println("Question 2 Solution");
		System.out.println("--------------------------------------------------");

		if (InputPoints.size() >= 2) {
			/**
			 * Sort points using MergeSort
			 */

			InputPoints.add(A);
			InputPoints.add(B);

			SortedInputPointsForQ2 = MergeSort(InputPoints, InputPoints.size());

			Shortest_Path_Around obj2 = new Shortest_Path_Around();
			Shortest_Path_Around = obj2.quickHull(SortedInputPointsForQ2);

			System.out.println("The points that forms Shortest Path Around between A and B are: ");
			for (int i = 0; i < Shortest_Path_Around.size(); i++) {
				System.out.println("(" + Shortest_Path_Around.get(i).x + ", " + Shortest_Path_Around.get(i).y + ")");
			}
			ArrayList<Point2D.Float> tmp = new ArrayList<Point2D.Float>();
			tmp.addAll(Shortest_Path_Around);
			visual_soln = tmp;

		}
	}

	/**
	 * 
	 * Charts methods
	 * 
	 */

	/**
	 * To set y coordinates
	 */
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

	/**
	 * To set x coordinates
	 */
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

	/**
	 * To Create ScatterChart
	 */
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

	/**
	 * To Create LineChart
	 */
	private LineChart<Number, Number> createLineChart() {
		final LineChart<Number, Number> chart = new LineChart<>(createXaxis(), createYaxis());

		chart.setCreateSymbols(false);
		chart.setMaxSize(500, 400);
		chart.setLegendVisible(false);

		return chart;
	}

	/**
	 * 
	 * Sorting methods
	 * 
	 */

	/**
	 * Merge Sort
	 */
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

	/**
	 * Merge
	 */
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

	/**
	 * Styling Charts
	 */
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