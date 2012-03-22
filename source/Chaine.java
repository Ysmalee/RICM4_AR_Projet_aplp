import java.io.File;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class Chaine extends UnicastRemoteObject implements _Chaine {
	private static final long serialVersionUID = -8698336868315052019L;

	private File _data;
	private String _name;

	
	/**
	 * Crée un objet chaine qui sera enregistré dans le registry
	 * @param name Nom de la chaine
	 * @param dataFile Fichier contenant les informations sur les hotels
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Chaine(String name, String dataFile) throws Exception, RemoteException {
		this._data = new File(dataFile);
		if( ! this._data.exists()) {
			throw new Exception("Data file does not exist");
		}
		this._name = name;
	}



	@Override
	/**
	 * Retourne la liste des hotels de la chaine correspondant à la localisation
	 */
	public List<Hotel> get(String localisation) throws RemoteException {
		List<Hotel> listRes = new ArrayList<Hotel>();

		//Lecture de la source d'info



		return listRes;
	}
	
	
	
	
	
	
	
	/**
	 * Fonction permettant de créer une nouvelle chaine d'hotel
	 * puis de l'enregistrer dans le registry
	 * @param args
	 */
	public static void main(String[] args) {
		Registry reg = null;
		String host = null;
		String name = null;
		String dataFile = null;
		int port = 0;

		// récupération des arguments
		if (args.length!=4){
			System.out.println("Chaine <hotel name> <data file> <registry host> <registry port>");
			System.exit(1);
		}
		try  {
			name = args[0];
			dataFile = args[1];
			host = args[2];
			port = Integer.parseInt(args[3]);
		}catch(Exception e) {
			System.out.println("Chaine <hotel name> <data file> <registry host> <registry port>");
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
		_Chaine c = null;
		try {
			c = new Chaine(name, dataFile);
		} catch (RemoteException e) {
			System.out.println("[Creation de la Chaine] Remote exception:" + e.toString());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("[Creation de la Chaine] Exception:" + e.toString());
			System.exit(1);
		}
		
		//Enregistrement de l'objet
		try {
			reg.rebind(name, c);
		} catch (AccessException e) {
			System.out.println("[Enregistrement de la Chaine] Remote exception:" + e.toString());
			System.exit(1);
		} catch (RemoteException e) {
			System.out.println("[Enregistrement de la Chaine] Exception:" + e.toString());
			System.exit(1);
		}
	}
	

}
