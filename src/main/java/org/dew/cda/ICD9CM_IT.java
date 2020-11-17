package org.dew.cda;

import org.dew.hl7.ICodeSystem;

public 
class ICD9CM_IT implements ICodeSystem 
{
  @Override
  public String getCodeSystem() {
    return "2.16.840.1.113883.6.103";
  }
  
  @Override
  public String getCodeSystemName() {
    return "ICD9CM";
  }
  
  @Override
  public String getCode(String displayName) {
    if(displayName == null || displayName.length() == 0) {
      return "";
    }
    
    String displayNameLC = displayName.toLowerCase();
    if(displayNameLC.indexOf("diabete mel") >= 0) {
      return "250";
    }
    else if(displayNameLC.indexOf("ipertensione ess") >= 0) {
      return "401";
    }
    
    return "";
  }
  
  @Override
  public String getDisplayName(String code) {
    if(code == null || code.length() == 0) {
      return "";
    }
    
    if(code.equals("250")) return "DIABETE MELLITO";
    if(code.equals("401")) return "IPERTENSIONE ESSENZIALE";
    
    return "";
  }
  
  @Override
  public String getTemplateId(String code) {
    if(code == null || code.length() == 0) {
      return "";
    }
    
    return "";
  }
  
  @Override
  public String getTemplateIdRoot(String code) {
    if(code == null || code.length() == 0) {
      return "";
    }
    
    // PSS - Lista dei Problemi
    return "2.16.840.1.113883.2.9.10.1.4.3.4.1";
  }
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof ICodeSystem) {
      String objeCodeSystem = ((ICodeSystem) object).getCodeSystem();
      String thisCodeSystem = getCodeSystem();
      if(objeCodeSystem == null && thisCodeSystem == null) return true;
      return objeCodeSystem != null && thisCodeSystem.equals(objeCodeSystem);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    String thisCodeSystem = getCodeSystem();
    return thisCodeSystem != null ? thisCodeSystem.hashCode() : 0;
  }
  
  @Override
  public String toString() {
    return getCodeSystemName();
  }
}
