package org.dew.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import org.dew.cda.*;
import org.dew.hl7.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@SuppressWarnings("deprecation")
public class TestHL7CDA2 extends TestCase {
  
  public static String KEYSTORE_FILE    = "keystore.jks";
  public static String KEYSTORE_PASS    = "password";
  public static String KEYSTORE_ALIAS   = "selfsigned";
  
  public static String PRIVATEKEY_FILE  = "signature.pem";
  public static String CERTIFICATE_FILE = "signature.crt";
  
  protected KeyStore keyStore;
  
  public TestHL7CDA2(String testName) {
    super(testName);
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
    
    sign(xml1);
    
    transform(xml1);
  }
  
  protected 
  String cdaSerialize(ClinicalDocument clinicalDocument) 
    throws Exception 
  {
    ICDASerializer cdaSerializer = new CDASerializer_IT();
    
    String xml = cdaSerializer.toXML(clinicalDocument);
    
    System.out.println(xml);
    
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
    
    System.out.println(html);
  }
  
  protected 
  void sign(String content) 
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
    
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("privatekey",  getSignPrivateKey());
    options.put("certificate", getSignCertificate());
    
    ICDASigner cdaSigner = new CDASignerXAdES();
    cdaSigner.setOptions(options);
    
    System.out.println("sign...");
    
    byte[] signature = cdaSigner.sign(content);
    
    if(signature == null) {
      System.out.println("signature is null");
    }
    else if(signature.length == 0) {
      System.out.println("signature is 0 length");
    }
    else {
      System.out.println(new String(signature));
    }
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
    
    System.out.println(html);
    
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
    clinicalDocument.setAuthor(new Person("XXXXXX01A01H501X", "XXX", "XXX", "Dott."));
    clinicalDocument.setCustodian(new Organization("120201", "ROMA 1"));
    
    clinicalDocument.addSection("DIAGNOSI", "Diagnosi")
      .addEntry("Diagnosi", "Scompenso cardiaco");
    
    clinicalDocument.addSection("PDF", "application/pdf", pdfReferto);
    
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
  
  protected
  KeyStore loadKeyStore()
    throws Exception
  {
    if(keyStore != null) return keyStore;
    
    String keystoreFile = KEYSTORE_FILE;
    if(keystoreFile == null || keystoreFile.length() == 0) {
      return null;
    }
    String password = KEYSTORE_PASS;
    if(password == null || password.length() == 0) {
      return null;
    }
    
    InputStream is = openResource(keystoreFile);
    if(is == null) return null;
    
    try {
      Security.addProvider(new BouncyCastleProvider());
      
      if(keystoreFile.endsWith(".p12")) {
        keyStore = KeyStore.getInstance("PKCS12", "BC");
      }
      else {
        keyStore = KeyStore.getInstance("JKS");
      }
      keyStore.load(is, password.toCharArray());
    }
    catch(Exception ex) {
      keyStore = null;
      ex.printStackTrace();
    }
    
    return keyStore;
  }
  
  protected
  PrivateKey getSignPrivateKey()
    throws Exception
  {
    if(keyStore == null) loadKeyStore();
    
    if(keyStore != null) {
      String alias = KEYSTORE_ALIAS;
      
      if(alias != null && alias.length() > 0) {
        String password = KEYSTORE_PASS;
        
        if(password != null && password.length() > 0) {
          Key result = keyStore.getKey(alias, password.toCharArray());
          if(result instanceof PrivateKey) {
            return (PrivateKey) result;
          }
        }
      }
    }
    
    return loadPrivateKey(PRIVATEKEY_FILE);
  }
  
  protected
  X509Certificate getSignCertificate()
    throws Exception
  {
    if(keyStore == null) loadKeyStore();
    
    if(keyStore != null) {
      String alias = KEYSTORE_ALIAS;
      
      if(alias != null && alias.length() > 0) {
        Certificate result = keyStore.getCertificate(alias);
        if(result instanceof X509Certificate) {
          return (X509Certificate) result;
        }
      }
    }
    
    return loadCertificate(CERTIFICATE_FILE);
  }
  
  public static
  X509Certificate loadCertificate(String sFile)
    throws Exception
  {
    InputStream is = openResource(sFile);
    
    if(is == null) return null;
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      int n;
      byte[] buff = new byte[1024];
      while((n = is.read(buff)) > 0) baos.write(buff, 0, n);
    }
    finally {
      if(is != null) try{ is.close(); } catch(Exception ex) {}
    }
    byte[] content = baos.toByteArray();
    if(content == null || content.length < 4) {
      throw new Exception("Invalid file");
    }
    if(content[0] == 45 && content[1] == 45 && content[2] == 45) {
      String sContent = new String(content);
      int iStart = sContent.indexOf("ATE-----");
      if(iStart > 0) {
        int iEnd = sContent.indexOf("-----END");
        if(iEnd > 0) {
          String sBase64 = sContent.substring(iStart+8, iEnd).trim();
          content = Base64Coder.decodeLines(sBase64);
        }
      }
    }
    ByteArrayInputStream bais = new ByteArrayInputStream(content);
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    return (X509Certificate) cf.generateCertificate(bais);
  }
  
  public static
  PrivateKey loadPrivateKey(String sFile)
    throws Exception
  {
    InputStream is = openResource(sFile);
    
    if(is == null) return null;
    
    PEMReader pemReader = null;
    try {
      Security.addProvider(new BouncyCastleProvider());
      
      pemReader = new PEMReader(new InputStreamReader(is));
      
      Object pemObject = pemReader.readObject();
      if(pemObject instanceof KeyPair) {
        return ((KeyPair) pemObject).getPrivate();
      }
      
      throw new Exception("Invalid pem file " + sFile);
    }
    finally {
      if(is != null) try{ is.close(); } catch(Exception ex) {}
      if(pemReader != null) try{ pemReader.close(); } catch(Exception ex) {}
    }
  }
  
  public static
  InputStream openResource(String fileName)
    throws Exception
  {
    if(fileName == null || fileName.length() == 0) {
      return null;
    }
    
    try {
      URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
      
      if(url == null) return null;
      
      return url.openStream();
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
}