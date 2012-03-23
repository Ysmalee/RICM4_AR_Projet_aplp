package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class BAMLoader extends URLClassLoader {
	
	Map<String,Class<?>> cache = new HashMap<String,Class<?>>();
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
	
	public BAMLoader(String jar){
		super(null);		
	}
	
	/**
	 * Ajoute un repository au BAMLoader
	 */
	protected void addURL(URL url){
		super.addURL(url);
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
			return super.findClass(name);
		} catch (ClassNotFoundException e) {
			Class<?> classe = cache.get(name);
			if(classe==null){
				throw new ClassNotFoundException ("class "+classe+" not found.");
			}
			return classe;
		}
	}
	
	/**
	 * Permet d'ajouter les classes d'un jar au cache
	 * @param jar
	 */
	public void integrateCode(Jar jar){
		for(Map.Entry<String,byte[]> e : jar.classIterator()){
			String name = e.getKey();
			Class<?> classe = e.getClass();
			cache.put(name, classe);			
		}
	}
}
