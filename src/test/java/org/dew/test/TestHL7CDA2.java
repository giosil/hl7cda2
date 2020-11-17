package org.dew.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.dew.cda.*;
import org.dew.hl7.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestHL7CDA2 extends TestCase {
  
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
    ClinicalDocument cda1 = buildClinicalDocument();
    
    String xml1 = cdaSerialize(cda1);
    
    ClinicalDocument cda2 = cdaDeserialize(xml1);
    
    cdaSerialize(cda2);
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
      .addEntry("Diagnosi",     "Scompenso cardiaco");
    
    clinicalDocument.addSection("PDF", "application/pdf", pdfReferto);
    
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
}
