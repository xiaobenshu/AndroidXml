package com.db.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;

public class RegularXml {

	private static String DimensPath = "\\values\\dimens0.xml";
	private static String StringsPath = "\\values\\strings0.xml";
	private static String LayoutPath = "\\layout\\";
	private String mInputFilePath;
	private String mOutputFilePath;
	private static int generID = 0;
	private HashMap<String, String> mStringMap = new HashMap<>();
	private HashMap<String, String> mDimensMap = new HashMap<>();
	
	public RegularXml(String inputDir, String outPutDir) {
		// TODO Auto-generated constructor stub
		this.mInputFilePath = inputDir;
		this.mOutputFilePath = outPutDir;
		FileUtil.createDir(mInputFilePath);
		FileUtil.createDir(mOutputFilePath);
		generID =0;
	}
	
	public void check() {	
		ArrayList<File> inputFiles = new ArrayList<File>();
		FileUtil.getFiles(mInputFilePath, inputFiles);
		ArrayList<File> layoutXmlFile = new ArrayList<File>();
		for (File file : inputFiles) {
			if (file.getAbsolutePath().endsWith(".xml")&& file.getAbsolutePath().contains("layout")) {
				layoutXmlFile.add(file);
			}
		}
		for (File file : layoutXmlFile) {
			Document document = XmlUtils.read(file.getAbsolutePath());
			if (document != null) {
				if (true/*document.getRootElement().getName().equals("resources")*/) {
					process(document.getRootElement());	
					XmlUtils.save(mOutputFilePath+"\\layout\\"+file.getName(), document);
				}
			}
		}
		Document dimensDoc = XmlUtils.createDocument();
		Element root = dimensDoc.addElement("resources");
		Set<Entry<String,String>> collectionsSet =  mDimensMap.entrySet();
		for (Entry<String, String> entry : collectionsSet) {	
			Element insertElement =  root.addElement("dimen");
			insertElement.addAttribute("name", entry.getKey());
			insertElement.addText(entry.getValue());		
		}
		XmlUtils.save(mOutputFilePath+DimensPath, dimensDoc);
		dimensDoc.clearContent();
		
		Document stringsDoc = XmlUtils.createDocument();
		Element root0 = stringsDoc.addElement("resources");
		Set<Entry<String,String>> collectionsSet0 =  mStringMap.entrySet();
		for (Entry<String, String> entry : collectionsSet0) {	
			Element insertElement =  root0.addElement("string");
			insertElement.addAttribute("name", entry.getKey());
			insertElement.addText(entry.getValue());		
		}
		XmlUtils.save(mOutputFilePath+StringsPath, stringsDoc);
		
	}
	
	
	private void process(Element element){
		if (element == null) {
			return;
		}
		String idName = checkID(element);	
		checkAttribute(element,idName,"layout_width");
		checkAttribute(element,idName,"layout_height");
		checkAttribute(element,idName,"textSize");
		checkAttribute(element,idName,"paddingLeft");
		checkAttribute(element,idName,"paddingRight");
		checkAttribute(element,idName,"paddingTop");
		checkAttribute(element,idName,"paddingBottom");
		checkAttribute(element,idName,"layout_marginTop");
		checkAttribute(element,idName,"layout_marginLeft");
		checkAttribute(element,idName,"layout_marginRight");
		checkAttribute(element,idName,"layout_marginBottom");		
		checkText(element, idName);	
		List<Element> list =  element.elements();
		if (list !=null ) {					
			for (Element element2 : list) {
				process(element2);
			}
		}	
	}
	
	private String checkID(Element el){
		return el.attributeValue("id") != null
				? el.attributeValue("id").replace("@+id/", "")
				: el.getName().replace(".","_");
	}
	
	public static boolean checkContainNub(String s) {
		if (s !=null) {
			s =s.trim();
		}
		char c = s.charAt(0);
		if(((c >='0' && c<= '9'))) {
			return true;
		} else {
			return false;
		}
	}

	private void checkAttribute(Element el, String elementName,String attributeName){
		String attribute_value = el.attributeValue(attributeName);
		if (attribute_value!=null && !attribute_value.contains("@") && checkContainNub(attribute_value)) {
			String new_Name = elementName +"_"+attributeName;
			if (mDimensMap.containsKey(new_Name)) {
				generID++;
				new_Name += "_"+generID;
				mDimensMap.put(new_Name, attribute_value);
			}else{
				mDimensMap.put(new_Name,  new String(attribute_value));
			}
			el.attribute(attributeName).setValue("@dimen/"+new_Name);
		}
	}
	
	
	
	private String checkText(Element el,String name){
		String text = el.attributeValue("text");
		if (text != null &&!text.contains("@")) {
			String new_text = name+"_text";
			if (mStringMap.containsKey(new_text)) {
				generID++;
				new_text += "_"+generID;
				mStringMap.put(new_text, new String(text));
			}else{
				mStringMap.put(new_text,  new String(text));
			}
			text = new_text;
			el.attribute("text").setValue("@string/"+new_text);
		}
		return text;
	}
}
