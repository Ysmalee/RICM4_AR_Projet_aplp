import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Représente un client effectuant une requête lui permettant d'obtenir les numéro de téléphones des hôtels répondant à son critère de choix.
 * @author  Morat
 */
public class LookForHotel{
	/**
	 * le critère de localisaton choisi
	 */
	private String _localisation;
	
	/**
	 * RMI Registry
	 */
	private Registry _reg;
	
	
	
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String loc, String host, int port){ 
		//Emplacement à rechercher
		this._localisation = loc;
		
		//Création/Connexion de l'objet registry
		try {
			this._reg = LocateRegistry.getRegistry(host, port);
		} catch (RemoteException e) {
			System.out.println("Echec de la connexion au RMI\n" + e.toString());
			System.exit(1);
		}
	}
	
	
	/**
	 * réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws Exception 
	 */
	public long call() throws Exception {
		String[] tabChaine = new String[4];
		tabChaine[0] = "Hotel1";
		tabChaine[1] = "Hotel2";
		tabChaine[2] = "Hotel3";
		tabChaine[3] = "Hotel4";
		
		List<Hotel> listeHotels = new ArrayList<Hotel>();

		System.out.println("Début de la requête: " + this._localisation);
		Date debut = new Date();
		
		//Récupère la liste des hotels
		for(int i=0; i<4; i++) {
			//Récupère l'objet
			_Chaine c = null;
			try {
				c = (_Chaine) this._reg.lookup(tabChaine[i]);
			} catch(Exception e) {
				throw new Exception("erreur lors de la communication avec le RMI registry", e);
			}

			//Concataination
			if(c != null) {
				try {
					listeHotels.addAll(c.get(this._localisation));
				} catch (RemoteException e) {
					throw new Exception("erreur lors de la communication avec le serveur " + tabChaine[i], e);
				}
			}
		}
		
		//Appel l'annuaire
		_Annuaire a = null;
		try {
			a = (_Annuaire) this._reg.lookup("Annuaire");
		} catch(Exception e) {
			throw new Exception("erreur lors de la communication avec le RMI registry", e);
		}
		List<Numero> listeNum = new ArrayList<Numero>();
		for(int i=0; i<listeHotels.size(); i++) {
			Numero n = a.get(listeHotels.get(i).getName());
			listeNum.add(n);
		}
		
		Date fin = new Date();
		System.out.println("Requête terminée: " + listeHotels.size() + " élements en " + (fin.getTime() - debut.getTime()) + "ms");
		
		return 0;
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LookForHotel obj = new LookForHotel("Paris", "localhost", 1099);
		try {
			obj.call();
		} catch (Exception e) {
			System.out.println("Impossible d'executer la requête\n" + e.toString());
		}
	}
	
}
