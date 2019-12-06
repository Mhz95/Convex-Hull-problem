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
public class ConvexHullVisualizer extends Application {
	
	public enum States {
		STARTED, // Points need to be sorted
		MOVING, // Move right or left and add one more point to the convex hull
		VERIFYING_POINT, // verify the added point was right turn
		DONE
	};

	ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>();
	boolean moving_right = true;
	States state = States.STARTED;
	int i = 0;
	ArrayList<Point2D.Float> convex_hull = new ArrayList<Point2D.Float>();
	Point2D.Float p, q, r = null;

	/**
	 * To be shown in the visualizer
	 */
	ArrayList<Point2D.Float> visual_soln = new ArrayList<Point2D.Float>();


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
				points.add(p);
			}
			
			sc.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Initialize Charts and add them to scene
		 */
		ScatterChart scChart = createScatterChart(points);
		LineChart li = createLineChart();

		Scene scene = new Scene(layerCharts(scChart, li),600,500);

		stage.setScene(scene);
		stage.setTitle("CSC 512 - Convex Hull Algorithm");
		stage.show();

	}


	/**
	 * To stack charts as layers
	 */
	private VBox layerCharts(final XYChart<Number, Number>... charts) {
		/**
		 * Style Charts
		 */
		for (int i = 1; i < charts.length; i++) {
			configureOverlayChart(charts[i]);
		}

		ScatterChart sc = (ScatterChart) charts[0];
		LineChart li = (LineChart) charts[1];

		/**
		 * Add GUI components to scene
		 */
		StackPane stackpaneChart = new StackPane();
		
		final HBox hboxBottom = new HBox();

		final Button next = new Button("Next");
		final Button auto = new Button("Auto");

		hboxBottom.setSpacing(10);
		hboxBottom.setPadding(new Insets(15, 20, 10, 10));
		hboxBottom.getChildren().addAll(next, auto);	
        
        StackPane stackpaneControlsBottom = new StackPane();
        stackpaneControlsBottom.getChildren().addAll(hboxBottom);
      
		/**
		 * To handle (Auto Move) button in the visualizer
		 */
		auto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				while(step(li)) {
					step(li);
				}
			}
		});
		/**
		 * To handle (Next) button in the visualizer
		 */
		next.setOnAction(new EventHandler<ActionEvent>() {
			int step = 0;

			@Override
			public void handle(ActionEvent e) {
				step(li);
			}
		});


        VBox vbox = new VBox();

		stackpaneChart.getChildren().addAll(charts);
        vbox.getChildren().addAll(stackpaneChart, stackpaneControlsBottom);

		return vbox;
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

	public void convex_hull_sort() {
		points = MergeSort(points, points.size());
	}

	public void change_state(States new_state) {
		System.out.println("State change " + state + "->" + new_state);
		state = new_state;
	}

	// Use a determinant to determine if point r is right of the directed line pq
	public boolean is_right_of_line(Point2D.Float p, Point2D.Float q, Point2D.Float r) {
		float D = p.x * q.y - p.x * r.y - p.y * q.x + p.y * r.x + q.x * r.y - q.y * r.x;
		return D > 0;
	}

	public void add_point_to_hull(LineChart li, int index) {
		System.out.println("Adding #" + index);
		Point2D.Float p = points.get(index);
		convex_hull.add(p);
		int n = convex_hull.size();
		if (n > 1) {
			XYChart.Series series = new XYChart.Series();
			series.getData().add(new XYChart.Data(convex_hull.get(n - 2).x, convex_hull.get(n - 2).y));
			series.getData().add(new XYChart.Data(convex_hull.get(n - 1).x, convex_hull.get(n - 1).y));
			li.getData().add(series);
			li.setLegendVisible(false);
			
			log_line_added(convex_hull.get(n - 2), convex_hull.get(n - 1));
		}

	}

	public void remove_point_from_hall(LineChart li) {
		System.out.println("Removing point.");
		int n = convex_hull.size();
		Point2D.Float p = convex_hull.get(n - 2);
		convex_hull.remove(n - 2);
		li.getData().remove(n - 2);

		n--;
		
		log_line_removed(p, convex_hull.get(n - 2));

		li.getData().remove(n - 2);
		li.setLegendVisible(false);
		
		log_line_added(convex_hull.get(n - 2), convex_hull.get(n - 1));

		XYChart.Series series2 = new XYChart.Series();
		series2.getData().add(new XYChart.Data(convex_hull.get(n - 2).x, convex_hull.get(n - 2).y));
		series2.getData().add(new XYChart.Data(convex_hull.get(n - 1).x, convex_hull.get(n - 1).y));
		
		li.getData().add(series2);
		li.setLegendVisible(false);
		
	}

	public boolean step(LineChart li) {
		switch (state) {
		case DONE:
			return false;

		case STARTED:
			convex_hull_sort();
			change_state(States.MOVING);
			// We do not want the sort to count as a pausing step
			// so let's call step again
			return step(li);

		case MOVING:
			// Add first and second points to the hull
			if (i == 0 && moving_right) {
				if (points.size() > 0) {
					add_point_to_hull(li, 0);
					if (points.size() > 1) {
						add_point_to_hull(li, 1);
					}
				}
				i = 1;
				if (points.size() <= 2) {
					change_state(States.DONE);
				}
				return state != States.DONE;
			}

			if (moving_right) {
				i++;
				if (i >= points.size()) {
					// Change direction
					log_direction_changed();

					System.out.println("Changing direction");
					i = points.size() - 2;
					moving_right = false;
					add_point_to_hull(li, i);
				}
			}

			// Moving left
			if (!moving_right) {
				i--;
				if (i == -1) {
					// We are done. Remove last item since it's the same as first.
					convex_hull.remove(convex_hull.size() - 1);
					change_state(States.DONE);
					log_convex_hull(convex_hull);
					return false;
				}
			}

			add_point_to_hull(li, i);
			change_state(States.VERIFYING_POINT);
			r = convex_hull.get(convex_hull.size() - 1);
			return true;

		case VERIFYING_POINT:
			int n = convex_hull.size();
			if (n <= 2) {
				change_state(States.MOVING);
				return step(li);
			}
			p = convex_hull.get(n - 3);
			q = convex_hull.get(n - 2);
			if (!is_right_of_line(p, q, r)) {
				remove_point_from_hall(li);
				return true;
			}
			change_state(States.MOVING);
			return step(li);
		}
		return true;
	}
	
	/**
	 * Logs 
	 * @param p
	 * @param q
	 */
	
	public void log_line_added(Point2D.Float p, Point2D.Float q) {
		System.out.println("Draw Line from :" + p + " To " + q);
	}

	public void log_line_removed(Point2D.Float p, Point2D.Float q) {
		System.out.println("Remove Line from :" + p + " To " + q);
	}

	public void log_convex_hull(ArrayList<Point2D.Float> convex_hull) {
		System.out.println("Draw Convex hull");
	}

	public void log_direction_changed() {
		System.out.println("Direction changed");
	}
	
}