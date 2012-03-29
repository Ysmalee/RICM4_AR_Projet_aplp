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
	private Parser_Hotel _parser;

	
	/**
	 * Cr��e un objet chaine qui sera enregistr�� dans le registry
	 * @param name Nom de la chaine
	 * @param dataFile Fichier contenant les informations sur les hotels
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Chaine(String name, String dataFile) throws Exception, RemoteException {
		this._data = new File(dataFile);
		if( ! this._data.exists()) {
			throw new Exception("Data file does not exist : \n" + dataFile);
		}
		this._name = name;
		this._parser = new Parser_Hotel(_data);
	}



	@Override
	/**
	 * Retourne la liste des hotels de la chaine correspondant �� la localisation
	 */
	public ArrayList<Hotel> get(String localisation) throws RemoteException {
		System.out.println("Req: " + localisation);
		ArrayList<Hotel> listRes = new ArrayList<Hotel>(_parser.get_hotels_from_xml(localisation));
		return listRes;
	}
	
	
	
	
	
	
	
	/**
	 * Fonction permettant de cr��er une nouvelle chaine d'hotel
	 * puis de l'enregistrer dans le registry
	 * @param args
	 */
	public static void main(String[] args) {
		Registry reg = null;
		String host = null;
		String name = null;
		String dataFile = null;
		int port = 0;

		// r��cup��ration des arguments
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

		
		
		//Cr��ation/Connexion de l'objet registry
		System.out.print("Connexion au registry");
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
		System.out.print("Création de l'objet chaine [" + name + "]" + " ... ");
		_Chaine c = null;
		try {
			c = new Chaine(name, dataFile);
		} catch (RemoteException e) {
			System.out.println("Erreur");
			System.out.println("[Creation de la Chaine] Remote exception:" + e.toString());
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Erreur");
			System.out.println("[Creation de la Chaine] Exception:" + e.toString());
			System.exit(1);
		}
		System.out.println("OK");
		
		
		
		//Enregistrement de l'objet
		System.out.print("Enregistrement de l'objet dans le registry ... ");
		try {
			reg.rebind(name, c);
		} catch (AccessException e) {
			System.out.println("Erreur");
			System.out.println("[Enregistrement de la Chaine] Remote exception:" + e.toString());
			System.exit(1);
		} catch (RemoteException e) {
			System.out.println("Erreur");
			System.out.println("[Enregistrement de la Chaine] Exception:" + e.toString());
			System.exit(1);
		}
		System.out.println("OK");
	}
	

}
