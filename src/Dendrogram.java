import java.awt.Point;

public class Dendrogram {
	public static final int SINGLE_LINKAGE = 0;
	public static final int CENTROID_LINKAGE = 1;
	private Point[] points;

	public Dendrogram(Point p){
		points[0] = p;
	}

	public Point[] getPoints(){
		return points;
	}

	public void ajouterCluster(Dendrogram c){
		for(Point cPoint : c.getPoints()){
			points[points.length] = cPoint;
		}
	}

	public int compare(Dendrogram d, int linkage){
		int res = -1;
		switch (linkage) {
			case SINGLE_LINKAGE:
				res = singleLinkage(d);
				break;
			case CENTROID_LINKAGE:
				res = centroidLinkage(d);
				break;

			default:
				break;
		}
		return res;
	}

	private int singleLinkage(Dendrogram d){
		int min = -1;
		for(Point p : points){
			for(Point p2 : d.getPoints()){
				int dist = (int)p.distance(p2);
				if(min == -1 || dist < min){
					min = dist;
				}
			}
		}
		return min;
	}

	private int centroidLinkage(Dendrogram d){
		Point c1 = new Point(0,0), c2 = new Point(0,0);
		for(Point p : points){
			c1.x += p.getX();
			c1.y += p.getY();
		}
		c1.x = c1.x/points.length;
		c1.y = c1.y/points.length;

		for(Point p2 : d.getPoints()){
			c2.x += p2.getX();
			c2.y += p2.getY();
		}
		c2.x = c2.x/d.getPoints().length;
		c2.y = c2.y/d.getPoints().length;

		return (int)c1.distance(c2);
	}
}
