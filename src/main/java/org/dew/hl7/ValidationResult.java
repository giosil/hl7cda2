package org.dew.hl7;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public 
class ValidationResult implements Serializable, ErrorHandler 
{
  private static final long serialVersionUID = 7828908593445728974L;
  
  protected List<String> warnings;
  protected List<String> errors;
  protected List<String> fatals;
  
  public ValidationResult()
  {
    warnings = new ArrayList<String>();
    errors   = new ArrayList<String>();
    fatals   = new ArrayList<String>();
  }
  
  public ValidationResult(String error)
  {
    warnings = new ArrayList<String>();
    errors   = new ArrayList<String>();
    fatals   = new ArrayList<String>();
    if(error != null && error.length() > 0) {
      errors.add(error);
    }
  }
  
  public ValidationResult(Exception ex)
  {
    warnings = new ArrayList<String>();
    errors   = new ArrayList<String>();
    fatals   = new ArrayList<String>();
    if(ex != null) {
      String error = ex.getMessage();
      if(error == null || error.length() == 0) {
        error = ex.toString();
      }
      errors.add(error);
    }
  }
  
  public List<String> getWarnings() {
    return warnings;
  }
  
  public List<String> getErrors() {
    return errors;
  }
  
  public List<String> getFatals() {
    return fatals;
  }
  
  public 
  boolean isSuccess() 
  {
    if(errors != null && errors.size() > 0) return false;
    if(fatals != null && fatals.size() > 0) return false;
    return true;
  }
  
  public 
  int getWarningsCount() 
  {
    if(warnings == null) return 0;
    return warnings.size();
  }
  
  public 
  int getErrorsCount() 
  {
    if(errors == null) return 0;
    return errors.size();
  }
  
  public 
  int getFatalsCount() 
  {
    if(fatals == null) return 0;
    return fatals.size();
  }
  
  public 
  ValidationResult clear() 
  {
    if(warnings == null) warnings = new ArrayList<String>();
    if(errors == null) errors = new ArrayList<String>();
    if(fatals == null) fatals = new ArrayList<String>();
    
    warnings.clear();
    errors.clear();
    fatals.clear();
    
    return this;
  }
  
  public 
  ValidationResult addWarning(String text) 
  {
    if(text == null || text.length() == 0) return this;
    if(warnings == null) warnings = new ArrayList<String>();
    warnings.add(text);
    
    return this;
  }
  
  public 
  ValidationResult addError(String text) 
  {
    if(text == null || text.length() == 0) return this;
    if(errors == null) errors = new ArrayList<String>();
    errors.add(text);
    
    return this;
  }
  
  public 
  ValidationResult addFatal(String text) 
  {
    if(text == null || text.length() == 0) return this;
    if(fatals == null) fatals = new ArrayList<String>();
    fatals.add(text);
    
    return this;
  }
  
  public 
  ValidationResult addWarnings(List<String> list) 
  {
    if(list == null || list.size() == 0) return this;
    if(warnings == null) warnings = new ArrayList<String>();
    warnings.addAll(list);
    
    return this;
  }
  
  public 
  ValidationResult addErrors(List<String> list) 
  {
    if(list == null || list.size() == 0) return this;
    if(errors == null) errors = new ArrayList<String>();
    errors.addAll(list);
    
    return this;
  }
  
  public 
  ValidationResult addFatals(List<String> list) 
  {
    if(list == null || list.size() == 0) return this;
    if(fatals == null) fatals = new ArrayList<String>();
    fatals.addAll(list);
    
    return this;
  }
  
  public 
  ValidationResult add(ValidationResult validationResult)
  {
    if(validationResult == null) return this;
    addWarnings(validationResult.getWarnings());
    addErrors(validationResult.getErrors());
    addFatals(validationResult.getFatals());
    
    return this;
  }
  
  @Override
  public 
  void warning(SAXParseException e) 
    throws SAXException 
  {
    addWarning(e.getLineNumber() + "," + e.getColumnNumber() + " " + e.getMessage());
  }
  
  @Override
  public 
  void error(SAXParseException e) 
    throws SAXException 
  {
    addError(e.getLineNumber() + "," + e.getColumnNumber() + " " + e.getMessage());
  }
  
  @Override
  public 
  void fatalError(SAXParseException e) 
    throws SAXException 
  {
    addFatal(e.getLineNumber() + "," + e.getColumnNumber() + " " + e.getMessage());
  }
}
