package algos;

import java.awt.Point;
import java.util.ArrayList;

public class Dendrogram {

    private final ArrayList<Point> points;

    public Dendrogram(Point p) {
        points = new ArrayList<>();
        points.add(p);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void ajouterCluster(Dendrogram c) {
        points.addAll(c.getPoints());
    }

    public int compare(Dendrogram d, int linkage) {
        int res = -1;
        switch (linkage) {
            case HCAClustering.SINGLE_LINKAGE:
                res = singleLinkage(d);
                break;
            case HCAClustering.CENTROID_LINKAGE:
                res = centroidLinkage(d);
                break;
            default:
                break;
        }
        return res;
    }

    private int singleLinkage(Dendrogram d) {
        int min = -1;
        for (Point p : points) {
            for (Point p2 : d.getPoints()) {
                int dist = (int) p.distance(p2);
                if (min == -1 || dist < min) {
                    min = dist;
                }
            }
        }
        return min;
    }

    private int centroidLinkage(Dendrogram d) {
        Point c1 = new Point(0, 0), c2 = new Point(0, 0);
        for (Point p : points) {
            c1.x += (int) p.getX();
            c1.y += (int) p.getY();
        }
        c1.x = c1.x / points.size();
        c1.y = c1.y / points.size();

        for (Point p2 : d.getPoints()) {
            c2.x += (int) p2.getX();
            c2.y += (int) p2.getY();
        }
        c2.x = c2.x / d.getPoints().size();
        c2.y = c2.y / d.getPoints().size();

        return (int) c1.distance(c2);
    }
}
