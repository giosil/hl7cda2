package org.dew.hl7;

/**
 * Interfaccia sistema di codifica.
 */
public 
interface ICodeSystem 
{
  /**
   * Restituisce il codice del sistema di codifica.
   * 
   * @return Codice sistema di codifica.
   */
  public String getCodeSystem();
  
  /**
   * Restituisce il nome del sistema di codifica.
   * 
   * @return Nome sistema di codifica.
   */
  public String getCodeSystemName();
  
  /**
   * Restituisce il codice corrispondente alla descrizione fornita.
   * 
   * @param displayName descrizione
   * @return code
   */
  public String getCode(String displayName);
  
  /**
   * Restituisce la descrizione corrispondente al codice fornito.
   * 
   * @param code Codice
   * @return displayName
   */
  public String getDisplayName(String code);
  
  /**
   * Restituisce l'eventuale template associato al codice fornito.
   * 
   * @param code Codice
   * @return templateId
   */
  public String getTemplateId(String code);
  
  /**
   * Restituisce l'eventuale root template associato al codice fornito.
   * 
   * @param code Codice
   * @return templateIdRoot
   */
  public String getTemplateIdRoot(String code);
}
