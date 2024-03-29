package org.dew.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dew.cda.CDADeserializer;
import org.dew.cda.CDARenderer_IT;
import org.dew.cda.CDASerializer_IT;
import org.dew.cda.CDASignerCAdES;
import org.dew.cda.CDASignerXAdES;
import org.dew.cda.CDAValidator;

import org.dew.hl7.ClinicalDocument;
import org.dew.hl7.ICDADeserializer;
import org.dew.hl7.ICDARenderer;
import org.dew.hl7.ICDASerializer;
import org.dew.hl7.ICDASigner;
import org.dew.hl7.ICDAValidator;
import org.dew.hl7.Organization;
import org.dew.hl7.Person;
import org.dew.hl7.ValidationResult;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public 
class TestHL7CDA2 extends TestCase 
{
  protected boolean trace = false;

  public TestHL7CDA2(String testName) {
    super(testName);
    
    String traceOpt = System.getProperty("trace");
    if(traceOpt != null && traceOpt.length() > 0) {
      char c0 = traceOpt.charAt(0);
      trace = "1JSTYjsty".indexOf(c0) >= 0;
    }
  }
  
  public static Test suite() {
    return new TestSuite(TestHL7CDA2.class);
  }
  
  public void testApp() throws Exception {
    serializeAndDeserialize();
  }
  
  protected
  void serializeAndDeserialize()
    throws Exception
  {
    ClinicalDocument cda1 = buildEmergencyReport();
    
    String xml1 = cdaSerialize(cda1);
    
    validate(xml1);
    
    ClinicalDocument cda2 = cdaDeserialize(xml1);
    
    cdaSerialize(cda2);
    
    cdaRenderer(cda2);
    
    transform(xml1);
    
    signXAdES(xml1);
  }
  
  protected 
  String cdaSerialize(ClinicalDocument clinicalDocument) 
    throws Exception 
  {
    ICDASerializer cdaSerializer = new CDASerializer_IT();
    
    String xml = cdaSerializer.toXML(clinicalDocument);
    
    if(trace) System.out.println(xml);
    
    return xml;
  }
  
  protected 
  ClinicalDocument cdaDeserialize(String content) 
    throws Exception 
  {
    ICDADeserializer cdaDeserializer = new CDADeserializer();
    
    cdaDeserializer.load(content);
    
    ClinicalDocument clinicalDocument = cdaDeserializer.getClinicalDocument();
    
    return clinicalDocument;
  }
  
  protected
  void cdaSignCAdES()
    throws Exception
  {
    ClinicalDocument cda = buildClinicalDocument();
    
    String xml = cdaSerialize(cda);
    
    signCAdES(xml);
  }
  
  protected
  void cdaSignXAdES()
    throws Exception
  {
    ClinicalDocument cda = buildClinicalDocument();
    
    String xml = cdaSerialize(cda);
    
    signXAdES(xml);
  }
  
  protected 
  void validate(String content) 
    throws Exception 
  {
    if(content == null) {
      System.out.println("content is null");
      return;
    }
    
    if(content.length() < 20) {
      System.out.println("content is not valid: \"" + content + "\"");
      return;
    }
    
    ICDAValidator validator = new CDAValidator();
    
    ValidationResult result = validator.validate(content.getBytes());
    
    if(result.isSuccess()) {
      System.out.println("content is valid");
      return;
    }
    
    List<String> warnings = result.getWarnings();
    if(warnings != null && warnings.size() > 0) {
      System.out.println("Validation warnings:");
      for(int i = 0; i < warnings.size(); i++) {
        System.out.println(warnings.get(i));
      }
    }
    
    List<String> errors = result.getErrors();
    if(errors != null && errors.size() > 0) {
      System.out.println("Validation errors:");
      for(int i = 0; i < errors.size(); i++) {
        System.out.println(errors.get(i));
      }
    }
    
    List<String> fatals = result.getFatals();
    if(fatals != null && fatals.size() > 0) {
      System.out.println("Validation fatals:");
      for(int i = 0; i < fatals.size(); i++) {
        System.out.println(fatals.get(i));
      }
    }
  }
  
