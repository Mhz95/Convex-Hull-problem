## CSC 512 Convex Hull Project 


### Problems 


#### Convex Hull Problem 

Given a set of n points (Xi , Yi ) in the Euclidian plane , a Convex hull is the smallest convex polygon which contains all the given points.

<img src="https://github.com/Mhz95/Project512/blob/master/screenshots/q1points.png" width="500">

#### Shortest Path Around

A shortest path around is one of the convex hull applications. Given a fenced area in the two-dimensional Euclidean plane in the shape of a convex polygon with vertices at points P1 (X1,Y1), P2 (X2,Y2),  … , Pn (Xn,Yn), and two more points A (XA,YA) and B (XB,YB). The goal is to find the length of the shortest path around between A and B.

<img src="https://github.com/Mhz95/Project512/blob/master/screenshots/q2points.png" width="500">

### Algorithms Pseudocode

```
Algorithm QuickHull ( InputPoints [ 0 … n-1 ] )

// This algorithm is used to find the convex hull of a given set of points.
// inputs: a set of sorted points (xi,yi) in the Euclidian plane.
// output: the set of points formed a convex hull. The algorithm return Convexhull array which contains all points belong to the convexhull. 


1. Convexhull = {} // an array to stored the points formed a convex hull

2. Find minx and maxx  P1,P2.

3. Add P1 and P2 to the set Convex hull.

4. Line P1 P2 divide the remaining points into 2 sets Upper_Set and Lower_Set.

5. Upper_Set = {(xi,yi) : (xi,yi) resides above the line P1 P2. }

6. Lower_Set = {(xi,yi) : (xi,yi) resides below the line P1 P2. }

7.  FindConvexHull (P1,P2, Upper_Set, Convexhull)

8.  FindConvexHull (P2,P1, Lower_Set, Convexhull)

9. Return Convexhull.


End QuickHull
```

```
Algorithm FindConvexHull ( Ponit P1 , Point P2, InputPoints [ 0 … n-1 ] , ConvexHull [0 .. n-1] )

// This algorithm is used to find the convex hull of a given set of points either Upper_Set/Lower_Set points
// inputs: a set of points (xi,yi) . Either the Upper_Set or Lower_Set.
// output: the set of points formed a convex hull upper/lower hull. The algorithm return Convexhull array which contains all points belong to the upper/lower hull.

 1. Find the farest point P_max from the line P1 P2
. 
 2. Add P_max to ConvexHull.

3. Line P1 P_max divide the set of points into two subsets also line P_max P2 divide the set into two subsets. P1 P2 P_max form a triangle. All points reside inside the triangle cannot be part of the convex hull.

4.  leftSet_P1_Pmax = {(xi,yi) : (xi,yi) resides above the line P1 P_max. }

5. leftSet_Pmax_P2=  {(xi,yi) : (xi,yi) resides above the line P_max P2. }

6. FindConvexHull( P1,P_max, leftSet_P1_Pmax, convexHull)

7. FindConvexHull ( P_max , P2 , leftSet_Pmax_P2 , convexHull )


End FindConvexHull
```
```
Algorithm ShortestPathAround ( InputPoints [ 0 … n-1 ] , Point A , Point B )

// This algorithm is used to find the shortest path around between two points A and B.
// inputs: a set of points (xi,yi) in the Euclidian plane along with the points A and B.
// output: the shortest path around between two points A and B. The algorithm return an array which contains the points belong to the shortest path.


1. Convexhull = {} // an array to stored the points formed a convex hull

2. Line A B divide the points into 2 sets Upper_Set and Lower_Set.

3. Upper_Set = {(xi,yi) : (xi,yi) resides above the line A B. }

4. Lower_Set = {(xi,yi) : (xi,yi) resides below the line A B. }

5.  FindConvexHull (A,B, Upper_Set, Convexhull) 
 
6.  Calculate the length of the Upper Convex.

7.  Upper_Convex = {(xi,yi) : (xi,yi) resides above the line A B and belong to the convex hull }

8. FindConvexHull (B,A, Lower_Set, Convexhull)

9. Calculate the length of the Lower Convex.

10. Lower_Convex = {(xi,yi) : (xi,yi) resides below the line A B and belong to the convex hull }

//  Compare the Upper_Convex_Length with the Lower_Convex_Length

11.  if (Upper_Convex_Length < Lower_Convex_Length )

     Return Upper_Convex

       else return Lower_Convex

End ShortestPathAround
```

### Screenshots

#### Convex Hull Problem 

<img src="https://github.com/Mhz95/Project512/blob/master/screenshots/InputQ1.png" width="500">
<img src="https://github.com/Mhz95/Project512/blob/master/screenshots/SolnQ1.png" width="500">


#### Shortest Path Around

<img src="https://github.com/Mhz95/Project512/blob/master/screenshots/InputQ2.png" width="500">
<img src="https://github.com/Mhz95/Project512/blob/master/screenshots/SolnQ2.png" width="500">

### Contributors

Alya Alshammari, Muneera Al-Zaidan, Nourah Al-Shahrani, Shams Al-Shamasi  

Supervisor: Dr. Mohammed Menai
Course: Algorithms Design and Analysis (CSC 512), KSU

November 2019