package org.dew.hl7;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public 
class ClinicalDocument implements Serializable
{
  private static final long serialVersionUID = 5884829576975382082L;
  
  private String authorityCode;
  private String authorityName;
  
  private String reference;
  
  private String stylesheet;
  private String realmCode;
  private String typeId;
  private String typeIdRoot;
  private String templateId;
  private String templateIdRoot;
  private String id;
  private String idRoot;
  private String code;
  private String displayName;
  private String translationCode;
  private String title;
  private Date   effectiveTime;
  private String confidentialityCode;
  private String languageCode;
  private String setId;
  private String setIdRoot;
  private String versionNumber;
  
  private Person patient;
  private Person guardian;
  private Person author;
  private Person legalAuthenticator;
  private Person dataEnterer;
  private Person informant;
  
  private Organization custodian;
  private Organization scopingOrg;
  private Organization participant;
  
  private String inFulfillmentOf;
  private String documentationOf;
  private String relatedDocumentId;
  private String relatedDocumentType;
  
  private String nonXMLBodyType;
  private String nonXMLBodyContent;
  
  private String signature;
  
  private List<Section> structuredBody;
  private Map<String, String> contents;
  
  public ClinicalDocument()
  {
  }
  
  public ClinicalDocument(String authorityCode)
  {
    this.authorityCode = authorityCode;
  }
  
  public ClinicalDocument(String authorityCode, String authorityName)
  {
    this.authorityCode = authorityCode;
    this.authorityName = authorityName;
  }

  public String getAuthorityCode() {
    return authorityCode;
  }

  public void setAuthorityCode(String authorityCode) {
    this.authorityCode = authorityCode;
  }

  public String getAuthorityName() {
    return authorityName;
  }

