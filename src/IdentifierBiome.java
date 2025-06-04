import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * IdentifierBiome
 */
public class IdentifierBiome {




	public static BufferedImage identifier(BufferedImage img,  Color biome, Palette palette, AlgoClustering algo, int ncluster, Color[] couleurs_clusters ) {

		
		ArrayList<Point> points = new ArrayList<>();

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				if (palette.getPlusProche(new Color(img.getRGB(x, y))).equals(biome)){
					points.add(new Point(x, y));
				}	
			}	
		}

		int[] clusters = algo.cluster(((Point[])points.toArray()), ncluster);

		BufferedImage nimg = AffichageBiome.eclaircirImage(img, 0.75);
	
		for (int i = 0; i < clusters.length; i++) {
			var p = points.get(i);
			img.setRGB(p.x, p.y, couleurs_clusters[clusters[i]].getRGB());	
		}
		return nimg;
	}


	public static void main(String[] args) throws Exception {
		BufferedImage img = ImageIO.read(new File(args[0]));

		BufferedImage nimg = identifier(img, Palette.PRAIRIE, new Palette(NormeCouleur.CIELAB), new AlgoClustering() {
			@Override
			public int[] cluster(Point[] points, int ncluster) {
				int[] r = new int[points.length];

				
				int midx = img.getWidth()/2;
				int midy = img.getHeight()/2;


				for (int i = 0; i < points.length; i++) {
					Point p = points[i];

					r[i] = p.x<midx?p.y<midy?0:1:p.y<midy?2:3;
					
				}
				return r;
			}
		}, 4, new Color[]{
			Color.red, Color.blue, Color.gray, Color.magenta
		});
	}
}
