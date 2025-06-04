import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * programme principal pour appliquer un flou par moyenne à une image.
 */
public class MainFlou {
    public static void main(String[] args) {
        try {
            // chargement de l'image
            BufferedImage image = ImageIO.read(new File("cartes/Planete 1.jpg"));

            // création de l'instance de FlouParMoyenne
            Blur blur = new FlouParMoyenne();

            // application du filtre avec un rayon de 3
            BufferedImage blurredImage = blur.blur(image, 3);

            // sauvegarde de l'image floutée
            ImageIO.write(blurredImage, "png", new File("cartes/Planete 1_avecBlur.jpg"));
            System.out.println("Image traitée avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}