  public void setAuthorityName(String authorityName) {
    this.authorityName = authorityName;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getStylesheet() {
    return stylesheet;
  }

  public void setStylesheet(String stylesheet) {
    this.stylesheet = stylesheet;
  }

  public String getRealmCode() {
    return realmCode;
  }

  public void setRealmCode(String realmCode) {
    this.realmCode = realmCode;
  }

  public String getTypeId() {
    return typeId;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  public String getTypeIdRoot() {
    return typeIdRoot;
  }

  public void setTypeIdRoot(String typeIdRoot) {
    this.typeIdRoot = typeIdRoot;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getTemplateIdRoot() {
    return templateIdRoot;
  }

  public void setTemplateIdRoot(String templateIdRoot) {
    this.templateIdRoot = templateIdRoot;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIdRoot() {
    return idRoot;
  }

  public void setIdRoot(String idRoot) {
    this.idRoot = idRoot;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getTranslationCode() {
    return translationCode;
  }

  public void setTranslationCode(String translationCode) {
    this.translationCode = translationCode;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getEffectiveTime() {
    return effectiveTime;
  }

  public void setEffectiveTime(Date effectiveTime) {
    this.effectiveTime = effectiveTime;
  }

  public String getConfidentialityCode() {
    return confidentialityCode;
  }

  public void setConfidentialityCode(String confidentialityCode) {
    this.confidentialityCode = confidentialityCode;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  public String getSetId() {
    return setId;
  }

  public void setSetId(String setId) {
    this.setId = setId;
  }

  public String getSetIdRoot() {
    return setIdRoot;
  }

  public void setSetIdRoot(String setIdRoot) {
    this.setIdRoot = setIdRoot;
  }

  public String getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
  }

  public Person getPatient() {
    return patient;
  }

  public void setPatient(Person patient) {
    this.patient = patient;
  }

  public Person getGuardian() {
    return guardian;
  }

  public void setGuardian(Person guardian) {
    this.guardian = guardian;
  }

  public Person getAuthor() {
    return author;
  }

  public void setAuthor(Person author) {
    this.author = author;
  }

  public Person getLegalAuthenticator() {
    return legalAuthenticator;
  }

  public void setLegalAuthenticator(Person legalAuthenticator) {
    this.legalAuthenticator = legalAuthenticator;
  }

  public Person getDataEnterer() {
    return dataEnterer;
  }

  public void setDataEnterer(Person dataEnterer) {
    this.dataEnterer = dataEnterer;
  }

  public Person getInformant() {
    return informant;
  }

  public void setInformant(Person informant) {
    this.informant = informant;
  }

  public Organization getCustodian() {
    return custodian;
  }

  public void setCustodian(Organization custodian) {
    this.custodian = custodian;
  }

  public Organization getScopingOrg() {
    return scopingOrg;
  }

  public void setScopingOrg(Organization scopingOrg) {
    this.scopingOrg = scopingOrg;
  }

  public Organization getParticipant() {
    return participant;
  }

  public void setParticipant(Organization participant) {
    this.participant = participant;
  }

  public String getInFulfillmentOf() {
    return inFulfillmentOf;
  }

  public void setInFulfillmentOf(String inFulfillmentOf) {
    this.inFulfillmentOf = inFulfillmentOf;
  }

  public String getDocumentationOf() {
    return documentationOf;
  }

  public void setDocumentationOf(String documentationOf) {
    this.documentationOf = documentationOf;
  }

  public String getRelatedDocumentId() {
    return relatedDocumentId;
  }

  public void setRelatedDocumentId(String relatedDocumentId) {
    this.relatedDocumentId = relatedDocumentId;
  }

  public String getRelatedDocumentType() {
    return relatedDocumentType;
  }

  public void setRelatedDocumentType(String relatedDocumentType) {
    this.relatedDocumentType = relatedDocumentType;
  }

  public String getNonXMLBodyType() {
    return nonXMLBodyType;
  }

  public void setNonXMLBodyType(String nonXMLBodyType) {
    this.nonXMLBodyType = nonXMLBodyType;
  }

  public String getNonXMLBodyContent() {
    return nonXMLBodyContent;
  }

  public void setNonXMLBodyContent(String nonXMLBodyContent) {
    this.nonXMLBodyContent = nonXMLBodyContent;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public List<Section> getStructuredBody() {
    return structuredBody;
  }

  public void setStructuredBody(List<Section> structuredBody) {
    this.structuredBody = structuredBody;
  }

  public Map<String, String> getContents() {
    return contents;
  }

  public void setContents(Map<String, String> contents) {
    this.contents = contents;
  }
  
  // Utilities
  
  public Section addSection(Section section) {
    if(section == null) {
      section = new Section();
    }
    
    if(structuredBody == null) {
      structuredBody = new ArrayList<Section>();
    }
    
    structuredBody.add(section);
    
    return section;
  }
  
  public Section addSection(String id, String title) {
    Section section = new Section(id, title);
    
    if(structuredBody == null) {
      structuredBody = new ArrayList<Section>();
    }
    
    structuredBody.add(section);
    
    return section;
  }
  
  public Section addSection(String id, String mediaType, byte[] mediaContent) {
    Section section = new Section(id, mediaType, mediaContent);
    
    if(structuredBody == null) {
      structuredBody = new ArrayList<Section>();
    }
    
    structuredBody.add(section);
    
    return section;
  }
  
  public ClinicalDocument putContent(String key, String text) {
    if(key == null || key.length() == 0) {
      return this;
    }
    
    if(contents == null) {
      contents = new HashMap<String, String>();
    }
    
    contents.put(key, text);
    
    return this;
  }
  
  public String getContent(String key) {
    if(key == null || key.length() == 0) {
      return null;
    }
    
    if(contents == null) {
      return null;
    }
    
    return contents.get(key);
  }
  
  public String getContent(String key, String defaultValue) {
    if(key == null || key.length() == 0) {
      return defaultValue;
    }
    
    if(contents == null) {
      return defaultValue;
    }
    
    String result =  contents.get(key);
    if(result == null) return defaultValue;
    
    return result;
  }
  
  // Overrides
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof ClinicalDocument) {
      String sId = ((ClinicalDocument) object).getId();
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
    return "ClinicalDocument(" + id + ")";
  }
}
