package jus.aor.mobilagent.kernel;

import java.net.URI;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Annuaire extends UnicastRemoteObject implements _Annuaire {
	private static final long serialVersionUID = -8728110123540730507L;

	
	/**
	 * Structure de données contenant les serveurs proposant le service clé
	 */
	private Map<String, List<URI>> _servicesDispo;
	private Semaphore _mutexServiceDispo;
	
	
	
	/**
	 * Crée le serveur d'annuaire
	 * @throws RemoteException
	 */
	public Annuaire() throws RemoteException{
		this._servicesDispo = new HashMap<String, List<URI>>();
		this._mutexServiceDispo = new Semaphore(1);
	}
	

	@Override
	public void registerService(String serviceName, URI server) throws RemoteException {
		this._mutexServiceDispo.acquireUninterruptibly();
		
		//Récupère la liste des serveurs déjà enregistrés
		List<URI> listeServeurs = null;
		if(this._servicesDispo.containsKey(serviceName)) {
			listeServeurs = this._servicesDispo.get(serviceName);
			this._servicesDispo.remove(serviceName);
		} else {
			listeServeurs = new ArrayList<URI>();
		}
		
		//Incrémente la liste
		if( ! listeServeurs.contains(server)) {
			listeServeurs.add(server);
		}
		
		//Replace la liste dans la map
		this._servicesDispo.put(serviceName, listeServeurs);
		
		this._mutexServiceDispo.release();
	}

	
	@Override
	public void removeService(String serviceName, URI server) throws RemoteException {
		this._mutexServiceDispo.acquireUninterruptibly();
		
		List<URI> listeServeurs = null;
		if(this._servicesDispo.containsKey(serviceName)) {
			listeServeurs = this._servicesDispo.get(serviceName);
			this._servicesDispo.remove(serviceName);
			
			//Supprime le serveur de la liste
			listeServeurs.remove(server);
			
			//Replace la liste dans la map
			if( ! listeServeurs.isEmpty()) {
				this._servicesDispo.put(serviceName, listeServeurs);
			}
		}
		
		this._mutexServiceDispo.release();
	}
	

	@Override
	public List<URI> getServers(String serviceName) throws RemoteException {
		this._mutexServiceDispo.acquireUninterruptibly();
		
		List<URI> ret = null;
		if( this._servicesDispo.containsKey(serviceName)) {
			ret = this._servicesDispo.get(serviceName);
		} else {
			ret = new ArrayList<URI>();
		}
		
		this._mutexServiceDispo.release();
		return ret;
	}

	
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Registry reg = null;
		int port = 0;

		// récupération des arguments
		if (args.length!=1){
			System.out.println("Annuaire <registry port>");
			System.exit(1);
		}
		try  {
			port = Integer.parseInt(args[0]);
		}catch(Exception e) {
			System.out.println("Annuaire <registry port>");
			System.exit(1);
		}


		
		//Création/Connexion de l'objet registry
		System.out.print("Création du registry ... ");
		try {
			//reg = LocateRegistry.createRegistry(port);
			reg = LocateRegistry.createRegistry(port);
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
			a = new Annuaire();
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
		
		
		while(true){}
	}


}
