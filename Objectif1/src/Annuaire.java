import java.io.File;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Annuaire extends UnicastRemoteObject implements _Annuaire {
	private static final long serialVersionUID = -7301547717991000393L;
	
	/**
	 * Fichier contenant l'annuaire
	 */
	private File _dataFile;
	private Parser_Annuaire _parser;



	/**
	 * Cr��ation d'un annuaire
	 * @param dataFile Fichier xml contenant les informations des abonn��s
	 * @throws Exception 
	 */
	protected Annuaire(String dataFile) throws Exception {
		super();
		this._dataFile = new File(dataFile);
		if( ! this._dataFile.exists()) {
			throw new Exception("Data file does not exist");
		}
		this._parser = new Parser_Annuaire(this._dataFile);
	}


	@Override
	/**
	 * Retourne le num��ro de l'abonn�� pass�� en parametre
	 */
	public Numero get(String abonne) throws RemoteException {
		String s = "Req : " + abonne;
		Numero num = _parser.get_numero_from_xml(abonne);
		s += num.toString();
		System.out.println(s);
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

		// r��cup��ration des arguments
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


		
		//Cr��ation/Connexion de l'objet registry
		System.out.print("Connexion au registry ... ");
		try {
			//reg = LocateRegistry.createRegistry(port);
			reg = LocateRegistry.getRegistry(host, port);
		} catch (RemoteException e) {
			System.out.println("Erreur");
			System.out.println("Echec de la connexion au RMI\n" + e.toString());
			System.exit(1);
		}
		System.out.println("OK");
		

		
		//Creation de l'objet distant
		System.out.print("Création de l'annuaire ...");
		_Annuaire a = null;
		try {
			a = new Annuaire(dataFile);
		} catch (RemoteException e) {
			System.out.println("Erreur");
			System.out.println("[Creation de l'Annuaire] Remote exception:" + e.toString());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Erreur");
			System.out.println("[Creation de l'Annuaire] Exception:" + e.toString());
			System.exit(1);
		}
		System.out.println("OK");
		

		
		//Enregistrement de l'objet
		System.out.print("Inscription dans le registry ... ");
		try {
			reg.rebind("Annuaire", a);
		} catch (AccessException e) {
			System.out.println("Erreur");
			System.out.println("[Enregistrement de l'Annuaire] Remote exception:" + e.toString());
			System.exit(1);
		} catch (RemoteException e) {
			System.out.println("Erreur");
			System.out.println("[Enregistrement de l'Annuaire] Exception:" + e.toString());
			System.exit(1);
		}
		System.out.println("OK");
	}

}
