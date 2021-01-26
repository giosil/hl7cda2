package org.dew.cda;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.dew.hl7.ICDASigner;

public 
class CDASignerXAdES implements ICDASigner 
{
  protected PrivateKey      privateKey;
  protected X509Certificate certificate;
  protected boolean         omitXmlDeclaration = true;
  protected boolean         indent = true;
  
  @Override
  public 
  void setOptions(Map<String, Object> options) 
  {
    if(options == null || options.isEmpty()) {
      return;
    }
    
    Object oPrivateKey = options.get("privatekey");
    if(oPrivateKey instanceof PrivateKey) {
      this.privateKey = (PrivateKey) oPrivateKey;
    }
    
    Object oCertificate = options.get("certificate");
    if(oCertificate instanceof X509Certificate) {
      this.certificate = (X509Certificate) oCertificate;
    }
    
    Object oOmitXmlDeclaration = options.get("omit_xml_declaration");
    if(oOmitXmlDeclaration instanceof Boolean) {
      this.omitXmlDeclaration = ((Boolean) oOmitXmlDeclaration).booleanValue();
    }
    else if(oOmitXmlDeclaration != null) {
      String sOmitXmlDeclaration = oOmitXmlDeclaration.toString();
      if(sOmitXmlDeclaration != null && sOmitXmlDeclaration.length() > 0) {
        char c0 = sOmitXmlDeclaration.charAt(0);
        this.omitXmlDeclaration = "1TYStys".indexOf(c0) >= 0;
      }
    }
    
    Object oIdent = options.get("indent");
    if(oIdent instanceof Boolean) {
      this.indent = ((Boolean) oIdent).booleanValue();
    }
    else if(oIdent != null) {
      String sIdent = oIdent.toString();
      if(sIdent != null && sIdent.length() > 0) {
        char c0 = sIdent.charAt(0);
        this.indent = "1TYStys".indexOf(c0) >= 0;
      }
    }
  }
  
  @Override
  public 
  byte[] sign(byte[] content) 
    throws Exception 
  {
    if(this.privateKey == null) {
      throw new Exception("privatekey unavailable");
    }
    if(this.certificate == null) {
      throw new Exception("certificate unavailable");
    }
    if(content == null || content.length == 0) {
      throw new Exception("Invalid content");
    }
    
    Element node = byteArrayToElement(content);
    
    XMLSignature xmlSignature = createXMLSignature();
    
    DOMSignContext domSignContext = new DOMSignContext(privateKey, node);
    
    xmlSignature.sign(domSignContext);
    
    return nodeToByteArray(node);
  }
  
  @Override
  public 
  byte[] sign(String content) 
    throws Exception 
  {
    if(this.privateKey == null) {
      throw new Exception("privatekey unavailable");
    }
    if(this.certificate == null) {
      throw new Exception("certificate unavailable");
    }
    if(content == null || content.length() == 0) {
      throw new Exception("Invalid content");
    }
    
    Element node = stringToElement(content);
    
    XMLSignature xmlSignature = createXMLSignature();
    
    DOMSignContext domSignContext = new DOMSignContext(privateKey, node);
    
    xmlSignature.sign(domSignContext);
    
    return nodeToByteArray(node);
  }
  
  protected 
  XMLSignature createXMLSignature()
    throws Exception
  {
    // XMLSignatureFactory signFactory   = XMLSignatureFactory.getInstance("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());
    XMLSignatureFactory signFactory   = XMLSignatureFactory.getInstance("DOM", new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());
    CanonicalizationMethod c14nMethod = signFactory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null);
    DigestMethod         digestMethod = signFactory.newDigestMethod(DigestMethod.SHA1, null);
    SignatureMethod        signMethod = signFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
    Transform               transform = signFactory.newTransform(CanonicalizationMethod.EXCLUSIVE, (TransformParameterSpec) null);
    
    List<Transform> transformList = Collections.singletonList(transform);
    Reference reference = signFactory.newReference("", digestMethod, transformList, null, null);
    
    List<Reference> referenceList = new ArrayList<Reference>();
    referenceList.add(reference);
    
    SignedInfo signInfo = signFactory.newSignedInfo(c14nMethod, signMethod, referenceList);
    
    KeyInfoFactory keyInfoFactory = signFactory.getKeyInfoFactory();
    KeyValue keyValue = keyInfoFactory.newKeyValue(certificate.getPublicKey());
    KeyInfo  keyInfo  = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValue));
    
    return signFactory.newXMLSignature(signInfo, keyInfo);
  }
  
  protected static
  Element byteArrayToElement(byte[] xml)
    throws Exception
  {
    if(xml == null || xml.length == 0) {
      return null;
    }
    
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document document = documentBuilder.parse(new ByteArrayInputStream(xml));
    return document.getDocumentElement();
  }
  
  protected static
  Element stringToElement(String xml)
    throws Exception
  {
    if(xml == null || xml.length() == 0) {
      return null;
    }
    
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
    return document.getDocumentElement();
  }
  
  protected
  String nodeToString(Node node)
    throws Exception
  {
    StringWriter stringWriter = new StringWriter();
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclaration ? "yes" : "no");
      transformer.setOutputProperty(OutputKeys.INDENT, indent ? "yes" : "no");
      transformer.transform(new DOMSource(node), new StreamResult(stringWriter));
    } 
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return stringWriter.toString();
  }
  
  protected
  byte[] nodeToByteArray(Node node)
    throws Exception
  {
    StringWriter stringWriter = new StringWriter();
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclaration ? "yes" : "no");
      transformer.setOutputProperty(OutputKeys.INDENT, indent ? "yes" : "no");
      transformer.transform(new DOMSource(node), new StreamResult(stringWriter));
    } 
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return stringWriter.toString().getBytes();
  }
}

