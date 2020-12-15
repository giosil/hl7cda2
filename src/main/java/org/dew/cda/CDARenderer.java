package org.dew.cda;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dew.hl7.ClinicalDocument;
import org.dew.hl7.ICDARenderer;
import org.dew.hl7.Person;
import org.dew.hl7.Section;

public 
class CDARenderer implements ICDARenderer 
{
  protected String style;
  protected String header;
  protected String footer;
  protected String tableStyle;
  protected String thStyle;
  protected String tdStyle;
  protected String pStyle;
  protected String titleStyle;
  protected String separator;
  
  @Override
  public 
  void setOptions(Map<String, Object> options) 
  {
    if(options == null || options.isEmpty()) {
      return;
    }
    Object optStyle  = options.get("style");
    Object optHeader = options.get("header");
    Object optFooter = options.get("footer");
    Object optTable  = options.get("table");
    Object optTh     = options.get("th");
    Object optTd     = options.get("td");
    Object optTitle  = options.get("title");
    Object optPar    = options.get("paragraph");
    Object optSep    = options.get("separator");
    
    if(optStyle != null) {
      style = optStyle.toString();
    }
    if(optHeader != null) {
      header = optHeader.toString();
    }
    if(optFooter != null) {
      footer = optFooter.toString();
    }
    if(optTable != null) {
      tableStyle = optTable.toString();
    }
    if(optTh != null) {
      thStyle = optTh.toString();
    }
    if(optTd != null) {
      tdStyle = optTd.toString();
    }
    if(optTitle != null) {
      titleStyle = optTitle.toString();
    }
    if(optPar != null) {
      pStyle = optPar.toString();
    }
    if(optSep != null) {
      separator = optSep.toString();
    }
    else {
      separator = "<hr />";
    }
  }
  
  @Override
  public 
  String toHTML(ClinicalDocument cda) 
  {
    StringBuilder sb = new StringBuilder(3000);
    sb.append("<html>\n");
    sb.append("\t<head>\n");
    sb.append("\t\t<title>" + CDAUtils.xml(getTitle(cda)) + "</title>\n");
    if(style != null && style.length() > 0) {
      sb.append("\t\t<style type=\"text/css\">\n");
      sb.append("\t\t\t" + style + "\n");
      sb.append("\t\t</style>\n");
    }
    sb.append("\t</head>\n");
    sb.append("\t<body>\n");
    if(header != null && header.length() > 0) {
      sb.append(header);
    }
    if(cda != null) {
      sb.append(getDocumentSection(cda));
      
      sb.append(getPatientSection(cda));
      
      List<Section> sections = cda.getStructuredBody();
      if(sections != null) {
        for(int i = 0; i < sections.size(); i++) {
          Section section = sections.get(i);
          if(section == null) continue;
          
          String text = section.getText();
          if(text == null || text.length() == 0) continue;
          
          String sectionTitle = section.getTitle();
          if(sectionTitle != null && sectionTitle.length() > 0) {
            String sH1 = "<h1";
            if(titleStyle != null && titleStyle.length() > 0) {
              if(titleStyle.indexOf(':') > 0) {
                sH1 += " style=\"" + titleStyle + "\"";
              }
              else {
                sH1 += " class=\"" + titleStyle + "\"";
              }
            }
            sH1 += ">";
            
            sb.append("\t\t" + sH1 + CDAUtils.xml(sectionTitle) + "</h1>\n");
            sb.append("\t\t<br />\n");
          }
          
          if(pStyle != null && pStyle.length() > 0) {
            if(pStyle.indexOf(':') > 0) {
              text = text.replace("<paragraph>", "<p style=\"" + pStyle + "\">");
            }
            else {
              text = text.replace("<paragraph>", "<p class=\"" + pStyle + "\">");
            }
          }
          else {
            text = text.replace("<paragraph>", "<p>");
          }
          text = text.replace("</paragraph>", "</p>");
          
          if(tableStyle != null && tableStyle.length() > 0) {
            if(tableStyle.indexOf(':') > 0) {
              text = text.replace("<table>", "<table style=\"" + tableStyle + "\">");
            }
            else {
              text = text.replace("<table>", "<table class=\"" + tableStyle + "\">");
            }
          }
          
          if(thStyle != null && thStyle.length() > 0) {
            if(thStyle.indexOf(':') > 0) {
              text = text.replace("<th>", "<th style=\"" + thStyle + "\">");
              text = text.replace("<th/>", "<th style=\"" + thStyle + "\">&nbsp;</th>");
            }
            else {
              text = text.replace("<th>", "<th class=\"" + thStyle + "\">");
              text = text.replace("<th/>", "<th class=\"" + thStyle + "\">&nbsp;</th>");
            }
          }
          
          if(tdStyle != null && tdStyle.length() > 0) {
            if(tdStyle.indexOf(':') > 0) {
              text = text.replace("<td>", "<td style=\"" + tdStyle + "\">");
              text = text.replace("<td/>", "<td style=\"" + tdStyle + "\">&nbsp;</td>");
            }
            else {
              text = text.replace("<td>", "<td class=\"" + tdStyle + "\">");
              text = text.replace("<td/>", "<td class=\"" + tdStyle + "\">&nbsp;</td>");
            }
          }
          
          sb.append("\t\t" + text + "\n");
          if(separator != null && separator.length() > 0) {
            sb.append("\t\t" + separator + "\n");
          }
        }
      }
      
      sb.append(getAuthorSection(cda));
    }
    if(footer != null && footer.length() > 0) {
      sb.append(footer);
    }
    sb.append("\t</body>\n");
    sb.append("</html>\n");
    return sb.toString();
  }
  
