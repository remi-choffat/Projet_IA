import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sun.tools.javac.util.ArrayUtils;


/**
 * DBSCANv2
 */
public class DBSCAN {

	final int NOISE = -1;

	public int[] removeEleme(int[] T, int ind) {
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

	public int[] concat(int[] A1, int[] A2) {
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




	
	double dist(Point x1, Point x2) {

		return Math.sqrt(Math.pow((x1.x - x2.x), 2) + Math.pow((x1.y - x2.y), 2));
	}


	/**
	 * @param DB, points
	 * @param eps, rayon des zone 
	 * @param minPts, noubme min de points dans le rayon
	 *
	 * @return int[] de meme taille que DB, ou -1 est un point bruit ou sinon [1..] le numero de cluster
	*/
	public int[] DBSCAN(Point[] DB, double eps, int minPts) {
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

	public int[] rangeQuery(Point[] DB, int Q, double eps) {
		ArrayList<Integer> N = new ArrayList<>();

		for (int i = 0; i < DB.length; i++) {
			Point P = DB[i];

			if (dist(P, DB[Q]) <= eps) {
				N.add(i);
			}
		}
		return N.stream().mapToInt(Integer::intValue).toArray();
	}


	public static void main(String[] args) {
		
		BufferedImage img = ImageIO.read(new File(args[0]));



		BufferedImage nimg = IdentifierBiome.identifier(img, Palette.PRAIRIE, new Palette(NormeCouleur.CIELAB), new AlgoClustering() {
			@Override
			public int[] cluster(Point[] points, int ncluster) {
				return 

			}
		}, 4, new Color[]{
			Color.red, Color.blue, Color.gray, Color.magenta
		});

	}
}
