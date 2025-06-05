import java.awt.Point;

/**
 * DBSCANClustering
 */
public class DBSCANClustering implements AlgoClustering {

	private double eps;
	private int minpts;

	public DBSCANClustering(double eps, int minpts) {
		this.eps = eps;
		this.minpts = minpts;
	}

	/**
	 * @param points 
	 * @param ncluster parametre ignoré
	 *
	 * @return int[] : 
	 * 	cluster 0 = points sans cluster
	 * 	cluster [1..] on ne sait pas combien de cluster on obtient
	 * 			
	 *
	 *
	 *
	 */
	@Override
	public int[] cluster(Point[] points, int ncluster) {

		int[] r = DBSCAN._DBSCAN(points, eps, minpts);

		for (int i = 0; i < r.length; i++) {
			if (r[i] == DBSCAN.NOISE) {
				r[i] = 0;
			}
			
		}
		return r;
		
	}

	
}
