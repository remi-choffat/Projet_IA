package algos;

import java.awt.Point;

/**
 * Implémentation de l'algorithme DBScan pour le clustering.
 * Cette classe implémente l'interface AlgoClustering.
 */
public class DBScanClustering implements AlgoClustering {

    private final double eps;
    private final int minpts;

    public DBScanClustering(double eps, int minpts) {
        this.eps = eps;
        this.minpts = minpts;
    }

    /**
     * @param points les points à clusteriser
     * @param ncluster parametre ignoré
     * @return int[] :
     * cluster 0 = points sans cluster
     * cluster [1..] on ne sait pas combien de cluster on obtient
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
