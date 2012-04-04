import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Parser_Hotel {
	Document document;
	
	Parser_Hotel(File fichier) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		//Using factory get an instance of document builder
		DocumentBuilder db = dbf.newDocumentBuilder();

		//parse using builder to get DOM representation of the XML file
		document = db.parse(fichier.getPath());

	}

	List<Hotel> get_hotels_from_xml(String ville){
		List<Hotel> rez = new ArrayList<Hotel>();
		
		Element docEle = document.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("Hotel");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0; i<nl.getLength(); i++) {
				Element el = (Element)nl.item(i);
				if(el.getAttribute("localisation").equals(ville)) {
					rez.add(new Hotel(el.getAttribute("name"), el.getAttribute("localisation")));
				}
				
			}
		}

		return rez;
	}	
}
