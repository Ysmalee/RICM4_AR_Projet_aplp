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
		super(new URL[]{},null);
	}
	
	public BAMLoader(URL[] urls, ClassLoader parent){
		super(urls,parent);
	}
	
	/**
	 * Ajoute un repository au BAMLoader
	 * @throws IOException 
	 * @throws JarException 
	 */
	protected void addURL(String jar) throws IOException{
		try {
			Jar j = new Jar(jar);
			if (ressources == null){
				ressources = j;
			}
			this.integrateCode(j);
		} catch (IOException e) {
			throw new IOException("Erreur lors de l'ajout du JAR", e);
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
		byte[] classe = cache.get(Jar.formatClassName(name));
		if(classe==null){
			throw new ClassNotFoundException ("class "+name+" not found.");
		}
		return this.defineClass(name,classe, 0, classe.length);
	}
	
	/**
	 * Permet d'ajouter les classes d'un jar au cache
	 * @param jar
	 */
	public void integrateCode(Jar jar){
		if (ressources == null){
			ressources = jar;
		}
		for(Map.Entry<String,byte[]> e : jar.classIterator()){
			String name = e.getKey();
			byte[] classe = e.getValue();
			cache.put(name,classe);			
		}
	}
	
	/**
	 * Méthode toString()
	 */
	public String toString(){
		String buff = "";
		for(String e : cache.keySet()){
			buff += e + "\n";
		}
			
		return buff;
	}
}
