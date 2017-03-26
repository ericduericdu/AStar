
import java.awt.Point;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.HashMap;
//import java.util.Queue;
import java.util.*;

/// A sample AI that takes a very suboptimal path.
/**
 * This is a sample AI that moves as far horizontally as necessary to reach the target,
 * then as far vertically as necessary to reach the target.  It is intended primarily as
 * a demonstration of the various pieces of the program.
 * 
 * @author Leonid Shamis
 */
public class StupidAI implements AIModule
{
    
	private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2){
        
        double heuristic = 0.0;
        double totalDistance = (Math.abs(pt2.x-pt1.x)+Math.abs(pt2.y-pt1.y))-Math.min(Math.abs(pt2.x-pt1.x),Math.abs(pt2.y-pt1.y));
        heuristic = totalDistance*.5;
        
        return heuristic;
     
	}
	
    /// Creates the path to the goal.
	public List<Point> createPath(final TerrainMap map)
    {
        
		final ArrayList<Point> path = new ArrayList<Point>();
		HashMap<Point, Double> cost = new HashMap<Point, Double>();
		HashMap<Point, Point> parent = new HashMap<Point, Point>();
		
		Queue<Point> q = new PriorityQueue<Point>
		(
			new Comparator<Point>()
			{
				public int compare(Point p1, Point p2)
                {
                    double cost1 = cost.get(p1) + getHeuristic(map, p1, map.getEndPoint());
                    double cost2 = cost.get(p2) + getHeuristic(map, p2, map.getEndPoint());
					if(cost1 > cost2)
						return 1;
                    else if (cost1 < cost2)
						return -1;
					else
						return 0;
				}
			}
		);
        
        Point CurrentPoint = map.getStartPoint();
        q.add(map.getStartPoint());
		cost.put(CurrentPoint, 0.0);
		parent.put(CurrentPoint, null);
		
		while(!q.isEmpty() && !(CurrentPoint.x == map.getEndPoint().x && CurrentPoint.y == map.getEndPoint().y))
        {
			CurrentPoint = q.remove();
			
			Point[] neighbors = map.getNeighbors(CurrentPoint);

			for(int i = 0; i < neighbors.length; i++)
            {
				double updatedCost = cost.get(CurrentPoint) + map.getCost(CurrentPoint, neighbors[i]);
				
				if(!cost.containsKey(neighbors[i]) || updatedCost < cost.get(neighbors[i]))
                {
                    
					cost.put(neighbors[i], updatedCost);
                    q.add(neighbors[i]);
					parent.put(neighbors[i], CurrentPoint);
				}
			}
		}
        
        path.add(new Point(map.getEndPoint()));
        
		while(!CurrentPoint.equals(map.getStartPoint()))
        {
			CurrentPoint = parent.get(CurrentPoint);
			path.add(0, new Point(CurrentPoint));
		}
		return path;
	}
    
}
