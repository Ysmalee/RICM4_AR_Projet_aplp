/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 * @author     Morat
 */
public final class Server {
	/** le nom logique du serveur */
	protected String name;
	/** le port où sera ataché le service du bus à agent mobile. Pafr défaut on prendra le port 10140 */
	protected int port=10140;
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger=null;
	/**
	 * Démarre un serveur de type mobilagent 
	 * @param port le port d'écuote du serveur d'agent 
	 * @param name le nom du serveur
	 */
	public Server(final int port, final String name){
		System.err.println("Creation serveur");
		this.name=name;
		try {
			this.port=port;
			/* mise en place du logger pour tracer l'application */
			loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			logger=Logger.getLogger(loggerName);
			/* démarrage du server d'agents mobiles attaché à cette machine */
			agentServer = new AgentServer(port);
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * Ajoute le service caractérisé par les arguments
	 * @param name nom du service
	 * @param classeName classe du service
	 * @param codeBase codebase du service
	 * @param args arguments de construction du service
	 */
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			agentServer.addService(name, classeName, codeBase, args);
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * deploie l'agent caractérisé par les arguments sur le serveur
	 * @param classeName classe du service
	 * @param args arguments de construction de l'agent
	 * @param codeBase codebase du service
	 * @param etapeAddress la liste des adresse des étapes
	 * @param etapeAction la liste ds actions des étapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress, List<String> etapeAction) {
		try {
        	
			//Création de l'agent
			BAMLoader agentLoader = new BAMLoader();
        	agentLoader.addURL(new URL(codeBase));
        	Class<?> classe = (Class<?>)Class.forName(classeName,true,agentLoader);
        	Agent agentRunning = (Agent)classe.getConstructor(int.class,String.class).newInstance(args);
        	
        	//Instanciation de l'agent
        	agentRunning.init(agentLoader, agentServer, name);
        	
        	//Création de la route de l'agent
        	if (etapeAddress.size() == etapeAction.size()){
	        	for (int i=0;i<etapeAddress.size();i++){
	        		_Action a = (_Action) getClass().getDeclaredField(etapeAction.get(i)).get(null);
	        		Etape e = new Etape(new URI(etapeAddress.get(i)),a);
	        		agentRunning.addEtape(e);
	        	}
        	} else {
        		throw new Exception("Les tailles des listes d'étapes et d'actions sont différentes");
        	}
        	
   		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
}
