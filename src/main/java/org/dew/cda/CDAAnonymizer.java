package org.dew.cda;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.Stack;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

class CDAAnonymizer extends DefaultHandler 
{
  protected TransformerHandler target;

  protected String currentTag;
  protected Stack<String> stackElements;
  protected boolean anonymizeText = false;

  public boolean anonymize(String inputFile, String outputFile) throws Exception {
    
    return anonymize(new FileReader(inputFile), new FileWriter(outputFile));
    
  }

  public boolean anonymize(Reader reader, Writer writer) throws Exception {
    if(reader == null || writer == null) return false;
    
    SAXTransformerFactory transformerFactory = (SAXTransformerFactory) TransformerFactory.newInstance();
    
    this.target = transformerFactory.newTransformerHandler();
    
    this.target.setResult(new StreamResult(writer));
    
    org.xml.sax.XMLReader xmlReader = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
    
    xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
    xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
    
    xmlReader.setContentHandler(this);
    
    xmlReader.parse(new InputSource(reader));
    
    return true;
  }

  @Override
  public void startDocument() throws SAXException {
    stackElements = new Stack<String>();
    target.startDocument();
  }

  @Override
  public void endDocument() throws SAXException {
    target.endDocument();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    stackElements.push(localName);
    int iStackSize = stackElements.size();
    currentTag = "";
    for (int i = 0; i < iStackSize; i++) {
      currentTag += "|" + stackElements.get(i);
    }
    currentTag = currentTag.substring(1).toLowerCase();
    
    AttributesImpl modifiedAttributes = new AttributesImpl(attributes);
    
    if(currentTag.endsWith("family")) {
      anonymizeText = true;
    }
    else if(currentTag.endsWith("given")) {
      anonymizeText = true;
    }
    else if(currentTag.endsWith("streetaddressline")) {
      anonymizeText = true;
    }
    else if(currentTag.endsWith("telecom")) {
      setValue(modifiedAttributes, "value", "mailto:", "mailto:xxx@xxx.com", "tel:3330000000");
    }
    else if(currentTag.endsWith("birthtime")) {
      setValue(modifiedAttributes, "value", "20000101");
    }
    else if(currentTag.endsWith("patientrole|id")) {
      setValue(modifiedAttributes, "extension", "PPPPPP00A01H501P");
    }
    else if(currentTag.endsWith("patientrole|patient|guardian|id")) {
      setValue(modifiedAttributes, "extension", "GGGGGG00A01H501P");
    }
    else if(currentTag.endsWith("author|assignedauthor|id")) {
      setValue(modifiedAttributes, "extension", "AAAAAA00A01H501P");
    }
    else if(currentTag.endsWith("author|assignedauthor|id")) {
      setValue(modifiedAttributes, "extension", "AAAAAA00A01H501P");
    }
    else if(currentTag.endsWith("dataenterer|assignedentity|id")) {
      setValue(modifiedAttributes, "extension", "IIIIII00A01H501P");
    }
    else if(currentTag.endsWith("legalauthenticator|assignedentity|id")) {
      setValue(modifiedAttributes, "extension", "LLLLLL00A01H501P");
    }
    else if(currentTag.endsWith("authenticator|assignedentity|id")) {
      setValue(modifiedAttributes, "extension", "UUUUUU00A01H501P");
    }
    else if(currentTag.endsWith("participant|associatedentity|id")) {
      setValue(modifiedAttributes, "extension", "CCCCCC00A01H501P");
    }
    else if(currentTag.endsWith("participant|participantrole|id")) {
      setValue(modifiedAttributes, "extension", "CCCCCC00A01H501P");
    }
    else if(currentTag.endsWith("informationrecipient|intendedrecipient|id")) {
      setValue(modifiedAttributes, "extension", "RRRRRR00A01H501P");
    }
    else if(currentTag.endsWith("encompassingencounter|responsibleparty|assignedentity|id")) {
      setValue(modifiedAttributes, "extension", "EEEEEE00A01H501P");
    }
    else if(currentTag.endsWith("performer|assignedentity|id")) {
      setValue(modifiedAttributes, "extension", "FFFFFF00A01H501P");
    }
    
    target.startElement(uri, localName, qName, modifiedAttributes);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if(anonymizeText) anonymizeText = false;
    
    target.endElement(uri, localName, qName);
    
    if(!stackElements.isEmpty()) stackElements.pop();
    currentTag = "";
    for (int i = 0; i < stackElements.size(); i++) {
      currentTag += "|" + stackElements.get(i);
    }
    currentTag = currentTag.length() > 0 ? currentTag.substring(1) : "";
    currentTag = currentTag.toLowerCase();
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if (anonymizeText) {
      // Crea un nuovo array di caratteri riempito di 'X' della stessa lunghezza
      char[] masked = new char[length];
      for (int i = 0; i < length; i++) {
        // Mantiene eventuali spazi bianchi o newline, anonimizza solo i caratteri visibili
        if (Character.isWhitespace(ch[start + i])) {
          masked[i] = ch[start + i];
        } else {
          masked[i] = 'X';
        }
      }
      // Invia i caratteri mascherati all'output
      target.characters(masked, 0, length);
    } else {
      // Invia i caratteri originali all'output
      target.characters(ch, start, length);
    }
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    target.ignorableWhitespace(ch, start, length);
  }

  protected static void setValue(AttributesImpl attributes, String qName, String newValue) {
    if(attributes == null || qName == null) return;
    int index = attributes.getIndex(qName);
    if(index < 0) return;
    attributes.setValue(index, newValue);
  }

  protected static void setValue(AttributesImpl attributes, String qName, String checkStartsWith, String newValueTrue, String newValueFalse) {
    if(attributes == null || qName == null || checkStartsWith == null) return;
    int index = attributes.getIndex(qName);
    if(index < 0) return;
    String originalValue = attributes.getValue(index);
    if(originalValue != null && originalValue.startsWith(checkStartsWith)) {
      attributes.setValue(index, newValueTrue);
    }
    else {
      attributes.setValue(index, newValueFalse);
    }
  }
}