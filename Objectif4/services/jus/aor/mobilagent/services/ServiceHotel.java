package jus.aor.mobilagent.kernel;

import java.io.File;
import java.util.List;

public class ServiceHotel implements _Service<List<Hotel>> {
    private Parser_Hotel parser;
    
    public ServiceHotel(Object... args) throws Exception {
    	String[] path = (String[])(args[0]);
    	File f = new File(path[0]);
    	if (!f.exists()){
    		throw new Exception("Fichier de données non existant");
    	}
        this.parser = new Parser_Hotel(f);
    }
    
    @Override
    // Param[0] -> localisation de l'hotel
    public List<Hotel> call(Object... params) throws IllegalArgumentException {
        List<Hotel> listeHotels = null;
        try {
            String localisation = params[0].toString();
            listeHotels = parser.get_hotels_from_xml(localisation);
        } catch (Exception e) {
            System.out.println("Erreur lors du parsage de l'hotel");
        }
        return listeHotels;
    }
}
