package ConvexHull_Project;


import java.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.awt.geom.*;

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
public class ConvexHullVisualizerNew extends Application {

	ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>();
	ArrayList<Point2D.Float> convex_hull = new ArrayList<Point2D.Float>();

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

	public void add_point_to_hull(LineChart li, int i) {
		// Here to add start&end points for the lines to be shown
		
		System.out.println("Adding #" + i);
//		visual_soln.add(points.get(i));
		int n = points.size();

		if (n > 1) {
			XYChart.Series series = new XYChart.Series();
			series.getData().add(new XYChart.Data(points.get(n - 2).x, points.get(n - 2).y));
			series.getData().add(new XYChart.Data(points.get(n - 1).x, points.get(n - 1).y));
			li.getData().add(series);
			li.setLegendVisible(false);
			
			log_line_added(points.get(n - 2), points.get(n - 1));
		}

	}

	public void remove_point_from_hall(LineChart li) {
		System.out.println("Removing point.");
		int n = visual_soln.size();
		Point2D.Float p = visual_soln.get(n - 2);
		visual_soln.remove(n - 2);
		li.getData().remove(n - 2);

		n--;
		
		log_line_removed(p, visual_soln.get(n - 2));

		li.getData().remove(n - 2);
		li.setLegendVisible(false);
		
		log_line_added(visual_soln.get(n - 2), visual_soln.get(n - 1));

		XYChart.Series series2 = new XYChart.Series();
		series2.getData().add(new XYChart.Data(visual_soln.get(n - 2).x, visual_soln.get(n - 2).y));
		series2.getData().add(new XYChart.Data(visual_soln.get(n - 1).x, visual_soln.get(n - 1).y));
		
		li.getData().add(series2);
		li.setLegendVisible(false);
		
	}

