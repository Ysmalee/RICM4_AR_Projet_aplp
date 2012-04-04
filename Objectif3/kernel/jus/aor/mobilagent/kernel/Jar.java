/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * Liste l'ensemble des ressources contenues dans un fichier jar.
 * @author Morat 
 */
public class Jar implements Iterable<Map.Entry<String,byte[]>>, Serializable{
	private static final long serialVersionUID = 1803791441059733817L;
	/** le contenu du fichier jar */
	private Map<String,byte[]> contents = new HashMap<String,byte[]>();
	/**
	 * création d'un Jar. l'ensemble des ressources contenues
	 * dans le fichier peuvent être accédées via les méthodes getResource ou getClass.
	 * @param fileName le nom du fichier jar
	 */
	public Jar(String fileName) throws JarException,IOException{
		// la taille de chacun des composants du jar
		Map<String,Integer> htSizes = new HashMap<String,Integer>();
		// le fichier jar
		JarFile jar = new JarFile(fileName);
		// l'ensemble des entrées contenues dans le jar
		Enumeration<? extends JarEntry> e = jar.entries();
		JarEntry entry;
		// calcul de la taille de chacun des composants du jar
		while(e.hasMoreElements()){
			entry = e.nextElement();
			htSizes.put(entry.getName(), new Integer((int) entry.getSize()));
		}
		jar.close();
		
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		JarInputStream jis = new JarInputStream(bis);
		entry = null;
		int size, sizeRead, sizeLastRead;
		byte[] buffer;
		//récupération des composants du jar
		while((entry = jis.getNextJarEntry()) != null){
			if(entry.isDirectory()) continue;
			size = htSizes.get(entry.getName()).intValue();
			buffer = new byte[(int) size];
			sizeRead = sizeLastRead = 0;
			while(((int) size - sizeRead) > 0){
				sizeLastRead = jis.read(buffer, sizeRead, (int) size - sizeRead);
				if(sizeLastRead == -1) break;
				sizeRead += sizeLastRead;
			}
			contents.put(entry.getName(), buffer);
		}
	}
	/**
   * Restitue le contenu d'un composant du jar.
   * @param name le nom de la ressource.
   * @return le contenu du ficher ou nul si la ressource n'existe pas.
   */
	public byte[] getResource(String name){
		byte[] result = contents.get(name);
		if(result==null) result = contents.get(formatClassName(name));
		return result;
	}
	/**
   * Restitue le contenu d'une classe du jar.
   * @param name le nom de la classe.
   * @return le contenu de la classe ou nul si la classe n'existe pas.
   */
	public byte[] getClass(String name){ return getResource(formatClassName(name));}
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Map.Entry<String,byte[]>> iterator(){return contents.entrySet().iterator();}
	/**
	 * @return itérable des classes du jar
	 */
	public Iterable<Map.Entry<String,byte[]>> classIterator(){
		final Iterator<Map.Entry<String,byte[]>> resource = iterator();
		return new Iterable<Map.Entry<String,byte[]>>(){
			public Iterator<Map.Entry<String,byte[]>> iterator(){
				return new Iterator<Map.Entry<String,byte[]>>(){
					boolean hasNext = true;
					Map.Entry<String,byte[]> current;
					{nextOne();}
					public boolean hasNext(){return hasNext;}
					public Map.Entry<String,byte[]> next(){
						Map.Entry<String,byte[]> temp = current;
						hasNext = resource.hasNext();
						nextOne();
						return temp;
					}
					public void remove(){throw new UnsupportedOperationException();}
					private void nextOne(){
						hasNext = resource.hasNext();
						while(hasNext){
							current = resource.next();
							if(isClassName(current.getKey())) break;
							hasNext = resource.hasNext();
						}
					}
				};
			}
		};
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		StringBuffer buf = new StringBuffer();
		for(Map.Entry<String,byte[]> entry : this) {
			buf.append("\t"); buf.append(entry.getKey()); buf.append("\n");
		}
		return "Jar[\n"+buf.toString()+"]";
	}
	/**
	 * remplace le séparateur de package par le séparateur de niveau.
	 * @param className le nom logique de la classe
	 * @return
	 */
	public static String formatClassName(String className) { return className.replace(".","/")+".class";}
	/**
	 * indique si le nom de la ressource est une classe (se termine par .class).
	 * @param name le nom de la ressource
	 * @return vrai si la ressource est une classe.
	 */
	protected boolean isClassName(String name) { return name.endsWith(".class");}
}

