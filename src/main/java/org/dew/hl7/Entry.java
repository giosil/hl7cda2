package org.dew.hl7;

import java.io.Serializable;

import java.util.Date;

public 
class Entry implements Serializable
{
  private static final long serialVersionUID = -8969866182850025459L;
  
  private String id;
  private String typeCode;
  private String templateId;
  private String code;
  private String codeSystem;
  private String codeSystemName;
  private String displayName;
  private String nullFlavor;
  private String statusCode;
  private String methodCode;
  private Date   effectiveTime;
  private Date   low;
  private Date   high;
  private String value;
  private String type;
  private String unit;
  private String text;
  private String reference;
  private String manufacturedProduct;
  private String participant;
  private String externalDocument;
  private String mediaId;
  private String mediaType;
  private String mediaContent;
  
  public Entry()
  {
  }
  
  public Entry(String id)
  {
    this.id = id;
  }
  
  public Entry(String code, String value)
  {
    this.code  = code;
    this.value = value;
    
    if(value != null) {
      value = value.trim();
      if(value.length() > 2 && Character.isDigit(value.charAt(0))) {
        int sep = value.indexOf(' ');
        if(sep > 0) {
          this.value = value.substring(0,sep);
          this.unit  = value.substring(sep+1);
        }
      }
    }
  }
  
  public Entry(String code, String value, String displayName)
  {
    this.code  = code;
    this.value = value;
    this.displayName = displayName;
    
    if(value != null) {
      value = value.trim();
      if(value.length() > 2 && Character.isDigit(value.charAt(0))) {
        int sep = value.indexOf(' ');
        if(sep > 0) {
          this.value = value.substring(0,sep);
          this.unit  = value.substring(sep+1);
        }
      }
    }
  }
  
  public Entry(String code, String value, String displayName, String unit)
  {
    this.code  = code;
    this.value = value;
    this.displayName = displayName;
    this.unit  = unit;
    
    if(unit == null || unit.length() == 0) {
      if(value != null) {
        value = value.trim();
        if(value.length() > 2 && Character.isDigit(value.charAt(0))) {
          int sep = value.indexOf(' ');
          if(sep > 0) {
            this.value = value.substring(0,sep);
            this.unit  = value.substring(sep+1);
          }
        }
      }
    }
  }
  
  public Entry(String mediaId, String mediaType, byte[] mediaContent)
  {
    this.mediaId   = mediaId;
    this.mediaType = mediaType;
    if(mediaContent != null && mediaContent.length > 0) {
      this.mediaContent = new String(Base64Coder.encode(mediaContent));
    }
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTypeCode() {
    return typeCode;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
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

  public String getNullFlavor() {
    return nullFlavor;
  }

  public void setNullFlavor(String nullFlavor) {
    this.nullFlavor = nullFlavor;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getMethodCode() {
    return methodCode;
  }

  public void setMethodCode(String methodCode) {
    this.methodCode = methodCode;
  }

  public Date getEffectiveTime() {
    return effectiveTime;
  }

  public void setEffectiveTime(Date effectiveTime) {
    this.effectiveTime = effectiveTime;
  }

  public Date getLow() {
    return low;
  }

  public void setLow(Date low) {
    this.low = low;
  }

  public Date getHigh() {
    return high;
  }

  public void setHigh(Date high) {
    this.high = high;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getManufacturedProduct() {
    return manufacturedProduct;
  }

  public void setManufacturedProduct(String manufacturedProduct) {
    this.manufacturedProduct = manufacturedProduct;
  }

  public String getParticipant() {
    return participant;
  }

  public void setParticipant(String participant) {
    this.participant = participant;
  }

  public String getExternalDocument() {
    return externalDocument;
  }

  public void setExternalDocument(String externalDocument) {
    this.externalDocument = externalDocument;
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

  @Override
  public boolean equals(Object object) {
    if (object instanceof Entry) {
      String sId    = ((Entry) object).getId();
      String sCode  = ((Entry) object).getCode();
      String sValue = ((Entry) object).getValue();
      String sMedId = ((Entry) object).getMediaId();
      String sKey = sId + ":" + sCode + ":" + sValue + ":" + sMedId;
      return sKey.equals(id  + ":" + code  + ":" + value + ":" + mediaId);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    String key = id + ":" + code + ":" + value + ":" + mediaId;
    return key.hashCode();
  }
  
  @Override
  public String toString() {
    return "Entry(" + id + "," + code + "," + mediaId + ")";
  }
}