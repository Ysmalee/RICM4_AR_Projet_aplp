package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.URL;

public class AgentInputStream extends ObjectInputStream{
	private ClassLoader loader;
	private AgentServer agentServer;
	
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
	
	public Agent readAgent() throws ClassNotFoundException, IOException{
		Jar j = (Jar) this.readObject();
		BAMLoader l = new BAMLoader(new URL[]{},loader);
		l.integrateCode(j);
		Agent agent = (Agent) this.readObject();
		agent.init(l, agentServer, agentServer.getName());
		return agent;
	}
	
	@Override
	protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
		return loader.loadClass(osc.getName());
	}
	
}