	public boolean step(LineChart li) {
		convex_hull_sort();
		
        float minX_coordinate = Float.MAX_VALUE; // the minimum x coordinate of the extreme point p1
        float maxX_coordinate = Float.MIN_VALUE; // the maximum x coordinate of the extreme point p2
        int p1_index = -1; int p2_index = -1; // the index of the extreme points p1 and p2
        
        /*
        Traversing the ArrayList to Search for the extreme points in the input points list
        */
        for (int i = 0; i < points.size(); i++)
        {
            if (points.get(i).x < minX_coordinate)
            {
                minX_coordinate = points.get(i).x; // swap
                p1_index = i; // the index of the extreme point p1 with minimum x coordinate
            }
            if (points.get(i).x > maxX_coordinate)
            {
                maxX_coordinate = points.get(i).x;
                p2_index= i;
            }
        }
        /*
        retrieve the extreme points p1 and p2 from the list using their index
        */
        Point2D.Float p1 = points.get(p1_index); // p1 with minimum x coordinate
        Point2D.Float p2 = points.get(p2_index); // p2 with maximum x coordinate
        /*
        Adding the extreme points p1 and p2 to the convexHull list
        and sending their coordinates to the visualizer to draw line between them
        */
        convex_hull.add(p1);
        convex_hull.add(p2);
        // sending convexHull array to visualizer

        /*
        Removing the extreme points p1 and p2 from the points list.
        */
        points.remove(p1);
        points.remove(p2);
 
        ArrayList<Point2D.Float> Upper_Set = new ArrayList<Point2D.Float>();
        ArrayList<Point2D.Float> Lower_Set = new ArrayList<Point2D.Float>();
       
      //Find direction of a given point using point_position function
        for (int i = 0; i < points.size(); i++)
        {
            Point2D.Float p = points.get(i);
            if (point_position(p1, p2, p) == -1) {
            	Upper_Set.add(p);
            }
            else if (point_position(p1, p2, p) == 1) {
            	Lower_Set.add(p);
            }

        }
        add_point_to_hull(li, p1_index);// TEST
        add_point_to_hull(li, p2_index);// TEST
        FindHull(li, p1, p2, Lower_Set, convex_hull);
        FindHull(li, p2, p1, Upper_Set, convex_hull);

        return false;
       // return convex_hull;
	}
	
	
	  public void FindHull(LineChart li, Point2D.Float p1, Point2D.Float p2, ArrayList<Point2D.Float> PointSet,
	            ArrayList<Point2D.Float> convexhull)
	    {
	        int newIndex = convexhull.indexOf(p2);//New Index for a Point p 
	        if (PointSet.size() == 0) 
	            return;
	        if (PointSet.size() == 1) 
	        {
	            Point2D.Float p = PointSet.get(0);
	            PointSet.remove(p);
	            convexhull.add(newIndex, p); //insert p between the extreme points 	
	            return;
	        }
	        /*
	        Find the Pmax , which is the farthest point from the line p1p2. By traversing the points
	        and calculating the distance between each point in the set and the line p1p2.Finally keep the index
	        or position of the farthest point and add it to the convexHull and remove it from the points set.
	        */
	        float Max_distance = Float.MIN_VALUE;
	        float PMax_distance = Float.MIN_VALUE; // the distance between PMax and line p1p2
	        int PMax_index = -1; // index of the farthest point from the line p1p2.(index of P max)
	        
	        for (int i = 0; i < PointSet.size(); i++)
	        {
	            Point2D.Float p = PointSet.get(i);
	            PMax_distance = Point_to_Line_distance(p1, p2, p); // P_distance is the distance between P and the line p1p2
	            if (PMax_distance > Max_distance)
	            {
	                Max_distance = PMax_distance;
	                PMax_index = i;
	            }
	        }
	        Point2D.Float PMax = PointSet.get(PMax_index);
	        PointSet.remove(PMax_index);
	        convexhull.add(newIndex, PMax);
	        
	        // identifying the point to the left of the line p1 p_max
	        ArrayList<Point2D.Float> leftSet_p1_pmax = new ArrayList<Point2D.Float>();
	        for (int i = 0; i < PointSet.size(); i++)
	        {
	            Point2D.Float p = PointSet.get(i);
	            if (point_position(p1, PMax, p) == 1)
	            {
	                leftSet_p1_pmax.add(p);
	            }
	        }
	 
	      // identifying the point to the left of the line p_max p2
	        ArrayList<Point2D.Float> leftSet_pmax_p2 = new ArrayList<Point2D.Float>();
	        for (int i = 0; i < PointSet.size(); i++)
	        {
	            Point2D.Float p = PointSet.get(i);
	            if (point_position(PMax, p2, p) == 1)
	            {
	                leftSet_pmax_p2.add(p);
	            }
	        }
	        FindHull(li, p1, PMax, leftSet_p1_pmax, convexhull);
	        FindHull(li, PMax, p2, leftSet_pmax_p2, convexhull);

	    }
	    
	    /* 
	    point_direstion is a function used to locate the direction of the point p
	    from the line segment p1p2 using cross product.    
	    */
	 
	    public int point_position (Point2D.Float p1, Point2D.Float p2, Point2D.Float p)
	    {
	       float crossProduct = 0;
	        
	        float p2p1x = p2.x - p1.x; 
	        float p2p1y = p2.y - p1.y;
	        float pp1x = p.x - p1.x;
	        float pp1y = p.y - p1.y;
	        
	        crossProduct = ( p2p1x * pp1y ) - ( p2p1y * pp1x ); 
	        
	        if (crossProduct > 0) // below
	            return 1;
	        else if (crossProduct == 0) //co-linear
	            return 0;
	        else
	            return -1; //above
	    }
	    
	    /*
	    Compute the distance between the point p and the line p1p2
	    */
	    public float Point_to_Line_distance(Point2D.Float p1, Point2D.Float p2, Point2D.Float p)
	    {
	        float x1 = p1.x;
	        float y1 = p1.y;
	        float x2 = p2.x;
	        float y2 = p2.y;
	        float x3 = p.x;
	        float y3 = p.y;   
	        /*
	        computing the distance between point p and line p1p2
	        */
	       float distance = (float) Line2D.ptSegDist(x1, y1, x2, y2, x3, y3); 
	       
	       if (distance <0) // if the distance is negative taking the absolute value
	       {
	         distance = Math.abs(distance);
	       }   
	        return distance;
	        
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