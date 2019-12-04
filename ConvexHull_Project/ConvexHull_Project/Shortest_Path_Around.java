/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConvexHull_Project;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Shams
 */
public class Shortest_Path_Around {
    
    /* quickHull function received the input points which extracted 
    from the given figure and stored in a .txt file. The input points 
    are stored in ArrayList called points.Choose an ArrayList to be
    dynamically add points and remove points.
    */
    public ArrayList<Point2D.Float> quickHull(ArrayList<Point2D.Float> points , Point2D.Float A , Point2D.Float B) 
    {
        ArrayList<Point2D.Float> convexHull = new ArrayList<Point2D.Float>(); // convexHull list to store all points belong to the convexHull
        
        convexHull.add(A);
        convexHull.add(B);
        // sending convexHull array to visualizer
        /*
        Removing the extreme points p1 and p2 from the points list.
        */
        points.remove(A);
        points.remove(B);
 
        ArrayList<Point2D.Float> Upper_Set = new ArrayList<Point2D.Float>();
        ArrayList<Point2D.Float> Lower_Set = new ArrayList<Point2D.Float>();
 
        for (int i = 0; i < points.size(); i++)
        {
            Point2D.Float p = points.get(i);
            if (point_position(A, B, p) == -1)
                Upper_Set.add(p);
            else if (point_position(A, B, p) == 1)
                Lower_Set.add(p);
        }
        
        FindHull(A, B, Lower_Set, convexHull);
        FindHull(B, A, Upper_Set, convexHull);
                    
           /*
            Divide returned convexHull into two lists upperlist and lower lists
            */
        ArrayList<Point2D.Float> Upper_DSet = new ArrayList<Point2D.Float>();
        ArrayList<Point2D.Float> Lower_DSet = new ArrayList<Point2D.Float>();   
        
        Lower_DSet.add(A);
        Upper_DSet.add(A);
        
            for(int i = 0 ; i< convexHull.size() ; i++)
            {
                Point2D.Float p = convexHull.get(i);
                if(point_position(A,B,p) == 1 )
                {
                   Lower_DSet.add(p);
                }
                else if (point_position(A,B,p) == -1 )
                {
                    Upper_DSet.add(p);
                }
                
            }
           

            Lower_DSet.add(B);
            Upper_DSet.add(B);
            
            
           /*
            Find distance of upper hull and lower hull
            */
            float Lower_Distance = 0.0f;
        for(int i = 0; i< Lower_DSet.size()-1 ; i++)
        {
            Point2D.Float p1 = Lower_DSet.get(i);
            Point2D.Float p2 = Lower_DSet.get(i+1);
            
            float p1p2x = (float) Math.pow(p1.x - p2.x,2);
            float p1p2y = (float) Math.pow(p1.y - p2.y,2);
            float Distance_p1p2 = (float) Math.sqrt(p1p2x + p1p2y);
            Lower_Distance = Lower_Distance + Distance_p1p2;
        }
        
         float Upper_Distance = 0.0f;
         for(int i = 0; i< Upper_DSet.size()-1 ; i++)
        {
            Point2D.Float p1 = Upper_DSet.get(i);
            Point2D.Float p2 = Upper_DSet.get(i+1);
            
            float p1p2x = (float) Math.pow(p1.x - p2.x,2);
            float p1p2y = (float) Math.pow(p1.y - p2.y,2);
            float Distance_p1p2 = (float) Math.sqrt(p1p2x + p1p2y);
            Upper_Distance = Upper_Distance + Distance_p1p2;
        }
         
         if ( Upper_Distance < Lower_Distance)
         {
             return Upper_DSet;
         }
 
         return Lower_DSet;
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



    

