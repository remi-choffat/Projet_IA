package algos;

import java.awt.*;
import java.util.ArrayList;

/**
 * Implémentation de l'algorithme HCA pour le clustering.
 * Cette classe implémente l'interface AlgoClustering.
 */
public class HCAClustering implements AlgoClustering {

	// Méthode de comparaison entre les clusters
    public static final int SINGLE_LINKAGE = 0;
    public static final int CENTROID_LINKAGE = 1;

    int linkage;

    public HCAClustering(int linkage) {
        this.linkage = linkage;
    }

    public int[] cluster(Point[] points, int ncluster) {
        ArrayList<Dendrogram> liste_d = new ArrayList<>();

		// Chaque point est représenté par un cluster
        for (Point p : points) {
            liste_d.add(new Dendrogram(p));
        }

		// On fusionne les clusters en les comparant jusqu'à ce qu'il n'en reste plus que le nombre désiré
        while (liste_d.size() > ncluster) {
			System.out.println("nb de clusters restants : " + liste_d.size());
            int min_dist = -1;
            int[] min_index = new int[2];
            int dist;

			// Parcours de la liste de clusters
            for (int i = 0; i < liste_d.size(); i++) {
                Dendrogram d1 = liste_d.get(i);
				// Parcours de la liste de clusters pour comparer avec le premier
                for (int j = 0; j < liste_d.size(); j++) {
                    Dendrogram d2 = liste_d.get(j);
                    if (j != i) {
                        dist = d1.compare(d2, linkage);
						// On sauvegarde les clusters avec une plus petite distance
                        if (min_dist == -1 || dist < min_dist) {
                            min_dist = dist;
                            min_index[0] = i;
                            min_index[1] = j;
                        }
                    }
                }
            }

			// Fusionne les clusters les plus proches
            liste_d.get(min_index[0]).ajouterCluster(liste_d.get(min_index[1]));
            liste_d.remove(min_index[1]);
        }

		// Lie les clusters avec la liste de point
		// Le résultat représente le numéro de cluster à l'indice d'un point
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
