package org.dew.cda;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.XMLConstants;

import javax.xml.transform.stream.StreamSource;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dew.hl7.ICDAValidator;
import org.dew.hl7.ValidationResult;

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
    
    validator.validate(new StreamSource(inputStream));
    
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
    
    validator.validate(new StreamSource(new ByteArrayInputStream(content)));
    
    return result;
  }
  
  protected
  java.net.URL getCDASchema()
  {
    return Thread.currentThread().getContextClassLoader().getResource("xsd/hl7/infrastructure/cda/C32_CDA.xsd");
  }
}
