package org.dew.cda;

import java.util.Calendar;
import java.util.Date;

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
  String getDate(Date date, String pattern)
  {
    if(date == null) return "";
    
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(date.getTime());
    
    return getDate(cal, pattern);
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
  String getDate(Calendar cal, String pattern)
  {
    if(cal == null) return "";
    
    int iYear   = cal.get(java.util.Calendar.YEAR);
    int iMonth  = cal.get(java.util.Calendar.MONTH) + 1;
    int iDay    = cal.get(java.util.Calendar.DATE);
    String sYear   = String.valueOf(iYear);
    String sMonth  = iMonth  < 10 ? "0" + iMonth  : String.valueOf(iMonth);
    String sDay    = iDay    < 10 ? "0" + iDay    : String.valueOf(iDay);
    
    if(pattern == null || pattern.length() == 0) {
      return sYear + sMonth + sDay;
    }
    if(pattern != null && pattern.length() == 2) {
      if(pattern.equalsIgnoreCase("us")) {
        return sMonth + "/" + sDay + "/" + sYear;
      }
      else {
        return sDay + "/" + sMonth + "/" + sYear;
      }
    }
    return sYear + "-" + sMonth + "-" + sDay;
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
    // Get total offset in minutes
    int iZoneOffsetMin = cal.get(Calendar.ZONE_OFFSET) / 60000;
    int iDST_OffsetMin = cal.get(Calendar.DST_OFFSET)  / 60000;
    int iTot_OffsetMin = iZoneOffsetMin + iDST_OffsetMin;
    boolean negOffset  = false;
    if(iTot_OffsetMin < 0) {
      iTot_OffsetMin = iTot_OffsetMin * -1;
      negOffset = true;
    }
    // Format offset
    int iTot_OffsetHH = iTot_OffsetMin / 60;
    int iTot_OffsetMM = iTot_OffsetMin % 60;
    String sOffsetHH  = iTot_OffsetHH < 10 ? "0" + iTot_OffsetHH : String.valueOf(iTot_OffsetHH);
    String sOffsetMM  = iTot_OffsetMM < 10 ? "0" + iTot_OffsetMM : String.valueOf(iTot_OffsetMM);
    String sOffset = negOffset ? "-" + sOffsetHH + sOffsetMM : "+" + sOffsetHH + sOffsetMM;
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
      if(c == '<')  sb.append("&lt;");
      else if(c == '>')  sb.append("&gt;");
      else if(c == '&')  sb.append("&amp;");
      else if(c > 127) {
        int code = (int) c;
        sb.append("&#" + code + ";");
      }
      else {
        sb.append(c);
      }
    }
    return sb.toString();
  }
  
  public static
  String xmlAttrib(String value)
  {
    if(value == null) return "";
    int length = value.length();
    if(length == 0) return "";
    StringBuilder sb = new StringBuilder(length);
    for(int i = 0; i < length; i++) {
      char c = value.charAt(i);
      if(c == '"') sb.append("&quot;");
      else if(c < 32) sb.append(" ");
      else if(c > 127) {
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
  
  public static
  String toString(Object value, String sDefaultValue)
  {
    if(value == null) return sDefaultValue;
    String result = value.toString();
    if(result == null || result.length() == 0) {
      return sDefaultValue;
    }
    return result;
  }
  
  public static
  String toISO8601Timestamp_Z(Calendar c)
  {
    if(c == null) return null;
    
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR,        c.get(Calendar.YEAR));
    cal.set(Calendar.MONTH,       c.get(Calendar.MONTH));
    cal.set(Calendar.DATE,        c.get(Calendar.DATE));
    cal.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
    cal.set(Calendar.MINUTE,      c.get(Calendar.MINUTE));
    cal.set(Calendar.SECOND,      c.get(Calendar.SECOND));
    
    int iZoneOffset = cal.get(Calendar.ZONE_OFFSET);
    cal.add(Calendar.MILLISECOND, -iZoneOffset);
    int iDST_Offset = cal.get(Calendar.DST_OFFSET);
    cal.add(Calendar.MILLISECOND, -iDST_Offset);
    
    int iYear  = cal.get(Calendar.YEAR);
    int iMonth = cal.get(Calendar.MONTH) + 1;
    int iDay   = cal.get(Calendar.DATE);
    int iHour  = cal.get(Calendar.HOUR_OF_DAY);
    int iMin   = cal.get(Calendar.MINUTE);
    int iSec   = cal.get(Calendar.SECOND);
    String sYear   = String.valueOf(iYear);
    String sMonth  = iMonth < 10 ? "0" + iMonth : String.valueOf(iMonth);
    String sDay    = iDay   < 10 ? "0" + iDay   : String.valueOf(iDay);
    String sHour   = iHour  < 10 ? "0" + iHour  : String.valueOf(iHour);
    String sMin    = iMin   < 10 ? "0" + iMin   : String.valueOf(iMin);
    String sSec    = iSec   < 10 ? "0" + iSec   : String.valueOf(iSec);
    if(iYear < 10) {
      sYear = "000" + sYear;
    }
    else if(iYear < 100) {
      sYear = "00" + sYear;
    }
    else if(iYear < 1000) {
      sYear = "0" + sYear;
    }
    return sYear + "-" + sMonth + "-" + sDay + "T" + sHour + ":" + sMin + ":" + sSec + "Z";
  }
}