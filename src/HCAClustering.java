import java.awt.*;
import java.util.ArrayList;

/**
 * Implémentation de l'algorithme HCA pour le clustering.
 * Cette classe implémente l'interface AlgoClustering.
 */
public class HCAClustering implements AlgoClustering {

    public static final int SINGLE_LINKAGE = 0;
    public static final int CENTROID_LINKAGE = 1;

    int linkage;

    public HCAClustering(int linkage) {
        this.linkage = linkage;
    }

    public int[] cluster(Point[] points, int ncluster) {
        ArrayList<Dendrogram> liste_d = new ArrayList<>();

        for (Point p : points) {
            liste_d.add(new Dendrogram(p));
        }

        while (liste_d.size() > ncluster) {
            int min_dist = -1;
            int[] min_index = new int[2];
            int dist;
            for (int i = 0; i < liste_d.size(); i++) {
                Dendrogram d1 = liste_d.get(i);
                for (int j = 0; j < liste_d.size(); j++) {
                    Dendrogram d2 = liste_d.get(j);
                    if (j != i) {
                        dist = d1.compare(d2, linkage);
                        if (min_dist == -1 || dist < min_dist) {
                            min_dist = dist;
                            min_index[0] = i;
                            min_index[1] = j;
                        }
                    }
                }
            }

            liste_d.get(min_index[0]).ajouterCluster(liste_d.get(min_index[1]));
            liste_d.remove(min_index[1]);
        }

        int[] resultat = new int[points.length];
        int num_cluster = 0;
        for (Dendrogram d : liste_d) {
            for (Point d_p : d.getPoints()) {
                int i = 0;
                for (Point p : points) {
                    if (d_p.equals(p)) {
                        resultat[i] = num_cluster;
                    }
                    i++;
                }
            }
            num_cluster++;
        }

        return resultat;
    }
}
