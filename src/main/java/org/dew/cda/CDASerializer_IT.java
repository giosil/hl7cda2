package org.dew.cda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dew.hl7.ClinicalDocument;
import org.dew.hl7.CodeSystems;
import org.dew.hl7.Entry;
import org.dew.hl7.ICDASerializer;
import org.dew.hl7.ICodeSystem;
import org.dew.hl7.Organization;
import org.dew.hl7.Person;
import org.dew.hl7.Section;

public 
class CDASerializer_IT implements ICDASerializer
{
  protected static final String OID_HL7_IT         = "2.16.840.1.113883.2.9.2";
  
  protected static final String OID_CRITICITA      = "2.16.840.1.113883.2.9.6.1.54.4";
  protected static final String OID_VAL_SAN_118    = "2.16.840.1.113883.2.9.6.1.33";
  protected static final String OID_ESITO_118      = "2.16.840.1.113883.2.9.6.1.34";
  
  protected static final String DEF_TYPE_ID        = "POCD_HD000040";
  protected static final String DEF_TYPE_ID_ROOT   = "2.16.840.1.113883.1.3";
  protected static final String DEF_AUTHORITY_CODE = "001";
  protected static final String DEF_REALM_CODE     = "IT";
  protected static final String DEF_LANGUAGE_CODE  = "it-IT";
  protected static final String DEF_COUNTRY        = "Italia";
  
  protected boolean comments = false;
  protected CodeSystems codeSystems;
  protected List<String> warnings;
  
  public CDASerializer_IT()
  {
    this(false);
  }
  
  public CDASerializer_IT(boolean comments)
  {
    this.comments = comments;
  }
  
  protected
  void init()
  {
    warnings = new ArrayList<String>();
    
    if(codeSystems == null) {
      codeSystems = new CodeSystems();
      codeSystems.register(CodeSystems.LOINC,     new LOINC_IT());
      codeSystems.register(CodeSystems.ICD9CM,    new ICD9CM_IT());
      codeSystems.register(CodeSystems.SNOMED_CT, new SNOMED_CT_IT());
    }
  }
  
  @Override
  public void setOptions(Map<String, Object> options) {
    if(options == null) return;
    
    Object optComments = options.get("comments");
    if(optComments instanceof Boolean) {
      this.comments = ((Boolean) optComments).booleanValue();
    }
    else if(optComments != null) {
      String sOptComments = optComments.toString();
      if(sOptComments != null && sOptComments.length() > 0) {
        char c0 = sOptComments.charAt(0);
        this.comments = "1TYStys".indexOf(c0) >= 0;
      }
    }
  }
  
  @Override
  public void setCodeSystems(CodeSystems codeSystems) {
    this.codeSystems = codeSystems;
  }
  
  @Override
  public List<String> getWarnings() {
    if(warnings == null) {
      return new ArrayList<String>(0);
    }
    return warnings;
  }
  
  @Override
  public 
  String toXML(ClinicalDocument cda)
      throws Exception
  {
    this.init();
    
    if(cda == null) {
      addWarning("ClinicalDocument object is null in toXML.");
      return "";
    }
    
    StringBuilder sb = new StringBuilder(5000);
    
    String stylesheet = cda.getStylesheet();
    
    if(stylesheet != null && stylesheet.length() > 1) {
      sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
      sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"" + stylesheet + "\"?>");
    }
    sb.append("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 xsd/hl7/infrastructure/cda/C32_CDA.xsd\">");
    
    buildHeader(sb, cda);
    
    buildRecordTarget(sb, cda);
    
    buildAuthor(sb, cda);
    
    buildCustodian(sb, cda);
    
    buildLegalAuthenticator(sb, cda);
    
    buildInFulfillmentOf(sb, cda);
    
    buildParticipant(sb, cda);
    
    buildBody(sb, cda);
    
    String signature = cda.getSignature();
    if(signature != null && signature.length() > 0) {
      sb.append(signature);
    }
    
    sb.append("</ClinicalDocument>");
    
