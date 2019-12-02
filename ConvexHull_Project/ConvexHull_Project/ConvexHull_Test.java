/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConvexHull_Project;

import java.util.*;

import java.io.File;
/**
 *
 * @author Shams
 *
 */
public class ConvexHull_Test
{
 
    public static void main(String args[]) throws Exception
    {
         ArrayList<Point> points = new ArrayList<Point>();
         File file = new File("C:\\\\Users\\\\Shams\\\\Desktop\\\\InputPoints.txt"); 
         
         Scanner sc = new Scanner(file); 
         System.out.println("Quick Hull Test");
  
    while (sc.hasNextLine()) 
    {
            int index = 0;
            int x = sc.nextInt();
            int y = sc.nextInt();
            
            Point p = new Point(x, y);
            points.add(index, p);
    }
          
        QuickHull qh = new QuickHull();
        ArrayList<Point> p = qh.quickHull(points);
        System.out.println("The points in the Convex hull using Quick Hull are: ");
        for (int i = 0; i < p.size(); i++)
        System.out.println("(" + p.get(i).x + ", " + p.get(i).y + ")");
        sc.close();
    }
}