  protected 
  void transform(String content) 
    throws Exception 
  {
    if(content == null) {
      System.out.println("content is null");
      return;
    }
    
    if(content.length() < 20) {
      System.out.println("content is not valid: \"" + content + "\"");
      return;
    }
    
    ICDARenderer cdaRenderer = new CDARenderer_IT();
    
    String html = cdaRenderer.transform(content, "CDAit.xsl");
    
    if(trace) System.out.println(html);
  }
  
  protected 
  void signXAdES(String content) 
    throws Exception 
  {
    if(content == null) {
      System.out.println("content is null");
      return;
    }
    
    if(content.length() < 20) {
      System.out.println("content is not valid: \"" + content + "\"");
      return;
    }
    
    ICDASigner cdaSigner = new CDASignerXAdES();
    
    System.out.println("sign...");
    
    byte[] signature = cdaSigner.sign(content);
    
    if(signature == null) {
      System.out.println("signature is null");
    }
    else if(signature.length == 0) {
      System.out.println("signature is 0 length");
    }
    else {
      if(trace) System.out.println(new String(signature));
    }
  }
  
  protected 
  void signCAdES(String content) 
    throws Exception 
  {
    if(content == null) {
      System.out.println("content is null");
      return;
    }
    
    if(content.length() < 20) {
      System.out.println("content is not valid: \"" + content + "\"");
      return;
    }
    
    ICDASigner cdaSigner = new CDASignerCAdES();
    
    System.out.println("sign...");
    
    byte[] signature = cdaSigner.sign(content);
    
    if(signature == null) {
      System.out.println("signature is null");
    }
    else if(signature.length == 0) {
      System.out.println("signature is 0 length");
    }
    else {
      System.out.println("signature has " + signature.length);
    }
    
    X509Certificate certificate = cdaSigner.validate(signature);
    
    if(trace) System.out.println(certificate);
  }
  
  protected 
  String cdaRenderer(ClinicalDocument clinicalDocument) 
    throws Exception 
  {
    ICDARenderer cdaRenderer = new CDARenderer_IT();
    
    Map<String, Object> renderOptions = new HashMap<String, Object>();
    renderOptions.put("style",     "body{ color: #202020; margin: 4 8 4 8; }");
    renderOptions.put("table",     "width: 100%;");
    renderOptions.put("th",        "background-color: #a8d7f7;");
    renderOptions.put("td",        "background-color: #cfeafc;");
    renderOptions.put("title",     "color: #000080;");
    renderOptions.put("paragraph", "font-style: italic;");
    
    cdaRenderer.setOptions(renderOptions);
    
    String html = cdaRenderer.toHTML(clinicalDocument);
    
    if(trace) System.out.println(html);
    
    return html;
  }
  
  protected  
  ClinicalDocument buildClinicalDocument() 
  {
    byte[] pdfReferto = "%PDF-1.1".getBytes();
    
    ClinicalDocument clinicalDocument = new ClinicalDocument("120", "Regione Lazio");
    
    clinicalDocument.setId("REF-20201102-0001");
    clinicalDocument.setEffectiveTime(toDate(2, 11, 2020, 16, 30));
    clinicalDocument.setTitle("Referto ambulatoriale");
    
    clinicalDocument.setPatient(new Person("RSSMRA75C03F839K", "ROSSI", "MARIO", "M", toDate(03, 3, 1975), "NAPOLI"));
    clinicalDocument.setAuthor(new Person("XXXXXX01A01H501X", "XXX", "XXX", "Dott.", "120201", "ROMA 1"));
    clinicalDocument.setCustodian(new Organization("120201", "ROMA 1"));
    clinicalDocument.setScopingOrg(new Organization("120201", "ROMA 1"));
    
    clinicalDocument.addSection("DIAGNOSI", "Diagnosi")
      .addEntry("Diagnosi", "Scompenso cardiaco");
    
    clinicalDocument.addSection("PDF", "application/pdf", pdfReferto);
    
    return clinicalDocument;
  }
  
