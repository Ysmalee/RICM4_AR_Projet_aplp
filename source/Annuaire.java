import java.io.File;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Annuaire extends UnicastRemoteObject implements _Annuaire {
	/**
	 * Fichier contenant l'annuaire
	 */
	private File _dataFile;



	/**
	 * Création d'un annuaire
	 * @param dataFile Fichier xml contenant les informations des abonnés
	 * @throws RemoteException
	 */
	protected Annuaire(String dataFile) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	/**
	 * Retourne le numéro de l'abonné passé en parametre
	 */
	public Numero get(String abonne) throws RemoteException {
		Numero num = null;


		return num;
	}








	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Registry reg = null;
		String host = null;
		String dataFile = null;
		int port = 0;

		// récupération des arguments
		if (args.length!=3){
			System.out.println("Annuaire <data file> <registry host> <registry port>");
			System.exit(1);
		}
		try  {
			dataFile = args[0];
			host = args[1];
			port = Integer.parseInt(args[2]);
		}catch(Exception e) {
			System.out.println("Annuaire <data file> <registry host> <registry port>");
			System.exit(1);
		}


		//Création/Connexion de l'objet registry
		try {
			//reg = LocateRegistry.createRegistry(port);
			reg = LocateRegistry.getRegistry(host, port);
		} catch (RemoteException e) {
			System.out.println("Echec de la connexion au RMI\n" + e.toString());
			System.exit(1);
		}

		//Creation de l'objet distant
		_Annuaire a = null;
		try {
			a = new Annuaire(dataFile);
		} catch (RemoteException e) {
			System.out.println("[Creation de l'Annuaire] Remote exception:" + e.toString());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("[Creation de l'Annuaire] Exception:" + e.toString());
			System.exit(1);
		}

		//Enregistrement de l'objet
		try {
			reg.rebind("Annuaire", a);
		} catch (AccessException e) {
			System.out.println("[Enregistrement de l'Annuaire] Remote exception:" + e.toString());
			System.exit(1);
		} catch (RemoteException e) {
			System.out.println("[Enregistrement de l'Annuaire] Exception:" + e.toString());
			System.exit(1);
		}
	}

}
