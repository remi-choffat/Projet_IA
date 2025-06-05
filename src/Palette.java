import java.awt.*;

/**
 * Représente une palette de couleurs prédéfinies pour des environnements naturels.
 */
public class Palette {

    public static final Color TUNDRA = new Color(71, 70, 61);
    public static final Color TAIGA = new Color(43, 50, 35);
    public static final Color FORET_TEMPEREE = new Color(59, 66, 43);
    public static final Color FORET_TROPICALE = new Color(46, 64, 34);
    public static final Color SAVANE = new Color(84, 106, 70);
    public static final Color PRAIRIE = new Color(104, 95, 82);
    public static final Color DESERT = new Color(152, 140, 120);
    public static final Color GLACIER = new Color(200, 200, 200);
    public static final Color EAU_PEU_PROFONDE = new Color(49, 83, 100);
    public static final Color EAU_PROFONDE = new Color(12, 31, 47);

    /**
     * Tableau de couleurs prédéfinies
     */
    private final Color[] couleurs = new Color[]{
            TUNDRA, // Toundra
            TAIGA, // Taïga
            FORET_TEMPEREE, // Forêt tempérée
            FORET_TROPICALE, // Forêt tropicale
            SAVANE, // Savane
            PRAIRIE, // Prairie
            DESERT, // Désert
            GLACIER, // Glacier
            EAU_PEU_PROFONDE, // Eau peu profonde
            EAU_PROFONDE  // Eau profonde
    };


    /**
     * Norme de distance des couleurs utilisée pour comparer les couleurs
     */
    private final NormeCouleur distanceCouleurs;


    /**
     * Constructeur de la classe Palette
     *
     * @param distanceCouleurs la norme de distance des couleurs
     */
    public Palette(NormeCouleur distanceCouleurs) {
        this.distanceCouleurs = distanceCouleurs;
    }


    /**
     * Renvoie le tableau de couleurs prédéfinies
     *
     * @return le tableau de couleurs
     */
    public Color[] getCouleurs() {
        return couleurs;
    }


    public Color getCouleur(String nomBiome) {
        return switch (nomBiome.toLowerCase()) {
            case "tundra" -> TUNDRA;
            case "taiga" -> TAIGA;
            case "forêt_tempérée" -> FORET_TEMPEREE;
            case "forêt_tropicale" -> FORET_TROPICALE;
            case "savane" -> SAVANE;
            case "prairie" -> PRAIRIE;
            case "désert" -> DESERT;
            case "glacier" -> GLACIER;
            case "eau_peu_profonde" -> EAU_PEU_PROFONDE;
            case "eau_profonde" -> EAU_PROFONDE;
            default -> throw new IllegalArgumentException("Nom de biome inconnu : " + nomBiome);
        };
    }


    /**
     * Récupère la couleur la plus proche d'une couleur donnée dans le tableau des couleurs prédéfinies
     *
     * @param c la couleur à comparer
     * @return la couleur la plus proche dans le tableau couleurs
     */
    public Color getPlusProche(Color c) {
        int closestColorIndex = 0;
        double minDistance = Double.MAX_VALUE;
        for (int k = 0; k < couleurs.length; k++) {
            double distance = distanceCouleurs.distanceCouleur(c, couleurs[k]);
            if (distance < minDistance) {
                minDistance = distance;
                closestColorIndex = k;
            }
        }
        return couleurs[closestColorIndex];
    }


    /**
     * Renvoie le nom de la couleur correspondant à une couleur donnée
     *
     * @param c la couleur à identifier
     * @return le nom de la couleur
     */
    public String getNomCouleur(Color c) {
        if (TUNDRA.equals(c)) return "Tundra";
        if (TAIGA.equals(c)) return "Taïga";
        if (FORET_TEMPEREE.equals(c)) return "Forêt tempérée";
        if (FORET_TROPICALE.equals(c)) return "Forêt tropicale";
        if (SAVANE.equals(c)) return "Savane";
        if (PRAIRIE.equals(c)) return "Prairie";
        if (DESERT.equals(c)) return "Désert";
        if (GLACIER.equals(c)) return "Glacier";
        if (EAU_PEU_PROFONDE.equals(c)) return "Eau peu profonde";
        if (EAU_PROFONDE.equals(c)) return "Eau profonde";
        return "Biome inconnu";
    }
}
