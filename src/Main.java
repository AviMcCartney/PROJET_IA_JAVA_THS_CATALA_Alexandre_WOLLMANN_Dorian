import Support.FFT.Complexe;
import Support.FFT.ComplexeCartesien;
import Support.FFT.FFTCplx;
import Support.Son.Son;
import Support.neurone.iNeurone;
import Support.neurone.NeuroneHeaviside;
import Support.neurone.Neurone;

public class Main
{
    public static void main(String[] args) {

        // Création z objets Son pour les fichiers sonores
        Son sonSinus = new Son("Sources_sonores/Sinusoide.wav");
        Son sonSinusHarmonique = new Son("Sources_sonores/Sinusoide3Harmoniques.wav");

        // Affichage des informations sur les fichiers sonores
        System.out.println("Lecture du fichier WAV Sources_sonores/Sinus.wav");
        System.out.println("Fichier Sources_sonores/Sinus.wav : "+sonSinus.donnees().length+" échantillons à "+sonSinus.frequence()+"Hz");
        System.out.println("Bloc 1 : "+sonSinus.bloc_deTaille(1, 512).length+" échantillons à "+sonSinus.frequence()+"Hz");

        // Extraction de blocs de données de taille 512 échantillons des fichiers sonores
        float [][] DonneesR = new float[10][512];
        for(int i = 0; i < 5; i++){
            DonneesR[i] = sonSinusHarmonique.bloc_deTaille(i+1,512);
            DonneesR[i+5] = sonSinus.bloc_deTaille(i+1, 512);
        }

        // Conversion des blocs de données réelles en tableaux de complexes pour utiliser la FFT
        Complexe[][] DonneesC = new Complexe[10][512];
        for(int i = 0; i < 10; i++){
            DonneesC[i] = new Complexe[DonneesR[i].length];
            for(int j = 0; j < 512; j++){
                DonneesC[i][j] = new ComplexeCartesien(DonneesR[i][j],0);
            }
        }

        // Application de la FFT sur chaque bloc de données complexes
        Complexe[][] Signaux = new Complexe[10][512];
        for(int i = 0; i < 10; i++){
            Signaux[i] = FFTCplx.appliqueSur(DonneesC[i]);
        }

        // Extraction des modules des valeurs complexes résultantes de la FFT
        float[][] Modules = new float[10][512];
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 512; j++){
                Modules[i][j] = (float)Signaux[i][j].mod();
            }
        }

        // Préparation des entrées pour le neurone
        final float[][] entrees = new float[10][512];
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 512; j++) {
                entrees[i][j] = Modules[i][j];
            }

        }

        // Affichage des entrées
        for(int i = 0; i < 10; i++){
            for(int j=0 ; j < 512; j++){
                System.out.println("Entree["+i+"]["+j+"]: "+ entrees[i][j]);
            }
        }

        // Résultats attendus pour chaque bloc
        final float[] resultats = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1};

        // Création du neurone et apprentissage
        final iNeurone Sinus = new NeuroneHeaviside(entrees[0].length);

        System.out.println("Apprentissage...");
        System.out.println("Nombre de tours : "+Sinus.apprentissage(entrees, resultats));

        // Affichage des synapses et du biais après apprentissage
        final Neurone vueNeurone = (Neurone)Sinus;
        System.out.print("Synapses : ");
        for (final float f : vueNeurone.synapses())
            System.out.print(f+" ");
        System.out.print("\nBiais : ");
        System.out.println(vueNeurone.biais());

        // Affichage des sorties du neurone après apprentissage
        for (int i = 0; i < entrees.length; ++i)
        {
            final float[] entree = entrees[i];
            Sinus.metAJour(entree);
            System.out.println("Sortie neurone"+i+" : "+Sinus.sortie());
        }

        // Ajout de bruit aux entrées pour tester la robustesse du neurone
        float[][] bruit = new float[10][512];
        final float Perturbation = 1000f;
        for(int i = 0; i < 10; i++){
            for(int j=0 ; j < 512 ; j++){
                bruit[i][j] = Modules[i][j] + (float)Math.random()*Perturbation;
            }
        }

        // Affichage des sorties du neurone pour les entrées bruitées
        for (int i = 0; i < entrees.length; ++i)
        {
            final float[] entree = bruit[i];
            Sinus.metAJour(entree);
            System.out.println("Sortie neurone bruitée"+i+" : "+ Sinus.sortie());
        }
        System.out.println("Hello le monde!");
    }
}