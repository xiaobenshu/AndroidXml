package com.db.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtils {

	public static void save(String fileName,Document document) {
		try {
			File newXml= FileUtil.createNewFile(fileName);		
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setIndentSize(4);  
			format.setNewlines(true);
			format.setTrimText(true);
			format.setPadText(true);
			format.setNewLineAfterDeclaration(true);
			format.setEncoding("utf-8");		
			XMLWriter output = new XMLWriter(new OutputStreamWriter(new FileOutputStream(newXml),"UTF-8"),format);
	        output.write(document);
	        output.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static Document read(String fileName) {		
		Document readDocument = null;
		if (!fileName.endsWith(".xml")) {
			return readDocument;
		}
		try {
			readDocument = new SAXReader().read(new File(fileName));		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return readDocument;
	}
	
	public static boolean addElment(Element parent, Element newElement) {
		try {
			parent.add(newElement);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static Document createDocument() {
		try {
			 return DocumentFactory.getInstance().createDocument();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
}
