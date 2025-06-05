import algos.AlgoClustering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
     * @return une nouvelle image où les pixels du biome identifié sont colorés selon les clusters, le reste de l'image étant éclairci, et le nombre de clusters
     */
    public static Object[] identifier(BufferedImage img, Color biome, Palette palette, AlgoClustering algo, int ncluster, Color[] couleurs_clusters) {

        // Vérifie que le nombre de couleurs de clusters correspond au nombre de clusters
        if (couleurs_clusters.length < ncluster) {
            throw new IllegalArgumentException("Il n'y a pas assez de couleurs pour représenter les clusters. Veuillez fournir au moins " + ncluster + " couleurs.");
        }

        ArrayList<Point> points = new ArrayList<>();

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (palette.getPlusProche(new Color(img.getRGB(x, y))).equals(biome)) {
                    points.add(new Point(x, y));
                }
            }
        }

        // si aucun point n'est trouvé, retourner l'image d'origine
        if (points.isEmpty()) {
            return new Object[]{img, 0};
        }

        int[] clusters = algo.cluster(points.toArray(new Point[0]), ncluster);
        int nbClusters = 0;
        for (int cluster : clusters) {
            if (cluster > nbClusters) {
                nbClusters = cluster;
            }
        }
        nbClusters++;

        BufferedImage nimg = AffichageBiome.eclaircirImage(img, 0.75);

        for (int i = 0; i < points.size(); i++) {
            var p = points.get(i);
            nimg.setRGB(p.x, p.y, couleurs_clusters[clusters[i]].getRGB());
        }

        // ajouter le nom du biome sur l'image
        Graphics2D g2d = nimg.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, img.getHeight() / 30));
        g2d.drawString(palette.getNomCouleur(biome), 20, 40);
        g2d.dispose();

        return new Object[]{nimg, nbClusters};
    }

}