  protected  
  ClinicalDocument buildVaccinationCertificate() 
  {
    byte[] pdfCertificate = "%PDF-1.1".getBytes();
    
    ClinicalDocument clinicalDocument = new ClinicalDocument("120", "Regione Lazio");
    
    clinicalDocument.setId("REF-20201102-0001");
    clinicalDocument.setEffectiveTime(toDate(2, 11, 2020, 16, 30));
    clinicalDocument.setCode("82593-5");
    clinicalDocument.setTitle("Certificato Vaccinale");
    
    clinicalDocument.setPatient(new Person("RSSMRA75C03F839K", "ROSSI", "MARIO", "M", toDate(03, 3, 1975), "NAPOLI"));
    clinicalDocument.setAuthor(new Person("XXXXXX01A01H501X", "XXX", "XXX", "Dott."));
    clinicalDocument.setCustodian(new Organization("120201", "ROMA 1"));
    
    clinicalDocument.addSection("SOMMINISTRAZIONE-1", "Somministrazione 1")
    .addEntry("Dose",                      "1")
    .addEntry("Data",                      "28/01/2021")
    .addEntry("Punto di somministrazione", "AVR - ASL ROMA2 - ROMA")
    .addEntry("Lotto",                     "EL1484")
    .addEntry("Farmaco",                   "049269018", "COMIRNATY BioNTech/Pfizer", 1, toDate(28, 1, 2021));
    
    clinicalDocument.addSection("PDF", "application/pdf", pdfCertificate);
    
    return clinicalDocument;
  }
  
  protected
  ClinicalDocument buildEmergencyReport() 
  {
    byte[] pdfReferto = "%PDF-1.1".getBytes();
    byte[] pdfScheda  = "%PDF-1.1".getBytes();
    
    ClinicalDocument clinicalDocument = new ClinicalDocument("160", "Regione Puglia");
    
    clinicalDocument.setId("BA-2020-000510-0");
    clinicalDocument.setEffectiveTime(toDate(13, 10, 2020, 16, 30));
    clinicalDocument.setTitle("Scheda Paziente 118");
    
    clinicalDocument.setPatient(new Person("RSSMRA75C03F839K", "ROSSI", "MARIO", "M", toDate(03, 3, 1975), "NAPOLI"));
    clinicalDocument.setAuthor(new Person("oper118"));
    clinicalDocument.setCustodian(new Organization("160114", "ASL BARI", "BARI", "70123", "LUNGOMARE STARITA", "6"));
    
    clinicalDocument.addSection("INIZIO_MISSIONE", "Missione")
      .addEntry("Data",                       "13/10/2020")
      .addEntry("Luogo dell'evento",          "PIAZZA VITTORIO EMANUELE 50 -- loc. MONOPOLI -- MONOPOLI (BA)")
      .addEntry("Motivo",                     "TRAUMA")
      .addEntry("Codice Criticita'",          "G", "Codice Giallo")
      .addEntry("Dinamica",                   "MINORE;INCIDENTE AUTO");
      
    clinicalDocument.addSection("VALUTAZIONE_PRIMARIA", "Valutazione Primaria")
      .addEntry("P.A. Sistolica",             "150 mm[Hg]")
      .addEntry("P.A. Diastolica",            "90 mm[Hg]")
      .addEntry("Temperatura",                "36.3 Cel")
      .addEntry("Ritmo al monitor",           "RS")
      .addEntry("Glicemia",                   "105 mg/dl")
      .addEntry("Apertura Occhi",             "4", "Spontanea")
      .addEntry("Risposta Verbale",           "5", "Orientata")
      .addEntry("Risposta Motoria",           "6", "Ubbidisce al comando")
      .addEntry("Mimica facciale: i due lati del volto non si muovono allo stesso modo",  "NO")
      .addEntry("Spostamento delle braccia: un braccio non si muove o cade giu'",         "NO")
      .addEntry("Linguaggio: Il paziente inceppa sulla parola, usa parole inappropriate o non e' in grado di parlare", "NO");
    
    clinicalDocument.addSection("VALUTAZIONE_SECONDARIA", "Valutazione Secondaria")
      .addEntry("Trauma",                     "Contusione spalla destra")
      .addEntry("Pupille",                    "");
    
    clinicalDocument.addSection("VALUTAZIONE_SANITARIA", "Valutazione sanitaria")
      .addEntry("Valutazione sanitaria",      "I1", "Soggetto affetto da forma morbosa di grado lieve")
      .addEntry("Diagnosi sul luogo",         "PATOLOGIA TRAUMATICA - CONTUSIONE");
    
    clinicalDocument.addSection("TERAPIA_TRATTAMENTI", "Terapia - Trattamenti")
      .addEntry("Terapia",                    "ECG ONLINE, CONTROLLO SATURAZIONE O2, GLICEMIA SU SANGUE CAPILLARE, MONITORAGGIO DELLA PRESSIONE ARTERIOSA SISTEMICA, VISITA GENERALE")
      .addEntry("REFERTO", "application/pdf", pdfReferto);
    
    clinicalDocument.addSection("PARAMETRI_POST_TRATTAMENTO", "Parametri post trattamento")
      .addEntry("P.A. Sistolica Post Trattamento",    "120 mm[Hg]")
      .addEntry("P.A. Diastolica Post Trattamento",   "80 mm[Hg]")
      .addEntry("Temperatura Post Trattamento",       "36.2 Cel")
      .addEntry("VAS Post Trattamento",               "1")
      .addEntry("Saturazione Post Trattamento",       "90 %");
    
    clinicalDocument.addSection("FINE_MISSIONE", "Fine missione")
      .addEntry("Codice Fine Missione",               "V", "Codice Verde")
      .addEntry("Esito",                              "3", "Trattamento sul posto senza trasporto (consenso esplicito informato)");
    
    clinicalDocument.addSection("PDF", "application/pdf", pdfScheda);
    
    return clinicalDocument;
  }
  
