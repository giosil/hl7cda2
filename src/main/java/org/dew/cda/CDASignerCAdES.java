package org.dew.cda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.ess.ESSCertIDv2;
import org.bouncycastle.asn1.ess.SigningCertificateV2;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import org.bouncycastle.cert.X509CertificateHolder;

import org.bouncycastle.cms.CMSAttributeTableGenerator;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.DefaultSignedAttributeTableGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import org.bouncycastle.util.Store;

import org.dew.hl7.Base64Coder;
import org.dew.hl7.ICDASigner;

@SuppressWarnings({"deprecation", "unchecked"})
public 
class CDASignerCAdES implements ICDASigner
{
  protected String keystoreFile    = "keystore.jks";
  protected String keystorePass    = "password";
  protected String keystoreAlias   = "selfsigned";
  protected String privateKeyPass  = "password";
  protected String privateKeyFile  = "signature.pem";
  protected String certificateFile = "signature.crt";
  
  protected PrivateKey      privateKey;
  protected X509Certificate certificate;
  
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
    
    this.keystoreFile    = CDAUtils.toString(options.get("keystore_file"),    keystoreFile);
    this.keystorePass    = CDAUtils.toString(options.get("keystore_pass"),    keystorePass);
    this.keystoreAlias   = CDAUtils.toString(options.get("keystore_alias"),   keystoreAlias);
    this.privateKeyPass  = CDAUtils.toString(options.get("privatekey_pass"),  privateKeyPass);
    this.privateKeyFile  = CDAUtils.toString(options.get("privatekey_file"),  privateKeyFile);
    this.certificateFile = CDAUtils.toString(options.get("certificate_file"), certificateFile);
  }
  
  @Override
  public 
  byte[] sign(byte[] content) 
    throws Exception 
  {
    if(this.privateKey == null) loadPrivateKey();
    
    if(this.privateKey == null) {
      throw new Exception("privatekey unavailable");
    }
    if(this.certificate == null) {
      throw new Exception("certificate unavailable");
    }
    if(content == null || content.length == 0) {
      throw new Exception("Invalid content");
    }
    
    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
    byte[] digestedCert = sha256.digest(certificate.getEncoded());
    
    // Attributo ESSCertID versione 2
    AlgorithmIdentifier aiSha256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
    ESSCertIDv2 essCert1 = new ESSCertIDv2(aiSha256, digestedCert);
    ESSCertIDv2[] essCert1Arr = { essCert1 };
    SigningCertificateV2 scv2 = new SigningCertificateV2(essCert1Arr);
    Attribute certHAttribute = new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificateV2, new DERSet(scv2));
    
    // Tabella Attributi
    ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
    asn1EncodableVector.add(certHAttribute);
    AttributeTable attributeTable = new AttributeTable(asn1EncodableVector);
    CMSAttributeTableGenerator attrGen = new DefaultSignedAttributeTableGenerator(attributeTable);
    
    CertStore certStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(Collections.singletonList(certificate)));
    
    CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
    generator.addSigner(privateKey, certificate, CMSSignedDataGenerator.DIGEST_SHA256, attrGen, null);
    generator.addCertificatesAndCRLs(certStore);
    
    CMSProcessable cmsProcessable = new CMSProcessableByteArray(content);
    CMSSignedData signedData = generator.generate(cmsProcessable, true, "BC");
    byte[] abPKCS7Signature = signedData.getEncoded();
    
    return abPKCS7Signature;
  }
  
  @Override
  public 
  byte[] sign(String content) 
    throws Exception 
  {
    return sign(content.getBytes());
  }
  
  @Override
  public 
  X509Certificate validate(byte[] signed) 
    throws Exception 
  {
    CMSSignedData signedData = new CMSSignedData(signed);
    
    Store certStore = signedData.getCertificates();
    
    SignerInformationStore signerInfos = signedData.getSignerInfos();
    
    Collection<SignerInformation> signers = signerInfos.getSigners();
    
    byte[] certificate = null;
    Iterator<SignerInformation> iterator = signers.iterator();
    while(iterator.hasNext()) {
      SignerInformation signer = iterator.next();
      Collection<X509CertificateHolder> matches = certStore.getMatches(signer.getSID());
      for (X509CertificateHolder holder : matches) {
        certificate = holder.getEncoded();
        if(certificate != null && certificate.length > 0) {
          break;
        }
      }
    }
    
    if(certificate != null && certificate.length > 0) {
      CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
      return (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(certificate));
    }
    
    return null;
  }
  
  @Override
  public 
  X509Certificate validate(String signed) 
    throws Exception 
  {
    if(signed == null || signed.length() == 0) {
      return null;
    }
    
    return validate(signed.getBytes());
  }
  
  @Override
  public 
  byte[] extract(byte[] signed) 
    throws Exception
  {
    if(signed == null || signed.length == 0) {
      return null;
    }
    
    byte[] content = null;
    
    CMSSignedData signedData = new CMSSignedData(signed);
    
    Object oSignedContent = signedData.getSignedContent().getContent();
    if(oSignedContent instanceof byte[]) {
      content = (byte[]) oSignedContent;
    }
    else if(oSignedContent != null) {
      content = oSignedContent.toString().getBytes();
    }
    
    return content;
  }
  
  protected
  void loadPrivateKey()
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
      certificate = loadCertificateFile();
    }
    if(privateKey == null) {
      privateKey = loadPrivateKeyFile();
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
  X509Certificate loadCertificateFile()
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
  PrivateKey loadPrivateKeyFile()
    throws Exception
  {
    if(privateKeyFile == null || privateKeyFile.length() == 0) {
      return null;
    }
    
    InputStream is = openResource(privateKeyFile);
    if(is == null) return null;
    
    PEMParser pemParser = null;
    try {
      Security.addProvider(new BouncyCastleProvider());
      
      pemParser = new PEMParser(new InputStreamReader(is));
      
      Object object = pemParser.readObject();
      
      if(object instanceof PEMKeyPair) {
        
        PEMKeyPair pemKeyPair = (PEMKeyPair) object;
        
        KeyPair keyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        
        return keyPair.getPrivate();
      }
      
      throw new Exception("Invalid pem file " + privateKeyFile);
    }
    finally {
      if(is != null) try{ is.close(); } catch(Exception ex) {}
      if(pemParser != null) try{ pemParser.close(); } catch(Exception ex) {}
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
