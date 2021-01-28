package org.dew.hl7;

import java.security.cert.X509Certificate;
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
  
  /**
   * Verifica la firma digitale.
   * 
   * @param signed byte[]
   * @throws Exception Error
   * @return null if not valid, X509Certificate instance if valid.
   */
  public X509Certificate validate(byte[] signed) throws Exception;
  
  /**
   * Verifica la firma digitale.
   * 
   * @param signed String
   * @throws Exception Error
   * @return null if not valid, X509Certificate instance if valid.
   */
  public X509Certificate validate(String signed) throws Exception;
  
  /**
   * Estra il contenuto dalla firma digitale.
   * 
   * @param signed byte[]
   * @throws Exception Error
   * @return content
   */
  public byte[] extract(byte[] signed) throws Exception;
}
