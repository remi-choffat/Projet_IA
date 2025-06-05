package algos;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Implémentation de l'algorithme DBSCAN pour le clustering.
 */
public class DBSCAN {

    public static final int NOISE = -1;
    public static final int UNVISITED = -2;


    private static int[] removeElement(int[] T, int ind) {
        int[] r = new int[T.length - 1];
        int j = 0;
        for (int i = 0; i < T.length - 1; i++) {
            if (i != ind) {
                r[j++] = T[i];
            }
        }
        return r;
    }


    private static int[] concat(int[] A1, int[] A2) {

        ArrayList<Integer> l = new ArrayList<>();

        for (int k : A1) {
            if (!l.contains(k)) {
                l.add(k);
            }
        }
        for (int i : A2) {
            if (!l.contains(i)) {
                l.add(i);
            }
        }
        return l.stream().mapToInt(Integer::intValue).toArray();
    }


    /**
     * @param DB,     points
     * @param eps,    rayon des zones
     * @param minPts, nombre min de points dans le rayon
     * @return int[] de meme taille que DB, ou -1 est un point bruit ou sinon [1..] le numero de cluster
     */
    public static int[] _DBSCAN(Point[] DB, double eps, int minPts) {
        int C = 0;
        int[] states = new int[DB.length];
		Arrays.fill(states, UNVISITED);

        for (int i = 0; i < DB.length; i++) {
            if (states[i] != UNVISITED) continue;

            int[] N = rangeQuery(DB, i, eps);

            if (N.length < minPts) {
                states[i] = -1;
                continue;
            }

            C++;
            states[i] = C;

            int[] S = removeElement(N, i);

            for (int j = 0; j < S.length; j++) {
				int idx = S[j];

                if (states[idx] == NOISE) {
                    states[idx] = C;
                }
                if (states[idx] != UNVISITED) {
                    continue;
                }
                states[idx] = C;

                int[] NN = rangeQuery(DB, idx, eps);
                if (NN.length >= minPts) {
                    S = concat(S, NN);
                }
            }
        }

        return states;
    }


    private static int[] rangeQuery(Point[] DB, int Q, double eps) {
        ArrayList<Integer> N = new ArrayList<>();

        for (int i = 0; i < DB.length; i++) {
            Point P = DB[i];

            if (P.distance(DB[Q]) <= eps) {
                N.add(i);
            }
        }
        return N.stream().mapToInt(Integer::intValue).toArray();
    }

}
