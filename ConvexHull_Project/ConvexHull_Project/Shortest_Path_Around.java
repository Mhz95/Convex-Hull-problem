package ConvexHull_Project;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Convex Hull Algorithm - Programming project
 * 
 * @Authors Alya Alshammari, Munerah H. Alzaidan, Norah Alshahrani, Shams Alshamasi
 * @Supervisor Prof. M.B. Menai
 * @Copyrights King Saud University CSC 512 Algorithms
 * @since NOV 2019
 */
 
public class Shortest_Path_Around {
    
    /* quickHull function received the input points which extracted 
    from the given figure and stored in a .txt file. The input points 
    are stored in ArrayList called points.Choose an ArrayList to
    dynamically add points and remove points.
    */
    public ArrayList<Point2D.Float> quickHull(ArrayList<Point2D.Float> points) 
    {
       ArrayList<Point2D.Float> convexHull = new ArrayList<Point2D.Float>(); // convexHull list to store all points belong to the convexHull
        
        float minX_coordinate = Float.MAX_VALUE; // the minimum x coordinate of the extreme point A
        float maxX_coordinate = Float.MIN_VALUE; // the maximum x coordinate of the extreme point B
        int A_index = -1; int B_index = -1; // the index of the extreme points p1 and p2
        
        /*
        Traversing the ArrayList to Search for the extreme points in the input points list
        */
        for (int i = 0; i < points.size(); i++)
        {
            if (points.get(i).x < minX_coordinate)
            {
                minX_coordinate = points.get(i).x; // swap
                A_index = i; // the index of the extreme point A with minimum x coordinate
            }
            if (points.get(i).x > maxX_coordinate)
            {
                maxX_coordinate = points.get(i).x;
                B_index= i; // the index of the extreme point B with maximum x coordinate
            }
        }
        /*
        retrieve the extreme points A and B from the list using their index
        */
        Point2D.Float A = points.get(A_index); // p1 with minimum x coordinate
        Point2D.Float B = points.get(B_index); // p2 with maximum x coordinate
        /*
        Adding the extreme points A and B to the convexHull list
        */
        convexHull.add(A);
        convexHull.add(B);
        /*
        RemovE the extreme points A and B from the points list.
        */
        points.remove(A);
        points.remove(B);
 
 
       ArrayList<Point2D.Float> Upper_Set = new ArrayList<Point2D.Float>(); // Upper_Set store all points reside above the line AB
       ArrayList<Point2D.Float> Lower_Set = new ArrayList<Point2D.Float>(); // Lower_Set store all points reside below the line AB
 
        for (int i = 0; i < points.size(); i++)
        {
            Point2D.Float p = points.get(i);
            if (point_position(A, B, p) == -1) // points below the line AB
                Lower_Set.add(p);
            else if (point_position(A, B, p) == 1) // points above the line AB
                Upper_Set.add(p);
        }
        
        FindConvexHull(A, B, Upper_Set, convexHull); // find the upper convex hull
        
        
        ArrayList<Point2D.Float> Upper_convex = new ArrayList<Point2D.Float>(); // Upper_convex stored the points belong to upper convex hull only
        
        for(int i = 0 ; i < convexHull.size() ; i++) 
        {
           Point2D.Float p = convexHull.get(i);
            Upper_convex.add(p);
        }
        
        /*
        Compute the upper convex hull length
        */
        float Upper_Convex_Length = Total_Convex_Length(Upper_convex); 
        convexHull.clear();
        convexHull.add(B);
        convexHull.add(A);
        
        FindConvexHull(B, A, Lower_Set, convexHull); // find the lower convex hull
        
       
        ArrayList<Point2D.Float> Lower_convex = new ArrayList<Point2D.Float>(); // lower convex store all points belong to lower convex
        
        for(int i = 0 ; i < convexHull.size() ; i++)
        {
            Point2D.Float p = convexHull.get(i);
            Lower_convex.add(p);
        }
        
        /*
        Compute the length of the lower convex
        */
        float Lower_Convex_Length = Total_Convex_Length(Lower_convex);
       
        /*
        Compare the length of the upper and lower convex
        */
        if ( Upper_Convex_Length < Lower_Convex_Length)
         {
             return Upper_convex;
         }
 
        else return Lower_convex;
    }
 
