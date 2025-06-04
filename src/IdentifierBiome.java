import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * IdentifierBiome
 */
public class IdentifierBiome {




	public BufferedImage identifier(BufferedImage img,  Color biome, Palette palette, AlgoClustering algo, int ncluster, Color[] couleurs_clusters ) {

		
		ArrayList<Point> points = new ArrayList<>();

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				if (palette.getPlusProche(new Color(img.getRGB(x, y))).equals(biome)){
					points.add(new Point(x, y));
				}	
			}	
		}

		int[] clusters = algo.cluster(((Point[])points.toArray()), ncluster);

		BufferedImage nimg = AffichageBiome.eclaircirImage(img, 75);
	
		for (int i = 0; i < clusters.length; i++) {
			var p = points.get(i);
			img.setRGB(p.x, p.y, couleurs_clusters[clusters[i]].getRGB());	
		}
		return nimg;
	}
}
