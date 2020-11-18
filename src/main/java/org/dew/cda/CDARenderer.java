package org.dew.cda;

import java.util.List;
import java.util.Map;

import org.dew.hl7.ClinicalDocument;
import org.dew.hl7.ICDARenderer;
import org.dew.hl7.Section;

public 
class CDARenderer implements ICDARenderer 
{
  protected String style;
  protected String header;
  protected String footer;
  
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
    
    if(optStyle != null) {
      style = optStyle.toString();
    }
    if(optHeader != null) {
      header = optHeader.toString();
    }
    if(optFooter != null) {
      footer = optFooter.toString();
    }
  }
  
  @Override
  public 
  String toHTML(ClinicalDocument cda) 
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<head>");
    sb.append("<title>" + CDAUtils.xml(getTitle(cda)) + "</title>");
    if(style != null && style.length() > 0) {
      sb.append("<style type=\"text/css\">");
      sb.append(style);
      sb.append("</style>");
    }
    sb.append("</head>");
    sb.append("<body>");
    if(header != null && header.length() > 0) {
      sb.append(header);
    }
    if(cda != null) {
      List<Section> sections = cda.getStructuredBody();
      if(sections != null) {
        for(int i = 0; i < sections.size(); i++) {
          Section section = sections.get(i);
          if(section == null) continue;
          
          String text = section.getText();
          if(text == null || text.length() == 0) continue;
          
          String sectionTitle = section.getTitle();
          if(sectionTitle != null && sectionTitle.length() > 0) {
            sb.append("<h1>" + CDAUtils.xml(sectionTitle) + "</h1>");
            sb.append("<br />");
          }
          
          sb.append(text);
          sb.append("<hr />");
        }
      }
    }
    if(footer != null && footer.length() > 0) {
      sb.append(footer);
    }
    sb.append("</body>");
    sb.append("</html>");
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
}

