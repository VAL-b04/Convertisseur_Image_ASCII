import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Convertisseur_Image
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        Scanner scanner = new Scanner(System.in);

        // Demander le dossier des frames
        System.out.print("Dossier des frames : ");
        String folderPath = scanner.nextLine();

        // Demander le nombre de frames
        System.out.print("Nombre de frames : ");
        int frameCount = scanner.nextInt();
        scanner.nextLine();

        // Demander combien de frames à sauter (pour accélérer)
        System.out.print("Prendre 1 frame toutes les X frames (1 = toutes, 2 = une sur deux, etc.) : ");
        int frameSkip = scanner.nextInt();
        scanner.nextLine();

        // Demander le préfixe des fichiers
        System.out.print("Prefixe des fichiers (ex: scene, frame, img) : ");
        String prefix = scanner.nextLine();

        // Demander les FPS pour la lecture
        System.out.print("FPS pour la lecture (recommande: 24) : ");
        int fps = scanner.nextInt();

        // Générer les noms de fichiers
        int nbFramesToLoad = (frameCount + frameSkip - 1) / frameSkip;
        String[] frames = new String[nbFramesToLoad];
        int index = 0;
        for (int i = 1; i <= frameCount; i += frameSkip)
        {
            frames[index] = folderPath + File.separator + String.format("%s%05d.png", prefix, i);
            index++;
        }

        // Lancer l'animation
        animateInTerminal(frames, fps);
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight)
    {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double ratio = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);

        int targetWidth = (int) (originalWidth * ratio);
        int targetHeight = (int) (originalHeight * ratio);

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }

    public static int[][] rgb2gl(BufferedImage inputRGB)
    {
        int height = inputRGB.getHeight();
        int width = inputRGB.getWidth();
        int[][] outGL = new int[height][width];

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int argb = inputRGB.getRGB(x, y);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                int gray = (r * 299 + g * 587 + b * 114) / 1000;
                outGL[y][x] = gray;
            }
        }

        return outGL;
    }

    public static String conversionChiffre(int[][] imageGL)
    {
        StringBuilder sb = new StringBuilder();
        int height = imageGL.length;
        int width = imageGL[0].length;
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixel = imageGL[y][x]; // 0 = noir, 255 = blanc

                char caractere;
                if (pixel < 51)
                {
                    caractere = '8';
                }
                else if (pixel < 102)
                {
                    caractere = '7';
                }
                else if (pixel < 153)
                {
                    caractere = '4';
                }
                else if (pixel < 204)
                {
                    caractere = '.';
                }
                else
                {
                    caractere = ' ';
                }

                sb.append(caractere);
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public static void animateInTerminal(String[] imagePaths, int fps) throws IOException, InterruptedException
    {
        int delay = 1000 / fps;

        // Charger la première image pour définir la taille
        BufferedImage firstImg = ImageIO.read(new File(imagePaths[0]));
        if (firstImg == null)
        {
            System.err.println("Impossible de charger la première image");
            return;
        }

        // Définir la taille du terminal basée sur l'image
        int terminalWidth = Math.min(firstImg.getWidth() / 4, 150);
        int terminalHeight = Math.min(firstImg.getHeight() / 8, 50);

        terminalWidth = Math.max(terminalWidth, 40);
        terminalHeight = Math.max(terminalHeight, 20);

        System.out.println("Taille du terminal : " + terminalWidth + "x" + terminalHeight);
        System.out.println("Appuyez sur Entree pour commencer...");
        System.in.read();
        
        // Animer toutes les frames
        for (int i = 0; i < imagePaths.length; i++)
        {
            String path = imagePaths[i];
            File imageFile = new File(path);

            if (!imageFile.exists())
            {
                System.err.println("Fichier introuvable : " + path);
                continue;
            }

            BufferedImage img = ImageIO.read(imageFile);
            if (img == null)
            {
                System.err.println("Impossible de charger : " + path);
                continue;
            }

            // Redimensionner l'image
            BufferedImage resizedImg = resizeImage(img, terminalWidth, terminalHeight);

            // Convertir en niveaux de gris
            int[][] imageGL = rgb2gl(resizedImg);

            // Convertir en caractères
            String asciiFrame = conversionChiffre(imageGL);

            // Afficher
            clearConsole();
            System.out.print(asciiFrame);

            // Attendre le délai entre frames
            Thread.sleep(delay);
        }
    }

    public static void clearConsole()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}