  protected
  String getTitle(ClinicalDocument cda)
  {
    if(cda == null) {
      return "Documento";
    }
    String title = cda.getTitle();
    if(title != null && title.length() > 0) {
      return title;
    }
    String displayName = cda.getDisplayName();
    if(displayName != null && displayName.length() > 0) {
      return displayName;
    }
    return "Documento";
  }
  
  protected
  String getDocumentSection(ClinicalDocument cda)
  {
    if(cda == null) return "";
    
    String sDocId = cda.getId();
    if(sDocId == null || sDocId.length() == 0) {
      sDocId = "&nbsp;";
    }
    Date dEffTime   = cda.getEffectiveTime();
    String sEffTime = CDAUtils.getDate(dEffTime, "-");
    if(sEffTime == null || sEffTime.length() == 0) {
      sEffTime = "&nbsp;";
    }
    String sConf = cda.getConfidentialityCode();
    if(sConf == null || sConf.length() == 0) {
      sConf = "N";
    }
    String sVer = cda.getVersionNumber();
    if(sVer == null || sVer.length() == 0) {
      sVer = "&nbsp;";
    }
    
    StringBuilder sb = new StringBuilder();
    String sH1 = "<h1";
    if(titleStyle != null && titleStyle.length() > 0) {
      if(titleStyle.indexOf(':') > 0) {
        sH1 += " style=\"" + titleStyle + "\"";
      }
      else {
        sH1 += " class=\"" + titleStyle + "\"";
      }
    }
    sH1 += ">";
    
    sb.append("\t\t" + sH1 + "Caratteristiche generali documento</h1>\n");
    sb.append("\t\t<br />\n");
    if(tableStyle != null && tableStyle.length() > 0) {
      if(tableStyle.indexOf(':') > 0) {
        sb.append("\t\t<table style=\"" + tableStyle + "\">");
      }
      else {
        sb.append("\t\t<table class=\"" + tableStyle + "\">");
      }
    }
    else {
      sb.append("\t\t<table>");
    }
    
    String sTH = "";
    if(thStyle != null && thStyle.length() > 0) {
      if(thStyle.indexOf(':') > 0) {
        sTH = "<th style=\"" + thStyle + "\">";
      }
      else {
        sTH = "<th class=\"" + thStyle + "\">";
      }
    }
    else {
      sTH = "<th>";
    }
    String sTD = "";
    if(tdStyle != null && tdStyle.length() > 0) {
      if(tdStyle.indexOf(':') > 0) {
        sTD = "<td style=\"" + tdStyle + "\">";
      }
      else {
        sTD = "<td class=\"" + tdStyle + "\">";
      }
    }
    else {
      sTD = "<td>";
    }
    
    sb.append("<tr>");
    sb.append(sTH + "Codice Identificativo</th>");
    sb.append(sTD + sDocId + "</td>");
    sb.append(sTH + "Data</th>");
    sb.append(sTD + sEffTime + "</td>");
    sb.append("</tr>");
    
    sb.append("<tr>");
    sb.append(sTH + "Livello Riservatezza</th>");
    sb.append(sTD + sConf + "</td>");
    sb.append(sTH + "Versione</th>");
    sb.append(sTD + sVer + "</td>");
    sb.append("</tr>");
    
    sb.append("</table>\n");
    if(separator != null && separator.length() > 0) {
      sb.append("\t\t" + separator + "\n");
    }
    return sb.toString();
  }
  
