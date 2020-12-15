package org.dew.cda;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.dew.hl7.ClinicalDocument;

public 
class CDAUtils 
{
  public static final String sLOINC_CODE_SYSTEM = "2.16.840.1.113883.6.1";
  
  public static
  String getCode(ClinicalDocument cda)
  {
    if(cda == null) return "";
    String code = cda.getCode();
    if(code != null && code.length() > 0) {
      String prefix = sLOINC_CODE_SYSTEM + ".";
      if(code.startsWith(prefix)) {
        return code.substring(prefix.length());
      }
      return code;
    }
    return "";
  }
  
  public static
  String getNormalizedCode(ClinicalDocument cda)
  {
    if(cda == null) return "";
    String code = cda.getCode();
    if(code != null && code.length() > 0) {
      if(code.length() < 10) {
        return sLOINC_CODE_SYSTEM + "." + code;
      }
      return code;
    }
    return "";
  }
  
  public static
  String getEffectiveTime(ClinicalDocument cda)
  {
    if(cda == null) return getTimestamp(Calendar.getInstance());
    
    return getTimestamp(cda.getEffectiveTime());
  }
  
  public static
  String getConfidentialityCode(ClinicalDocument cda)
  {
    if(cda == null) return "N";
    
    String result = cda.getConfidentialityCode();
    if(result == null || result.length() == 0) {
      cda.setConfidentialityCode("N");
      return "N";
    }
    
    return getExtension(result, "N");
  }
  
  public static
  String getDate(Date date)
  {
    if(date == null) return "";
    
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(date.getTime());
    
    return getDate(cal);
  }
  
  public static
  String getDate(Date date, String sep)
  {
    if(date == null) return "";
    
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(date.getTime());
    
    return getDate(cal, sep);
  }
  
  public static
  String getDate(Calendar cal)
  {
    if(cal == null) return "";
    
    int iYear   = cal.get(java.util.Calendar.YEAR);
    int iMonth  = cal.get(java.util.Calendar.MONTH) + 1;
    int iDay    = cal.get(java.util.Calendar.DATE);
    String sYear   = String.valueOf(iYear);
    String sMonth  = iMonth  < 10 ? "0" + iMonth  : String.valueOf(iMonth);
    String sDay    = iDay    < 10 ? "0" + iDay    : String.valueOf(iDay);
    
    return sYear + sMonth + sDay;
  }
  
  public static
  String getDate(Calendar cal, String sep)
  {
    if(cal == null) return "";
    
    int iYear   = cal.get(java.util.Calendar.YEAR);
    int iMonth  = cal.get(java.util.Calendar.MONTH) + 1;
    int iDay    = cal.get(java.util.Calendar.DATE);
    String sYear   = String.valueOf(iYear);
    String sMonth  = iMonth  < 10 ? "0" + iMonth  : String.valueOf(iMonth);
    String sDay    = iDay    < 10 ? "0" + iDay    : String.valueOf(iDay);
    
    if(sep == null) sep = "";
    
    return sYear + sep + sMonth + sep + sDay;
  }
  
  public static
  String getTimestamp(Date date)
  {
    if(date == null) return "";
    
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(date.getTime());
    
    return getTimestamp(cal);
  }
  
  public static
  String getTimestamp(Calendar cal)
  {
    if(cal == null) return "";
    
    int iYear   = cal.get(java.util.Calendar.YEAR);
    int iMonth  = cal.get(java.util.Calendar.MONTH) + 1;
    int iDay    = cal.get(java.util.Calendar.DATE);
    int iHour   = cal.get(Calendar.HOUR_OF_DAY);
    int iMinute = cal.get(Calendar.MINUTE);
    int iSecond = cal.get(Calendar.SECOND);
    String sYear   = String.valueOf(iYear);
    String sMonth  = iMonth  < 10 ? "0" + iMonth  : String.valueOf(iMonth);
    String sDay    = iDay    < 10 ? "0" + iDay    : String.valueOf(iDay);
    String sHour   = iHour   < 10 ? "0" + iHour   : String.valueOf(iHour);
    String sMinute = iMinute < 10 ? "0" + iMinute : String.valueOf(iMinute);
    String sSecond = iSecond < 10 ? "0" + iSecond : String.valueOf(iSecond);
    String sOffset = "";
    if(TimeZone.getDefault().inDaylightTime(cal.getTime())) {
      sOffset = "+0200";
    }
    else {
      sOffset = "+0100";
    }
    return sYear + sMonth + sDay + sHour + sMinute + sSecond + sOffset;
  }
  
