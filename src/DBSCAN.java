import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * DBSCANv2
 */
public class DBSCAN {

	public static final int NOISE = -1;

	private static int[] removeEleme(int[] T, int ind) {
		int[] r = new int[T.length-1];
		int j = 0;
		for (int i = 0; i < T.length; i++) {
			if (i == ind) {

			} else {
				r[j++] = T[i];
			}
		}
		return r;
	}

	private static int[] concat(int[] A1, int[] A2) {
		int[] r = new int[A1.length + A2.length];
		int i =0;
		for (; i < A1.length; i++) {
			r[i] = A1[i];
		}
		for (int j = 0; j < A2.length; j++) {
			r[i+j] = A2[j];
		}
		return r;
	}

	private static double dist(Point x1, Point x2) {
		return x1.distance(x2);
	}


	/**
	 * @param DB, points
	 * @param eps, rayon des zone 
	 * @param minPts, noubme min de points dans le rayon
	 *
	 * @return int[] de meme taille que DB, ou -1 est un point bruit ou sinon [1..] le numero de cluster
	*/
	public static int[] _DBSCAN(Point[] DB, double eps, int minPts) {
		int C = 0;
		boolean[] traite = new boolean[DB.length];
		int[] states = new int[DB.length];

		for (int i = 0; i < DB.length; i++) {
			Point pi = DB[i];
			if (traite[i]) continue;

			int[] N = rangeQuery(DB, i, eps);

			if (N.length < minPts) {
				states[i] = -1;
				continue;
			}

			C++;
			states[i] = C;

			int[] S = removeEleme(N, i);

			for (int j = 0; j < S.length; j++) {
				Point Q = DB[j];

				if (states[j] == NOISE) {
					states[j] = C;
				}
				if (states[j] == 0) {
					continue;
				}
				states[j] = C;
				
				int[] NN =  rangeQuery(DB, j, eps);
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

			if (dist(P, DB[Q]) <= eps) {
				N.add(i);
			}
		}
		return N.stream().mapToInt(Integer::intValue).toArray();
	}


	// exemple d'utilisation'
	public static void main(String[] args) throws Exception {
		
		BufferedImage img = ImageIO.read(new File(args[0]));
		BufferedImage nimg = IdentifierBiome.identifier(img, Palette.PRAIRIE, new Palette(NormeCouleur.CIELAB), new AlgoClustering() {
			@Override
			public int[] cluster(Point[] points, int ncluster) {
				double eps = 5;
				int minPts = 5;
				int[] r = DBSCAN._DBSCAN(points, eps, minPts);

				// on remplace les bruits
				for (int i = 0; i < r.length; i++) {
					if (r[i] == NOISE) {
						r[i] = 0;
					}
					
				}

				return r;

			}
		}, 4, new Color[]{ // la premier couleur represente les point sans clusters
			Color.red, Color.blue, Color.gray, Color.magenta
		});

	}
}
