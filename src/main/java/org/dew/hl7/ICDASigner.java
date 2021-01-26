package org.dew.hl7;

import java.util.Map;

/**
 * Interfaccia per la firma digitale di ClinicalDocument.
 */
public 
interface ICDASigner 
{
  /**
   * Impostazione opzioni di firma digitale.
   * 
   * @param options Opzioni
   */
  public void setOptions(Map<String, Object> options);
  
  /**
   * Firma digitalmente il contenuto.
   * 
   * @param content byte[]
   * @throws Exception Error
   * @return signature
   */
  public byte[] sign(byte[] content) throws Exception;
  
  /**
   * Firma digitalmente il contenuto.
   * 
   * @param content String
   * @throws Exception Error
   * @return signature
   */
  public byte[] sign(String content) throws Exception;
}