        /*
        FindConvexHull used to find the convex hull of specified set of input points 
        */
        public void FindConvexHull(Point2D.Float p1, Point2D.Float p2, ArrayList<Point2D.Float> PointSet,
            ArrayList<Point2D.Float> convexhull)
    {
        int insert_location = convexhull.indexOf(p2);
        if (PointSet.size() == 0)
            return;
        if (PointSet.size() == 1) 
        {
            Point2D.Float p = PointSet.get(0);
            PointSet.remove(p);
            convexhull.add(insert_location, p);
            return;
        }
        /*
        Find the P max , which is the farthest point from the line p1p2. By traversing the points
        and calculating the distance between each point in the set and the line p1p2.Finally keep the index
        or position of the farthest point and add it to the convexHull and remove it from the points set.
        */
        float Max_distance = Float.MIN_VALUE;
        float PMax_distance = Float.MIN_VALUE; // the distance between PMax and line p1p2
        int PMax_index = -1; // index of the farthest point from the line p1p2.(index of P max)
        
        for (int i = 0; i < PointSet.size(); i++)
        {
            Point2D.Float p = PointSet.get(i);
            PMax_distance = Point_to_Line_distance(p1, p2, p); // PMax_distance is the distance between Pmax and the line p1p2
            if (PMax_distance > Max_distance)
            {
                Max_distance = PMax_distance;
                PMax_index = i;
            }
        }
        Point2D.Float PMax = PointSet.get(PMax_index);
        PointSet.remove(PMax_index);
        convexhull.add(insert_location, PMax);
 
        // identifying the points to the left ( above ) the line p1 p_max
        ArrayList<Point2D.Float> leftSet_p1_pmax = new ArrayList<Point2D.Float>();
        for (int i = 0; i < PointSet.size(); i++)
        {
            Point2D.Float p = PointSet.get(i);
            if (point_position(p1, PMax, p) == 1)
            {
                leftSet_p1_pmax.add(p);
            }
        }
 
      // identifying the points to the left of the line p_max p2
        ArrayList<Point2D.Float> leftSet_pmax_p2 = new ArrayList<Point2D.Float>();
        for (int i = 0; i < PointSet.size(); i++)
        {
            Point2D.Float p = PointSet.get(i);
            if (point_position(PMax, p2, p) == 1)
            {
                leftSet_pmax_p2.add(p);
            }
        }
        /*
        All points reside inside the triangle P1 PMax P2 are not belong to the convex hull
        the function should recursively solved the problem for the set of points which
        locate outside the triangle ( above line P1 PMax and above line PMax P2
        */
        FindConvexHull(p1, PMax, leftSet_p1_pmax, convexhull);
        FindConvexHull(PMax, p2, leftSet_pmax_p2, convexhull);
 
    }
    
    /* 
    point_position used to identify the location of the point p
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
    /*
    Compute the total length of the convex specified by a set of points
    received in the parameter.
    */
    
    public float Total_Convex_Length (ArrayList<Point2D.Float> convex)
    {
        float convex_length = 0.0f;
        for(int i = 0; i< convex.size()-1 ; i++)
        {
            Point2D.Float dp1 = convex.get(i);
            Point2D.Float dp2 = convex.get(i+1);
            
            float p1p2x = (float) Math.pow(dp1.x - dp2.x,2);
            float p1p2y = (float) Math.pow(dp1.y - dp2.y,2);
            float Distance_p1p2 = (float) Math.sqrt(p1p2x + p1p2y);
            convex_length = convex_length + Distance_p1p2;
            convex_length = Math.abs(convex_length);
        }
        return convex_length;
    }
}
