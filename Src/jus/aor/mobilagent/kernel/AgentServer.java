package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AgentServer extends Thread{
	
	/** port du serveur */
	private int _port;
	/** Nom du serveur */
	private String nameServer;
	/** Socket de l'agent */
	private Socket agentSocket;
	
	
	
	/**Jar contenant les services */
	Jar services;
	/** Map contenant les différents sesrvices offerts par le serveur */
	Map<String,_Service<?>> _services = new HashMap<String,_Service<?>>();
	
	/**Constructeur*/
    public AgentServer(int port, String name){
        this._port=port;
        this.nameServer=name;
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
    	ServerSocket ss;
    	try{
    		ss = new ServerSocket(_port);
	    	//Attente d'un agent
    		System.out.println("Waiting for agent...");
    		while (true){
				agentSocket = ss.accept();
				
				//Récupération de l'agent
				System.err.println("Récupération de l'agent...");
				InputStream is = agentSocket.getInputStream();
				AgentInputStream ais = new AgentInputStream(is,this.getClass().getClassLoader(),this);
				Agent agentRecu = ais.readAgent();
				System.err.println("Agent récupéré...");
				
		   		//Fermeture de l'AgentInputStream
		   		ais.close();
				
		    	//L'agent exécute son action courante
		   		agentRecu.getRoute().get().action.execute();
		   		agentRecu.getRoute().next();
				System.out.println("Agent " + agentRecu.getName() + " is running.");
		   		
				//Lancement de l'agent
				Thread temp = new Thread(agentRecu);
				temp.start();	
			}	
    	}catch (IOException e) {
			e.printStackTrace();
			try {
				Logger logger=Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.nameServer);
				logger.log(Level.FINE,"AgentServer - run : ", e);
			} catch (UnknownHostException ex) {};
			
		} catch (ClassNotFoundException e) {
			try {
				Logger logger=Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.nameServer);
				logger.log(Level.FINE,"AgentServer - run : ", e);
			} catch (UnknownHostException ex) {};		}
		
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



////Récupération du jar de l'agent
//System.err.println("Récupération du jar de l'agent...");
//ObjectInputStream ois = new ObjectInputStream(agentSocket.getInputStream());
//Jar jar = (Jar)ois.readObject();
//BAMLoader agentLoader = new BAMLoader();
//agentLoader.integrateCode(jar);
//System.err.println("Jar récupéré...");