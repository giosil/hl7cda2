package org.dew.cda;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Stack;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.dew.hl7.ClinicalDocument;
import org.dew.hl7.Entry;
import org.dew.hl7.ICDADeserializer;
import org.dew.hl7.Organization;
import org.dew.hl7.Person;
import org.dew.hl7.Section;

public 
class CDADeserializer implements ICDADeserializer, ContentHandler
{
  protected String currentTag;
  protected String currentValue;
  protected Stack<String> stackElements;
  
  protected ClinicalDocument cda;
  
  protected Section currentSection;
  protected Entry   currentEntry;
  protected String  contentId;
  
  protected String oidAuthority = "2.16.840.1.113883.2.9.2"; // HL7 Italia
  
  public 
  void setOIDAuthority(String oidAuthority)
  {
    this.oidAuthority = oidAuthority;
  }
  
  @Override
  public
  void load(String file)
    throws Exception
  {
    cda = new ClinicalDocument();
    
    boolean isXmlContent = file.indexOf('<') >= 0;
    if(isXmlContent) {
      
      load(file.getBytes());
      
    }
    else {
      byte[] content = null;
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(file);
        content = new byte[fis.available()];
        fis.read(content);
      }
      finally {
        if(fis != null) try { fis.close(); } catch(Exception ex) {}
      }
      
      load(content);
    }
  }
  
  @Override
  public
  void load(byte[] content)
    throws Exception
  {
    cda = new ClinicalDocument();
    
    if(content == null || content.length < 40) {
      return;
    }
    
    List<IndexRange> listOfIndexRange = getTextSectionsIndexRanges(content);
    
    String document = null;
    if(listOfIndexRange != null && listOfIndexRange.size() > 0) {
      String xml = new String(content);
      int length = xml.length();
      StringBuilder sbDoc = new StringBuilder();
      for(int i = 0; i < length; i++) {
        char c = xml.charAt(i);
        boolean isNarrativeText = false;
        for(int r = 0; r < listOfIndexRange.size(); r++) {
          IndexRange indexRange = listOfIndexRange.get(r);
          if(i >= indexRange.start && i <= indexRange.end) {
            isNarrativeText = true;
            break;
          }
        }
        if(c == '<') {
          if(isNarrativeText) {
            sbDoc.append("&lt;");
          }
          else {
            sbDoc.append(c);
          }
        }
        else if(c == '>') {
          if(isNarrativeText) {
            sbDoc.append("&gt;");
          }
          else {
            sbDoc.append(c);
          }
        }
        else {
          sbDoc.append(c);
        }
      }
      document = sbDoc.toString();
    }
    else {
      document = new String(content);
    }
    
    InputSource inputSource = new InputSource(new ByteArrayInputStream(document.getBytes()));
    XMLReader xmlReader = XMLReaderFactory.createXMLReader();
    xmlReader.setContentHandler(this);
    xmlReader.parse(inputSource);
  }
  
  @Override
  public
  ClinicalDocument getClinicalDocument()
  {
    return cda;
  }
  
  @Override
  public
  void startDocument()
    throws SAXException
  {
    stackElements = new Stack<String>();
    
    currentSection = null;
    currentEntry   = null;
    contentId      = null;
  }
  
  @Override
  public
  void endDocument()
    throws SAXException
  {
  }
  
  @Override
  public
  void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException
  {
    stackElements.push(localName);
    int iStackSize = stackElements.size();
    currentTag = "";
    for (int i = 0; i < iStackSize; i++) {
      currentTag += "|" + stackElements.get(i);
    }
    currentTag = currentTag.substring(1).toLowerCase();
    currentValue = "";
    
    if(currentTag.endsWith("clinicaldocument|templateid")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          cda.setTemplateId(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("root")) {
          cda.setTemplateIdRoot(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|id")) {
      String sIdRoot = null;
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          cda.setId(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("root")) {
          sIdRoot = attributes.getValue(i);
          cda.setIdRoot(sIdRoot);
        }
      }
      if(oidAuthority != null && oidAuthority.length() > 0) {
        if(sIdRoot != null && sIdRoot.startsWith(oidAuthority)) {
          int iSep = sIdRoot.indexOf('.', oidAuthority.length() + 1);
          if(iSep > 0) {
            cda.setAuthorityCode(sIdRoot.substring(oidAuthority.length() + 1, iSep));
          }
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|code")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("code")) {
          cda.setCode(attributes.getValue(i));
        }
        if(sLocalName.equalsIgnoreCase("displayName")) {
          cda.setDisplayName(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|code|translation")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("code")) {
          cda.setTranslationCode(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|effectivetime")) {
      String effectiveTime = null;
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("value")) {
          effectiveTime = attributes.getValue(i);
        }
      }
      if(effectiveTime != null && effectiveTime.length() > 7) {
        cda.setEffectiveTime(toDate(effectiveTime));
      }
    }
    else if(currentTag.endsWith("clinicaldocument|confidentialitycode")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("code")) {
          cda.setConfidentialityCode(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|setid")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          cda.setSetId(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("root")) {
          cda.setSetIdRoot(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|versionnumber")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("value")) {
          cda.setVersionNumber(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|id")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          getPatient().setId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|administrativegendercode")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("code")) {
          getPatient().setGender(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|birthtime")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("value")) {
          getPatient().setBirthTime(toDate(attributes.getValue(i)));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|guardian|id")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          getGuardian().setId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|author|assignedauthor|id")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          getAuthor().setId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|informant|assignedentity|id")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          getInformant().setId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|legalauthenticator|assignedentity|id")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          getLegalAuthenticator().setId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|legalauthenticator|assignedentity|representedorganization|id")) {
      String sExtension  = "";
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          sExtension = attributes.getValue(i);
        }
      }
      getScopingOrg().setId(sExtension);
    }
    else if(currentTag.endsWith("clinicaldocument|custodian|assignedcustodian|representedcustodianorganization|id")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          getCustodian().setId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("clinicaldocument|relateddocument|parentdocument|id")) {
      String sExtension  = "";
      String sRoot = "";
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          sExtension = attributes.getValue(i);
        }
        else if(sLocalName.equalsIgnoreCase("root")) {
          sRoot = attributes.getValue(i);
        }
      }
      cda.setRelatedDocumentId(sRoot + "." + sExtension);
    }
    else if(currentTag.endsWith("clinicaldocument|infulfillmentof|order|id")) {
      String sExtension = "";
      String sRoot = "";
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("extension")) {
          sExtension = attributes.getValue(i);
        }
        else if(sLocalName.equalsIgnoreCase("root")) {
          sRoot = attributes.getValue(i);
        }
      }
      cda.setInFulfillmentOf(sRoot + "." + sExtension);
    }
    else if(currentTag.endsWith("section")) {
      currentSection = new Section();
      cda.addSection(currentSection);
      
      currentEntry   = null;
      contentId      = null;
      
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("id")) {
          currentSection.setId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("section|templateid")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("root")) {
          getCurrentSection().setTemplateId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("section|code")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("code")) {
          getCurrentSection().setCode(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("codeSystem")) {
          getCurrentSection().setCodeSystem(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("codeSystemName")) {
          getCurrentSection().setCodeSystemName(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("displayName")) {
          getCurrentSection().setDisplayName(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("|content")) {
      contentId = null;
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("id")) {
          contentId = attributes.getValue(i);
        }
      }
    }
    else if(currentTag.endsWith("|reference")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("value")) {
          String refId = attributes.getValue(i);
          if(refId != null && refId.length() > 1 && refId.startsWith("#")) {
            refId = refId.substring(1);
            
            Entry entry = getCurrentEntry();
            entry.setReference(refId);
            if(entry.getCode() == null && entry.getDisplayName() == null) {
              entry.setDisplayName(refId);
            }
            
            String value = entry.getValue();
            if(value == null || value.length() == 0) {
              if(refId.endsWith("-DES")) {
                String des = cda.getContent(refId);
                String cod = cda.getContent(refId.substring(0, refId.length()-4) + "-COD");
                if(cod != null && cod.length() > 0) {
                  entry.setValue(cod);
                  entry.setText(des);
                }
                else {
                  entry.setValue(des);
                }
              }
              else if(refId.endsWith("-COD")) {
                String cod = cda.getContent(refId);
                String des = cda.getContent(refId.substring(0, refId.length()-4) + "-DES");
                if(cod != null && cod.length() > 0) {
                  entry.setValue(cod);
                  entry.setText(des);
                }
                else {
                  entry.setValue(des);
                }
              }
              else {
                entry.setValue(cda.getContent(refId));
              }
            }
          }
        }
      }
    }
    else if(currentTag.endsWith("|observation")) {
      currentEntry = new Entry();
      getCurrentSection().addEntry(currentEntry);
    }
    else if(currentTag.endsWith("|act")) {
      currentEntry = new Entry();
      getCurrentSection().addEntry(currentEntry);
    }
    else if(currentTag.endsWith("section|entry|organizer|id")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("root")) {
          getCurrentEntry().setId(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("section|entry|observationmedia")) {
      currentEntry = new Entry();
      getCurrentSection().addEntry(currentEntry);
      
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("id")) {
          if(isCurrentSectionMedia()) {
            getCurrentSection().setMediaId(attributes.getValue(i));
          }
          else {
            getCurrentEntry().setMediaId(attributes.getValue(i));
          }
        }
      }
    }
    else if(currentTag.endsWith("section|entry|observationmedia|value")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("mediaType")) {
          if(isCurrentSectionMedia()) {
            getCurrentSection().setMediaType(attributes.getValue(i));
          }
          else {
            getCurrentEntry().setMediaType(attributes.getValue(i));
          }
        }
      }
    }
    else if(currentTag.endsWith("observation|code")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("code")) {
          getCurrentEntry().setCode(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("codeSystem")) {
          getCurrentEntry().setCodeSystem(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("codeSystemName")) {
          getCurrentEntry().setCodeSystemName(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("displayName")) {
          getCurrentEntry().setDisplayName(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("nullFlavor")) {
          getCurrentEntry().setNullFlavor(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("observation|value")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("value")) {
          getCurrentEntry().setValue(attributes.getValue(i));
        }
        else if(sLocalName.equalsIgnoreCase("unit")) {
          getCurrentEntry().setUnit(attributes.getValue(i));
        }
      }
    }
    else if(currentTag.endsWith("entry|effectiveTime")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("value")) {
          getCurrentEntry().setEffectiveTime(toDate(attributes.getValue(i)));
        }
      }
    }
    else if(currentTag.endsWith("entry|effectiveTime|low")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("value")) {
          getCurrentEntry().setLow(toDate(attributes.getValue(i)));
        }
      }
    }
    else if(currentTag.endsWith("entry|effectiveTime|high")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("value")) {
          getCurrentEntry().setHigh(toDate(attributes.getValue(i)));
        }
      }
    }
    else if(currentTag.endsWith("manufacturedLabeledDrug|code")) {
      for(int i = 0; i < attributes.getLength(); i++) {
        String sLocalName = attributes.getLocalName(i);
        if(sLocalName.equalsIgnoreCase("code")) {
          getCurrentEntry().setManufacturedProduct(attributes.getValue(i));
        }
      }
    }
  }
  
  @Override
  public
  void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    if(currentTag.endsWith("clinicaldocument|title")) {
      cda.setTitle(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|addr|city")) {
      getPatient().setCity(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|addr|postalcode")) {
      getPatient().setPostalCode(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|addr|streetname")) {
      getPatient().setStreetName(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|addr|housenumber")) {
      getPatient().setHouseNumber(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|addr|country")) {
      getPatient().setCountry(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|name|prefix")) {
      getPatient().setPrefix(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|name|given")) {
      getPatient().setGiven(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|name|family")) {
      getPatient().setFamily(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|birthplace|place|addr|city")) {
      getPatient().setBirthPlace(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|birthplace|place|addr|country")) {
      getPatient().setBirthCountry(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|guardian|guardianperson|name|prefix")) {
      getGuardian().setPrefix(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|guardian|guardianperson|name|given")) {
      getGuardian().setGiven(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|recordtarget|patientrole|patient|guardian|guardianperson|name|family")) {
      getGuardian().setFamily(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|author|assignedauthor|assignedperson|name|prefix")) {
      getAuthor().setPrefix(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|author|assignedauthor|assignedperson|name|given")) {
      getAuthor().setGiven(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|author|assignedauthor|assignedperson|name|family")) {
      getAuthor().setFamily(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|informant|assignedentity|assignedperson|name|prefix")) {
      getInformant().setPrefix(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|informant|assignedentity|assignedperson|name|given")) {
      getInformant().setGiven(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|informant|assignedentity|assignedperson|name|family")) {
      getInformant().setFamily(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|legalauthenticator|assignedentity|assignedperson|name|prefix")) {
      getLegalAuthenticator().setPrefix(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|legalauthenticator|assignedentity|assignedperson|name|given")) {
      getLegalAuthenticator().setGiven(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|legalauthenticator|assignedentity|assignedperson|name|family")) {
      getLegalAuthenticator().setFamily(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|custodian|assignedcustodian|representedcustodianorganization|name")) {
      getCustodian().setName(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|custodian|assignedcustodian|representedcustodianorganization|addr|city")) {
      getCustodian().setCity(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|custodian|assignedcustodian|representedcustodianorganization|addr|postalcode")) {
      getCustodian().setPostalCode(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|custodian|assignedcustodian|representedcustodianorganization|addr|streetname")) {
      getCustodian().setStreetName(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|custodian|assignedcustodian|representedcustodianorganization|addr|housenumber")) {
      getCustodian().setHouseNumber(currentValue);
    }
    else if(currentTag.endsWith("clinicaldocument|custodian|assignedcustodian|representedcustodianorganization|addr|country")) {
      getCustodian().setCountry(currentValue);
    }
    else if(currentTag.endsWith("section|title")) {
      getCurrentSection().setTitle(currentValue);
    }
    else if(currentTag.endsWith("section|text")) {
      getCurrentSection().setText(currentValue);
    }
    else if(currentTag.endsWith("|content")) {
      cda.putContent(contentId, currentValue);
    }
    else if(currentTag.endsWith("section|entry|observationmedia|value")) {
      if(isCurrentSectionMedia()) {
        getCurrentSection().setMediaContent(currentValue);
      }
      else {
        getCurrentEntry().setMediaContent(currentValue);
      }
    }
    
    if(!stackElements.isEmpty()) stackElements.pop();
    currentTag = "";
    for (int i = 0; i < stackElements.size(); i++) {
      currentTag += "|" + stackElements.get(i);
    }
    currentTag = currentTag.length() > 0 ? currentTag.substring(1) : "";
    currentTag = currentTag.toLowerCase();
  }
  
  @Override
  public
  void characters(char[] ch, int start, int length)
    throws SAXException
  {
    currentValue += new String(ch, start, length).trim();
  }
  
  @Override
  public void setDocumentLocator(Locator locator) {}
  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {}
  @Override
  public void endPrefixMapping(String prefix) throws SAXException {}
  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
  @Override
  public void processingInstruction(String target, String data) throws SAXException {}
  @Override
  public void skippedEntity(String name) throws SAXException {}
  
  protected 
  Person getPatient()
  {
    Person result = cda.getPatient();
    if(result == null) {
      result = new Person();
      cda.setPatient(result);
    }
    return result;
  }
  
  protected 
  Person getGuardian()
  {
    Person result = cda.getGuardian();
    if(result == null) {
      result = new Person();
      cda.setGuardian(result);
    }
    return result;
  }
  
  protected 
  Person getAuthor()
  {
    Person result = cda.getAuthor();
    if(result == null) {
      result = new Person();
      cda.setAuthor(result);
    }
    return result;
  }
  
  protected 
  Person getInformant()
  {
    Person result = cda.getInformant();
    if(result == null) {
      result = new Person();
      cda.setInformant(result);
    }
    return result;
  }
  
  protected 
  Person getLegalAuthenticator()
  {
    Person result = cda.getLegalAuthenticator();
    if(result == null) {
      result = new Person();
      cda.setLegalAuthenticator(result);
    }
    return result;
  }
  
  protected 
  Organization getCustodian()
  {
    Organization result = cda.getCustodian();
    if(result == null) {
      result = new Organization();
      cda.setCustodian(result);
    }
    return result;
  }
  
  protected 
  Organization getScopingOrg()
  {
    Organization result = cda.getScopingOrg();
    if(result == null) {
      result = new Organization();
      cda.setScopingOrg(result);
    }
    return result;
  }
  
  protected
  Section getCurrentSection() 
  {
    if(currentSection == null) {
      currentSection = new Section();
      cda.addSection(currentSection);
    }
    return currentSection;
  }
  
  protected
  Entry getCurrentEntry() 
  {
    if(currentSection == null) {
      currentSection = new Section();
      cda.addSection(currentSection);
    }
    if(currentEntry == null) {
      currentEntry = new Entry();
      currentSection.addEntry(currentEntry);
    }
    return currentEntry;
  }
  
  protected
  boolean isCurrentSectionMedia() 
  {
    if(currentSection == null) {
      currentSection = new Section();
      cda.addSection(currentSection);
    }
    List<Entry> entries = currentSection.getEntries();
    if(entries == null || entries.size() == 0) {
      return true;
    }
    else if(entries.size() == 1) {
      Entry entry = entries.get(0);
      if(entry == null || entry.getCode() == null) {
        entries.clear();
        currentSection.setEntries(entries);
        return true;
      }
    }
    return false;
  }
  
  protected
  Date toDate(String dateTime)
  {
    if(dateTime == null || dateTime.length() < 8) {
      return null;
    }
    if(dateTime.length() >= 8 && dateTime.length() < 14) {
      try {
        int yyyy = Integer.parseInt(dateTime.substring(0, 4));
        int mm   = Integer.parseInt(dateTime.substring(4, 6));
        int dd   = Integer.parseInt(dateTime.substring(6, 8));
        return new GregorianCalendar(yyyy, mm-1, dd).getTime();
      }
      catch(Exception ex) {
        return null;
      }
    }
    else if(dateTime.length() >= 14) {
      try {
        int yyyy = Integer.parseInt(dateTime.substring(0, 4));
        int mm   = Integer.parseInt(dateTime.substring(4, 6));
        int dd   = Integer.parseInt(dateTime.substring(6, 8));
        int hh   = Integer.parseInt(dateTime.substring(8, 10));
        int mi   = Integer.parseInt(dateTime.substring(10, 12));
        int ss   = Integer.parseInt(dateTime.substring(12, 14));
        return new GregorianCalendar(yyyy, mm-1, dd, hh, mi, ss).getTime();
      }
      catch(Exception ex) {
        return null;
      }
    }
    return null;
  }
  
  protected
  List<IndexRange> getTextSectionsIndexRanges(byte[] content)
  {
    List<IndexRange> listResult = new ArrayList<IndexRange>();
    
    String xml = new String(content);
    int length = xml.length();
    
    boolean isTag     = false;
    boolean isComment = false;
    int     startTag  = -1;
    boolean isTextSec = false;
    IndexRange range  = null;
    String  contentId = null;
    int     contentIx = -1;
    StringBuilder sbTag = new StringBuilder();
    for(int i = 0; i < length; i++) {
      char c = xml.charAt(i);
      
      if(isComment) {
        if(c == '>' && xml.substring(i-2, i).equals("--")) {
          isComment = false;
        }
        continue;
      }
      else if(c == '-') {
        if(i > 2 && xml.substring(i-3, i).equals("<!-")) {
          isComment = true;
        }
      }
      
      if(c == '<') {
        isTag = true;
        sbTag.setLength(0);
        startTag = i;
      }
      else if(c == '>') {
        isTag = false;
        
        String  tagName  = sbTag.toString().toLowerCase().trim();
        boolean closeTag = tagName.startsWith("/");
        if(closeTag) {
          tagName = tagName.substring(1);
        }
        int sepAttribs = tagName.indexOf(' ');
        if(sepAttribs > 0) {
          tagName = tagName.substring(0, sepAttribs);
        }
        int sepNamespace = tagName.indexOf(':');
        if(sepNamespace >= 0) {
          tagName = tagName.substring(sepNamespace + 1);
        }
        
        if(tagName.equals("text")) {
          if(closeTag) {
            if(range != null) {
              if(isTextSec) {
                range.end = (startTag - 1);
                listResult.add(range);
              }
            }
          }
          else {
            range = new IndexRange(i + 1);
            isTextSec = false;
          }
        }
        else if(tagName.equals("table")) {
          isTextSec = true;
        }
        else if(tagName.equals("paragraph")) {
          isTextSec = true;
        }
        else if(tagName.equals("content")) {
          if(closeTag) {
            if(contentId != null && contentId.length() > 0 && contentIx >= 0 && contentIx < startTag) {
              cda.putContent(contentId, xml.substring(contentIx, startTag));
            }
          }
          else {
            contentId = getValueFromKeyValuePair(sbTag.toString(), "ID");
            contentIx = i + 1;
          }
        }
      }
      else if(isTag) {
        sbTag.append(c);
      }
    }
    
    return listResult;
  }
  
  public static
  String getValueFromKeyValuePair(String text, String key)
  {
    int[] valueBounds = getValueBoundsFromKeyValuePair(text, key, '=');
    if(valueBounds == null || valueBounds.length < 2 || valueBounds[0] < 0) {
      return null;
    }
    String result = text.substring(valueBounds[0], valueBounds[1]);
    return result.replace("\\\"", "\"").replace("\\'", "'").replace("\\n", "\n").replace("\\t", "\t");
  }
  
  public static
  int[] getValueBoundsFromKeyValuePair(String text, String key, char sep)
  {
    if(text == null || text.length() == 0) {
      return new int[] {-1, -1};
    }
    if(key == null || key.length() == 0) {
      return new int[] {-1, -1};
    }
    
    int keyLength  = key.length();
    int textLength = text.length();
    int begValue   = -1;
    int endValue   = -1;
    int currIdx    = 0;
    while(true) {
      currIdx = text.indexOf(key, currIdx);
      if(currIdx < 0) break;
      
      if(currIdx > 0) {
        char c_1 = text.charAt(currIdx - 1);
        if(c_1 > 32) {
          currIdx += keyLength;
          if(currIdx >= textLength) break;
          continue;
        }
      }
      
      int s = currIdx + keyLength;
      if(s < textLength) {
        
        // Find sep
        int idxSep = -1;
        for(int j = s; j < textLength; j++) {
          char c = text.charAt(j);
          if(c == sep) {
            idxSep = j;
            break;
          }
          if(c < 33) continue;
          break;
        }
        
        if(idxSep < 0) {
          currIdx += keyLength;
          if(currIdx >= textLength) break;
          continue;
        }
        
        // Find value
        char wrapp = ' ';
        boolean isValue = false;
        for(int j = idxSep + 1; j < textLength; j++) {
          char c = text.charAt(j);
          if(c < 33 && !isValue) continue;
          if(!isValue) {
            isValue = true;
            if(c == '"' || c == '\'') {
              wrapp = c;
              begValue = j + 1;
            }
            else {
              begValue = j;
            }
          }
          else if(c == wrapp) {
            if(text.charAt(j - 1) == '\\') {
              continue;
            }
            endValue = j;
            break;
          }
        }
        
        if(begValue > 0 && endValue >= begValue) {
          break;
        }
        currIdx += keyLength;
        if(currIdx >= textLength) break;
      }
    }
    if(begValue >= 0 && endValue >= begValue) {
      return new int[] {begValue, endValue};
    }
    return new int[] {-1, -1};
  }
  
  static class IndexRange
  {
    public int start = -1;
    public int end   = -1;
    
    public IndexRange()
    {
    }
    
    public IndexRange(int start)
    {
      this.start = start;
    }
    
    public IndexRange(int start, int end)
    {
      this.start = start;
      this.end   = end;
    }
    
    @Override
    public boolean equals(Object object) {
      if (object instanceof IndexRange) {
        return toString().equals(object.toString());
      }
      return false;
    }
    
    @Override
    public int hashCode() {
      return toString().hashCode();
    }
    
    @Override
    public String toString() {
      return "IndexRange(" + start + "," + end + ")";
    }
  }
}
