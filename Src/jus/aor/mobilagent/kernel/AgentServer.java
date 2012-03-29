package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.server.SocketSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AgentServer extends Thread{
	
	/** port du serveur */
	private int _port;
	/** Socket du serveur */
	private ServerSocket ss;
	/** Socket de l'agent */
	private Socket agentSocket;
	
	
	/**Jar contenant les services */
	Jar services;
	/** Map contenant les différents sesrvices offerts par le serveur */
	Map<String,_Service<?>> _services = new HashMap<String,_Service<?>>();
	
	/**Constructeur*/
    public AgentServer(int port){
        this._port=port;
        try {
			ss = new ServerSocket(_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Ajout d'un service
	 * @param name nom du service
	 * @param service service à ajouter
     */
    public void addService(String name, _Service<?> service) {
    	_services.put(name, service);
	}
    
    /**
     * Retourne un service
     * @param name nom du service
     */
    public _Service<?> getService(String name){
    	_Service<?> service = _services.get(name);
    	return service;
    }
    
    /**
     * Boucle de réception des agents
     */
    public void run(){
    	//Attente d'un agent
    	while (true){
			System.out.println("Waiting for agent...");
			try {
				agentSocket = ss.accept();
				
				//Récupération du jar de l'agent
				Jar jar = (Jar)(new ObjectInputStream(agentSocket.getInputStream())).readObject();
				BAMLoader agentLoader = new BAMLoader();
				agentLoader.integrateCode(jar);
				
				//Récupération de l'agent
				AgentInputStream ais = new AgentInputStream(agentSocket.getInputStream(), agentLoader);
				_Agent agentRecu = (_Agent) ais.readObject();
				
				//Lancement de l'agent
				Thread temp = new Thread(agentRecu);
				temp.start();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Agent " + agentSocket.getInetAddress() + " arrived");
		}
    }
    
    /**
	 * Restitue l'URI de ce serveur qui est de la forme : "mobilagent://<host>:<port>"
	 * ou null si cette opération échoue.
	 * @return l'URI du serveur
     * @throws URISyntaxException 
	 */
    public URI site() throws URISyntaxException{
    	if (this.getName()!=null){
			return new URI("mobilagent://"+this.getName()+":"+Integer.toString(_port));
    	} else {
    		return null;
    	}
    }
    
    /**
     * méthode toString
     */
    public String toString(){
    	return toString();
    }
    
    /**
     * Getter pour récupérer le port du serveur
     * @return port du serveur
     */
    public int getPort(){
    	return _port;
    }
}
