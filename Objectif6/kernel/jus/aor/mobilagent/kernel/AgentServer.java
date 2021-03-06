package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



public class AgentServer extends Thread{
	//RMI
	private Registry _rmiReg;
	
	//Serveur
	/** port du serveur */
	private int _port;
	/** Nom du serveur */
	private String nameServer;
	/** Socket de l'agent */
	private Socket agentSocket;
	/** Date de fin*/
	private Date fin;
    
	/** Map contenant les différents sesrvices offerts par le serveur */
	Map<String,_Service<?>> _services = new HashMap<String,_Service<?>>();
	
	/**Constructeur*/
    public AgentServer(int port, String name, String rmi_hostname, int rmi_port){
        this._port=port;
        this.nameServer=name;

        //Connexion au RMI registry
        try {
			this._rmiReg = LocateRegistry.getRegistry(rmi_hostname, rmi_port);
		} catch (RemoteException e) {
			try {
				Logger logger=Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.nameServer);
				logger.log(Level.WARNING,"Impossible de contacter le RMI registry", e);
			} catch (UnknownHostException ex) {};
			this._rmiReg = null;
		}
    
    }
    /**Constructeur*/
    public AgentServer(int port, String name){
        this(port, name, "localhost", 1099);
    }
    
    /**
     * Ajout d'un service
	 * @param name nom du service
	 * @param service service à ajouter
     */
    public void addService(String name, _Service<?> service) {
    	_services.put(name, service);
    	
    	//Inscription dans l'annuaire
    	if(this._rmiReg != null) {
    		jus.aor.mobilagent.kernel._Annuaire annu = null;
    		try {
    			Remote r = this._rmiReg.lookup("Annuaire");
    			annu = (jus.aor.mobilagent.kernel._Annuaire) r;
    		} catch(Exception e) {
    			try {
    				Logger logger=Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.nameServer);
    				logger.log(Level.WARNING,"Impossible de récupérer l'annuaire distant", e);
    			} catch (UnknownHostException ex) {};
    		}
    		if(annu != null) {
	    		try {
	    			annu.registerService(name, this.site());
	    		} catch(Exception e) {
	    			try {
	    				Logger logger=Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.nameServer);
	    				logger.log(Level.WARNING,"Impossible d'enregistrer le service", e);
	    			} catch (UnknownHostException ex) {};
	    		}
    		}
    	}
	}
    
    
    
    /**
     * Interroge l'annuaire sur le contenu des serveurs
     * @param service Service recherché
     */
    public List<URI> askAnnuaire(String service) {
    	//Récupération de la reférence vers l'annuaire
		_Annuaire annu = null;
		try {
			annu = (_Annuaire) this._rmiReg.lookup("Annuaire");
		} catch(Exception e) {
			try {
				Logger logger=Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.nameServer);
				logger.log(Level.FINE,"Impossible de récupérer la réference de l'annuaire", e);
			} catch (UnknownHostException ex) {};
			return null;
		}
		
		//Récupération des serveurs d'hotels
		List<URI> listeServices = null;
		try {
			listeServices = annu.getServers(service);
		} catch (RemoteException e) {
			try {
				Logger logger=Logger.getLogger("jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.nameServer);
				logger.log(Level.FINE,"Impossible d'effectuer la demande à l'annuaire", e);
			} catch (UnknownHostException ex) {};
			return null;
		}
		
		return listeServices;
		
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
				if (agentRecu.getRoute().isFinished()){
					fin=new Date();
					agentRecu.retour();
					long duree = fin.getTime()-agentRecu.getDateDebut().getTime();
					System.out.println("La durée totale d'exécution du programme est de : "+duree+" ms.");
					System.out.println("Exécution de l'agent terminée !!!");
				} else {
					agentRecu.getRoute().get().action.execute();
			   		agentRecu.getRoute().next();
			   		
					//Lancement de l'agent
					Thread temp = new Thread(agentRecu);
					temp.start();
				}
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
     * @throws UnknownHostException 
	 */
    public URI site() throws UnknownHostException, URISyntaxException{
		return new URI("mobilagent://"+InetAddress.getLocalHost().getHostName()+":"+Integer.toString(_port));
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
    
    /**
     * Getter du nom du serveur
     * @return nom du serveur
     */
    public String getNameServer(){
    	return this.nameServer;
    }  
}
