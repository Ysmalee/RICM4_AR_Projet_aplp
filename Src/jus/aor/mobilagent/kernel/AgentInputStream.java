package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.ServerSocket;

public class AgentInputStream extends ObjectInputStream{
	private BAMLoader loader;
	
	/**
	 * Constructeur
	 * @param i
	 * @throws IOException
	 */
	public AgentInputStream(InputStream i, BAMLoader b) throws IOException{
		super(i);
		this.loader = b;
	}
	
	@Override
	protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
		return loader.loadClass(osc.getName());
	}
}
