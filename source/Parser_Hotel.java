import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Parser_Hotel {
	Document document;
	List<?> liste;
	Parser_Hotel(File fichier) {
		 
		  SAXBuilder builder = new SAXBuilder();
		  Element rootNode = document.getRootElement();
		  this.liste = rootNode.getChildren("Hotel");
			 
		  try {
			this.document = (Document) builder.build(fichier);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	List<Hotel> get_hotels_from_xml(String ville){
		List<Hotel> rez = new ArrayList<Hotel>();
		for (int i = 0; i < liste.size(); i++) {
			
			Element node = (Element) liste.get(i);
			String endroit = node.getAttributeValue("localisation");
			if(endroit.equals(ville)){
				Hotel undeplus = new Hotel(node.getAttributeValue("name"),endroit);
				rez.add(undeplus);
			}
		}
	return rez;
	}	
}
