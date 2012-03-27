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
	
	/** 	 */
	Map<String,_Service<?>> _services = new HashMap<String,_Service<?>>();
	
    public AgentServer(int port){
        this._port=port;
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
     * Lancement du server d'agents
     */
    public void run(){

    }
    
    /**
     * Boucle de réception des agents
     */
    public void runAgent(){
		try {
			ServerSocket ss = new ServerSocket(_port);
			while (true){
    			Socket socket = ss.accept();
    			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
    			ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
    			
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void site(){
    	
    }
    
    /**
     * méthode toString
     */
    public String toString(){
    	return this.toString();
    }
}
