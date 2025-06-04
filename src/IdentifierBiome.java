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
 * Permet d'identifier les clusters d'un biome spécifique dans une image et de les colorer en fonction des clusters.
 */
public class IdentifierBiome {

    /**
     * Identifie les pixels d'un biome spécifique dans une image et les colore en fonction des clusters.
     *
     * @param img               l'image à traiter
     * @param biome             le biome à identifier (par exemple, Palette.PRAIRIE)
     * @param palette           la palette de couleurs utilisée pour identifier le biome
     * @param algo              l'algorithme de clustering à utiliser
     * @param ncluster          le nombre de clusters à former
     * @param couleurs_clusters les couleurs des clusters à appliquer aux pixels identifiés
     * @return une nouvelle image où les pixels du biome identifié sont colorés selon les clusters, le reste de l'image étant éclairci
     */
    public static BufferedImage identifier(BufferedImage img, Color biome, Palette palette, AlgoClustering algo, int ncluster, Color[] couleurs_clusters) {

        ArrayList<Point> points = new ArrayList<>();

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (palette.getPlusProche(new Color(img.getRGB(x, y))).equals(biome)) {
                    points.add(new Point(x, y));
                }
            }
        }

        int[] clusters = algo.cluster(points.toArray(new Point[0]), ncluster);

        BufferedImage nimg = AffichageBiome.eclaircirImage(img, 0.75);

        for (int i = 0; i < points.size(); i++) {
            var p = points.get(i);
            nimg.setRGB(p.x, p.y, couleurs_clusters[clusters[i]].getRGB());
        }
        return nimg;
    }


    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File(args[0]));

        BufferedImage nimg = identifier(img, Palette.SAVANE, new Palette(NormeCouleur.CIELAB), new AlgoClustering() {
            @Override
            public int[] cluster(Point[] points, int ncluster) {
                int[] r = new int[points.length];

                int midx = img.getWidth() / 2;
                int midy = img.getHeight() / 2;

                for (int i = 0; i < points.length; i++) {
                    Point p = points[i];

                    r[i] = p.x < midx ? p.y < midy ? 0 : 1 : p.y < midy ? 2 : 3;

                }
                return r;
            }
        }, 4, new Color[]{
                Color.red, Color.blue, Color.gray, Color.magenta
        });

        ImageIO.write(nimg, "png", new File("cartes/cartes_modifiees/Planete 1_clusters.jpg"));
    }
}
