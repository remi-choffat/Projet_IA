import java.awt.image.BufferedImage;

/**
 * Classe pour afficher les différents biomes sur une image.
 */
public class AffichageBiome {

    /**
     * Éclaircit une image en augmentant la luminosité de chaque pixel
     *
     * @param image       l'image à éclaircir
     * @param pourcentage le pourcentage d'éclaircissement (0.0 à 1.0)
     * @return l'image éclaircie
     */
    public static BufferedImage eclaircirImage(BufferedImage image, double pourcentage) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int newR = (int) Math.round(r + pourcentage * (255 - r));
                int newG = (int) Math.round(g + pourcentage * (255 - g));
                int newB = (int) Math.round(b + pourcentage * (255 - b));

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                int newRGB = (newR << 16) | (newG << 8) | newB;
                result.setRGB(x, y, newRGB);
            }
        }
        return result;
    }


}
