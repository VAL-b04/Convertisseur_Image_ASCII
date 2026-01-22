# Convertisseur d'image en ASCII

Un projet Java permettant de convertir des séquences d'images (frames vidéo) en animations ASCII art affichées directement dans le terminal. Ce projet a été influencé par les idées d'[**Adam Bajic**](https://github.com/AdamBa-25)

## Description

Ce projet reprend les bases du [**Brouillimg**](https://github.com/VAL-b04/Brouillimg), un ancien projet de cryptage/décryptage d'images. Le convertisseur utilise la conversion en niveaux de gris (valeurs 0-255) pour transformer chaque pixel en caractère ASCII, créant ainsi une représentation textuelle animée de votre vidéo.

Le principe est simple : chaque pixel est analysé selon sa luminosité (niveau de gris) et converti en caractère correspondant, du plus sombre (`8`) au plus clair (espace), permettant de recréer l'image dans le terminal.

## Fonctionnalités

- Conversion d'images PNG en ASCII art
- Animation de séquences d'images dans le terminal
- Redimensionnement automatique selon la taille du terminal
- Contrôle de la vitesse d'animation (FPS personnalisable)
- Option de skip de frames pour accélérer le rendu
- Conversion en niveaux de gris avec mapping vers 5 caractères différents

## Installation

1. Clonez le dépôt :
```bash
git clone https://github.com/VAL-b04/Convertisseur_Image_ASCII
cd Convertisseur_Image_ASCII
```

2. Compilez le programme :
```bash
javac Convertisseur_Image.java
```

## Préparation des frames vidéo

Pour obtenir les frames de votre vidéo, utilisez **VLC Media Player** :

1. Ouvrez votre vidéo dans VLC
2.  **Outils** → **Préférences** → **Vidéo** → **Filtre vidéo de scene**

[**Tutoriels YouTube disponibles**](https://www.youtube.com/watch?v=seUvEd-UtxA) pour extraire des frames avec VLC.

### Important : Optimisation des vidéos

Pour un résultat optimal, privilégiez des vidéos avec :
- ✅ **Forts contrastes** (noir et blanc prononcés)
- ✅ **Niveaux de blanc et noir élevés**
- ✅ Pas trop de détails fins (qui se perdent dans la conversion)
- ✅ Mouvements clairs et distincts

Les vidéos avec des nuances de gris moyennes ou peu de contraste donneront un résultat moins lisible.

## Utilisation

Lancez le programme :
```bash
java Convertisseur_Image
```

Le programme vous demandera successivement :

1. **Dossier des frames** : Chemin vers le dossier contenant vos images
   ```
   Exemple: C:\Users\Votre_Nom\Videos\frames
   ```

2. **Nombre de frames** : Nombre total d'images dans le dossier
   ```
   Exemple: 240
   ```

3. **Skip de frames** : Prendre 1 frame toutes les X frames
   ```
   1 = toutes les frames
   2 = une frame sur deux (animation 2x plus rapide)
   3 = une frame sur trois (animation 3x plus rapide)
   ```

4. **Préfixe des fichiers** : Préfixe utilisé pour nommer vos frames
   ```
   Exemple: scene, frame, img
   Les fichiers doivent être nommés: scene00001.png, scene00002.png, etc.
   ```

5. **FPS pour la lecture** : Vitesse d'animation souhaitée
   ```
   Recommandé: 24 FPS
   12-15 FPS pour ralenti
   30-60 FPS pour accéléré
   ```

### Exemple d'exécution

```
Dossier des frames : frames
Nombre de frames : 120
Prendre 1 frame toutes les X frames (1 = toutes, 2 = une sur deux, etc.) : 2
Prefixe des fichiers (ex: scene, frame, img) : frame
FPS pour la lecture (recommande: 24) : 24
Taille du terminal : 100x40
Appuyez sur Entree pour commencer...
```

## Fonctionnement technique

### Conversion en niveaux de gris

L'algorithme utilise la formule pondérée standard :
```
Gris = (R × 299 + G × 587 + B × 114) / 1000
```

### Mapping des caractères

Les pixels sont convertis selon leur niveau de gris (0-255) :

| Plage de gris | Caractère | Représentation |
|---------------|-----------|----------------|
| 0 - 50        | `8`       | Très sombre    |
| 51 - 101      | `7`       | Sombre         |
| 102 - 152     | `4`       | Moyen          |
| 153 - 203     | `.`       | Clair          |
| 204 - 255     | ` `       | Très clair     |

### Redimensionnement automatique

Le programme adapte automatiquement la taille de rendu :
- Largeur max : 150 caractères (min: 40)
- Hauteur max : 50 lignes (min: 20)
- Ratio d'aspect conservé

## Structure du projet

```
Convertisseur_Image/
│
├── Convertisseur_Image.java    # Code source principal
├── README.md                    # Ce fichier
│
└── frames/                      # Dossier exemple pour vos frames
    ├── frame00001.png
    ├── frame00002.png
    └── ...
```
