
/* Récupération de l'annuaire dans le fichier xml */
DocumentBuilder docBuilder = null;
Document doc=null;
docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
doc = docBuilder.parse(new File(...));

String name, numero;
NodeList list = doc.getElementsByTagName("Telephone");
NamedNodeMap attrs;
/* acquisition de toutes les entrées de l'annuaire */*
for(int i =0; i<list.getLength();i++) {
	attrs = list.item(i).getAttributes();
	name=attrs.getNamedItem("name").getNodeValue();
	numero=attrs.getNamedItem("numero").getNodeValue();
}


/* récupération d'informations de configuration */
DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document doc = docBuilder.parse(new File(...));
//On récupère les arguments pour la construction de Chaine
String arguments = doc.getElementsByTagName("service").item(0).getAttributes().getNamedItem("args").getNodeValue();

