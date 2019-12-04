/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConvexHull_Project;

/**
 *
 * @author Shams
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.geom.*; // used for Line2D and Point2D

 
public class QuickHull
{
    /* quickHull function received the input points which extracted 
    from the given figure and stored in a .txt file. The input points 
    are stored in ArrayList called points.Choose an ArrayList to be
    dynamically add points and remove points.
    */
    public ArrayList<Point2D.Float> quickHull(ArrayList<Point2D.Float> points) 
    {
        ArrayList<Point2D.Float> convexHull = new ArrayList<Point2D.Float>(); // convexHull list to store all points belong to the convexHull
        /*
        Declaring the extreme x coordinates ( minimum x coordinate & maximum x coordinate )
        and storing the index of the point with maximum x and minimum x
        */
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
        and sending thier coordinates to the visualizer to draw line between them
        */
        convexHull.add(p1);
        convexHull.add(p2);
        // sending convexHull array to visualizer
        /*
        Removing the extreme points p1 and p2 from the points list.
        */
        points.remove(p1);
        points.remove(p2);
 
        ArrayList<Point2D.Float> Upper_Set = new ArrayList<Point2D.Float>();
        ArrayList<Point2D.Float> Lower_Set = new ArrayList<Point2D.Float>();
 
        for (int i = 0; i < points.size(); i++)
        {
            Point2D.Float p = points.get(i);
            if (point_position(p1, p2, p) == -1)
                Upper_Set.add(p);
            else if (point_position(p1, p2, p) == 1)
                Lower_Set.add(p);
        }
        FindHull(p1, p2, Lower_Set, convexHull);
        FindHull(p2, p1, Upper_Set, convexHull);
 
        return convexHull;
    }
 
   
 
    public void FindHull(Point2D.Float p1, Point2D.Float p2, ArrayList<Point2D.Float> PointSet,
            ArrayList<Point2D.Float> convexhull)
    {
        int insertPosition = convexhull.indexOf(p2);
        if (PointSet.size() == 0)
            return;
        if (PointSet.size() == 1) 
        {
            Point2D.Float p = PointSet.get(0);
            PointSet.remove(p);
            convexhull.add(insertPosition, p);
            return;
        }
        /*
        Find the P max , which is the furthest point from the line p1p2. By traversing the points
        and calculating the distance between each point in the set and the line p1p2.Finally keep the index
        or position of the furthest point and add it to the convexHull and remove it from the points set.
        */
        float Max_distance = Float.MIN_VALUE;
        float PMax_distance = Float.MIN_VALUE; // the distance between PMax and line p1p2
        int PMax_index = -1; // index of the furthest point from the line p1p2.(index of P max)
        
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
        convexhull.add(insertPosition, PMax);
 
        // identifying the pointe to the left of the line p1 p_max
        ArrayList<Point2D.Float> leftSet_p1_pmax = new ArrayList<Point2D.Float>();
        for (int i = 0; i < PointSet.size(); i++)
        {
            Point2D.Float p = PointSet.get(i);
            if (point_position(p1, PMax, p) == 1)
            {
                leftSet_p1_pmax.add(p);
            }
        }
 
      // identifying the pointe to the left of the line p_max p2
        ArrayList<Point2D.Float> leftSet_pmax_p2 = new ArrayList<Point2D.Float>();
        for (int i = 0; i < PointSet.size(); i++)
        {
            Point2D.Float p = PointSet.get(i);
            if (point_position(PMax, p2, p) == 1)
            {
                leftSet_pmax_p2.add(p);
            }
        }
        FindHull(p1, PMax, leftSet_p1_pmax, convexhull);
        FindHull(PMax, p2, leftSet_pmax_p2, convexhull);
 
    }
    
    /* 
    point_direstion is a function used to locate the direction of the point p
    from the line segment p1p2 using cross product.    
    */
 
    public int point_position (Point2D.Float p1, Point2D.Float p2, Point2D.Float p)
    {
       float crossProduct = 0;
        
        float p1p2x = p2.x - p1.x; 
        float p2p1y = p2.y - p1.y;
        float pp1y = p.y - p1.y;
        float pp1x = p.x - p1.x;
        
        crossProduct = ( p1p2x * pp1y ) - ( p2p1y * pp1x ); 
        
        if (crossProduct > 0)
            return 1;
        else if (crossProduct == 0)
            return 0;
        else
            return -1;
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
       
       if (distance <0) // if the distance is negative taking the absloute value
       {
         distance = Math.abs(distance);
       }   
        return distance;
    }
}


