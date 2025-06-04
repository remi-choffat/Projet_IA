import javax.imageio.ImageIO;
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
            ImageIO.write(blurredImage, "png", new File("cartes/cartes_modifiees/Planete 1_avecBlur.jpg"));
            System.out.println("Image traitée avec succès !");

            // Met en évidence un biome sélectionné sur l'image floutée
            BufferedImage imageBiome = AffichageBiome.afficherBiome(blurredImage, new Palette(NormeCouleur.REDMEANS), Palette.SAVANE);
            ImageIO.write(imageBiome, "png", new File("cartes/cartes_modifiees/Planete 1_biomes.jpg"));

        } catch (IOException e) {
            System.err.println("Erreur lors du traitement de l'image : " + e.getMessage());
        }
    }

}