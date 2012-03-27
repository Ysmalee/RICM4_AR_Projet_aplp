package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.SocketSecurityException;
import java.util.HashMap;
import java.util.Map;

public class AgentServer extends Thread{
	
	/** port du serveur */
	private int _port;
	/** Socket du serveur */
	private ServerSocket ss;
	/** Socket de l'agent */
	private Socket agentSocket;
	
	/** 	 */
	Map<String,_Service<?>> _services = new HashMap<String,_Service<?>>();
	
    public AgentServer(int port){
        this._port=port;
        try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Ajout d'un service
	 * @param name nom du service
	 * @param classeName classe du service
	 * @param codeBase codebase du service
	 * @param args arguments de construction du service
     */
    void addService(String name, String classeName, String codeBase, Object[] args) {
        
    }
    
    /**
     * Retourne un service
     */
//    public _Service getService(){
//        
//    }
    
    /**
     * Boucle de réception des agents
     */
    public void run(){
    	//Attente d'un agent
    	while (true){
			System.out.println("Waiting for agent...");
			try {
				agentSocket = ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Agent " + agentSocket.getInetAddress() + " arrived");
		}
    }
    
    /**
     * @throws IOException 
     */
    public void runAgent() throws IOException{
			ObjectInputStream ois = new ObjectInputStream(agentSocket.getInputStream());
			ObjectOutputStream ous = new ObjectOutputStream(agentSocket.getOutputStream());
    }
    
    public void site(){
    	
    }
    
    /**
     * méthode toString
     */
    public String toString(){
    	return this.toString();
    }
    
    /**
     * Getter pour récupérer le port du serveur
     * @return port du serveur
     */
    public int getPort(){
    	return _port;
    }
}
