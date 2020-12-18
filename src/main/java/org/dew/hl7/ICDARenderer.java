package org.dew.hl7;

import java.util.Map;

/**
 * Interfaccia per il rendering di oggetti ClinicalDocument.
 */
public 
interface ICDARenderer 
{
  /**
   * Impostazione opzioni di rendering.
   * 
   * @param options Opzioni
   */
  public void setOptions(Map<String, Object> options);
  
  /**
   * Trasforma il contenuto tramite file stylesheet.
   * 
   * @param content byte[]
   * @throws Exception Error
   * @return transformation
   */
  public String transform(byte[] content, String xslFile) throws Exception;
  
  /**
   * Trasforma il contenuto tramite file stylesheet.
   * 
   * @param content String
   * @throws Exception Error
   * @return transformation
   */
  public String transform(String content, String xslFile) throws Exception;
  
  /**
   * Rendering oggetto ClinicalDocument.
   * 
   * @param cda Oggetto ClinicalDocument
   * @return HTML
   */
  public String toHTML(ClinicalDocument cda);
}

