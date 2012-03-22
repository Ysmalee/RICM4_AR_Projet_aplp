package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

public class AgentLoader extends URLClassLoader {
	
	public AgentLoader(){
		super(null);
	}
	
	public void addURL(URL url){
		super.addURL(url);
	}
	
	public void extractCode(){
		
	}
	
	public Class findClass(String name){
		try {
			return super.findClass(name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void integrateCode(){
		
	}
}
