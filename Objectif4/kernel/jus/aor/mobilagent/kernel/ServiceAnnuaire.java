package kernel;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ServiceAnnuaire implements jus.aor.mobilagent.kernel._Service<Map<Hotel, Numero>> {
    Parser_Annuaire parser;
   
    public ServiceAnnuaire(Object... args) throws Exception
    {
        this.parser = new Parser_Annuaire(new File(args[0].toString()));
    }
    
    @Override
    // Param[0] -> Liste d'hotel
    public Map<Hotel, Numero> call(Object... params) throws IllegalArgumentException {
        Map<Hotel, Numero> mapHotelNumero = null;
        List<Hotel> listeHotels = null;
        try {
            listeHotels = (List<Hotel>) params[0];
            
            for (Hotel h : listeHotels) {
                mapHotelNumero.put(h, this.parser.get_numero_from_xml(h.name));
            }
            
        } catch (Exception e) {
            System.out.println("Erreur lors du parsage de l'annuaire");
        }
        return mapHotelNumero;
    }
}
