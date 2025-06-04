import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GaussianBlur implements Blur{

	// Matrice des filtres du flou gaussien
	public static final int SIMPLE = 0;
	public static final int MOYEN = 1;
	public static final int LARGE = 2;

	private int[][][] filtres_gauss = {
		{
			{1,	2,	1},
			{2,	4,	2},
			{1,	2,	1},
		},
		{
			{1,	4,	7,	4,	1},
			{4,	16,	26,	16,	4},
			{7,	26,	41,	26,	7},
			{4,	16,	26,	16,	4},
			{1,	4,	7,	4,	1},
		},
		{
			{0,	0,	1,	2,	1,	0,	0},
			{0,	3,	13,	22,	13,	3,	0},
			{1,	13,	59,	97,	59,	13,	1},
			{2,	22,	97,	159,	97,	22,	2},
			{1,	13,	59,	97,	59,	13,	1},
			{0,	3,	13,	22,	13,	3,	0},
			{0,	0,	1,	2,	1,	0,	0},
		},
	};
	
	public BufferedImage blur(BufferedImage img, int size){

		// Sélection du filtre en fonction de la taille choisie
		int [][] filtre = filtres_gauss[size];

		// On choisi par combien sera divisé la moyenne
		int divide = 0;
		for (int yf = 0; yf < filtre.length; yf++) {
			for (int xf = 0; xf < filtre.length; xf++) {
				divide += filtre[yf][xf];
			}
		}

		// Calcul du nombre de lignes et de colonnes de la nouvelle image
		int new_width = img.getWidth()/filtre[0].length;
		int new_height = img.getHeight()/filtre.length;

		// Création de la nouvelle image
		BufferedImage new_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

		// On parcourt chaque pixel de la nouvelle image
		for(int y = 0; y < img.getHeight(); y++){
			for(int x = 0; x < img.getWidth(); x++){
				int d = divide;
				int[] somme = {0, 0, 0};
				int middle = filtre.length/2;

				// Calcul de la couleur en fonction des voisins et de la matrice
				for (int yf = 0; yf < filtre.length; yf++) {
					for (int xf = 0; xf < filtre.length; xf++) {
						if(y + yf - middle >= 0 && x + xf - middle >= 0 && y + yf < img.getHeight() && x + xf < img.getWidth()){
							int[] coord = {x+xf - middle, y+yf - middle};
							Color c = new Color(img.getRGB(coord[0], coord[1]));
							somme[0] += c.getRed() * filtre[yf][xf];
							somme[1] += c.getGreen() * filtre[yf][xf];
							somme[2] += c.getBlue() * filtre[yf][xf];
						}else{
							d -= filtre[yf][xf];
						}
					}
				}

				// Appliquer nouvelle couleur
				int[] moy = {somme[0]/divide, somme[1]/divide, somme[2]/divide};
				Color rgb = new Color(moy[0], moy[1], moy[2]);
				new_img.setRGB(x, y, rgb.getRGB());
			}
		}
		return new_img;
	}
}
