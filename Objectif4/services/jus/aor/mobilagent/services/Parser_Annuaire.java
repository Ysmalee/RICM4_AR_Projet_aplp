package jus.aor.mobilagent.kernel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Parser_Annuaire {
	Map<String,Numero> _annuaire;
	
	Parser_Annuaire(File fichier) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		//Using factory get an instance of document builder
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		this._annuaire = new HashMap<String, Numero>();
		//parse using builder to get DOM representation of the XML file
		Document document = db.parse(fichier.getPath());
		Element docEle = document.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("Telephone");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0; i<nl.getLength(); i++) {
				Element el = (Element)nl.item(i);
				this._annuaire.put(el.getAttribute("name"),new Numero(el.getAttribute("numero")));				
			}
		}
	}

	Numero get_numero_from_xml(String nomCherche){
		return this._annuaire.get(nomCherche);
	}	
}