  protected
  String getPatientSection(ClinicalDocument cda)
  {
    if(cda == null) return "";
    
    Person person = cda.getPatient();
    if(person == null) return "";
    
    String sId = person.getId();
    if(sId == null || sId.length() == 0) {
      sId = "&nbsp;";
    }
    String sName   = "";
    String sPref   = person.getPrefix();
    String sFamily = person.getFamily();
    String sGiven  = person.getGiven();
    if(sPref != null && sPref.length() > 0) {
      sName += sPref + " ";
    }
    if(sFamily != null && sFamily.length() > 0) {
      sName += sFamily + " ";
    }
    if(sGiven != null && sGiven.length() > 0) {
      sName += sGiven + " ";
    }
    if(sName == null || sName.length() == 0) {
      sName = "&nbsp;";
    }
    else {
      sName = sName.trim();
    }
    
    StringBuilder sb = new StringBuilder();
    String sH1 = "<h1";
    if(titleStyle != null && titleStyle.length() > 0) {
      if(titleStyle.indexOf(':') > 0) {
        sH1 += " style=\"" + titleStyle + "\"";
      }
      else {
        sH1 += " class=\"" + titleStyle + "\"";
      }
    }
    sH1 += ">";
    
    sb.append("\t\t" + sH1 + "Dati relativi al paziente</h1>\n");
    sb.append("\t\t<br />\n");
    if(tableStyle != null && tableStyle.length() > 0) {
      if(tableStyle.indexOf(':') > 0) {
        sb.append("\t\t<table style=\"" + tableStyle + "\">");
      }
      else {
        sb.append("\t\t<table class=\"" + tableStyle + "\">");
      }
    }
    else {
      sb.append("\t\t<table>");
    }
    
    String sTH = "";
    if(thStyle != null && thStyle.length() > 0) {
      if(thStyle.indexOf(':') > 0) {
        sTH = "<th style=\"" + thStyle + "\">";
      }
      else {
        sTH = "<th class=\"" + thStyle + "\">";
      }
    }
    else {
      sTH = "<th>";
    }
    String sTD = "";
    if(tdStyle != null && tdStyle.length() > 0) {
      if(tdStyle.indexOf(':') > 0) {
        sTD = "<td style=\"" + tdStyle + "\">";
      }
      else {
        sTD = "<td class=\"" + tdStyle + "\">";
      }
    }
    else {
      sTD = "<td>";
    }
    
    sb.append("<tr>");
    sb.append(sTH + "Nominativo</th>");
    sb.append(sTD + sName + "</td>");
    sb.append(sTH + "Identificativo</th>");
    sb.append(sTD + sId + "</td>");
    sb.append("</tr>");
    
    sb.append("</table>\n");
    if(separator != null && separator.length() > 0) {
      sb.append("\t\t" + separator + "\n");
    }
    return sb.toString();
  }
  
  protected
  String getAuthorSection(ClinicalDocument cda)
  {
    if(cda == null) return "";
    
    Person person = cda.getAuthor();
    if(person == null) return "";
    
    String sId = person.getId();
    if(sId == null || sId.length() == 0) {
      sId = "&nbsp;";
    }
    String sName   = "";
    String sPref   = person.getPrefix();
    String sFamily = person.getFamily();
    String sGiven  = person.getGiven();
    if(sPref != null && sPref.length() > 0) {
      sName += sPref + " ";
    }
    if(sFamily != null && sFamily.length() > 0) {
      sName += sFamily + " ";
    }
    if(sGiven != null && sGiven.length() > 0) {
      sName += sGiven + " ";
    }
    if(sName == null || sName.length() == 0) {
      sName = "&nbsp;";
    }
    else {
      sName = sName.trim();
    }
    
    StringBuilder sb = new StringBuilder();
    String sH1 = "<h1";
    if(titleStyle != null && titleStyle.length() > 0) {
      if(titleStyle.indexOf(':') > 0) {
        sH1 += " style=\"" + titleStyle + "\"";
      }
      else {
        sH1 += " class=\"" + titleStyle + "\"";
      }
    }
    sH1 += ">";
    
    sb.append("\t\t" + sH1 + "Dati autore del documento</h1>\n");
    sb.append("\t\t<br />\n");
    if(tableStyle != null && tableStyle.length() > 0) {
      if(tableStyle.indexOf(':') > 0) {
        sb.append("\t\t<table style=\"" + tableStyle + "\">");
      }
      else {
        sb.append("\t\t<table class=\"" + tableStyle + "\">");
      }
    }
    else {
      sb.append("\t\t<table>");
    }
    
    String sTH = "";
    if(thStyle != null && thStyle.length() > 0) {
      if(thStyle.indexOf(':') > 0) {
        sTH = "<th style=\"" + thStyle + "\">";
      }
      else {
        sTH = "<th class=\"" + thStyle + "\">";
      }
    }
    else {
      sTH = "<th>";
    }
    String sTD = "";
    if(tdStyle != null && tdStyle.length() > 0) {
      if(tdStyle.indexOf(':') > 0) {
        sTD = "<td style=\"" + tdStyle + "\">";
      }
      else {
        sTD = "<td class=\"" + tdStyle + "\">";
      }
    }
    else {
      sTD = "<td>";
    }
    
    sb.append("<tr>");
    sb.append(sTH + "Nominativo</th>");
    sb.append(sTD + sName + "</td>");
    sb.append(sTH + "Identificativo</th>");
    sb.append(sTD + sId + "</td>");
    sb.append("</tr>");
    
    sb.append("</table>\n");
    if(separator != null && separator.length() > 0) {
      sb.append("\t\t" + separator + "\n");
    }
    return sb.toString();
  }
}
