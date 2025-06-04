import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Programme principal
 */
public class Main {

    public static void main(String[] args) {
        try {
            // chargement de l'image
            BufferedImage image = ImageIO.read(new File("cartes/Planete 1.jpg"));

            // création de l'instance de FlouParMoyenne
            Blur blur = new FlouParMoyenne();

            // application du filtre avec un rayon de 1 pixel
            BufferedImage blurredImage = blur.blur(image, 1);

            // sauvegarde de l'image floutée
            ImageIO.write(blurredImage, "jpg", new File("cartes/cartes_modifiees/Planete 1_avecBlur.jpg"));

            // Définir les biomes
            Color[] biomes = new Color[]{
                    Palette.SAVANE,
                    Palette.EAU_PEU_PROFONDE,
                    Palette.FORET_TROPICALE,
                    Palette.EAU_PROFONDE,
                    Palette.GLACIER,
                    Palette.DESERT
            };

            Palette palette = new Palette(NormeCouleur.REDMEANS);

            // Mise en évidence des biomes
            for (Color biome : biomes) {
                BufferedImage imageBiome = AffichageBiome.afficherBiome(blurredImage, palette, biome);
                ImageIO.write(imageBiome, "jpg", new File("cartes/cartes_modifiees/Planete_1_" + palette.getNomCouleur(biome) + "_biome.jpg"));
            }

            // Mise en évidence des écosystèmes
            Color[] ecosystemColors = new Color[]{
                    Color.RED,
                    Color.BLUE,
                    Color.GREEN,
                    Color.YELLOW,
                    Color.MAGENTA,
                    Color.CYAN
            };

            AlgoClustering algo = new KMeansClustering();

            for (Color biome : biomes) {
                BufferedImage nimg = IdentifierBiome.identifier(blurredImage, biome, palette, algo, 3, ecosystemColors);
                ImageIO.write(nimg, "jpg", new File("cartes/cartes_modifiees/Planete_1_" + palette.getNomCouleur(biome) + "_ecosystems.jpg"));
            }

            System.out.println("Traitement terminé avec succès !");

        } catch (IOException e) {
            System.err.println("Erreur lors du traitement de l'image : " + e.getMessage());
        }
    }
}