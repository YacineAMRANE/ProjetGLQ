package iug;

import outils.Demande;

public interface IIUG {
		void eteindreTousBoutons();
		void allumerBouton(Demande d);
		void eteindreBouton(Demande t);
		void changerPosition(int position);
		void ajouterMessage(String message);
}
