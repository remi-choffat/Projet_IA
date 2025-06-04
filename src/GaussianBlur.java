import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GaussianBlur implements Blur{

	public static final int SIMPLE = 0;
	public static final int MOYEN = 1;
	public static final int LARGE = 2;

	private int[][][] filtres_gauss = {
		{
			{1,	2, 1},
			{2, 4, 2},
			{1, 2, 1},
		},
		{
			{1,	4,	7,	4,	1},
			{4,	16, 26, 16, 4},
			{7, 26, 41, 26, 7},
			{4,	16, 26, 16, 4},
			{1,	4,	7,	4,	1},
		},
		{
			{0,	0,	1,	2,	1,	0,	0},
			{0,	3,	13,	22,	13,	3,	0},
			{1,	13,	59,	97,	59,	13,	1},
			{2,	22,	97,	159,97,	22,	2},
			{1,	13,	59,	97,	59,	13,	1},
			{0,	3,	13,	22,	13,	3,	0},
			{0,	0,	1,	2,	1,	0,	0},
		},
	};
	
	public BufferedImage blur(BufferedImage img, int size){
		int divide;
		switch (size) {
			case SIMPLE:
				divide = 16;
				break;

			case MOYEN:
				divide = 273;
				break;

			case LARGE:
				divide = 1003;
				break;
			default:
				divide = 16;
				break;
		}
		int [][] filtre = filtres_gauss[size];
		int new_width = img.getWidth()/filtre[0].length;
		int new_height = img.getHeight()/filtre.length;
		BufferedImage new_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

		for(int y = 0; y < new_height; y++){
			for(int x = 0; x < new_width; x++){
				int[] somme = {0, 0, 0};
				for (int yf = 0; yf < filtre.length; yf++) {
					for (int xf = 0; xf < filtre.length; xf++) {
						int[] img_coord = {x*filtre[yf].length+xf, y*filtre.length+yf};
						Color c = new Color(img.getRGB(img_coord[0], img_coord[1]));
						somme[0] += c.getRed() * filtre[yf][xf];
						somme[1] += c.getGreen() * filtre[yf][xf];
						somme[2] += c.getBlue() * filtre[yf][xf];
					}
				}
				int[] moy = {somme[0]/divide, somme[1]/divide, somme[2]/divide};
				Color rgb = new Color(moy[0], moy[1], moy[2]);
				for (int yf = 0; yf < filtre.length; yf++) {
					for (int xf = 0; xf < filtre.length; xf++) {
						int[] img_coord = {x*filtre[yf].length+xf, y*filtre.length+yf};
						new_img.setRGB(img_coord[0], img_coord[1], rgb.getRGB());
					}
				}
			}
		}
		return new_img;
	}
}
