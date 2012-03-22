import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Parser_Annuaire {
	Document document;
	List<?> liste;
	Parser_Annuaire(File fichier) {
		 
		  SAXBuilder builder = new SAXBuilder();
		  Element rootNode = document.getRootElement();
		  this.liste = rootNode.getChildren("Telephone");
			 
		  try {
			this.document = (Document) builder.build(fichier);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Numero get_numero_from_xml(String nomCherche){
		for (int i = 0; i < liste.size(); i++) {
			Element node = (Element) liste.get(i);
			String nom = node.getAttributeValue("name");
			if(nom.equals(nomCherche)){
				Numero number = new Numero(node.getAttributeValue("numero"));
				return number;
			}
		}
		return null;
	}	
}