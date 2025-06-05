package algos;

import java.awt.Point;

/**
 * Interface pour les algorithmes de clustering.
 */
public interface AlgoClustering {

    /**
     * Méthode pour effectuer le clustering des points.
     *
     * @param points   les points à regrouper
     * @param ncluster le nombre de clusters souhaité
     * @return un tableau d'entiers représentant le cluster auquel chaque point appartient
     */
    int[] cluster(Point[] points, int ncluster);

}
