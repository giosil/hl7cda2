package org.dew.hl7;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public 
class Section implements Serializable
{
  private static final long serialVersionUID = 562136808492921337L;
  
  private String id;
  private String templateId;
  private String code;
  private String codeSystem;
  private String codeSystemName;
  private String displayName;
  private String title;
  private String text;
  private String mediaId;
  private String mediaType;
  private String mediaContent;
  private List<Entry> entries;
  
  public Section()
  {
  }
  
  public Section(String id)
  {
    this.id = id;
  }
  
  public Section(String id, String title)
  {
    this.id    = id;
    this.title = title;
  }
  
  public Section(String id, String mediaType, String mediaContent)
  {
    this.id           = id;
    this.mediaType    = mediaType;
    this.mediaContent = mediaContent;
  }
  
  public Section(String id, String mediaType, byte[] mediaContent)
  {
    this.id           = id;
    this.mediaType    = mediaType;
    if(mediaContent != null && mediaContent.length > 0) {
      this.mediaContent = new String(Base64Coder.encode(mediaContent));
    }
  }
  
  public Section(String id, String title, String templateId, String code, String displayName)
  {
    this.id          = id;
    this.title       = title;
    this.templateId  = templateId;
    this.code        = code;
    this.displayName = displayName;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getTemplateId() {
    return templateId;
  }
  
  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public String getCodeSystem() {
    return codeSystem;
  }
  
  public void setCodeSystem(String codeSystem) {
    this.codeSystem = codeSystem;
  }
  
  public String getCodeSystemName() {
    return codeSystemName;
  }
  
  public void setCodeSystemName(String codeSystemName) {
    this.codeSystemName = codeSystemName;
  }
  
  public String getDisplayName() {
    return displayName;
  }
  
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public String getMediaId() {
    return mediaId;
  }
  
  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }
  
  public String getMediaType() {
    return mediaType;
  }
  
  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }
  
  public String getMediaContent() {
    return mediaContent;
  }
  
  public void setMediaContent(String mediaContent) {
    this.mediaContent = mediaContent;
  }
  
  public List<Entry> getEntries() {
    return entries;
  }
  
  public void setEntries(List<Entry> entries) {
    this.entries = entries;
  }
  
  // Utilities
  
  public Section addEntry(Entry entry) {
    if(entry == null) {
      return this;
    }
    
    if(entries == null) {
      entries = new ArrayList<Entry>();
    }
    
    entries.add(entry);
    
    return this;
  }
  
  public Section addEntry(String code, String value) {
    if(value == null || value.length() == 0) {
      return this;
    }
    
    Entry entry = new Entry(code, value);
    
    if(entries == null) {
      entries = new ArrayList<Entry>();
    }
    
    entries.add(entry);
    
    return this;
  }
  
  public Section addEntry(String code, String value, String displayName) {
    if(value == null || value.length() == 0) {
      return this;
    }
    
    Entry entry = new Entry(code, value, displayName);
    
    if(entries == null) {
      entries = new ArrayList<Entry>();
    }
    
    entries.add(entry);
    
    return this;
  }
  
  public Section addEntry(String code, String value, String displayName, int doseQuantity, Date effectiveTime) {
    if(value == null || value.length() == 0) {
      return this;
    }
    
    Entry entry = new Entry(code, value, displayName, doseQuantity, effectiveTime);
    
    if(entries == null) {
      entries = new ArrayList<Entry>();
    }
    
    entries.add(entry);
    
    return this;
  }
  
  public Section addEntry(String code, String value, String displayName, String unit) {
    if(value == null || value.length() == 0) {
      return this;
    }
    
    Entry entry = new Entry(code, value, displayName, unit);
    
    if(entries == null) {
      entries = new ArrayList<Entry>();
    }
    
    entries.add(entry);
    
    return this;
  }
  
  public Section addEntry(String code, String[] asValDescUnit) {
    if(asValDescUnit == null || asValDescUnit.length == 0) {
      return this;
    }
    
    if(asValDescUnit.length == 1) {
      
      return addEntry(code, asValDescUnit[0]);
      
    }
    else if(asValDescUnit.length == 2) {
      
      return addEntry(code, asValDescUnit[0], asValDescUnit[1]);
      
    }
    
    return addEntry(code, asValDescUnit[0], asValDescUnit[1], asValDescUnit[2]);
  }
  
  public Section addEntry(String mediaId, String mediaType, byte[] mediaContent) {
    if(mediaContent == null || mediaContent.length == 0) {
      return this;
    }
    
    Entry entry = new Entry(mediaId, mediaType, mediaContent);
    
    if(entries == null) {
      entries = new ArrayList<Entry>();
    }
    
    entries.add(entry);
    
    return this;
  }
  
  // Overrides
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof Section) {
      String sId = ((Section) object).getId();
      if(sId == null && id == null) return true;
      return sId != null && sId.equals(id);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    if(id != null) return id.hashCode();
    return 0;
  }
  
  @Override
  public String toString() {
    return "Section(" + id + ")";
  }
}
