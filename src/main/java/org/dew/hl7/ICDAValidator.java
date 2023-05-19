package org.dew.hl7;

import java.io.InputStream;

/**
 * Interfaccia per la validazione di un documento HL7 CDA.
 */
public 
interface ICDAValidator 
{
  public ValidationResult validate(InputStream inputStream) throws Exception;
  
  public ValidationResult validate(InputStream inputStream, String schematron) throws Exception;
  
  public ValidationResult validate(byte[] content) throws Exception;
  
  public ValidationResult validate(byte[] content, String schematron) throws Exception;
}
