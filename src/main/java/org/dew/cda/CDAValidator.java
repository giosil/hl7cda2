package org.dew.cda;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dew.hl7.ICDAValidator;
import org.dew.hl7.ValidationResult;
import org.w3c.dom.Document;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.svrl.jaxb.FailedAssert;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import com.helger.schematron.svrl.jaxb.Text;
import com.helger.schematron.xslt.SchematronResourceSCH;
import com.helger.xml.serialize.read.DOMReader;

public 
class CDAValidator implements ICDAValidator 
{
  @Override
  public 
  ValidationResult validate(InputStream inputStream) 
    throws Exception 
  {
    ValidationResult result = new ValidationResult();
    
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    
    Schema schema = schemaFactory.newSchema(getCDASchema());
    
    Validator validator = schema.newValidator();
    
    validator.setErrorHandler(result);
    
    Source source = new StreamSource(inputStream);
    
    validator.validate(source);
    
    return result;
  }
  
  @Override
  public 
  ValidationResult validate(InputStream inputStream, String schematron) 
    throws Exception 
  {
    ValidationResult result = new ValidationResult();
    
    ISchematronResource schematronResource = getSchematronResource(schematron);
    
    if(schematronResource != null && schematronResource.isValidSchematron()) {
      
      Document document = DOMReader.readXMLDOM(inputStream);
      
//      com.helger.commons.state.EValidity eValidity = schematronResource.getSchematronValidity(new DOMSource(document));
//      if(eValidity != null && eValidity.isInvalid()) {
//        // Invalid document
//      }
      
      SchematronOutputType schematronOutputType = schematronResource.applySchematronValidationToSVRL(new DOMSource(document));
      
      List<String> messages = getSchematronMessages(schematronOutputType);
      
      result.addErrors(messages);
    }
    
    return result;
  }
  
  @Override
  public 
  ValidationResult validate(byte[] content) 
    throws Exception 
  {
    ValidationResult result = new ValidationResult();
    
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    
    Schema schema = schemaFactory.newSchema(getCDASchema());
    
    Validator validator = schema.newValidator();
    
    validator.setErrorHandler(result);
    
    Source source = new StreamSource(new ByteArrayInputStream(content));
    
    validator.validate(source);
    
    return result;
  }
  
  @Override
  public 
  ValidationResult validate(byte[] content, String schematron) 
    throws Exception 
  {
    ValidationResult result = new ValidationResult();
    
    ISchematronResource schematronResource = getSchematronResource(schematron);
    
    if(schematronResource != null && schematronResource.isValidSchematron()) {
      
      Document document = DOMReader.readXMLDOM(content);
      
//      com.helger.commons.state.EValidity eValidity = schematronResource.getSchematronValidity(new DOMSource(document));
//      if(eValidity != null && eValidity.isInvalid()) {
//        // Invalid document
//      }
      
      SchematronOutputType schematronOutputType = schematronResource.applySchematronValidationToSVRL(new DOMSource(document));
      
      List<String> messages = getSchematronMessages(schematronOutputType);
      
      result.addErrors(messages);
    }
    
    return result;
  }
  
  protected
  java.net.URL getCDASchema()
  {
    return Thread.currentThread().getContextClassLoader().getResource("xsd/hl7/infrastructure/cda/C32_CDA.xsd");
  }
  
  protected
  ISchematronResource getSchematronResource(String schematron)
  {
    if(schematron == null || schematron.length() == 0) {
      return null;
    }
    int sep = schematron.indexOf('/');
    if(sep < 0) sep = schematron.indexOf('\\');
    if(sep < 0) {
      try {
        return new SchematronResourceSCH(new ClassPathResource("sch/" + schematron));
      }
      catch(Exception ex) {
        System.err.println("Resource schematron " + schematron + " not available");
        return null;
      }
    }
    File file = new File(schematron);
    if(!file.exists()) return null;
    try {
      return new SchematronResourceSCH(new FileSystemResource(schematron));
    }
    catch(Exception ex) {
      System.err.println("File schematron " + schematron + " not available");
    }
    return null;
  }
  
  protected
  List<String> getSchematronMessages(SchematronOutputType schematronOutputType)
  {
    List<String> result = new ArrayList<String>();
    if(schematronOutputType == null) {
      return result;
    }
    List<Object> failedAsserts = schematronOutputType.getActivePatternAndFiredRuleAndFailedAssert();
    if(failedAsserts == null || failedAsserts.size() == 0) {
      return result;
    }
    for(Object objAssert : failedAsserts) {
      if(objAssert instanceof FailedAssert) {
        FailedAssert failedAssert = (FailedAssert) objAssert;
        Text text = failedAssert.getText();
        if(text != null) {
          int count = text.getContentCount();
          for(int i = 0; i < count; i++) {
            Object content = text.getContentAtIndex(i);
            if(content == null) continue;
            String item = content.toString();
            if(item == null || item.length() == 0) continue;
            result.add(item);
          }
        }
      }
    }
    return result;
  }
}
