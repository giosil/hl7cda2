package org.dew.cda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import java.net.URL;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
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
import javax.xml.crypto.dsig.keyinfo.X509Data;
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

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import org.dew.hl7.Base64Coder;
import org.dew.hl7.ICDASigner;

@SuppressWarnings("deprecation")
public 
class CDASignerXAdES implements ICDASigner 
{
  protected String keystoreFile    = "keystore.jks";
  protected String keystorePass    = "password";
  protected String keystoreAlias   = "selfsigned";
  protected String privateKeyPass  = "password";
  protected String privateKeyFile  = "signature.pem";
  protected String certificateFile = "signature.crt";
  
  protected PrivateKey      privateKey;
  protected X509Certificate certificate;
  
  protected boolean omitDec = true;
  protected boolean indent  = true;
  
  protected String typAlgorithm = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
  protected String canAlgorithm = "http://www.w3.org/2006/12/xml-c14n11#WithComments";
  protected String digAlgorithm = "http://www.w3.org/2001/04/xmlenc#sha256";
  protected String sigAlgorithm = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
  
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
      this.omitDec = ((Boolean) oOmitXmlDeclaration).booleanValue();
    }
    else if(oOmitXmlDeclaration != null) {
      String sOmitXmlDeclaration = oOmitXmlDeclaration.toString();
      if(sOmitXmlDeclaration != null && sOmitXmlDeclaration.length() > 0) {
        char c0 = sOmitXmlDeclaration.charAt(0);
        this.omitDec = "1TYStys".indexOf(c0) >= 0;
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
    
    this.keystoreFile    = CDAUtils.toNotEmptyString(options.get("keystore_file"),    keystoreFile);
    this.keystorePass    = CDAUtils.toNotEmptyString(options.get("keystore_pass"),    keystorePass);
    this.keystoreAlias   = CDAUtils.toNotEmptyString(options.get("keystore_alias"),   keystoreAlias);
    this.privateKeyPass  = CDAUtils.toNotEmptyString(options.get("privatekey_pass"),  privateKeyPass);
    this.privateKeyFile  = CDAUtils.toNotEmptyString(options.get("privatekey_file"),  privateKeyFile);
    this.certificateFile = CDAUtils.toNotEmptyString(options.get("certificate_file"), certificateFile);
    
    this.typAlgorithm = CDAUtils.toNotEmptyString(options.get("type_algorithm"), typAlgorithm);
    this.canAlgorithm = CDAUtils.toNotEmptyString(options.get("can_algorithm"),  canAlgorithm);
    this.digAlgorithm = CDAUtils.toNotEmptyString(options.get("dig_algorithm"),  digAlgorithm);
    this.sigAlgorithm = CDAUtils.toNotEmptyString(options.get("sign_algorithm"), sigAlgorithm);
  }
  
  @Override
  public 
  byte[] sign(byte[] content) 
    throws Exception 
  {
    if(this.privateKey == null) loadKey();
    
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
    if(this.privateKey == null) loadKey();
    
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
    // typAlgorithm = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
    // canAlgorithm = "http://www.w3.org/2006/12/xml-c14n11#WithComments";
    // digAlgorithm = "http://www.w3.org/2001/04/xmlenc#sha256";
    // sigAlgorithm = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
    
    XMLSignatureFactory signFactory   = XMLSignatureFactory.getInstance("DOM", new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());
    CanonicalizationMethod c14nMethod = signFactory.newCanonicalizationMethod(canAlgorithm, (C14NMethodParameterSpec) null);
    DigestMethod         digestMethod = signFactory.newDigestMethod(digAlgorithm, null);
    SignatureMethod        signMethod = signFactory.newSignatureMethod(sigAlgorithm, null);
    Transform            sigTransform = signFactory.newTransform(typAlgorithm, (TransformParameterSpec) null);
    Transform            canTransform = signFactory.newTransform(canAlgorithm, (TransformParameterSpec) null);
    
    List<Transform> transformList = new ArrayList<Transform>();
    transformList.add(sigTransform);
    transformList.add(canTransform);
    
    Reference reference = signFactory.newReference("", digestMethod, transformList, null, null);
    
    SignedInfo signInfo = signFactory.newSignedInfo(c14nMethod, signMethod, Collections.singletonList(reference));
    
    KeyInfoFactory keyInfoFactory = signFactory.getKeyInfoFactory();
    X509Data x509Data = keyInfoFactory.newX509Data(Collections.singletonList(certificate));
    KeyInfo keyInfo   = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
    
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
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitDec ? "yes" : "no");
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
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitDec ? "yes" : "no");
      transformer.setOutputProperty(OutputKeys.INDENT, indent ? "yes" : "no");
      transformer.transform(new DOMSource(node), new StreamResult(stringWriter));
    } 
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return stringWriter.toString().getBytes();
  }
  
  protected
  void loadKey()
    throws Exception
  {
    certificate = null;
    privateKey  = null;
    
    if(keystoreAlias != null && keystoreAlias.length() > 0) {
      KeyStore keyStore = loadKeyStore();
      
      if(keyStore != null) {
        if(keystorePass == null) keystorePass = "";
        if(privateKeyPass == null || privateKeyPass.length() == 0) {
          if(keystorePass != null && keystorePass.length() > 0) {
            privateKeyPass = keystorePass;
          }
        }
        
        Certificate aliasCertificate = keyStore.getCertificate(keystoreAlias);
        if(aliasCertificate instanceof X509Certificate) {
          certificate = (X509Certificate) aliasCertificate;
        }
        
        Key aliasKey = keyStore.getKey(keystoreAlias, privateKeyPass.toCharArray());
        if(aliasKey instanceof PrivateKey) {
          privateKey = (PrivateKey) aliasKey;
        }
      }
    }
    
    if(certificate == null) {
      certificate = loadCertificate();
    }
    if(privateKey == null) {
      privateKey = loadPrivateKey();
    }
  }
  
  protected
  KeyStore loadKeyStore()
    throws Exception
  {
    KeyStore keyStore = null;
    
    if(keystoreFile == null || keystoreFile.length() == 0) {
      return keyStore;
    }
    
    InputStream is = openResource(keystoreFile);
    if(is == null) return keyStore;
    
    if(keystorePass == null) keystorePass = "";
    
    Security.addProvider(new BouncyCastleProvider());
    
    if(keystoreFile.endsWith(".p12")) {
      keyStore = KeyStore.getInstance("PKCS12", "BC");
    }
    else {
      keyStore = KeyStore.getInstance("JKS");
    }
    keyStore.load(is, keystorePass.toCharArray());
    
    return keyStore;
  }
  
  protected
  X509Certificate loadCertificate()
    throws Exception
  {
    if(certificateFile == null || certificateFile.length() == 0) {
      return null;
    }
    
    InputStream is = openResource(certificateFile);
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
  
  protected
  PrivateKey loadPrivateKey()
    throws Exception
  {
    if(privateKeyFile == null || privateKeyFile.length() == 0) {
      return null;
    }
    
    InputStream is = openResource(privateKeyFile);
    if(is == null) return null;
    
    PEMReader pemReader = null;
    try {
      Security.addProvider(new BouncyCastleProvider());
      
      pemReader = new PEMReader(new InputStreamReader(is));
      
      Object pemObject = pemReader.readObject();
      if(pemObject instanceof KeyPair) {
        return ((KeyPair) pemObject).getPrivate();
      }
      
      throw new Exception("Invalid pem file " + privateKeyFile);
    }
    finally {
      if(is != null) try{ is.close(); } catch(Exception ex) {}
      if(pemReader != null) try{ pemReader.close(); } catch(Exception ex) {}
    }
  }
  
  protected static
  InputStream openResource(String fileName)
    throws Exception
  {
    if(fileName == null || fileName.length() == 0) {
      return null;
    }
    if(fileName.startsWith("/") || fileName.indexOf(':') > 0) {
      return new FileInputStream(fileName);
    }
    URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
    if(url == null) return null;
    return url.openStream();
  }
}

