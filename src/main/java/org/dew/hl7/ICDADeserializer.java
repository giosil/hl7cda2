package org.dew.hl7;

/**
 * Interfaccia per la deserializzazione di file o contenuto xml in ClinicalDocument.
 */
public 
interface ICDADeserializer 
{
  /**
   * Set OID of authority.
   * 
   * @param oidAuthority OID autority (e.g. 2.16.840.1.113883.2.9.2). 
   */
  public void setOIDAuthority(String oidAuthority);
  
  /**
   * Load file or string xml cda.
   * 
   * @param file filePath or content 
   * @throws Exception Error
   */
  public void load(String file) throws Exception;
  
  /**
   * Load content xml cda.
   * 
   * @param content byte[]
   * @throws Exception Error
   */
  public void load(byte[] content) throws Exception;
  
  /**
   * Get ClinicalDocument object deserialized.
   * 
   * @return ClinicalDocument
   */
  public ClinicalDocument getClinicalDocument();
}
