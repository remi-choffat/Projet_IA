import algos.AlgoClustering;
import algos.DBScanClustering;
import algos.HCAClustering;
import algos.KMeansClustering;

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
            if (args.length < 3) {
                System.err.println("Usage: java Main <image_path> <output_directory> <algorithm> [<number_of_clusters>] [<eps> <minPts>] [<nom_biome>] [<taille_image_px>]");
                // Exemple java Main "cartes/Planete 1.jpg" "cartes/cartes_modifiees" KMeans 4 FORET_TEMPEREE 100
                // Exemple java Main "cartes/Planete 1.jpg" "cartes/cartes_modifiees" DBScan 5.0 4
                return;
            }

            final String imagePath = args[0];
            final String outputDirectory = args[1];

            // Création de l'algorithme de clustering
            AlgoClustering algo;
            switch (args[2].toLowerCase()) {
                case "kmeans":
                    algo = new KMeansClustering();
                    break;
                case "dbscan":
                    double eps = 5.0; // Valeur par défaut pour eps
                    int minPts = 4; // Valeur par défaut pour minPts
                    if (args.length >= 4) {
                        eps = Double.parseDouble(args[3]);
                    }
                    if (args.length >= 5) {
                        minPts = Integer.parseInt(args[4]);
                    }
                    algo = new DBScanClustering(eps, minPts);
                    break;
                case "hca_single":
                    algo = new HCAClustering(HCAClustering.SINGLE_LINKAGE);
                    break;
                case "hca_centroid":
                    algo = new HCAClustering(HCAClustering.CENTROID_LINKAGE);
                    break;
                default:
                    System.err.println("Algorithme inconnu : " + args[3]);
                    System.err.println("Utilisez 'KMeans', 'DBScan', 'HCA_Single' ou 'HCA_Centroid'.");
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

            int numberOfClusters = 4; // Valeur par défaut pour le nombre de clusters
            int tailleImagePx;
            String nomBiome = null;
            if (!(algo instanceof DBScanClustering)) {
                if (args.length >= 4) {
                    try {
                        numberOfClusters = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        nomBiome = args[3]; // Si ce n'est pas un nombre, c'est le nom du biome
                    }
                }
                if (args.length >= 5) {
                    nomBiome = args[4];
                }
                if (args.length >= 6) {
                    try {
                        tailleImagePx = Integer.parseInt(args[5]);
                        if (tailleImagePx <= 0) {
                            System.err.println("La taille de l'image doit être un nombre positif.");
                            return;
                        }
                        image = Utils.redimensionner(image, tailleImagePx);
                    } catch (NumberFormatException e) {
                        System.err.println("La taille de l'image doit être un nombre entier positif.");
                        return;
                    }
                }
            } else {
                if (args.length >= 6) {
                    nomBiome = args[5];
                }
                if (args.length >= 7) {
                    try {
                        tailleImagePx = Integer.parseInt(args[6]);
                        if (tailleImagePx <= 0) {
                            System.err.println("La taille de l'image doit être un nombre positif.");
                            return;
                        }
                        image = Utils.redimensionner(image, tailleImagePx);
                    } catch (NumberFormatException e) {
                        System.err.println("La taille de l'image doit être un nombre entier positif.");
                        return;
                    }
                }
            }

            // Application du flou
            Blur blur = new GaussianBlur();
            BufferedImage blurredImage = blur.blur(image, GaussianBlur.SIMPLE);

            Palette palette = new Palette(NormeCouleur.REDMEANS);

            // Définit les biomes à traiter
            Color[] biomes;
            if (nomBiome != null) {
                biomes = new Color[]{palette.getCouleur(nomBiome)};
            } else {
                biomes = palette.getCouleurs();
            }

            // Mise en évidence des biomes
            for (int i = 0; i < biomes.length; i++) {
                Color biome = biomes[i];
                int percent = (int) (i * 100.0 / biomes.length);
                System.out.print("\rIdentification des différents biomes (" + percent + " %)...");
                BufferedImage imageBiome = AffichageBiome.afficherBiome(blurredImage, palette, biome);
                ImageIO.write(imageBiome, extension, new File(outputDir, imageName + "_" + palette.getNomCouleur(biome) + "." + extension));
            }
            System.out.print("\rIdentification des différents biomes (100 %)...\n");

            // Couleurs pour mettre en évidence les différents clusters : liste aléatoire de taille 100
            Color[] ecosystemColors = new Color[100];
            for (int i = 0; i < ecosystemColors.length; i++) {
                ecosystemColors[i] = new Color((int) (Math.random() * 0x1000000));
            }

            // Génération des images d'écosystèmes
            for (int i = 0; i < biomes.length; i++) {
                Color biome = biomes[i];
                int percent = (int) (i * 100.0 / biomes.length);
                System.out.print("\rGénération des images d'écosystèmes (" + percent + " %)...");
                Object[] result = IdentifierBiome.identifier(blurredImage, biome, palette, algo, numberOfClusters, ecosystemColors);
                BufferedImage nimg = (BufferedImage) result[0];
                int nbClusters = (int) result[1];
                ImageIO.write(nimg, extension, new File(outputDir, (imageName + "_" + palette.getNomCouleur(biome) + "_" + nbClusters + "-clusters_" + args[2] + "." + extension).replace(" ", "_")));
            }
            System.out.print("\rGénération des images d'écosystèmes (100 %)...\n");

            System.out.println("Traitement terminé avec succès ! \nVos images sont enregistrées dans le répertoire : " + outputDir.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erreur lors du traitement de l'image : " + e.getMessage());
        }

    }
}