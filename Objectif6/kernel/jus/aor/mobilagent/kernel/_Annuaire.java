package jus.aor.mobilagent.kernel;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface _Annuaire extends Remote {
	
	
	/**
	 * Enregistre un service disponible sur un serveur
	 * @param serviceName Nom du service à enregistrer
	 * @param server Adresse du serveur (mobileagen://host:port
	 */
	public void registerService(String serviceName, URI server) throws RemoteException;
	
	
	
	/**
	 * Supprime un service d'un serveur
	 * @param serviceName Nom du service à enregistrer
	 * @param server Adresse du serveur (mobileagen://host:port
	 */
	public void removeService(String serviceName, URI server) throws RemoteException;
	
	
	
	/**
	 * Retourne les serveurs proposant le service passé en paramètre
	 * @param serviceName Nom du service cherché
	 * @return Liste des serveurs proposant le service
	 * @throws RemoteException
	 */
	public List<URI> getServers(String serviceName) throws RemoteException;
	
	
}