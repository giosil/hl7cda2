# HL7CDA2

An extensible library to manage HL7 CDA2 documents.

## Examples

### Serialize a Clinical Document

```java
ClinicalDocument cda = buildClinicalDocument();

ICDASerializer ser = new CDASerializer_IT();
    
String xml = ser.toXML(cda);
```

### Deserialize a Clinical Document

```java
ICDADeserializer dser = new CDADeserializer();

dser.load(xml);

ClinicalDocument cda = dser.getClinicalDocument();
```

### Render a Clinical Document

```java
ICDARenderer ren = new CDARenderer_IT();

Map<String, Object> opt = new HashMap<String, Object>();
opt.put("style",     "body{ color: #202020; margin: 4 8 4 8; }");
opt.put("table",     "width: 100%;");
opt.put("th",        "background-color: #a8d7f7;");
opt.put("td",        "background-color: #cfeafc;");
opt.put("title",     "color: #000080;");
opt.put("paragraph", "font-style: italic;");

ren.setOptions(opt);
    
String html = ren.toHTML(cda);
```

### Transform a Clinical Document

```java
ICDARenderer ren = new CDARenderer_IT();
    
String html = ren.transform(xml, "CDAit.xsl");
```

### Validate a Clinical Document

```java
ICDAValidator val = new CDAValidator();

ValidationResult result = val.validate(xml);

System.out.println(result.isSuccess());
System.out.println(result.getErrors());
System.out.println(result.getFatals());
```

### XAdES signature

```java
ICDASigner sig = new CDASignerXAdES();

byte[] signed = sig.sign(xml);

System.out.println(new String(signed));
```

### CAdES signature

```java
ICDASigner sig = new CDASignerCAdES();

byte[] pkcs7 = sig.sign(xml);
```

## Build

- `git clone https://github.com/giosil/hl7cda2.git`
- `mvn clean install`
- `mvn cobertura:cobertura` - To run the unit tests and generate a Cobertura report.
- `mvn checkstyle:checkstyle` - To check source code respect the rules defined in checkstyle.xml.

## Contributors

* [Giorgio Silvestris](https://github.com/giosil)
