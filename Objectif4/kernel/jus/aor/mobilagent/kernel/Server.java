/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.lang.reflect.Constructor;
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
	/** le port où sera ataché le service du bus à agent mobile. Par défaut on prendra le port 10140 */
	protected int port=10142;
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger=null;
	/**
	 * Démarre un serveur de type mobilagent 
	 * @param port le port d'écoute du serveur d'agent 
	 * @param name le nom du serveur
	 */
	public Server(final int port, final String name){
		System.out.println("Creation du serveur");
		this.name=name;
		try {
			this.port=port;
			/* mise en place du logger pour tracer l'application */
			loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			logger=Logger.getLogger(loggerName);
			/* démarrage du server d'agents mobiles attaché à cette machine */
			System.out.println("Création du serveur d'agent");
			agentServer = new AgentServer(port, name);
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
			System.out.println("Lancement du serveur d'agent");
			agentServer.start();
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
			//Instanciation d'un service
        	Class<?> classe = (Class<?>)Class.forName(classeName,true,this.getClass().getClassLoader());
        	_Service<?> service = (_Service<?>)classe.getConstructor(Object[].class).newInstance(args);
			
        	//Ajout du service
        	agentServer.addService(name, service);
        	
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
			BAMLoader agentLoader = new BAMLoader(new URL[]{},this.getClass().getClassLoader());
        	agentLoader.addURL(codeBase);
        	Class<?> classe = (Class<?>)Class.forName(classeName,true,agentLoader);
        	Constructor<?> c = classe.getConstructor(Object[].class);
        	Agent runningAgent = (Agent)c.newInstance(new Object[]{args});
        	
        	//Instanciation de l'agent
        	runningAgent.init(agentServer, name);
			
        	//Création de la route de l'agent
        	if (etapeAddress.size() == etapeAction.size()){
	        	for (int i=0;i<etapeAddress.size();i++){
	        		Field f = runningAgent.getClass().getDeclaredField(etapeAction.get(i));
	        		f.setAccessible(true);
	        		_Action a = (_Action) f.get(runningAgent);
	        		Etape e = new Etape(new URI(etapeAddress.get(i)),a);
	        		runningAgent.addEtape(e);
	        	}
        	} else {
        		throw new Exception("Les tailles des listes d'étapes et d'actions sont différentes");
        	}
        	
        	//Etape retour de l'agent
        	runningAgent.getRoute().retour = new Etape(agentServer.site(),_Action.NIHIL);
        	
        	//Déploiement de l'agent
        	System.out.println("Lancement de l'agent");
        	runningAgent.start();
        	
   		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
}