  public static
  String xml(String value)
  {
    if(value == null) return "";
    int length = value.length();
    if(length == 0) return "";
    StringBuilder sb = new StringBuilder(length);
    for(int i = 0; i < length; i++) {
      char c = value.charAt(i);
      if(c == '<')  sb.append("&lt;");   else
        if(c == '>')  sb.append("&gt;");   else
          if(c == '&')  sb.append("&amp;");  else
            if(c > 127) {
              int code = (int) c;
              sb.append("&#" + code + ";");
            }
            else {
              sb.append(c);
            }
    }
    return sb.toString();
  }
  
  // root.extension^description -> root
  public static
  String getDescription(String value, String defaultValue)
  {
    if(value == null || value.length() == 0) {
      return defaultValue;
    }
    int desc = value.lastIndexOf('^');
    if(desc > 0) {
      value = value.substring(desc + 1);
    }
    return defaultValue;
  }
  
  // root.extension^description -> root
  public static
  String getRoot(String value, String defaultValue)
  {
    if(value == null || value.length() == 0) {
      return defaultValue;
    }
    int desc = value.lastIndexOf('^');
    if(desc > 0) {
      value = value.substring(0, desc);
    }
    int sep = value.lastIndexOf('.');
    if(sep < 0) {
      return defaultValue;
    }
    return value.substring(0, sep);
  }
  
  // root.extension^description -> extension
  public static
  String getExtension(String value)
  {
    if(value == null || value.length() == 0) {
      return "";
    }
    int desc = value.lastIndexOf('^');
    if(desc > 0) {
      value = value.substring(0, desc);
    }
    int sep = value.lastIndexOf('.');
    if(sep < 0) {
      return value;
    }
    return value.substring(sep + 1);
  }
  
  // root.extension^description -> extension
  public static
  String getExtension(String value, String defaultValue)
  {
    if(value == null || value.length() == 0) {
      return defaultValue;
    }
    int desc = value.lastIndexOf('^');
    if(desc > 0) {
      value = value.substring(0, desc);
    }
    int sep = value.lastIndexOf('.');
    if(sep < 0) {
      return value;
    }
    return value.substring(sep + 1);
  }
  
  // codeSystem.code^displayName -> displayName
  public static
  String getDisplayName(String value, String defaultValue)
  {
    if(value == null || value.length() == 0) {
      return defaultValue;
    }
    int desc = value.lastIndexOf('^');
    if(desc > 0) {
      value = value.substring(desc + 1);
    }
    return defaultValue;
  }
  
  // codeSystem.code^displayName -> root
  public static
  String getCodeSystem(String value, String defaultValue)
  {
    if(value == null || value.length() == 0) {
      return defaultValue;
    }
    int desc = value.lastIndexOf('^');
    if(desc > 0) {
      value = value.substring(0, desc);
    }
    int sep = value.lastIndexOf('.');
    if(sep < 0) {
      return defaultValue;
    }
    return value.substring(0, sep);
  }
  
  // codeSystem.code^displayName -> code
  public static
  String getCode(String value)
  {
    if(value == null || value.length() == 0) {
      return "";
    }
    int desc = value.lastIndexOf('^');
    if(desc > 0) {
      value = value.substring(0, desc);
    }
    int sep = value.lastIndexOf('.');
    if(sep < 0) {
      return value;
    }
    return value.substring(sep + 1);
  }
}