import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
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

        // si aucun point n'est trouvé, retourner l'image d'origine
        if (points.isEmpty()) {
            return img;
        }

        int[] clusters = algo.cluster(points.toArray(new Point[0]), ncluster);

        BufferedImage nimg = AffichageBiome.eclaircirImage(img, 0.75);

        for (int i = 0; i < points.size(); i++) {
            var p = points.get(i);
            nimg.setRGB(p.x, p.y, couleurs_clusters[clusters[i]].getRGB());
        }

        // ajouter le nom du biome sur l'image
        Graphics2D g2d = nimg.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        g2d.drawString(palette.getNomCouleur(biome), 20, 40);
        g2d.dispose();

        return nimg;
    }


    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File("cartes/Planete 1.jpg"));

        // Définir la liste des biomes à traiter
        Color[] biomes = new Color[]{
                Palette.SAVANE,
                Palette.EAU_PEU_PROFONDE,
                Palette.FORET_TROPICALE,
                Palette.EAU_PROFONDE,
                Palette.GLACIER,
                Palette.DESERT
        };

        // Définir les couleurs pour les écosystèmes
        Color[] ecosystemColors = new Color[]{
                Color.RED,
                Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.MAGENTA,
                Color.CYAN
        };

        Palette palette = new Palette(NormeCouleur.CIELAB);
        AlgoClustering algo = new KMeansClustering();

        // Pour chaque biome, créer une image avec les écosystèmes mis en évidence
        for (int i = 0; i < biomes.length; i++) {
            Color biome = biomes[i];
            BufferedImage nimg = identifier(img, biome, palette, algo, 3, ecosystemColors); // 3 clusters par biome
            ImageIO.write(nimg, "png", new File("cartes/cartes_modifiees/Planete_1_" + palette.getNomCouleur(biome) + "_ecosystems.png"));
        }

        System.out.println("Les écosystèmes ont été mis en évidence et sauvegardés !");
    }
}
