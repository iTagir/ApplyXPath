package com.timalika.ApplyXpath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.apache.commons.io.FilenameUtils;


/**
 * Hello world!
 *
 */
public class App 
{
	private String xmlFilePath = null;
	private String xpathFilePath = null;
	
    public static void main( String[] args )
    {
        App app = new App(args[1]);
        app.TransformXmlFile();
    }
    public App(){
    	
    }
    public App(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
		this.xpathFilePath = FilenameUtils.removeExtension(xmlFilePath);
		this.xpathFilePath = this.xpathFilePath + ".ucxpath";
	}
    
    public void TransformXmlFile(){
    	Document doc = this.GetXmlDocument(this.xmlFilePath);
    }
    
    public Document GetXmlDocument(String fileName){
    	File fXmlFile = new File(fileName);
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Document doc = null;
    	try {
			doc = dBuilder.parse(fXmlFile);
			return doc;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return null;
    	
    }
    
    public NodeList ApplyXpath(Document doc, String xpath) {
    	      
    	XPath xPathObj =  XPathFactory.newInstance().newXPath();
    	
    	try {
			NodeList nodeList = (NodeList) xPathObj.compile(xpath)
			           .evaluate(doc, XPathConstants.NODESET);
			return nodeList;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    public void SetNodeValue(NodeList nodeList, String newValue){
    	for(int i=0; i<nodeList.getLength();i++){
    		nodeList.item(i).setNodeValue(newValue);
    	}
    }
    
    public boolean SaveDocument(Document doc, String fileName){
    	Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	Result output = new StreamResult(new File(fileName));
    	Source input = new DOMSource(doc);

    	try {
			transformer.transform(input, output);
			return true;
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return false;
    }
    
    public Map<String, String> ReadXpathExpressions() {
    	String filePath = this.xpathFilePath;
    	Map<String, String> xpathsMap = new HashMap<String, String>();
        BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        String line = "";
        try {
			while ((line = in.readLine()) != null) {
			    String parts[] = line.split("->");
			    xpathsMap.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        System.out.println(xpathsMap.toString());
        return xpathsMap;
    }
}
