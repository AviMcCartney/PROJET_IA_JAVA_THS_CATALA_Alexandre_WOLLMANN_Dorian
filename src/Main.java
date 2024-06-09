import Support.FFT.Complexe;
import Support.FFT.ComplexeCartesien;
import Support.FFT.FFTCplx;
import Support.Son.Son;
import Support.neurone.iNeurone;
import Support.neurone.Neurone;
import Support.neurone.NeuroneHeaviside;

public class Main{

    public static float[] Normalisation(Complexe[] tab){
        float[] modules = new float[tab.length];
        float max = 0;
        for(int i = 0; i < tab.length; i++){
            modules[i] = (float) tab[i].mod();
            max = (modules[i] > max ? modules[i] : max);
        }

        for (int i = 0; i < tab.length; i++)
            modules[i] /= max;

        return modules;
    }


    public static void main(String[] args){
        Son sonSinus = new Son("Sources_sonores/Sinusoide.wav");
        Son sonBruit = new Son("Sources_sonores/Bruit.wav");
        Son sonCarre = new Son("Sources_sonores/Carre.wav");

        String[] ordre = {"Sinusoide", "Carre", "Bruit"};

        System.out.println("Lecture des fichiers WAV...");
        System.out.println("Fichier Sources_sonores/Sinus.wav : "+sonSinus.donnees().length+" échantillons à "+sonSinus.frequence()+"Hz");
        System.out.println("Fichier Sources_sonores/Carre.wav : "+sonCarre.donnees().length+" échantillons à "+sonCarre.frequence()+"Hz");
        System.out.println("Fichier Sources_sonores/Bruit.wav : "+sonBruit.donnees().length+" échantillons à "+sonBruit.frequence()+"Hz");


        float [][] donneesR = new float[30][512];
        for(int i = 0; i < 5; i++){
            donneesR[i] = sonSinus.bloc_deTaille(i+1,512);
            donneesR[i+5] = sonCarre.bloc_deTaille(i+1, 512);
            donneesR[i+10] = sonBruit.bloc_deTaille(i+1, 512);

            donneesR[i+15] = sonSinus.bloc_deTaille(i+5,512);
            donneesR[i+20] = sonCarre.bloc_deTaille(i+5, 512);
            donneesR[i+25] = sonBruit.bloc_deTaille(i+5, 512);
        }


        Complexe[][] donneesC = new Complexe[30][512];
        for(int i = 0; i < 30; i++){
            donneesC[i] = new Complexe[donneesR[i].length];
            for(int j = 0; j < 512; j++){
                donneesC[i][j] = new ComplexeCartesien(donneesR[i][j],0);
            }
        }

        Complexe[][] Signaux = new Complexe[30][512];
        for(int i = 0; i < 30; i++){
            Signaux[i] = FFTCplx.appliqueSur(donneesC[i]);
        }



        float[][] modules = new float[30][512];
        for(int i = 0; i < 30; i++){
            for(int j = 0; j < 512; j++){
                modules[i] = Normalisation(Signaux[i]);
            }
        }


        final float[][] entrees1 = new float[15][512];
        final float[][] entrees2 = new float[15][512];
        for(int i = 0; i < 15; i++){
            for (int j = 0; j < 512; j++){
                entrees1[i][j] = modules[i][j];
                entrees2[i][j] = modules[i+15][j];
            }
        }


        // for(int i = 0; i < 30; i++){
        //     for(int j=0 ; j < 512; j++){
        //         System.out.println("Entree["+i+"]["+j+"]: "+ entrees1[i][j]);
        //     }
        // }


        final float[] resultatsSin = {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        // final float[] resultatsSin2 = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        // final float[] resultatsSinH = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        // final float[] resultatsCombinaison = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        final float[] resultatsCarre = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0};
        final float[] resultatsBruit = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
        //final float[][] resultats = {resultatsSin, resultatsSin2, resultatsSinH, resultatsCombinaison, resultatsCarre, resultatsBruit};
        final float[][] resultats = {resultatsSin, resultatsCarre, resultatsBruit};

        final iNeurone sinus = new NeuroneHeaviside(entrees1[0].length);
        // final iNeurone sinus2 = new NeuroneHeaviside(entrees1[0].length);
        // final iNeurone sinusH = new NeuroneHeaviside(entrees1[0].length);
        // final iNeurone combinaison = new NeuroneHeaviside(entrees1[0].length);
        final iNeurone carre = new NeuroneHeaviside(entrees1[0].length);
        final iNeurone bruit = new NeuroneHeaviside(entrees1[0].length);
        //final iNeurone[] neurones = {sinus, sinus2, sinusH, Combinaison, Carre, Bruit};
        final iNeurone[] neurones = {sinus, carre, bruit};



        System.out.println("Apprentissage...");
        for(int i = 0; i < 3; i++)
            System.out.println("Nombre de tours du neurone " + ordre[i] + ": "+neurones[i].apprentissage(entrees1, resultats[i]));


        Neurone vueNeurone;
        for(int i = 0; i < 3; i++){
            vueNeurone = (Neurone)neurones[i];
            System.out.print("Synapses : ");
            for (final float f : vueNeurone.synapses())
                System.out.print(f+" ");

            System.out.print("\nBiais : ");
            System.out.println(vueNeurone.biais());
        }


        for(int i = 0; i < 3; i++){
            for(int j = 0; j < entrees2.length; j++){
                final float[] entree = entrees2[j];
                neurones[i].metAJour(entree);
                System.out.println("Entree ("+ordre[i]+") "+j+" : "+neurones[i].sortie());
            }
        }


        float[][] bruits = new float[15][512];
        final float perturbation = 10000.f;
        for(int i = 0; i < modules.length; i++){
            for(int j=0 ; j < 512 ; j++){
                bruits[0][j] = modules[i][j] + (float)Math.random()*perturbation;
            }
        }


        for(int i = 0; i < 3; i++){
            for (int j = 0; j < entrees1.length; j++){
                final float[] entree = bruits[j];
                sinus.metAJour(entree);
                System.out.println("Entree bruitée ("+ordre[i]+") "+j+" : "+ neurones[i].sortie());
            }
        }
    }
}