import java.awt.*;
import java.util.Random;

/**
 * Implémentation de l'algorithme K-Means pour le clustering.
 * Cette classe implémente l'interface AlgoClustering.
 */
public class KMeansClustering implements AlgoClustering {

    private static final Random random = new Random();

    /**
     * Implémentation de l'algorithme K-Means pour le clustering.
     *
     * @param points   les points à regrouper
     * @param ncluster le nombre de clusters souhaité
     * @return un tableau d'entiers représentant le cluster auquel chaque point appartient
     */
    @Override
    public int[] cluster(Point[] points, int ncluster) {

        // Vérification des paramètres pour éviter les erreurs
        if (points == null) {
            throw new IllegalArgumentException("Les points ne peuvent pas être null.");
        }
        if (ncluster < 1 || ncluster > points.length) {
            throw new IllegalArgumentException("Le nombre de clusters doit être compris entre 1 et le nombre de points.");
        }

        // Initialisation des centroïdes avec des points aléatoires
        Point[] centroids = new Point[ncluster];
        for (int i = 0; i < ncluster; i++) {
            centroids[i] = points[random.nextInt(points.length)];
        }

        int[] clusters = new int[points.length];
        boolean changed;

        // Itérations de l'algorithme K-Means
        do {
            changed = false;
            // Attribuer chaque point au centroïde le plus proche
            for (int i = 0; i < points.length; i++) {
                int closestCentroid = 0;
                double minDistance = euclideanDistance(points[i], centroids[0]);

                for (int j = 1; j < ncluster; j++) {
                    double distance = euclideanDistance(points[i], centroids[j]);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestCentroid = j;
                    }
                }

                if (clusters[i] != closestCentroid) {
                    clusters[i] = closestCentroid;
                    changed = true;
                }
            }

            // Recalculer les centroïdes
            int[] counts = new int[ncluster];
            double[] sumX = new double[ncluster];
            double[] sumY = new double[ncluster];

            for (int i = 0; i < points.length; i++) {
                int cluster = clusters[i];
                counts[cluster]++;
                sumX[cluster] += points[i].x;
                sumY[cluster] += points[i].y;
            }

            for (int j = 0; j < ncluster; j++) {
                if (counts[j] > 0) {
                    centroids[j] = new Point((int) (sumX[j] / counts[j]), (int) (sumY[j] / counts[j]));
                }
            }
        } while (changed);

        return clusters;
    }


    /**
     * Calcule la distance euclidienne entre deux points.
     *
     * @param p1 le premier point
     * @param p2 le deuxième point
     * @return la distance euclidienne entre p1 et p2
     */
    private double euclideanDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

}