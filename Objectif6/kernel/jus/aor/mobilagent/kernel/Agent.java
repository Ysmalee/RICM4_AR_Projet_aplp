package jus.aor.mobilagent.kernel;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class Agent extends Thread implements _Agent{
    
	private static final long serialVersionUID = 1L;
	protected transient AgentServer server;
    private Route route;
    
	/** Date de début */
    private Date debut;
  
    /**
     * Initialisation de l'AgentServer
     * @param server Serveur de l'agent
     * @param serverName nome du serveur
     */
    @Override
    public void init(AgentServer agentServer, String serverName){
    	this.server=agentServer;
        this.server.setName(serverName);
		try {
			if (route==null){
				debut = new Date();
				route = new Route(new Etape(new URI(serverName), _Action.NIHIL));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    }

    /**
     * Initialisation de l'agent lors de sa création
     * @param loader BAMLoader de l'agent
     * @param server Serveur de l'agent
     * @param serverName nome du serveur
     */
    @Override
    public void init(BAMLoader loader, AgentServer server, String serverName){
        this.init(server, serverName);  
    }
    
    /**
     * Ajout d'une étape à la route
     * @param etape nouvelle etape à ajouter à la route
     */    
    @Override
    public void addEtape(Etape etape){
        route.add(etape);
    }

    /**
     * Exécute l'action de l'étape courante de l'agent
     */
    @Override
    public void run() {
    	//Envoi de l'agent
    	try{
    		System.out.println("Debut de l'envoi de l'agent...");
    		//Création de la socket
    		Socket agentSocket = new Socket(route.get().server.getHost(), route.get().server.getPort());
    		
    		//Envoi du jar de l'agent
    		Jar jar = ((BAMLoader)getClass().getClassLoader()).extractCode();
    		ObjectOutputStream ous = new ObjectOutputStream(agentSocket.getOutputStream());
    		ous.writeObject(jar);
    		
    		//Envoi de l'agent
			ous.writeObject(this);
			
			//Fermeture des objets
			ous.close();
			agentSocket.close();
			
			System.out.println("Fin de l'envoi...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    } 
    
    /**
     * Getter de la route de l'agent
     * @return route
     */
    public Route getRoute(){
    	return route;
    }
    
    /**
     * Getter de l'agent serveur
     * @return agent serveur
     */
    public AgentServer getAgentServer(){
    	return this.server;
    }
    
    /**
     * Getter de l'étape retour de l'agent
     * @return etape retour
     */
    public Etape getEtapeRetour(){
    	return this.getRoute().retour;
    }
    
    /**
     * Retourne la date à laquelle le programme a commencé
     * @return date début
     */
    public Date getDateDebut(){
    	return debut;
    }
    
    public void retour(){}
}
