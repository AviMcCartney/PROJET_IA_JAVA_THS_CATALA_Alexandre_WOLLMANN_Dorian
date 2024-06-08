package Support.neurone;

public class testNeurone
{
	public static void main(String[] args)
	{
		// Tableau des entrées de la fonction ET (0 = faux, 1 = vrai)
		final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};

		// Tableau des sorties de la fonction ET
		final float[] resultatsET = {0, 0, 0, 1};

		// Tableau des sorties de la fonction OU
		final float[] resultatsOU = {0, 1, 1, 1};

		// On crée un neurone taillé pour apprendre la fonction ET
		final iNeurone Heaviside_ET = new NeuroneHeaviside(entrees[0].length);

		// On crée un neurone taillé pour apprendre la fonction OU
		final iNeurone Heaviside_OU = new NeuroneHeaviside(entrees[0].length);

		//final iNeurone n = new NeuroneSigmoide(entrees[0].length);
		//final iNeurone n = new NeuroneReLU(entrees[0].length);

		// On affiche chaque cas appris
		System.out.println("\nSortie avant apprentissage pour les entrées correspondantes : ");
		for (int i = 0; i < entrees.length; ++i)
		{
			// Pour une entrée donnée
			final float[] entree = entrees[i];
			// On met à jour la sortie du neurone pour ET
			Heaviside_ET.metAJour(entree);
			// On affiche cette sortie pour ET
			System.out.println("----------APPRENTISSAGE ET ----------------");
			System.out.println("Entree "+i+" : " + "{" + entrees[i][0] + " , " + entrees[i][1] + "} " + "Sortie : " +Heaviside_ET.sortie());

			// On met à jour la sortie du neurone pour OU
			Heaviside_OU.metAJour(entree);
			// On affiche cette sortie pour OU
			System.out.println("----------APPRENTISSAGE OU ----------------");
			System.out.println("Entree "+i+" : " + "{" + entrees[i][0] + " , " + entrees[i][1] + "} " + "Sortie : " +Heaviside_OU.sortie());
		}

		System.out.println("Apprentissage…");
		// On lance l'apprentissage de la fonction ET sur ce neurone
		System.out.println("**Heaviside** Nombre de tours pour le ET: "+Heaviside_ET.apprentissage(entrees, resultatsET));
		// On lance l'apprentissage de la fonction OU sur ce neurone
		System.out.println("**Heaviside** Nombre de tours pour le OU: "+Heaviside_OU.apprentissage(entrees, resultatsOU));

		// On affiche les valeurs des synapses et du biais du neurone ET

		// Conversion dynamique d'une référence iNeurone vers une référence neurone.
		// Sans cette conversion on ne peut pas accéder à synapses() et biais()
		// à partir de la référence de type iNeurone
		// Cette conversion peut échouer si l'objet derrière la référence iNeurone
		// n'est pas de type neurone, ce qui n'est cependant pas le cas ici
		final Neurone vueNeuroneET = (Neurone)Heaviside_ET;
		System.out.print("Synapses ET : ");
		for (final float f : vueNeuroneET.synapses())
			System.out.print(f+" ");
		System.out.print("\nBiais ET : ");
		System.out.println(vueNeuroneET.biais());

		// On affiche les valeurs des synapses et du biais du neurone OU
		final Neurone vueNeuroneOU = (Neurone)Heaviside_OU;
		System.out.print("Synapses OU : ");
		for (final float f : vueNeuroneOU.synapses())
			System.out.print(f+" ");
		System.out.print("\nBiais OU : ");
		System.out.println(vueNeuroneOU.biais());

		// On affiche chaque cas appris
		System.out.println("\nSortie après apprentissage pour les entrées correspondantes : ");
		for (int i = 0; i < entrees.length; ++i)
		{
			// Pour une entrée donnée
			final float[] entree = entrees[i];
			// On met à jour la sortie du neurone pour ET
			Heaviside_ET.metAJour(entree);
			// On affiche cette sortie pour ET
			System.out.println("----------APPRENTISSAGE ET ----------------");
			System.out.println("Entree "+i+" : " + "{" + entrees[i][0] + " , " + entrees[i][1] + "} " + "Sortie : " +Heaviside_ET.sortie());

			// On met à jour la sortie du neurone pour OU
			Heaviside_OU.metAJour(entree);
			// On affiche cette sortie pour OU
			System.out.println("----------APPRENTISSAGE OU ----------------");
			System.out.println("Entree "+i+" : " + "{" + entrees[i][0] + " , " + entrees[i][1] + "} " + "Sortie : " +Heaviside_OU.sortie());
		}
		final float[] resultats = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		 //Boucle Apprentissage avec BRUIT
		int compteurEchecs = 0;
		int compteurEssai = 0;
		final float[][] entrees2 = {{0.3f, 0.1f},{0.15f, 0.9f},{0.7f, 0.2f},{1.2f, 1.1f}};
		for(int k = 0; k < 1000; k++){
			final iNeurone n = new NeuroneHeaviside(entrees[0].length);
			n.apprentissage(entrees, resultats);
			for(int i = 0; i < 4; i++){
				compteurEssai++;
				n.metAJour(entrees2[i]);
				if(Math.abs(resultats[i] - n.sortie()) > n.ToleranceSortie()){
					compteurEchecs++;
				}
//                System.out.println("Entrées : {" + entrees2[i][0] + ", " + entrees2[i][1] + "} -- Sortie : " + n.sortie());
			}
		}
		System.out.println("Nombre d'échec : " + compteurEchecs + " Nombre d'essai : " + compteurEssai);

		/* MOYENNE D'ECHEC
		float moyenne = 0;
		int nb_tour = 0;
		System.out.println("\nApprentissage…");
		for(int k = 0; k < 10; k++){
			final iNeurone n = new NeuroneHeaviside(entrees[0].length);
			Neurone vueNeurone = (Neurone)n;

			System.out.print("\n\nSynapses Avant : ");
			for (final float f : vueNeurone.synapses())
				System.out.print(f+" ");
			System.out.print("\nBiais Avant: ");
			System.out.println(vueNeurone.biais());


			nb_tour = n.apprentissage(entrees, resultats);
			moyenne += nb_tour;

			vueNeurone = (Neurone)n;
			System.out.print("Synapses Après: ");
			for (final float f : vueNeurone.synapses())
				System.out.print(f+" ");
			System.out.print("\nBiais Après: ");
			System.out.println(vueNeurone.biais());
		}
		moyenne = moyenne/1000;
		System.out.println("Moyenne des tours d'apprentissage : "+moyenne);*/
	}
}
