import java.awt.Color;

/**
 * NormeCouleur
 */
public interface NormeCouleur {

	double distanceCouleur(Color c1, Color c2);

	

	private static double r(Color c1, Color c2) {
		return 0.5 * (c1.getRed() + c2.getRed());
	}
	private static double dr(Color c1, Color c2) {
		return c1.getRed() - c2.getRed();
	}
	private static double dg(Color c1, Color c2) {
		return c1.getGreen() - c2.getGreen();
	}
	private static double db(Color c1, Color c2) {
		return c1.getBlue() - c2.getBlue();
	}



	public final NormeCouleur REDMEANS = (Color c1, Color c2) ->  {
			return Math.sqrt(
				(2 + r(c1, c2)/256) * Math.pow(dr(c1, c2), 2) + 4 * Math.pow(dg(c1, c2), 2) + (
					2 + (255 - r(c1, c2))/256 
					) * Math.pow(db(c1, c2), 2)
				);
	};

	public final NormeCouleur BASE =  (Color c1, Color c2) ->  {
			return (Math.pow(dr(c1, c2), 2) + 
					Math.pow(dg(c1, c2), 2) + 
					Math.pow(db(c1, c2), 2)  
					);	
		};


	private static double c(int[] c1) {
		return Math.sqrt(Math.pow(c1[1], 2) + Math.pow(c1[2], 2));
	}


	public final NormeCouleur CIELAB = (Color c1, Color c2) -> {
		int[] lab1 = Utils.rgb2lab(c1);
		int[] lab2 = Utils.rgb2lab(c2);

		return Math.sqrt(
					Math.pow(((lab1[0] - lab2[0])/1), 2) + 
					Math.pow((c(lab1) - c(lab2))/ (1 + 0.045 * c(lab1)), 2) + 
					Math.pow(
						Math.sqrt(
							Math.pow(lab1[1] - lab2[1], 2) + 
							Math.pow(lab1[2] - lab2[2], 2) + 
							Math.pow(c(lab1) - c(lab2), 2)
							)/ (1 +0.015 * c(lab1)),
						2
						)
				);
	};



	
}
