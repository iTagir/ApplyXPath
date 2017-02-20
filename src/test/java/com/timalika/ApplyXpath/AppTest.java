package com.timalika.ApplyXpath;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    
    public void setUp(){
    	App app = new App("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    }
    
    public void testGetXmlDocument() {
    	App app = new App("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    	Document d = app.GetXmlDocument("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    	assertNotNull(d);
    }
    
    public void testApplyXPath() {
    	App app = new App("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test><tag1>text here</tag1></test>";
    	try {
			doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes("utf-8"))));
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String xpath= "//test/tag1/text()";
    	NodeList nl = app.ApplyXpath(doc, xpath);
    	assertNotNull(nl);
    	assertEquals(nl.item(0).getTextContent(), "text here");
    }
    
    public void testSetNodeValue(){
    	App app = new App("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    	Document d = app.GetXmlDocument("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    	String xpath= "//test/tag1/text()";
    	NodeList nl = app.ApplyXpath(d, xpath);
    	app.SetNodeValue(nl, "test value");
    }
    
    public void testSaveDocument(){
    	String newFile = "src/test/java/com/Timalika/ApplyXPath/testdoc1.xml";
    	try { 
            File file = new File(newFile);
            if(file.delete()) { 
               //System.out.println(file.getName() + " is deleted!");
            } else {
               System.out.println("Delete operation is failed.");
       		}
         } catch(Exception e) {
            e.printStackTrace();
         }
    	
    	App app = new App("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    	
    	Document d = app.GetXmlDocument("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    	String xpath= "//test/tag1/text()";
    	NodeList nl = app.ApplyXpath(d, xpath);
    	app.SetNodeValue(nl, "test value");
    	
    	boolean result = app.SaveDocument(d, newFile);
    	assertTrue(result);
    	File file = new File(newFile);
    	assertTrue(file.exists());
    }
    
    public void testReadXpathExpressions(){
    	App app = new App("src/test/java/com/Timalika/ApplyXPath/testdoc.xml");
    	Map<String,String> m = app.ReadXpathExpressions();
    	assertNotNull(m);
    	assertEquals(m.get("//test/tag1/text()"), "new value");
    }
}
