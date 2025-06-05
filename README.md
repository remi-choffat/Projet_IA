# Rapport
## Introduction
L'objectif de la SAÉ est d'identifier les différents biomes d'une planètes et les rassembler en cluster pour différencier plusieurs écosystèmes sur l'image. Il faudra donc passer par plusieurs étapes afin d'arriver à notre objectif : 
 - [Prétraitement de l'image](#prétraitement-de-limage)
 - [Détection et visualisation des biomes](#détection-et-visualisation-des-biomes)
 - [Détection et affichage des écosystèmes pour chaque biome](#détection-et-affichage-des-écosystèmes-pour-chaque-biome)
## Prétraitement de l'image
Avant d'effectuer les calculs sur l'image, il faut la traiter. Pour cela, on va appliquer un flou qui permettra d'avoir une image plus homogène avec des couleurs plus semblables. Les clusters seront donc plus denses.    
Pour appliquer un flou, on a implémenter deux techniques :
 - le flou par moyenne
 - le flou gaussien
Le flou par moyenne utilise un filtre carré en 3x3. Chaque pixel va donc prendre la moyenne de couleur de tous ses voisins.   
Le flou gaussien utilise des filtres précalculés en 3x3, 5x5 ou 7x7. Les coefficients sont plus élevés au centre et faibles aux extrémités. La moyenne du pixel est calculée à partir du filtre.

#TODO INSÉRER COMPARAISON FLOU

## Détection et visualisation des biomes
Chaque pixel de l'image floutée est ensuite parcouru et comparé avec une palette de couleur qui défini chaque biome. Un pixel appartiendra au biome dont la couleur est la plus proche d'une palette.   
Les pixels obtenus sont stockés puis envoyés aux algorithmes de clustering pour définir les écosystèmes.

#TODO INSÉRER IMAGE BIOME 

## Détection et affichage des écosystèmes pour chaque biome
