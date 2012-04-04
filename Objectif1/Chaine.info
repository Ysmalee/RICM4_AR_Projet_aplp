/* récupération des hôtels de la chaîne dans le fichier xml passé en 1er argument */
DocumentBuilder docBuilder = null;
Document doc=null;
docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
doc = docBuilder.parse(new File(args[0]));

String name, localisation;
NodeList list = doc.getElementsByTagName("Hotel");
NamedNodeMap attrs;
/* acquisition de toutes les entrées de la base d'hôtels */
for(int i =0; i<list.getLength();i++) {
	attrs = list.item(i).getAttributes();
	name=attrs.getNamedItem("name").getNodeValue();
	localisation=attrs.getNamedItem("localisation").getNodeValue();
	hotels.add(new Hotel(name,localisation));
}

/* récupération d'informations de configuration */
DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document doc = docBuilder.parse(new File(args[0]));
//On récupère les arguments pour la construction de Chaine
String arguments = doc.getElementsByTagName("service").item(0).getAttributes().getNamedItem("args").getNodeValue();

