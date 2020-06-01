package main.db;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.util.Utils;

public class QueryFileParser {
	
	private static final String ELEMENT_QUERY = "query";
	private static final String ATTRIBUTE_KEY = "key";
	private static final String SCHEMA_FILE = "queries/queries.xsd";
	private static Schema schema = null;

	public static Map<String, String> loadQueriesFromFile(File file, Map<String, String> queriesMap) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);
		validateQueryFile(document);
		document.getDocumentElement().normalize();
		NodeList queryList = document.getElementsByTagName(ELEMENT_QUERY);
		for (int temp = 0; temp < queryList.getLength(); temp++) {
			Node node = queryList.item(temp);
			Element element = (Element) node;
			String key = element.getAttribute(ATTRIBUTE_KEY);
			String query = element.getTextContent();
			if(queriesMap.containsKey(key)) {
				throw new SAXException("key:" +  key + " defined multiple times.");
			}
			queriesMap.put(key, query);
		}
		return queriesMap;
	}

	private static void validateQueryFile(Document document) throws SAXException, IOException {
		if(schema == null) {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			  SchemaFactory factory = SchemaFactory.newInstance(language);
			  schema = factory.newSchema(Utils.getFileFromResources(SCHEMA_FILE));
		}
		Validator validator = schema.newValidator();
		validator.validate(new DOMSource(document));
	}
}