    return sb.toString();
  }
  
  protected 
  void addWarning(String text)
  {
    if(text == null || text.length() == 0) {
      return;
    }
    warnings.add("[" + getClass().getSimpleName() + "] " + text);
    System.err.println("[" + getClass().getSimpleName() + "] " + text);
  }
  
  protected 
  void buildHeader(StringBuilder sb, ClinicalDocument cda)
      throws Exception
  {
    if(cda == null) return;
    
    String authorityCode = cda.getAuthorityCode();
    if(authorityCode == null || authorityCode.length() == 0) {
      authorityCode = DEF_AUTHORITY_CODE;
      cda.setAuthorityCode(authorityCode);
      addWarning("ClinicalDocument.authorityCode is empty (" + authorityCode + " default).");
    }
    String authorityName = cda.getAuthorityName();
    if(authorityName == null || authorityName.length() == 0) {
      authorityName = getAuthorityName(authorityCode);
      cda.setAuthorityName(authorityName);
    }
    String idRoot    = OID_HL7_IT + "." + authorityCode + ".4.4";
    String setIdRoot = OID_HL7_IT + "." + authorityCode + ".4.4";
    
    ICodeSystem loinc = codeSystems.getCodeSystem(authorityCode, CodeSystems.LOINC);
    if(loinc == null) {
      addWarning("No codesystem found for " + authorityCode + "," + CodeSystems.LOINC);
      return;
    }
    
    String id = cda.getId();
    if(id == null || id.length() == 0) {
      addWarning("ClinicalDocument.id is empty");
      id = "DOC-" + System.currentTimeMillis();
    }
    String setId = cda.getSetId();
    if(setId == null || setId.length() == 0) {
      setId = id;
    }
    
    String code = CDAUtils.getCode(cda);
    if(code == null || code.length() == 0) {
      code = loinc.getCode(cda.getTitle());
      
      if(code == null || code.length() == 0) {
        addWarning("Nessun codice LOINC individuata per " + cda.getTitle());
      }
      else {
        cda.setCode(code);
      }
    }
    String displayName  = cda.getDisplayName();
    if(displayName == null || displayName.length() > 0) {
      displayName = loinc.getDisplayName(code);
      
      if(displayName == null || displayName.length() == 0) {
        addWarning("Nessuna descrizione LOINC individuata per " + code);
      }
      else {
        cda.setDisplayName(displayName);
      }
    }
    String templateId     = cda.getTemplateId();
    String templateIdRoot = cda.getTemplateIdRoot();
    
    if(templateId == null || templateId.length() == 0) {
      templateId = loinc.getTemplateId(code);
      
      if(templateId == null || templateId.length() == 0) {
        addWarning("Nessun templateId LOINC individuato per " + code);
      }
    }
    if(templateIdRoot == null || templateIdRoot.length() == 0) {
      templateIdRoot = loinc.getTemplateIdRoot(code);
      
      if(templateIdRoot == null || templateIdRoot.length() == 0) {
        addWarning("Nessun templateIdRoot LOINC individuato per " + code);
      }
    }
    
    String typeId        = cda.getTypeId();
    String typeIdRoot    = cda.getTypeIdRoot();
    String realmCode     = cda.getRealmCode();
    String languageCode  = cda.getLanguageCode();
    String versionNumber = cda.getVersionNumber();
    String title         = cda.getTitle();
    
    if(typeId == null || typeId.length() == 0) {
      typeId = DEF_TYPE_ID;
    }
    if(typeIdRoot == null || typeIdRoot.length() == 0) {
      typeIdRoot = DEF_TYPE_ID_ROOT;
    }
    if(realmCode == null || realmCode.length() == 0) {
      realmCode = DEF_REALM_CODE;
    }
    if(languageCode == null || languageCode.length() == 0) {
      languageCode = DEF_LANGUAGE_CODE;
    }
    if(versionNumber == null || versionNumber.length() == 0) {
      versionNumber = "1";
    }
    
    sb.append("<realmCode code=\"" + realmCode + "\" />");
    sb.append("<typeId extension=\"" + typeId + "\" root=\"" + typeIdRoot + "\" />");
    sb.append("<templateId extension=\"" + templateId + "\" root=\"" + templateIdRoot + "\" />");
    sb.append("<id assigningAuthorityName=\"" + authorityName + "\" extension=\"" + id + "\" root=\"" + idRoot + "\" />");
    sb.append("<code code=\"" + code + "\" codeSystem=\"" + loinc.getCodeSystem() + "\" codeSystemName=\"" + loinc.getCodeSystemName() + "\" displayName=\"" + displayName + "\" />");
    if(title != null && title.length() > 0) {
      sb.append("<title>" + CDAUtils.xml(title) + "</title>");
    }
    sb.append("<effectiveTime value=\"" + CDAUtils.getEffectiveTime(cda) + "\" />");
    sb.append("<confidentialityCode code=\"" + CDAUtils.getConfidentialityCode(cda) + "\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"Confidentiality\" />");
    sb.append("<languageCode code=\"" + languageCode + "\" />");
    sb.append("<setId assigningAuthorityName=\"" + authorityName + "\" extension=\"" + setId + "\" root=\"" + setIdRoot + "\" />");
    sb.append("<versionNumber value=\"" + versionNumber + "\" />");
  }
  
  protected 
  void buildRecordTarget(StringBuilder sb, ClinicalDocument cda)
    throws Exception
  {
    if(sb == null || cda == null) return;
    
    Person patient = cda.getPatient();
    if(patient == null) {
      addWarning("ClinicalDocument.patient is null");
      return;
    }
    
    String patientId = patient.getId();
    if(patientId == null || patientId.length() < 4) {
      addWarning("ClinicalDocument.patient.id is null");
      return;
    }
    
    Person guardian = cda.getGuardian();
    
    String authorityCode = cda.getAuthorityCode();
    if(authorityCode == null || authorityCode.length() == 0) {
      authorityCode = DEF_AUTHORITY_CODE;
      cda.setAuthorityCode(authorityCode);
      addWarning("ClinicalDocument.authorityCode is empty (" + authorityCode + " default).");
    }
    
    int sep = patientId.lastIndexOf('.');
    if(sep > 0) {
      patientId = patientId.substring(sep + 1);
    }
    String root = "2.16.840.1.113883.2.9.4.3.2";
    if(patientId.length() != 16) {
      root = OID_HL7_IT + "." + authorityCode + ".4.2";
    }
    else if(Character.isDigit(patientId.charAt(0)) || !Character.isDigit(patientId.charAt(6)) || Character.isDigit(patientId.charAt(8))) {
      root = OID_HL7_IT + "." + authorityCode + ".4.2";
    }
    
    String city         = patient.getCity();
    String postalCode   = patient.getPostalCode();
    String streetName   = patient.getStreetName();
    String houseNumber  = patient.getHouseNumber();
    String country      = patient.getCountry();
    String gender       = patient.getGender();
    Date   birthTime    = patient.getBirthTime();
    String birthPlace   = patient.getBirthPlace();
    String birthCountry = patient.getBirthCountry();
    String prefix       = patient.getPrefix();
    String given        = patient.getGiven();
    String family       = patient.getFamily();
    String email        = patient.getEmail();
    String phone        = patient.getPhone();
    
    boolean checkGiven  = given  != null && given.length()  > 0;
    boolean checkFamily = family != null && family.length() > 0;
    
    sb.append("<recordTarget>");
    sb.append("<patientRole>");
    sb.append("<id extension=\"" + patientId + "\" root=\"" + root + "\" />");
    if(city != null && city.length() > 1) {
      sb.append("<addr>");
      sb.append("<city>" + city + "</city>");
      if(postalCode != null && postalCode.length() == 5) {
        sb.append("<postalCode>" + postalCode + "</postalCode>");
      }
      if(streetName != null && streetName.length() > 1) {
        sb.append("<streetName>" + CDAUtils.xml(streetName) + "</streetName>");
      }
      if(houseNumber != null && houseNumber.length() > 0) {
        sb.append("<houseNumber>" + CDAUtils.xml(houseNumber) + "</houseNumber>");
      }
      if(country != null && country.length() > 0) {
        sb.append("<country>" + country + "</country>");
      }
      else {
        sb.append("<country>" + DEF_COUNTRY + "</country>");
      }
      sb.append("</addr>");
    }
    if(email != null && email.length() > 4 && email.indexOf('@') > 0 && email.indexOf('.') > 0) {
      sb.append("<telecom use=\"HP\" value=\"mailto://" + email.trim().toLowerCase() + "\" />");
    }
    if(phone != null && phone.length() > 5) {
      sb.append("<telecom use=\"HP\" value=\"tel://" + phone.trim() + "\" />");
    }
    sb.append("<patient>");
    if(checkGiven || checkFamily) {
      sb.append("<name>");
      if(prefix != null && prefix.length() > 0) {
        sb.append("<prefix>" + CDAUtils.xml(prefix) + "</prefix>");
      }
      if(given != null && given.length() > 0) {
        sb.append("<given>" + CDAUtils.xml(given) + "</given>");
      }
      if(family != null && family.length() > 0) {
        sb.append("<family>" + CDAUtils.xml(family) + "</family>");
      }
      sb.append("</name>");
    }
    if(gender != null && gender.length() > 0) {
      sb.append("<administrativeGenderCode code=\"" + gender + "\" codeSystem=\"2.16.840.1.113883.5.1\" />");
    }
    if(birthTime != null) {
      sb.append("<birthTime value=\"" + CDAUtils.getDate(birthTime) + "\" />");
    }
    if(guardian != null && guardian.getFamily() != null) {
      String guardianId     = guardian.getId();
      String guardianPrefix = guardian.getPrefix();
      String guardianGiven  = guardian.getGiven();
      String guardianFamily = guardian.getFamily();
      sb.append("<guardian>");
      if(guardianId != null && guardianId.length() > 0) {
        String rootGuardian = "2.16.840.1.113883.2.9.4.3.2";
        if(guardianId.length() != 16) {
          rootGuardian = OID_HL7_IT + "." + authorityCode + ".4.2";
        }
        else if(Character.isDigit(guardianId.charAt(0)) || !Character.isDigit(guardianId.charAt(6)) || Character.isDigit(guardianId.charAt(8))) {
          rootGuardian = OID_HL7_IT + "." + authorityCode + ".4.2";
        }
        sb.append("<id extension=\"" + guardianId + "\" root=\"" + rootGuardian + "\" />");
      }
      sb.append("<guardianPerson>");
      if(guardianPrefix != null && guardianPrefix.length() > 0) {
        sb.append("<prefix>" + CDAUtils.xml(guardianPrefix) + "</prefix>");
      }
      if(guardianGiven != null && guardianGiven.length() > 0) {
        sb.append("<given>" + CDAUtils.xml(guardianGiven) + "</given>");
      }
      if(guardianFamily != null && guardianFamily.length() > 0) {
        sb.append("<family>" + CDAUtils.xml(guardianFamily) + "</family>");
      }
      sb.append("</guardianPerson>");
      sb.append("</guardian>");
    }
    if(birthPlace != null && birthPlace.length() > 0) {
      sb.append("<birthplace>");
      sb.append("<place>");
      sb.append("<addr>");
      sb.append("<city>" + CDAUtils.xml(birthPlace) + "</city>");
      if(birthCountry != null && birthCountry.length() > 0) {
        sb.append("<country>" + CDAUtils.xml(birthCountry) + "</country>");
      }
      else if(patientId != null && patientId.length() == 16){
        char codFisComNas = patientId.charAt(11);
        if(codFisComNas != 'Z') {
          sb.append("<country>" + CDAUtils.xml(DEF_COUNTRY) + "</country>");
        }
      }
      sb.append("</addr>");
      sb.append("</place>");
      sb.append("</birthplace>");
    }
    sb.append("</patient>");
    sb.append("</patientRole>");
    sb.append("</recordTarget>");
  }
  
  protected 
  void buildAuthor(StringBuilder sb, ClinicalDocument cda)
      throws Exception
  {
    if(sb == null || cda == null) return;
    
    Person author = cda.getAuthor();
    if(author == null) {
      addWarning("ClinicalDocument.author is null");
      return;
    }
    
    String authorId = author.getId();
    if(authorId == null || authorId.length() < 4) {
      addWarning("ClinicalDocument.author.id is null");
      return;
    }
    
    String authorityCode = cda.getAuthorityCode();
    if(authorityCode == null || authorityCode.length() == 0) {
      authorityCode = DEF_AUTHORITY_CODE;
      cda.setAuthorityCode(authorityCode);
      addWarning("ClinicalDocument.authorityCode is empty (" + authorityCode + " default).");
    }
    
    int sep = authorId.lastIndexOf('.');
    if(sep > 0) {
      authorId = authorId.substring(sep + 1);
    }
    String root = "2.16.840.1.113883.2.9.4.3.2";
    if(authorId.length() != 16) {
      root = OID_HL7_IT + "." + authorityCode + ".4.2";;
    }
    else if(Character.isDigit(authorId.charAt(0)) || !Character.isDigit(authorId.charAt(6)) || Character.isDigit(authorId.charAt(8))) {
      root = OID_HL7_IT + "." + authorityCode + ".4.2";;
    }
    
    String prefix = author.getPrefix();
    String given  = author.getGiven();
    String family = author.getFamily();
    String email  = author.getEmail();
    String phone  = author.getPhone();
    
    boolean checkGiven  = given  != null && given.length()  > 0;
    boolean checkFamily = family != null && family.length() > 0;
    
    sb.append("<author>");
    sb.append("<time value=\"" + CDAUtils.getEffectiveTime(cda) + "\" />");
    if(email != null && email.length() > 4 && email.indexOf('@') > 0 && email.indexOf('.') > 0) {
      sb.append("<telecom use=\"WP\" value=\"mailto://" + email.trim().toLowerCase() + "\" />");
    }
    if(phone != null && phone.length() > 5) {
      sb.append("<telecom use=\"WP\" value=\"tel://" + phone.trim() + "\" />");
    }
    sb.append("<assignedAuthor>");
    sb.append("<id extension=\"" + authorId + "\" root=\"" + root + "\" />");
    if(checkGiven || checkFamily) {
      sb.append("<assignedPerson>");
      sb.append("<name>");
      if(prefix != null && prefix.length() > 0) {
        sb.append("<prefix>" + CDAUtils.xml(prefix) + "</prefix>");
      }
      if(given != null && given.length() > 0) {
        sb.append("<given>" + CDAUtils.xml(given) + "</given>");
      }
      if(family != null && family.length() > 0) {
        sb.append("<family>" + CDAUtils.xml(family) + "</family>");
      }
      sb.append("</name>");
      sb.append("</assignedPerson>");
    }
    sb.append("</assignedAuthor>");
    sb.append("</author>");
  }
  
  protected 
  void buildCustodian(StringBuilder sb, ClinicalDocument cda)
      throws Exception
  {
    if(sb == null || cda == null) return;
    
    Organization custodian = cda.getCustodian();
    if(custodian == null) {
      addWarning("ClinicalDocument.custodian is null");
      return;
    }
    
    String orgId = custodian.getId();
    if(orgId == null || orgId.length() == 0) {
      addWarning("ClinicalDocument.custodian.id is empty");
      return;
    }
    
    String authorityCode = cda.getAuthorityCode();
    if(authorityCode == null || authorityCode.length() == 0) {
      authorityCode = DEF_AUTHORITY_CODE;
      cda.setAuthorityCode(authorityCode);
      addWarning("ClinicalDocument.authorityCode is empty (" + authorityCode + " default).");
    }
    String authorityName = cda.getAuthorityName();
    if(authorityName == null || authorityName.length() == 0) {
      authorityName = getAuthorityName(authorityCode);
      cda.setAuthorityName(authorityName);
    }
    String orgIdRoot = OID_HL7_IT + "." + authorityCode + ".4.11";
    
    String orgName        = custodian.getName();
    String orgCity        = custodian.getCity();
    String orgPostalCode  = custodian.getPostalCode();
    String orgStreetName  = custodian.getStreetName();
    String orgHouseNumber = custodian.getHouseNumber();
    String orgCountry     = custodian.getCountry();
    
    sb.append("<custodian>");
    sb.append("<assignedCustodian>");
    sb.append("<representedCustodianOrganization>");
    sb.append("<id assigningAuthorityName=\"" + authorityName + "\" extension=\"" + orgId + "\" root=\"" + orgIdRoot + "\" />");
    if(orgName != null && orgName.length() > 0) {
      sb.append("<name>" + CDAUtils.xml(orgName) + "</name>");
    }
    if(orgCity != null && orgCity.length() > 1) {
      sb.append("<addr>");
      sb.append("<city>" + orgCity + "</city>");
      if(orgPostalCode != null && orgPostalCode.length() == 5) {
        sb.append("<postalCode>" + orgPostalCode + "</postalCode>");
      }
      if(orgStreetName != null && orgStreetName.length() > 1) {
        sb.append("<streetName>" + CDAUtils.xml(orgStreetName) + "</streetName>");
      }
      if(orgHouseNumber != null && orgHouseNumber.length() > 0) {
        sb.append("<houseNumber>" + CDAUtils.xml(orgHouseNumber) + "</houseNumber>");
      }
      if(orgCountry != null && orgCountry.length() > 0) {
        sb.append("<country>" + orgCountry + "</country>");
      }
      else {
        sb.append("<country>" + DEF_COUNTRY + "</country>");
      }
      sb.append("</addr>");
    }
    sb.append("</representedCustodianOrganization>");
    sb.append("</assignedCustodian>");
    sb.append("</custodian>");
  }
  
  protected 
  void buildLegalAuthenticator(StringBuilder sb, ClinicalDocument cda)
      throws Exception
  {
    if(sb == null || cda == null) return;
    
    Person legalAuth = cda.getLegalAuthenticator();
    if(legalAuth == null) return;
    
    String legalAuthId = legalAuth.getId();
    if(legalAuthId == null || legalAuthId.length() < 4) {
      return;
    }
    
    String authorityCode = cda.getAuthorityCode();
    if(authorityCode == null || authorityCode.length() == 0) {
      authorityCode = DEF_AUTHORITY_CODE;
      cda.setAuthorityCode(authorityCode);
      addWarning("ClinicalDocument.authorityCode is empty (" + authorityCode + " default).");
    }
    String authorityName = cda.getAuthorityName();
    if(authorityName == null || authorityName.length() == 0) {
      authorityName = getAuthorityName(authorityCode);
      cda.setAuthorityName(authorityName);
    }
    String orgIdRoot = OID_HL7_IT + "." + authorityCode + ".4.11";
    
    int sep = legalAuthId.lastIndexOf('.');
    if(sep > 0) {
      legalAuthId = legalAuthId.substring(sep + 1);
    }
    String root = "2.16.840.1.113883.2.9.4.3.2";
    if(legalAuthId.length() != 16) {
      root = OID_HL7_IT + "." + authorityCode + ".4.2";
    }
    else if(Character.isDigit(legalAuthId.charAt(0)) || !Character.isDigit(legalAuthId.charAt(6)) || Character.isDigit(legalAuthId.charAt(8))) {
      root = OID_HL7_IT + "." + authorityCode + ".4.2";
    }
    
    String prefix = legalAuth.getPrefix();
    String given  = legalAuth.getGiven();
    String family = legalAuth.getFamily();
    String email  = legalAuth.getEmail();
    String phone  = legalAuth.getPhone();
    
    boolean checkGiven  = given  != null && given.length()  > 0;
    boolean checkFamily = family != null && family.length() > 0;
    
    Organization organization = legalAuth.getOrganization();
    String orgId    = null;
    String orgName  = null;
    String orgCity  = null;
    String orgPostalCode  = null;
    String orgStreetName  = null;
    String orgHouseNumber = null;
    String orgCountry     = null;
    if(organization != null) {
      orgId          = organization.getId();
      orgName        = organization.getName();
      orgCity        = organization.getCity();
      orgPostalCode  = organization.getPostalCode();
      orgStreetName  = organization.getStreetName();
      orgHouseNumber = organization.getHouseNumber();
      orgCountry     = organization.getCountry();
    }
    
    sb.append("<legalAuthenticator>");
    sb.append("<time value=\"" + CDAUtils.getEffectiveTime(cda) + "\" />");
    sb.append("<signatureCode code=\"S\" />");
    sb.append("<assignedEntity>");
    sb.append("<id extension=\"" + legalAuthId + "\" root=\"" + root + "\" />");
    if(email != null && email.length() > 4 && email.indexOf('@') > 0 && email.indexOf('.') > 0) {
      sb.append("<telecom use=\"WP\" value=\"mailto://" + email.trim().toLowerCase() + "\" />");
    }
    if(phone != null && phone.length() > 5) {
      sb.append("<telecom use=\"WP\" value=\"tel://" + phone.trim() + "\" />");
    }
    if(checkGiven || checkFamily) {
      sb.append("<assignedPerson>");
      sb.append("<name>");
      if(prefix != null && prefix.length() > 0) {
        sb.append("<prefix>" + CDAUtils.xml(prefix) + "</prefix>");
      }
      if(given != null && given.length() > 0) {
        sb.append("<given>" + CDAUtils.xml(given) + "</given>");
      }
      if(family != null && family.length() > 0) {
        sb.append("<family>" + CDAUtils.xml(family) + "</family>");
      }
      sb.append("</name>");
      sb.append("</assignedPerson>");
    }
    sb.append("</assignedEntity>");
    if(orgId != null && orgId.length() > 1) {
      sb.append("<representedOrganization>");
      sb.append("<id assigningAuthorityName=\"" + authorityName + "\" extension=\"" + orgId + "\" root=\"" + orgIdRoot + "\" />");
      if(orgName != null && orgName.length() > 0) {
        sb.append("<name>" + CDAUtils.xml(orgName) + "</name>");
      }
      if(orgCity != null && orgCity.length() > 1) {
        sb.append("<addr>");
        sb.append("<city>" + orgCity + "</city>");
        if(orgPostalCode != null && orgPostalCode.length() == 5) {
          sb.append("<postalCode>" + orgPostalCode + "</postalCode>");
        }
        if(orgStreetName != null && orgStreetName.length() > 1) {
          sb.append("<streetName>" + CDAUtils.xml(orgStreetName) + "</streetName>");
        }
        if(orgHouseNumber != null && orgHouseNumber.length() > 0) {
          sb.append("<houseNumber>" + CDAUtils.xml(orgHouseNumber) + "</houseNumber>");
        }
        if(orgCountry != null && orgCountry.length() > 0) {
          sb.append("<country>" + orgCountry + "</country>");
        }
        else {
          sb.append("<country>" + DEF_COUNTRY + "</country>");
        }
        sb.append("</addr>");
      }
      sb.append("</representedOrganization>");
    }
    sb.append("</legalAuthenticator>");
  }
  
  protected 
  void buildInFulfillmentOf(StringBuilder sb, ClinicalDocument cda)
    throws Exception
  {
    if(sb == null || cda == null) return;
    
    String inFulfillmentOf = cda.getInFulfillmentOf();
    if(inFulfillmentOf == null || inFulfillmentOf.length() == 0) {
      return;
    }
    
    String root      = "";
    String extension = "";
    if(inFulfillmentOf != null && inFulfillmentOf.length() > 10 && inFulfillmentOf.indexOf('.') > 0) {
      root      = CDAUtils.getRoot(inFulfillmentOf, "2.16.840.1.113883.2.9.4.3.8");
      extension = CDAUtils.getExtension(inFulfillmentOf);
    }
    else if(inFulfillmentOf != null && inFulfillmentOf.length() == 15) {
      root = "2.16.840.1.113883.2.9.4.3.8"; // NRE
      extension = inFulfillmentOf;
    }
    else if(inFulfillmentOf != null && inFulfillmentOf.length() != 15) {
      root = "2.16.840.1.113883.2.9.2." + cda.getAuthorityCode();
      extension = inFulfillmentOf;
    }
    
    sb.append("<inFulfillmentOf>");
    sb.append("<order classCode=\"ACT\" moodCode=\"RQO\">");
    sb.append("<id root=\"" + root + "\" extension=\"" + extension + "\" />");
    sb.append("</order>");
    sb.append("</inFulfillmentOf>");
  }
  
  protected 
  void buildParticipant(StringBuilder sb, ClinicalDocument cda)
      throws Exception
  {
    if(sb == null || cda == null) return;
    
    Organization participant = cda.getParticipant();
    if(participant == null) return;
    
    String orgId = participant.getId();
    if(orgId == null || orgId.length() == 0) return;
    
    sb.append("<participant typeCode=\"IND\">");
    sb.append("<associatedEntity classCode=\"GUAR\">");
    sb.append("<id assigningAuthorityName=\"SSN-MIN SALUTE-500001\" extension=\"" + orgId + "\" root=\"2.16.840.1.113883.2.9.4.1.1\" />");
    sb.append("</associatedEntity>");
    sb.append("</participant>");
  }
  
  protected 
  void buildBody(StringBuilder sb, ClinicalDocument cda)
      throws Exception
  {
    if(cda == null) return;
    
    ICodeSystem loinc = codeSystems.getCodeSystem(cda.getAuthorityCode(), CodeSystems.LOINC);
    if(loinc == null) {
      addWarning("No codesystem found for " + cda.getAuthorityCode() + "," + CodeSystems.LOINC);
      return;
    }
    
    List<Section> sections   = cda.getStructuredBody();
    String nonXMLBodyType    = cda.getNonXMLBodyType();
    String nonXMLBodyContent = cda.getNonXMLBodyContent();
    
    sb.append("<component>");
    
    if(nonXMLBodyContent != null && nonXMLBodyContent.length() > 0) {
      sb.append("<nonXMLBody>");
      if(nonXMLBodyType != null && nonXMLBodyType.length() > 0) {
        sb.append("<text mediaType=\"" + nonXMLBodyType + "\" representation=\"B64\">");
      }
      else {
        sb.append("<text representation=\"B64\">");
      }
      sb.append(nonXMLBodyContent);
      sb.append("</text>");
      sb.append("</nonXMLBody>");
      
      if(sections == null || sections.size() == 0) {
        sb.append("</component>");
        return;
      }
    }
    
    if(sections == null || sections.size() == 0) {
      sb.append("<structuredBody>");
      sb.append("<component>");
      sb.append("<section></section>");
      sb.append("</component>");
      sb.append("</structuredBody>");
      sb.append("</component>");
      return;
    }
    
    sb.append("<structuredBody>");
    for(int i = 0; i < sections.size(); i++) {
      Section section = sections.get(i);
      if(section == null) continue;
      
      String sectionId    = section.getId();
      String title        = section.getTitle();
      String mediaId      = section.getMediaId();
      String mediaType    = section.getMediaType();
      String mediaContent = section.getMediaContent();
      
      if(mediaContent != null && mediaContent.length() > 0) {
        String cdaCode = CDAUtils.getCode(cda);
        if(cdaCode == null || cdaCode.length() == 0) {
          cdaCode = loinc.getCode(cda.getTitle());
          if(cdaCode == null || cdaCode.length() == 0) {
            addWarning("Nessun codice LOINC individuato per " + cda.getTitle());
          }
        }
        String cdaDisplayName  = cda.getDisplayName();
        if(cdaDisplayName == null || cdaDisplayName.length() > 0) {
          cdaDisplayName = loinc.getDisplayName(cdaCode);
          if(cdaDisplayName == null || cdaDisplayName.length() == 0) {
            addWarning("Nessuna descrizione LOINC individuata per " + cdaCode);
          }
        }
        
        sb.append("<component>");
        if(sectionId != null && sectionId.length() > 0) {
          sb.append("<section ID=\"" + sectionId + "\">");
        }
        else {
          sb.append("<section>");
        }
        sb.append("<code code=\"" + cdaCode + "\" codeSystem=\"" + loinc.getCodeSystem() + "\" codeSystemName=\"" + loinc.getCodeSystemName() + "\" displayName=\"" + cdaDisplayName + "\" />");
        if(title != null && title.length() > 0) {
          sb.append("<title>" + CDAUtils.xml(title) + "</title>");
        }
        sb.append("<entry>");
        if(mediaId != null && mediaId.length() > 0) {
          sb.append("<observationMedia ID=\"" + mediaId + "\" classCode=\"OBS\" moodCode=\"EVN\">");
        }
        else {
          sb.append("<observationMedia classCode=\"OBS\" moodCode=\"EVN\">");
        }
        if(mediaType != null && mediaType.length() > 0) {
          sb.append("<value mediaType=\"" + mediaType + "\" representation=\"B64\">");
        }
        else {
          sb.append("<value representation=\"B64\">");
        }
        sb.append(mediaContent);
        sb.append("</value>");
        sb.append("</observationMedia>");
        sb.append("</entry>");
        sb.append("</section>");
        sb.append("</component>");
        
        continue;
      }
      
      String templateId   = section.getTemplateId();
      String code         = CDAUtils.getCode(section.getCode());
      String displayName  = section.getDisplayName();
      
      if(templateId == null || templateId.length() == 0) {
        templateId = getSectionTemplateId(section);
      }
      if(code == null || code.length() == 0) {
        code = getSectionCode(section, CDAUtils.getCode(cda));
      }
      if(displayName == null || displayName.length() == 0) {
        displayName = loinc.getDisplayName(code);
        if(displayName == null || displayName.length() == 0) {
          addWarning("Nessuna descrizione LOINC individuata per " + code);
        }
      }
      
      sb.append("<component>");
      if(sectionId != null && sectionId.length() > 0) {
        sb.append("<section ID=\"" + sectionId + "\">");
      }
      else {
        sb.append("<section>");
      }
      if(comments) {
        String comment = getSectionComment(section);
        if(comment != null && comment.length() > 0) {
          sb.append("<!-- " + comment + " -->");
        }
      }
      if(templateId != null && templateId.length() > 0) {
        sb.append("<templateId root=\"" + templateId + "\" />");
      }
      if(code != null && code.length() > 0) {
        if(displayName != null && displayName.length() > 0) {
          sb.append("<code code=\"" + code + "\" codeSystem=\"" + loinc.getCodeSystem() + "\" codeSystemName=\"" + loinc.getCodeSystemName() + "\" displayName=\"" + displayName + "\" />");
        }
        else {
          sb.append("<code code=\"" + code + "\" codeSystem=\"" + loinc.getCodeSystem()  + "\" codeSystemName=\"" + loinc.getCodeSystemName() + "\" />");
        }
      }
      if(title != null && title.length() > 0) {
        sb.append("<title>" + CDAUtils.xml(title) + "</title>");
      }
      
      buildNarrativeBlock(sb, cda, section);
      
      buildEntries(sb, cda, section);
      
      sb.append("</section>");
      sb.append("</component>");
    }
    sb.append("</structuredBody>");
    sb.append("</component>");
  }
  
  protected 
  void buildNarrativeBlock(StringBuilder sb, ClinicalDocument cda, Section section)
      throws Exception
  {
    if(section == null) {
      sb.append("<text>");
      sb.append("</text>");
      return;
    }
    
    String text = section.getText();
    if(text != null && text.length() > 0) {
      sb.append("<text>");
      sb.append(text);
      sb.append("</text>");
      
      // Entry proventienti dalla deserializzazione
      List<Entry> entries = section.getEntries();
      if(entries != null && entries.size() > 0) {
        for(int i = 0; i < entries.size(); i++) {
          Entry entry = entries.get(i);
          if(entry == null) continue;
          String ref = entry.getReference();
          if(ref != null && ref.length() > 7) {
            if(ref.endsWith("-COD")) {
              entry.setReference(ref.substring(0, ref.length()-4));
            }
            else if(ref.endsWith("-DES")) {
              entry.setReference(ref.substring(0, ref.length()-4));
            }
          }
        }
      }
      
      return;
    }
    
    List<Entry> entries = section.getEntries();
    if(entries == null || entries.size() == 0) {
      sb.append("<text>");
      sb.append("</text>");
      return;
    }
    
    boolean addColDescription = false;
    for(int i = 0; i < entries.size(); i++) {
      Entry entry = entries.get(i);
      if(entry == null) continue;
      String description = entry.getDisplayName();
      if(description != null && description.length() > 0) {
        addColDescription = true;
      }
    }
    
    boolean firstEncounter = false;
    String sectionId = section.getId();
    if(sectionId != null && sectionId.length() > 0) {
      String sectionIdLC = sectionId.toLowerCase();
      if(sectionIdLC.indexOf("prim") >= 0 || sectionIdLC.indexOf("iniz") >= 0) {
        firstEncounter = true;
      }
    }
    
    sb.append("<text>");
    sb.append("<table>");
    sb.append("<tbody>");
    for(int i = 0; i < entries.size(); i++) {
      Entry entry = entries.get(i);
      if(entry == null) continue;
      
      String item = getItem(entry);
      if(item == null || item.length() == 0) continue;
      
      String evalue  = entry.getValue();
      String eunit   = entry.getUnit();
      String edesc   = entry.getDisplayName();
      
      String prefix  = "";
      if(item.indexOf(" post") > 0 && !firstEncounter) {
        prefix = "POS-";
      }
      else if(item.indexOf(" iniz") < 0 && item.indexOf(" first") < 0 && !firstEncounter) {
        prefix = "POS-";
      }
      
      if(item.indexOf("cod") >= 0 && item.indexOf("fine") >= 0) {
        // Codice fine missione 
        entry.setReference("COD-FIN");
      }
      else if(item.indexOf("crit") >= 0) {
        // Codice criticita'
        entry.setReference("COD-CRI");
      }
      else if(item.indexOf("valutaz") >= 0 || item.indexOf("sanit") >= 0) {
        entry.setReference(prefix + "VAL-SAN");
      }
      else if(item.indexOf("diag") >= 0 || item.indexOf("icd9") >= 0) {
        entry.setReference("DIAGNOSI");
      }
      else if(item.indexOf("esame") >= 0 && item.indexOf("ob") >= 0) {
        entry.setReference("ESA-OBIETTIVO");
      }
      else if(item.indexOf("esito") >= 0) {
        entry.setReference("ESITO");
      }
      else if(item.startsWith("terap")) {
        entry.setReference("TERAPIA");
      }
      else if(item.startsWith("trat")) {
        entry.setReference("TRATTAMENTI");
      }
      else if(item.indexOf("sistol") >= 0 || item.indexOf("massima") >= 0) {
        entry.setReference(prefix + "PAR-PAS");
      }
      else if(item.indexOf("diasto") >= 0 || item.indexOf("minima") >= 0) {
        entry.setReference(prefix + "PAR-PAD");
      }
      else if(item.indexOf("frequen") >= 0 && item.indexOf("card") >= 0) {
        entry.setReference(prefix + "PAR-FRC");
      }
      else if(item.indexOf("frequen") >= 0 && item.indexOf("respir") >= 0) {
        entry.setReference(prefix + "PAR-FRR");
      }
      else if(item.indexOf("temper") >= 0) {
        entry.setReference(prefix + "PAR-TEM");
      }
      else if(item.indexOf("ritmo") >= 0) {
        entry.setReference(prefix + "PAR-RTM");
      }
      else if(item.indexOf("occhi") >= 0) {
        entry.setReference(prefix + "PAR-GSE"); // Glasgow Score Eye opening
      }
      else if(item.indexOf("verbal") >= 0) {
        entry.setReference(prefix + "PAR-GSV"); // Glasgow Score Verbal
      }
      else if(item.indexOf("motor") >= 0) {
        entry.setReference(prefix + "PAR-GSM"); // Glasgow Score Motor
      }
      else if(item.indexOf("vas") >= 0 || item.indexOf("visual") >= 0) {
        entry.setReference(prefix + "PAR-VAS");
      }
      else if(item.indexOf("satur") >= 0 || item.indexOf("ossig") >= 0) {
        entry.setReference(prefix + "PAR-SAT");
      }
      else if(item.indexOf("glicem") >= 0 || item.indexOf("glucos") >= 0) {
        entry.setReference(prefix + "PAR-GLI");
      }
      else if(item.indexOf("traum") >= 0 && item.indexOf("score") >= 0) {
        entry.setReference(prefix + "PAR-RTS");
      }
      else if(item.indexOf("traum") >= 0 && item.indexOf("score") < 0) {
        entry.setReference(prefix + "VAL-TRA");
      }
      else if(item.indexOf("pupil") >= 0) {
        entry.setReference(prefix + "VAL-PUP");
      }
      else {
        if(!isNotValuableEntry(entry)) {
          addWarning("Reference non impostata per entry " + entry + ".");
        }
      }
      
      String ecode = entry.getCode();
      if(ecode == null || ecode.length() == 0 || Character.isDigit(ecode.charAt(0))) {
        if(evalue == null || evalue.length() == 0) {
          continue;
        }
      }
      if(ecode  == null) ecode  = "";
      if(evalue == null) evalue = "";
      if(eunit  != null && eunit.length() > 0) {
        if(evalue != null && evalue.length() > 0) {
          evalue += " " + eunit;
        }
      }
      
      String eref = entry.getReference();
      
      sb.append("<tr>");
      sb.append("<td>" + CDAUtils.xml(ecode) + "</td>");
      if(eref != null && eref.length() > 0) {
        if(edesc != null && edesc.length() > 0) {
          sb.append("<td><content ID=\"" + append(eref, "-COD") + "\">" + CDAUtils.xml(evalue) + "</content></td>");
        }
        else {
          sb.append("<td><content ID=\"" + eref + "\">" + CDAUtils.xml(evalue) + "</content></td>");
        }
      }
      else {
        sb.append("<td>" + CDAUtils.xml(evalue) + "</td>");
      }
      if(addColDescription) {
        if(eref != null && eref.length() > 0) {
          if(edesc != null && edesc.length() > 0) {
            sb.append("<td><content ID=\"" + append(eref, "-DES") + "\">" + CDAUtils.xml(edesc) + "</content></td>");
          }
          else {
            sb.append("<td></td>");
          }
        }
        else {
          sb.append("<td>" + CDAUtils.xml(edesc) + "</td>");
        }
      }
      sb.append("</tr>");
    }
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</text>");
  }
  
  protected 
  void buildEntries(StringBuilder sb, ClinicalDocument cda, Section section)
      throws Exception
  {
    if(cda == null || section == null) return;
    
    ICodeSystem loinc = codeSystems.getCodeSystem(cda.getAuthorityCode(), CodeSystems.LOINC);
    if(loinc == null) {
      addWarning("No codesystem found for " + cda.getAuthorityCode() + "," + CodeSystems.LOINC);
      return;
    }
    
    List<Entry> entries = section.getEntries();
    if(entries == null || entries.size() == 0) {
      return;
    }
    
    boolean organizer      = false;
    boolean firstEncounter = false;
    String sectionId = section.getId();
    if(sectionId != null && sectionId.length() > 0) {
      String sectionIdLC = sectionId.toLowerCase();
      if(sectionIdLC.indexOf("val") >= 0 && sectionIdLC.indexOf("san") < 0) {
        organizer = true;
      }
      if(sectionIdLC.indexOf("par") >= 0) {
        organizer = true;
      }
      if(sectionIdLC.indexOf("prim") >= 0 || sectionIdLC.indexOf("iniz") >= 0) {
        firstEncounter = true;
      }
    }
    
    if(organizer) {
      String organizerTemplateId  = getSectionTemplateId(section);
      String organizerCode        = getSectionCode(section, CDAUtils.getCode(cda));
      String organizerDisplayName = getSectionDisplayName(section);
      
      if(organizerDisplayName == null || organizerDisplayName.length() == 0) {
        organizerDisplayName = loinc.getDisplayName(organizerCode);
      }
      
      sb.append("<entry>");
      sb.append("<organizer classCode=\"CLUSTER\" moodCode=\"EVN\">");
      if(organizerTemplateId != null && organizerTemplateId.length() > 0) {
        sb.append("<templateId root=\"" + organizerTemplateId + "\" />");
      }
      
      String organizerId = "";
      if(entries != null && entries.size() > 0) {
        organizerId = entries.get(0).getId();
      }
      if(organizerId == null || organizerId.length() < 4) {
        organizerId = UUID.randomUUID().toString();
      }
      
      sb.append("<id root=\"" + organizerId + "\" />");
      if(organizerCode != null && organizerCode.length() > 0) {
        if(organizerDisplayName != null && organizerDisplayName.length() > 0) {
          sb.append("<code code=\"" + organizerCode + "\" codeSystem=\"" + loinc.getCodeSystem() + "\" codeSystemName=\"" + loinc.getCodeSystemName() + "\" displayName=\"" + organizerDisplayName + "\" />");
        }
        else {
          sb.append("<code code=\"" + organizerCode + "\" codeSystem=\"" + loinc.getCodeSystem() + "\" codeSystemName=\"" + loinc.getCodeSystemName() + "\" />");
        }
      }
      sb.append("<statusCode code=\"completed\" />");
      sb.append("<effectiveTime value=\"" + CDAUtils.getEffectiveTime(cda) + "\" />");
    }
    
    for(int i = 0; i < entries.size(); i++) {
      Entry entry = entries.get(i);
      if(entry == null) continue;
      
      String mediaId      = entry.getMediaId();
      String mediaType    = entry.getMediaType();
      String mediaContent = entry.getMediaContent();
      
      if(mediaContent != null && mediaContent.length() > 0) {
        if(organizer) {
          sb.append("<component typeCode=\"COMP\">");
        }
        else {
          sb.append("<entry typeCode=\"COMP\">");
        }
        if(mediaId != null && mediaId.length() > 0) {
          sb.append("<observationMedia ID=\"" + mediaId + "\" classCode=\"OBS\" moodCode=\"EVN\">");
        }
        else {
          sb.append("<observationMedia classCode=\"OBS\" moodCode=\"EVN\">");
        }
        if(mediaType != null && mediaType.length() > 0) {
          sb.append("<value mediaType=\"" + mediaType + "\" representation=\"B64\">");
        }
        else {
          sb.append("<value representation=\"B64\">");
        }
        sb.append(mediaContent);
        sb.append("</value>");
        sb.append("</observationMedia>");
        if(organizer) {
          sb.append("</component>");
        }
        else {
          sb.append("</entry>");
        }
        continue;
      }
      
      String entryValue = buildEntryValue(cda, entry, firstEncounter);
      if(entryValue == null || entryValue.length() == 0) {
        continue;
      }
      
      boolean isAct = isAct(entry);
      
      if(organizer) {
        sb.append("<component typeCode=\"COMP\">");
      }
      else {
        String evalue = getItemVal(entry);
        String eref   = entry.getReference();
        if(eref != null && eref.length() > 0 && isDescriptionValue(evalue)) {
          sb.append("<entry typeCode=\"DRIV\">");
        }
        else {
          sb.append("<entry typeCode=\"COMP\">");
        }
      }
      
      if(isAct) {
        sb.append("<act classCode=\"INFRM\" moodCode=\"EVN\">");
      }
      else {
        sb.append("<observation classCode=\"OBS\" moodCode=\"EVN\">");
      }
      
      sb.append(entryValue);
      
      if(isAct) {
        sb.append("</act>");
      }
      else {
        sb.append("</observation>");
      }
      
      if(organizer) {
        sb.append("</component>");
      }
      else {
        sb.append("</entry>");
      }
    }
    
    if(organizer) {
      sb.append("</organizer>");
      sb.append("</entry>");
    }
  }
  
  protected 
  boolean isNotValuableEntry(Entry entry)
  {
    if(entry == null) return true;
    
    String item = getItem(entry);
    if(item == null || item.length() == 0) {
      return true;
    }
    
    if(item.startsWith("data"))     return true;
    if(item.startsWith("luogo"))    return true;
    if(item.startsWith("motivo"))   return true;
    if(item.startsWith("dinamica")) return true;
    if(item.startsWith("note"))     return true;
    if(item.startsWith("mimica"))   return true;
    if(item.startsWith("spostam"))  return true;
    if(item.startsWith("lingua"))   return true;
    
    return false;
  }
  
  protected 
  String getItem(Entry entry)
  {
    if(entry == null) return "";
    
    String code = entry.getCode();
    if(code == null || code.length() == 0) {
      String codeSystemName = entry.getCodeSystemName();
      if(codeSystemName != null && codeSystemName.length() > 0) {
        return codeSystemName.toLowerCase();
      }
      String displayName = entry.getDisplayName();
      if(displayName != null && displayName.length() > 0) {
        return displayName.toLowerCase();
      }
      return "";
    }
    
    if(code.length() < 3) {
      // Possibile valore
      String codeSystemName = entry.getCodeSystemName();
      if(codeSystemName != null && codeSystemName.length() > 0) {
        return codeSystemName.toLowerCase();
      }
      String displayName = entry.getDisplayName();
      if(displayName != null && displayName.length() > 0) {
        return displayName.toLowerCase();
      }
      return "";
    }
    
    if(code.length() > 0 && Character.isDigit(code.charAt(0))) {
      // Classificazione
      String displayName = entry.getDisplayName();
      if(displayName != null && displayName.length() > 0) {
        return displayName.toLowerCase();
      }
      return "";
    }
    
    return code.toLowerCase();
  }
  
  protected 
  String getItemVal(Entry entry)
  {
    if(entry == null) return "";
    
    String value = entry.getValue();
    if(value != null && value.length() > 0) {
      return value;
    }
    
    String code = entry.getCode();
    if(code == null || code.length() == 0) {
      return "";
    }
    if(code.length() < 3) {
      return code;
    }
    
    return "";
  }
  
  protected 
  String buildEntryValue(ClinicalDocument cda, Entry entry, boolean firstEncounter)
      throws Exception
  {
    if(entry == null) return null;
    
    String item = getItem(entry);
    if(item == null || item.length() == 0) return null;
    
    if(isNotValuableEntry(entry)) {
      return null;
    }
    
    String evalue = getItemVal(entry);
    String eunit  = entry.getUnit();
    String edesc  = entry.getText();
    if(edesc == null || edesc.length() == 0) {
      edesc = entry.getDisplayName();
    }
    String eref   = entry.getReference();
    Date   etime  = entry.getEffectiveTime();
    
    if(evalue == null || evalue.length() == 0) {
      if(edesc == null || edesc.length() == 0) {
        return null;
      }
    }
    
    if(item.indexOf(" iniz") > 0) {
      firstEncounter = true;
    }
    else if(item.indexOf(" first") > 0) {
      firstEncounter = true;
    }
    
    ICodeSystem loinc = codeSystems.getCodeSystem(cda.getAuthorityCode(), CodeSystems.LOINC);
    if(loinc == null) {
      addWarning("No codesystem found for " + cda.getAuthorityCode() + "," + CodeSystems.LOINC);
      return null;
    }
    
    StringBuilder sb = new StringBuilder();
    
    if(item.indexOf("crit") >= 0 || (item.indexOf("cod") >= 0 &&  item.indexOf("fine") >= 0)) {
      
      buildCritVal(sb, eref, evalue, edesc, etime);
      
    }
    else if(item.indexOf("sistol") >= 0 || item.indexOf("massima") >= 0) {
      
      buildLOINCVal(sb, loinc, firstEncounter ? "11378-7" : "8480-6", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("diasto") >= 0 || item.indexOf("minima") >= 0) {
      
      buildLOINCVal(sb, loinc, firstEncounter ? "11377-9" : "8462-4", eref, evalue, eunit, edesc, etime);
      
    }
    else if((item.indexOf("frequen") >= 0 && item.indexOf("card") >= 0)) {
      
      buildLOINCVal(sb, loinc, firstEncounter ? "18708-8" : "8867-4", eref, evalue, eunit, edesc, etime);
      
    }
    else if((item.indexOf("frequen") >= 0 && item.indexOf("respir") >= 0)) {
      
      buildLOINCVal(sb, loinc, firstEncounter ? "18686-6" : "9279-1", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("temper") >= 0) {
      
      buildLOINCVal(sb, loinc, firstEncounter ? "11289-6" : "8310-5", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("occhi") >= 0) {
      
      buildLOINCVal(sb, loinc, firstEncounter ? "11324-1" : "67847-4", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("verbal") >= 0) {
      
      buildLOINCVal(sb, loinc, firstEncounter ? "11326-6" : "67846-6", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("motor") >= 0) {
      
      buildLOINCVal(sb, loinc, firstEncounter ? "11325-8" : "67845-8", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("glicem") >= 0 || item.indexOf("glucos") >= 0) {
      
      buildLOINCVal(sb, loinc, "14743-9", eref, evalue, eunit, edesc, etime);
      
    }
    else if((item.indexOf("traum") >= 0 && item.indexOf("score") < 0)) {
      
      buildLOINCVal(sb, loinc, "11376-1", eref, evalue, eunit, edesc, etime);
      
    }
    else if((item.indexOf("traum") >= 0 && item.indexOf("score") >= 0)) {
      
      buildLOINCVal(sb, loinc, "74291-6", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("pupil") >= 0) {
      
      buildLOINCVal(sb, loinc, "29084-1", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("saturaz") >= 0 || item.indexOf("ossigen") >= 0) {
      
      buildLOINCVal(sb, loinc, "59413-5", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.startsWith("vas") || item.indexOf("visual") >= 0) {
      
      buildLOINCVal(sb, loinc, "38214-3", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("ritmo") >= 0) {
      
      buildLOINCVal(sb, loinc, "56849-3", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("esame") >= 0 && item.indexOf("ob") >= 0) {
      
      buildLOINCVal(sb, loinc, "22029-3", eref, evalue, eunit, edesc, etime);
      
    }
    else if(item.indexOf("valutaz") >= 0 || item.indexOf("sanit") >= 0) {
      
      if(edesc != null && edesc.length() > 0) {
        sb.append("<code code=\"" + evalue + "\" codeSystem=\"" + OID_VAL_SAN_118 + "\" codeSystemName=\"Valutazione sanitaria del paziente 118\" displayName=\"" + edesc + "\">");
        if(eref != null && eref.length() > 0) {
          sb.append("<originalText>");
          sb.append("<reference value=\"#" + append(eref, "-COD") + "\" />");
          sb.append("</originalText>");
        }
        sb.append("</code>");
        sb.append("<text>"); 
        sb.append("<reference value=\"#" + append(eref, "-DES") + "\" />"); 
        sb.append("</text>"); 
      }
      else {
        sb.append("<code code=\"" + evalue + "\" codeSystem=\"" + OID_VAL_SAN_118 + "\" codeSystemName=\"Valutazione sanitaria del paziente 118\">");
        if(eref != null && eref.length() > 0) {
          sb.append("<originalText>");
          sb.append("<reference value=\"#" + eref + "\" />");
          sb.append("</originalText>");
        }
        sb.append("</code>");
      }
      
    }
    else if(item.indexOf("diag") >= 0 || item.indexOf("icd9") >= 0) {
      
      if(evalue != null && evalue.length() > 0) {
        char c0 = evalue.charAt(0);
        if(!Character.isDigit(c0)) {
          // Is not a ICD9-CM code.
          evalue = null;
        }
      }
      
      ICodeSystem icd9cm = codeSystems.getCodeSystem(cda.getAuthorityCode(), CodeSystems.ICD9CM);
      if(icd9cm != null) {
        if(evalue != null && evalue.length() > 0) {
          sb.append("<code code=\"" + evalue + "\" codeSystem=\"" + icd9cm.getCodeSystem() + "\" codeSystemName=\"" + icd9cm.getCodeSystemName() + "\">");
        }
        else {
          sb.append("<code codeSystem=\"" + icd9cm.getCodeSystem() + "\" codeSystemName=\"" + icd9cm.getCodeSystemName() + "\" nullFlavor=\"NA\">");
        }
        if(eref != null && eref.length() > 0) {
          sb.append("<originalText>");
          sb.append("<reference value=\"#" + eref + "\" />");
          sb.append("</originalText>");
        }
        sb.append("</code>");
      }
      else {
        addWarning("No codesystem found for " + cda.getAuthorityCode() + "," + CodeSystems.ICD9CM);
      }
    }
    else if(item.startsWith("terap") || item.startsWith("trat")) {
      
      if(eref != null && eref.length() > 0) {
        sb.append("<code nullFlavor=\"NA\">");
        sb.append("<originalText>");
        sb.append("<reference value=\"#" + eref + "\" />");
        sb.append("</originalText>");
        sb.append("</code>");
      }
      
    }
    else if(item.indexOf("esito") >= 0) {
      
      if(edesc != null && edesc.length() > 0) {
        sb.append("<code code=\"" + evalue + "\" codeSystem=\"" + OID_ESITO_118 + "\" codeSystemName=\"Esito trattamento\" displayName=\"" + edesc + "\">");
      }
      else {
        sb.append("<code code=\"" + evalue + "\" codeSystem=\"" + OID_ESITO_118 + "\" codeSystemName=\"Esito trattamento\">");
      }
      if(eref != null && eref.length() > 0) {
        sb.append("<originalText>");
        if(edesc != null && edesc.length() > 0) {
          sb.append("<reference value=\"#" + append(eref, "-COD") + "\" />");
        }
        else {
          sb.append("<reference value=\"#" + eref + "\" />");
        }
        sb.append("</originalText>");
      }
      sb.append("</code>");
      
    }
    else {
      addWarning("Entry " + entry + " non supportata.");
      
      if(eref != null && eref.length() > 0) {
        sb.append("<code nullFlavor=\"NA\">");
        sb.append("<originalText>");
        sb.append("<reference value=\"#" + eref + "\" />");
        sb.append("</originalText>");
        sb.append("</code>");
      }
    }
    
    return sb.toString();
  }
  
  protected 
  void buildCritVal(StringBuilder sb, String eref, String evalue, String edesc, Date etime)
      throws Exception
  {
    if(evalue == null || evalue.length() == 0) {
      evalue = "Verde";
    }
    if(edesc == null || edesc.length() == 0) {
      if(evalue.equalsIgnoreCase("B")) {
        edesc = "Codice Bianco";
      }
      else if(evalue.equalsIgnoreCase("V")) {
        edesc = "Codice Verde";
      }
      else if(evalue.equalsIgnoreCase("G")) {
        edesc = "Codice Giallo";
      }
      else if(evalue.equalsIgnoreCase("R")) {
        edesc = "Codice Rosso";
      }
      else {
        edesc = "Codice " + evalue;
      }
    }
    if(evalue.length() > 1) {
      evalue = evalue.substring(0, 1);
    }
    evalue = evalue.toUpperCase();
    
    sb.append("<code code=\"" + evalue + "\" codeSystem=\"" + OID_CRITICITA + "\" codeSystemName=\"Codici Criticita'\">");
    if(eref != null && eref.length() > 0) {
      sb.append("<originalText>");
      if(edesc != null && edesc.length() > 0) {
        sb.append("<reference value=\"#" + append(eref, "-COD") + "\" />");
      }
      else {
        sb.append("<reference value=\"#" + eref + "\" />");
      }
      sb.append("</originalText>");
    }
    sb.append("</code>");
    sb.append("<text>" + CDAUtils.xml(edesc) + "</text>");
    if(etime != null) {
      sb.append("<effectiveTime value=\"" + CDAUtils.getTimestamp(etime) + "\" />");
    }
  }
  
  protected 
  void buildLOINCVal(StringBuilder sb, ICodeSystem loinc, String code, String eref, String evalue, String eunit, String edesc, Date etime)
      throws Exception
  {
    String displayName = loinc.getDisplayName(code);
    if(displayName == null || displayName.length() == 0) {
      addWarning("Nessuna descrizione LOINC individuata per " + code);
    }
    
    if(code != null && code.length() > 0) {
      if(displayName != null && displayName.length() > 0) {
        sb.append("<code code=\"" + code + "\" codeSystem=\"" + loinc.getCodeSystem() + "\" codeSystemName=\"" + loinc.getCodeSystemName() + "\" displayName=\"" + displayName + "\" />");
      }
      else {
        sb.append("<code code=\"" + code + "\" codeSystem=\"" + loinc.getCodeSystem() + "\" codeSystemName=\"" + loinc.getCodeSystemName() + "\" />");
      }
    }
    if(eunit != null && eunit.length() > 0) {
      if(eref != null && eref.length() > 0) {
        sb.append("<text>");
        sb.append("<reference value=\"#" + eref + "\" />");
        sb.append("</text>");
      }
      
      sb.append("<value xsi:type=\"PQ\" value=\"" + evalue + "\" unit=\"" + eunit + "\" />");
    }
    else {
      if(eref != null && eref.length() > 0) {
        sb.append("<text>");
        if(edesc != null && edesc.length() > 0 && !edesc.equals(displayName)) {
          sb.append("<reference value=\"#" + append(eref, "-DES") + "\" />");
        }
        else {
          sb.append("<reference value=\"#" + eref + "\" />");
        }
        sb.append("</text>");
      }
      
      if(!isDescriptionValue(evalue)) {
        sb.append("<value xsi:type=\"CD\">");
        sb.append("<originalText>");
        if(edesc != null && edesc.length() > 0 && !edesc.equals(displayName)) {
          sb.append("<reference value=\"#" + append(eref, "-COD") + "\" />");
        }
        else {
          sb.append("<reference value=\"#" + eref + "\" />");
        }
        sb.append("</originalText>");
        sb.append("</value>");
      }
    }
    if(etime != null) {
      sb.append("<effectiveTime value=\"" + CDAUtils.getTimestamp(etime) + "\" />");
    }
  }
  
  protected 
  boolean isAct(Entry entry)
  {
    if(entry == null) return false;
    
    String item = getItem(entry);
    if(item == null) return false;
    
    if(item.indexOf("crit") >= 0) {
      return true;
    }
    else if(item.indexOf("cod") >= 0 && item.indexOf("fin") >= 0) {
      return true;
    }
    else if(item.indexOf("diag") >= 0 || item.indexOf("icd9") >= 0) {
      return true;
    }
    else if(item.indexOf("esito") >= 0) {
      return true;
    }
    else if(item.startsWith("terap")) {
      return true;
    }
    else if(item.startsWith("trat")) {
      return true;
    }
    return false;
  }
  
  protected 
  String getSectionTemplateId(Section section)
  {
    if(section == null) return null;
    
    String result = section.getTemplateId();
    if(result != null && result.length() > 0) {
      return result;
    }
    
    String sectionId = section.getId();
    if(sectionId == null || sectionId.length() == 0) {
      return null;
    }
    
    String sectionIdLC = sectionId.toLowerCase();
    if(sectionIdLC.indexOf("val") >= 0) {
      if(sectionIdLC.indexOf("pri") >= 0) {
        // Scheda 118 - Valutazione Primaria
        return "2.16.840.1.113883.2.9.10.1.6.56";
      }
      else if(sectionIdLC.indexOf("sec") >= 0) {
        // Scheda 118 - Valutazione Secondaria
        return "2.16.840.1.113883.2.9.10.1.4.2.13";
      }
      else {
        // Scheda 118 - Valutazione Sanitaria
        return "2.16.840.1.113883.2.9.10.1.4.2.13";
      }
    }
    else if(sectionIdLC.indexOf("mis") >= 0) {
      if(sectionIdLC.indexOf("fin") >= 0) {
        // Scheda 118 - Fine Missione
        return "2.16.840.1.113883.2.9.10.1.6.21";
      }
      else {
        // Scheda 118 - Missione
        return "2.16.840.1.113883.2.9.10.1.6.21";
      }
    }
    else if(sectionIdLC.startsWith("terap") || sectionIdLC.startsWith("trat")) {
      // Terapia / Trattamenti
      return "2.16.840.1.113883.2.9.10.1.4.2.11";
    }
    else if(sectionIdLC.indexOf("par") >= 0) {
      // Parametri
      return "2.16.840.1.113883.2.9.10.1.4.2.8";
    }
    
    return null;
  }
  
  protected 
  String getSectionCode(Section section, String defaultCode)
  {
    if(section == null) return null;
    
    String result = section.getCode();
    if(result != null && result.length() > 0) {
      return CDAUtils.getCode(result);
    }
    
    String sectionId = section.getId();
    if(sectionId == null || sectionId.length() == 0) {
      return defaultCode;
    }
    
    String sectionIdLC = sectionId.toLowerCase();
    if(sectionIdLC.indexOf("val") >= 0) {
      if(sectionIdLC.indexOf("pri") >= 0) {
        // Scheda 118 - Valutazione Primaria
        return "78337-3";
      }
      else if(sectionIdLC.indexOf("sec") >= 0) {
        // Scheda 118 - Valutazione Secondaria
        return "47420-5";
      }
      else {
        // Scheda 118 - Valutazione Sanitaria
        return "56817-0";
      }
    }
    else if(sectionIdLC.indexOf("mis") >= 0) {
      if(sectionIdLC.indexOf("fin") >= 0) {
        // Scheda 118 - Fine Missione
        return "54094-8";
      }
      else {
        // Scheda 118 - Missione
        return "54094-8";
      }
    }
    else if(sectionIdLC.startsWith("terap") || sectionIdLC.startsWith("trat")) {
      // Terapia / Trattamenti
      return "47519-4";
    }
    else if(sectionIdLC.indexOf("par") >= 0) {
      // Parametri
      return "8716-3";
    }
    else if(sectionIdLC.indexOf("ref") >= 0) {
      // Referto
      return "62387-6";
    }
    else if(sectionIdLC.indexOf("diag") >= 0) {
      // Diagnosi
      return "29548-5";
    }
    
    addWarning("Codice sezione non individuato per sectionId=" + sectionId + " (def. " + defaultCode + ").");
    
    return defaultCode;
  }
  
  protected 
  String getSectionDisplayName(Section section)
  {
    if(section == null) return null;
    
    String sectionId = section.getId();
    if(sectionId == null || sectionId.length() == 0) {
      return null;
    }
    
    String sectionIdLC = sectionId.toLowerCase();
    if(sectionIdLC.indexOf("val") >= 0) {
      if(sectionIdLC.indexOf("pri") >= 0) {
        // Scheda 118 - Valutazione Primaria
        return "Inquadramento clinico iniziale";
      }
      else if(sectionIdLC.indexOf("sec") >= 0) {
        // Scheda 118 - Valutazione Secondaria
        return "Inquadramento clinico secondario";
      }
      else {
        // Scheda 118 - Valutazione Sanitaria
        return "Valutazione Sanitaria";
      }
    }
    else if(sectionIdLC.indexOf("mis") >= 0) {
      if(sectionIdLC.indexOf("fin") >= 0) {
        // Scheda 118 - Fine Missione
        return "Note Triage";
      }
      else {
        // Scheda 118 - Missione
        return "Note Triage";
      }
    }
    else if(sectionIdLC.indexOf("terap") >= 0 || sectionIdLC.indexOf("trat") >= 0) {
      // Terapia / Trattamenti / Prestazioni
      return "Storia di Procedure";
    }
    else if(sectionIdLC.indexOf("par") >= 0) {
      // Parametri
      return "Parametri vitali";
    }
    
    addWarning("DisplayName sezione non individuata per sectionId=" + sectionId + "");
    
    return "";
  }
  
  protected
  String getSectionComment(Section section)
  {
    if(section == null) return null;
    
    String result = section.getCode();
    if(result != null && result.length() > 0) {
      return CDAUtils.getDisplayName(result, result);
    }
    
    String sectionId = section.getId();
    if(sectionId == null || sectionId.length() == 0) {
      return null;
    }
    
    String sectionIdLC = sectionId.toLowerCase();
    if(sectionIdLC.indexOf("val") >= 0) {
      if(sectionIdLC.indexOf("pri") >= 0) {
        // Scheda 118 - Valutazione Primaria
        return "cfr. VPS / Sezione Inquadramento clinico iniziale";
      }
      else if(sectionIdLC.indexOf("sec") >= 0) {
        // Scheda 118 - Valutazione Secondaria
        return "cfr. PSS / Stato funzionale del Paziente";
      }
      else {
        // Scheda 118 - Valutazione Sanitaria
        return "cfr. VPS / Sezione Motivo Visita - Problema principale";
      }
    }
    else if(sectionIdLC.indexOf("mis") >= 0) {
      if(sectionIdLC.indexOf("fin") >= 0) {
        // Scheda 118 - Fine Missione
        return "cfr. VPS / Sezione Triage";
      }
      else {
        // Scheda 118 - Missione
        return "cfr. VPS / Sezione Triage";
      }
    }
    else if(sectionIdLC.indexOf("terap") >= 0 || sectionIdLC.indexOf("trat") >= 0) {
      // Terapia / Trattamenti
      return "cfr PSS / Sezione trattamenti e procedure terapeutiche";
    }
    else if(sectionIdLC.indexOf("par") >= 0) {
      // Parametri
      return "cfr PSS / Parametri Vitali";
    }
    
    addWarning("Commento sezione non individuato per sectionId=" + sectionId + ".");
    
    return null;
  }
  
  protected static
  String getAuthorityName(String authorityCode)
  {
    if(authorityCode == null || authorityCode.length() == 0) {
      return "";
    }
    if(authorityCode.equals("010")) return "Regione Piemonte";
    if(authorityCode.equals("020")) return "Regione Val D'Aosta";
    if(authorityCode.equals("030")) return "Regione Lombardia";
    if(authorityCode.equals("041")) return "Provincia Autonoma Bolzano";
    if(authorityCode.equals("042")) return "Provincia Autonoma Trento";
    if(authorityCode.equals("050")) return "Regione Veneto";
    if(authorityCode.equals("060")) return "Regione Friuli Venezia Giulia";
    if(authorityCode.equals("070")) return "Regione Liguria";
    if(authorityCode.equals("080")) return "Regione Emilia Romagna";
    if(authorityCode.equals("090")) return "Regione Toscana";
    if(authorityCode.equals("100")) return "Regione Umbria";
    if(authorityCode.equals("110")) return "Regione Marche";
    if(authorityCode.equals("120")) return "Regione Lazio";
    if(authorityCode.equals("130")) return "Regione Abruzzo";
    if(authorityCode.equals("140")) return "Regione Molise";
    if(authorityCode.equals("150")) return "Regione Campania";
    if(authorityCode.equals("160")) return "Regione Puglia";
    if(authorityCode.equals("170")) return "Regione Basilicata";
    if(authorityCode.equals("180")) return "Regione Calabria";
    if(authorityCode.equals("190")) return "Regione Sicilia";
    if(authorityCode.equals("200")) return "Regione Sardegna";
    return "Italia";
  }
  
  protected 
  String append(String text, String suffix)
  {
    if(text == null || text.length() == 0) {
      return suffix;
    }
    if(text.endsWith(suffix)) {
      return text;
    }
    return text + suffix;
  }
  
  protected 
  boolean isDescriptionValue(String value)
      throws Exception
  {
    if(value == null || value.length() < 8) {
      return false;
    }
    if(value.indexOf(' ') > 0) {
      return true;
    }
    if(!Character.isDigit(value.charAt(0)) && !Character.isDigit(value.charAt(value.length()-1))) {
      return true;
    }
    return false;
  }
}
