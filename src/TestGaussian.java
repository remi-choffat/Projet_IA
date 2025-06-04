import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TestGaussian {
    public static void main(String[] args) throws IOException {
        BufferedImage img = ImageIO.read(new File(args[0]));
        Blur b = new GaussianBlur();
        BufferedImage new_img = b.blur(img, GaussianBlur.SIMPLE);

        File new_file = new File("cartes/cartes_modifiees/gaussian.png");
        ImageIO.write(new_img, "PNG", new_file);
    }
}
