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
    
	private BAMLoader agentLoader;
    private AgentServer server;
    private Route route;
    protected Class<?> classe;
	private Socket agentSocket;
    
  
    /**
     * Initialisation de l'AgentServer
     * @param server Serveur de l'agent
     * @param serverName nome du serveur
     */
    @Override
    public void init(AgentServer agentServer, String serverName){
    	this.server=agentServer;
        this.server.setName(serverName);
    }

    /**
     * Initialisation de l'agent lors de sa création
     * @param loader BAMLoader de l'agent
     * @param server Serveur de l'agent
     * @param serverName nome du serveur
     */
    @Override
    public void init(BAMLoader loader, AgentServer server, String serverName){
        try {
            this.init(server, serverName);
			route = new Route(new Etape(new URI(serverName), _Action.NIHIL));
	        agentLoader=loader;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
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
    	} else {
    		route.retour.action.execute();
        	//Envoi de l'agent
    		sendAgent();
    	}
    }
   
    /**
     * Envoi de l'agent sur le prochain serveur
     */
    public void sendAgent(){
    	try{
    		agentSocket = new Socket(route.next().server.toString(), route.next().server.getPort());
			//ObjectInputStream ois = new ObjectInputStream(agentSocket.getInputStream());
			ObjectOutputStream ous = new ObjectOutputStream(agentSocket.getOutputStream());
			ous.writeObject(this);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
}
