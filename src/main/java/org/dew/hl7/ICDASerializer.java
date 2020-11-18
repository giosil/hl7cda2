package org.dew.hl7;

import java.util.List;
import java.util.Map;

/**
 * Interfaccia per la serializzazione di oggetti ClinicalDocument.
 */
public 
interface ICDASerializer 
{
  /**
   * Impostazione opzioni di serializzazione (es. comments, ecc.)
   * 
   * @param options Opzioni
   */
  public void setOptions(Map<String, Object> options);
  
  /**
   * Impostazione codifiche.
   * 
   * @param codeSystems Codifiche
   */
  public void setCodeSystems(CodeSystems codeSystems);
  
  /**
   * Serializzazione oggetto ClinicalDocument.
   * 
   * @param cda Oggetto ClinicalDocument
   * @return XML
   * @throws Exception Error
   */
  public String toXML(ClinicalDocument cda) throws Exception;
  
  /**
   * Warning generati durante l'ultima serializzazione.
   * 
   * @return Lista di warning
   */
  public List<String> getWarnings();
}
