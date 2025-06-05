import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Programme principal
 */
public class Main {

    /**
     * Point d'entrée du programme.
     *
     * @param args les arguments de la ligne de commande, où le premier argument est le chemin de l'image et le second est le répertoire de sortie.
     */
    public static void main(String[] args) {

        try {
            if (args.length < 4) {
                System.err.println("Usage: java Main <image_path> <output_directory> <number_of_clusters> <algorithm>");
                // Exemple java Main "cartes/Planete 1.jpg" "cartes/cartes_modifiees" 4 KMeans
                return;
            }

            final String imagePath = args[0];
            final String outputDirectory = args[1];
            final int numberOfClusters = Integer.parseInt(args[2]);

            // Création de l'algorithme de clustering
            AlgoClustering algo;
            switch (args[3].toLowerCase()) {
                case "kmeans":
                    algo = new KMeansClustering();
                    break;
                default:
                    System.err.println("Algorithme inconnu : " + args[3]);
                    System.err.println("Utilisez 'KMeans'.");
                    return;
            }

            File imageFile = new File(imagePath);
            String imageName = imageFile.getName();
            final String extension = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();
            imageName = imageName.substring(0, imageName.lastIndexOf(extension) - 1);

            // Vérification de l'existence du répertoire de sortie
            File outputDir = new File(outputDirectory);
            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()) {
                    System.err.println("Erreur lors de la création du répertoire de sortie : " + outputDir.getAbsolutePath());
                    return;
                }
            }

            // Chargement de l'image à partir du chemin spécifié
            BufferedImage image = ImageIO.read(imageFile);

            System.out.println("Traitement de l'image " + imageName);

            // création de l'instance de FlouParMoyenne
            Blur blur = new FlouParMoyenne();

            // application du filtre de flou avec un rayon de 1 pixel
            BufferedImage blurredImage = blur.blur(image, 1);

            Palette palette = new Palette(NormeCouleur.REDMEANS);

            // Définir les biomes (tous les biomes de Palette)
            Color[] biomes = palette.getCouleurs();

            // Mise en évidence des biomes
            for (int i = 0; i < biomes.length; i++) {
                Color biome = biomes[i];
                int percent = (int) ((i + 1) * 100.0 / biomes.length);
                System.out.print("\rIdentification des différents biomes (" + percent + " %)...");
                BufferedImage imageBiome = AffichageBiome.afficherBiome(blurredImage, palette, biome);
                ImageIO.write(imageBiome, extension, new File(outputDir, imageName + "_" + palette.getNomCouleur(biome) + "." + extension));
            }
            System.out.println();

            // Couleurs pour mettre en évidence les différents clusters
            Color[] ecosystemColors = new Color[]{
                    Color.RED,
                    Color.BLUE,
                    Color.GREEN,
                    Color.YELLOW,
                    Color.MAGENTA,
                    Color.CYAN,
                    Color.ORANGE
            };

            for (int i = 0; i < biomes.length; i++) {
                Color biome = biomes[i];
                int percent = (int) ((i + 1) * 100.0 / biomes.length);
                System.out.print("\rGénération des images d'écosystèmes (" + percent + " %)...");
                BufferedImage nimg = IdentifierBiome.identifier(blurredImage, biome, palette, algo, numberOfClusters, ecosystemColors);
                ImageIO.write(nimg, extension, new File(outputDir, imageName + "_" + palette.getNomCouleur(biome) + "_ecosystemes." + extension));
            }

            System.out.println("\nTraitement terminé avec succès ! Vos images sont enregistrées dans le répertoire : " + outputDir.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erreur lors du traitement de l'image : " + e.getMessage());
        }

    }
}