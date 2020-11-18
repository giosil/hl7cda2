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
   * Rendering oggetto ClinicalDocument.
   * 
   * @param cda Oggetto ClinicalDocument
   * @return HTML
   */
  public String toHTML(ClinicalDocument cda);
}

