package jus.aor.mobilagent.kernel;

public class AgentServer extends Thread{
	
	private int _port;
	
    public AgentServer(int port){
        this._port=port;
    }
    
    /**
     * ajout d'un service
     */
    void addService(String name, String classeName, String codeBase, Object[] args) {
        throw new UnsupportedOperationException("Not yet implemented");
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
    public void run()
    {
        while (true)
        {
            //Recevoir les agents  
        }
    }
}
