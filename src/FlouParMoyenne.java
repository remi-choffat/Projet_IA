import java.awt.image.BufferedImage;

/**
 * Classe pour appliquer un flou par moyenne à une image.
 * Cette classe implémente l'interface Blur.
 */
public class FlouParMoyenne implements Blur{
    /**
     * Applique un flou par moyenne à l'image donnée.
     *
     * @param image  l'image à flouter
     * @param radius le rayon du flou (en pixels)
     * @return l'image floutée
     */
    @Override
    public BufferedImage blur(BufferedImage image, int radius) {

        // verification des paramètres pour éviter les erreurs
        if (image == null || radius < 1) {
            return image;
        }

        // récupération des dimensions de l'image
        int width = image.getWidth();
        int height = image.getHeight();

        // création d'une nouvelle image pour stocker le résultat
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // calcul de la taille du filtre : (2 * radius + 1) x (2 * radius + 1)
        int filterSize = 2 * radius + 1;
        double coefficient = 1.0 / (filterSize * filterSize); // coef pour chaque élément du filtre

        // création de la matrice de convolution (tous les coef sont égaux)
        double[][] kernel = new double[filterSize][filterSize];
        for (int i = 0; i < filterSize; i++) {
            for (int j = 0; j < filterSize; j++) {
                kernel[i][j] = coefficient; // chaque coef vaut 1/(taille * taille)
            }
        }

        // parcours de chaque pixel de l'image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // initialisation des accumulations pour les canaux RGB
                double red = 0.0, green = 0.0, blue = 0.0;

                // parcours des pixels voisins dans la fenêtre du filtre
                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        // Gestion des bords : limit des coordonnées pour rester dans l'image
                        int pixelX = Math.min(Math.max(x + kx, 0), width - 1);
                        int pixelY = Math.min(Math.max(y + ky, 0), height - 1);

                        // récupération de la couleur RGB du pixel voisin
                        int rgb = image.getRGB(pixelX, pixelY);

                        // ajout de la contribution du pixel voisin pour chaque canal
                        red += ((rgb >> 16) & 0xFF) * kernel[ky + radius][kx + radius];
                        green += ((rgb >> 8) & 0xFF) * kernel[ky + radius][kx + radius];
                        blue += (rgb & 0xFF) * kernel[ky + radius][kx + radius];
                    }
                }

                // conversion et limit des valeurs RGB dans [0, 255]
                int newRed = Math.min(Math.max((int) red, 0), 255);
                int newGreen = Math.min(Math.max((int) green, 0), 255);
                int newBlue = Math.min(Math.max((int) blue, 0), 255);

                // création du nouveau pixel RGB
                int newRGB = (newRed << 16) | (newGreen << 8) | newBlue;

                // assignation du nouveau pixel à l'image de sortie
                output.setRGB(x, y, newRGB);
            }
        }

        // retourne l'image floutée
        return output;
    }
}