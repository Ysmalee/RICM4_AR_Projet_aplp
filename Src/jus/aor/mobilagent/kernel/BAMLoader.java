package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

public class BAMLoader extends URLClassLoader {
	
	private static URL[] _urls;
	
	/*
	 * Constructeur
	 */
	public BAMLoader(){
		super(_urls);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.net.URLClassLoader#addURL(java.net.URL)
	 */
	protected void addURL(URL url){
		super.addURL(url);
	}
	
}
