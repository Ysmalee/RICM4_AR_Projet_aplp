package jus.aor.mobilagent.kernel;

import java.io.File;
import java.util.List;
import org.jdom.JDOMException;

public class ServiceHotel implements jus.aor.mobilagent.kernel._Service<List<Hotel>> {
    private Parser_Hotel parser;
    
    public ServiceHotel(Object... args) throws Exception
    {
        this.parser = new Parser_Hotel(new File(args[0].toString()));
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
