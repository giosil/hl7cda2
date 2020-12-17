package org.dew.hl7;

import java.io.InputStream;

/**
 * Interfaccia per la validazione di un documento HL7 CDA.
 */
public 
interface ICDAValidator 
{
  public ValidationResult validate(InputStream inputStream) throws Exception;
  
  public ValidationResult validate(byte[] content) throws Exception;
}
