package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.URL;

public class AgentInputStream extends ObjectInputStream{
	private ClassLoader loader;
	private AgentServer agentServer;
	private BAMLoader bam;
	
	/**
	 * Constructeur
	 * @param i
	 * @throws IOException
	 */
	public AgentInputStream(InputStream i, ClassLoader loader, AgentServer agentServer) throws IOException {
		super(i);
		this.loader = loader;
		this.agentServer = agentServer;
	}
	
	/**
	 * Récupération de l'agent envoyé
	 * @return agent reçu
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Agent readAgent() throws ClassNotFoundException, IOException{
		Jar j = (Jar) this.readObject();
		bam = new BAMLoader(new URL[]{},loader);
		bam.integrateCode(j);
		Agent agent = (Agent) this.readObject();
		agent.init(bam, agentServer, agentServer.getNameServer());
		return agent;
	}
	
	@Override
	protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
		try{
			return bam.loadClass(osc.getName());
		} catch (Exception e) {
			return super.resolveClass(osc);
		}
	}
	
}