  protected
  ClinicalDocument buildAnnulDocument() 
  {
    ClinicalDocument clinicalDocument = new ClinicalDocument("160", "Regione Puglia");
    
    clinicalDocument.setId("BA-2020-000510-0-ANN");
    clinicalDocument.setEffectiveTime(toDate(13, 10, 2020, 16, 30));
    clinicalDocument.setTitle("Annullamento");
    
    clinicalDocument.setPatient(new Person("RSSMRA75C03F839K", "ROSSI", "MARIO", "M", toDate(03, 3, 1975), "NAPOLI"));
    clinicalDocument.setAuthor(new Person("oper118"));
    clinicalDocument.setCustodian(new Organization("160114", "ASL BARI", "BARI", "70123", "LUNGOMARE STARITA", "6"));
    
    clinicalDocument.setRelatedDocumentId("BA-2020-000510-0");
    clinicalDocument.setRelatedDocumentType("XFRM");
    
    clinicalDocument.addSection("CAUSALE", "Motivazione Annullamento Documento")
      .addEntry("Text", "Documento annullato per rettifica");
    
    return clinicalDocument;
  }
  
  protected static
  Date toDate(int iDD, int iMM, int iYYYY)
  {
    Calendar calendar = new GregorianCalendar(iYYYY, iMM-1, iDD);
    
    return calendar.getTime();
  }
  
  protected static 
  Date toDate(int iDD, int iMM, int iYYYY, int iHH, int iMI)
  {
    Calendar calendar = new GregorianCalendar(iYYYY, iMM-1, iDD, iHH, iMI, 0);
    
    return calendar.getTime();
  }
  
  protected static 
  Date toDate(int iDD, int iMM, int iYYYY, int iHH, int iMI, int iSS)
  {
    Calendar calendar = new GregorianCalendar(iYYYY, iMM-1, iDD, iHH, iMI, iSS);
    
    return calendar.getTime();
  }
  
  protected static
  byte[] readFile(String file)
    throws Exception
  {
    int sep = file.indexOf('/');
    if(sep < 0) sep = file.indexOf('\\');
    InputStream is = null;
    if(sep < 0) {
      URL url = Thread.currentThread().getContextClassLoader().getResource(file);
      is = url.openStream();
    }
    else {
      is = new FileInputStream(file);
    }
    try {
      int n;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buff = new byte[1024];
      while((n = is.read(buff)) > 0) baos.write(buff, 0, n);
      return baos.toByteArray();
    }
    finally {
      if(is != null) try{ is.close(); } catch(Exception ex) {}
    }
  }
}