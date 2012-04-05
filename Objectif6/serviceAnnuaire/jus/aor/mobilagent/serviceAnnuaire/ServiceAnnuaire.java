package jus.aor.mobilagent.serviceAnnuaire;

import jus.aor.mobilagent.kernel.Numero;
import jus.aor.mobilagent.kernel.Hotel;

import java.io.File;

public class ServiceAnnuaire implements jus.aor.mobilagent.kernel._Service<Numero> {
    Parser_Annuaire parser;
   
    public ServiceAnnuaire(Object... args) throws Exception {
    	String[] path = (String[])(args[0]);
    	File f = new File(path[0]);
    	if (!f.exists()){
    		throw new Exception("Fichier de données non existant");
    	}
        this.parser = new Parser_Annuaire(f);
    }
    
    @Override
    // Param[0] -> Liste d'hotel
    public Numero call(Object... params) throws IllegalArgumentException {
    	String name = null;
        if (params[0] instanceof String){
        	name = (String) params[0];
        	return this.parser.get_numero_from_xml(name);
        } else {
        	throw new IllegalArgumentException("Problème avec params dans la méthode call de ServiceAnnuaire.");
        }
    }
}
