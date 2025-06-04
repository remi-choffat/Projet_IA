import java.awt.image.BufferedImage;

/**
 * Interface pour appliquer un flou à une image.
 */
public interface Blur {

    /**
     * Applique un flou à l'image donnée.
     *
     * @param image  l'image à flouter
     * @param radius le rayon du flou (en pixels)
     * @return l'image floutée
     */
    BufferedImage blur(BufferedImage image, int radius);

}
