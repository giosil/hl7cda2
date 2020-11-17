package org.dew.hl7;

import java.util.HashMap;
import java.util.Map;

public 
class CodeSystems 
{
  protected static Map<String, ICodeSystem> mapInstances = new HashMap<String, ICodeSystem>();
  
  public static String LOINC     = "LOINC";
  public static String ICD9CM    = "ICD9CM";
  public static String SNOMED_CT = "SNOMED_CT";
  public static String CATALOG   = "CATALOG";
  public static String AIC       = "AIC";
  public static String ATC       = "ATC";
  public static String GE        = "GE";
  
  public CodeSystems()
  {
  }
  
  public
  void register(String name, ICodeSystem codeSystem)
  {
    if(name == null || name.length() == 0) {
      return;
    }
    
    String nameUC = name.toUpperCase();
    
    mapInstances.put(nameUC, codeSystem);
  }
  
  public
  ICodeSystem getCodeSystem(String name)
  {
    if(name == null || name.length() == 0) {
      return null;
    }
    
    String nameUC = name.toUpperCase();
    
    return mapInstances.get(nameUC);
  }
  
  public
  ICodeSystem getCodeSystem(String authorityCode, String name)
  {
    if(name == null || name.length() == 0) {
      return null;
    }
    
    String nameUC = name.toUpperCase();
    
    if(authorityCode != null && authorityCode.length() > 0) {
      ICodeSystem result = mapInstances.get(authorityCode + "." + name);
      
      if(result != null) return result;
    }
    
    return mapInstances.get(nameUC);
  }
}
