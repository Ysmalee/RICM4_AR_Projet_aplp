package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarException;

public class BAMLoader extends URLClassLoader {
	
	Map<String,byte[]> cache = new HashMap<String,byte[]>();
	Jar ressources;
	
	/**
	 * Constructeurs 	
	 */
	public BAMLoader(){
		super(new URL[]{});
	}
	
	public BAMLoader(URL[] urls, ClassLoader parent){
		super(urls,parent);
	}
	
	/**
	 * Ajoute un repository au BAMLoader
	 */
	protected void addURL(URL url){
		super.addURL(url);
		try {
			Jar jar = new Jar(url.getFile());
			this.integrateCode(jar);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Récupère le jar contenant les ressources
	 * @return Jar
	 */
	public Jar extractCode(){
		return ressources;
	}
	
	/**
	 * Utilise la méthode de la super classe
	 * Si ClassNotFoundException, vérifie si la classe est dans le cache
	 */
	public Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			return super.findClass(ressources.formatClassName(name));
		} catch (ClassNotFoundException e) {
			byte[] classe = cache.get(ressources.formatClassName(name));
			if(classe==null){
				throw new ClassNotFoundException ("class "+classe+" not found.");
			}
			return this.defineClass(name,classe, 0, classe.length);
		}
	}
	
	/**
	 * Permet d'ajouter les classes d'un jar au cache
	 * @param jar
	 */
	public void integrateCode(Jar jar){
		for(Map.Entry<String,byte[]> e : jar.classIterator()){
			String name = e.getKey();
			byte[] classe = e.getValue();
			cache.put(name,classe);			
		}
	}
}
