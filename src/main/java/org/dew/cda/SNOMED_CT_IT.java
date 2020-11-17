package org.dew.cda;

import org.dew.hl7.ICodeSystem;

public 
class SNOMED_CT_IT implements ICodeSystem
{
  @Override
  public String getCodeSystem() {
    return "2.16.840.1.113883.6.96";
  }
  
  @Override
  public String getCodeSystemName() {
    return "SNOMED-CT";
  }
  
  @Override
  public String getCode(String displayName) {
    if(displayName == null || displayName.length() == 0) {
      return "";
    }
    
    String displayNameLC = displayName.toLowerCase();
    if(displayNameLC.equals("0 rh +")) {
      return "278147001";
    }
    else if(displayNameLC.indexOf("0 rh -") >= 0) {
      return "278148006";
    }
    else if(displayNameLC.equals("a rh +")) {
      return "278149003";
    }
    else if(displayNameLC.indexOf("a rh -") >= 0) {
      return "278152006";
    }
    else if(displayNameLC.equals("b rh +")) {
      return "278150003";
    }
    else if(displayNameLC.indexOf("b rh -") >= 0) {
      return "278153001";
    }
    else if(displayNameLC.equals("ab rh +")) {
      return "278151004";
    }
    else if(displayNameLC.indexOf("ab rh -") >= 0) {
      return "278154007";
    }
    
    return "";
  }
  
  @Override
  public String getDisplayName(String code) {
    if(code == null || code.length() == 0) {
      return "";
    }
    
    if(code.equals("278147001")) return "0 RH +";
    if(code.equals("278148006")) return "0 RH -";
    if(code.equals("278149003")) return "A RH +";
    if(code.equals("278152006")) return "A RH +";
    if(code.equals("278150003")) return "B RH +";
    if(code.equals("278153001")) return "B RH +";
    if(code.equals("278151004")) return "AB RH +";
    if(code.equals("278154007")) return "AB RH +";
    
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
    
    // PSS - Indagini diagnostiche ed esami di laboratorio
    return "2.16.840.1.113883.2.9.10.1.4.2.14";
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
