package jus.aor.mobilagent.kernel;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Agent extends Thread implements _Agent{
    
    private transient AgentServer server;
    private Route route;
    
  
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
			route = new Route(new Etape(new URI(serverName), _Action.NIHIL));
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
    	//L'agent exécute son action courante
    	if (route.hasNext){
    		route.get().action.execute();
    		route.next();
    	} else {
    		route.retour.action.execute();
    	}
    	//Envoi de l'agent
		sendAgent();
    }
   
    /**
     * Envoi de l'agent sur le prochain serveur
     */
    public void sendAgent(){
    	try{
    		System.out.println("Debut du sendAgent()");
    		//Création de la socket
    		Socket agentSocket = new Socket(route.get().server.getHost(), route.get().server.getPort());
    		
    		//Envoi du jar de l'agent
    		Jar jar = ((BAMLoader) getClass().getClassLoader()).extractCode();
    		ObjectOutputStream ous = new ObjectOutputStream(agentSocket.getOutputStream());
    		ous.writeObject(jar);
    		
    		//Envoi de l'agent
			ous.writeObject(this);
			
			//Fermeture de la socket
			agentSocket.close();
			
			System.out.println("Fin du sendAgent()");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
}
