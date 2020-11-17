# HL7CDA2

An extensible library to manage HL7 CDA2 documents.

## Examples

### Serialize Clinical Document

```java
ClinicalDocument cda = buildClinicalDocument();

ICDASerializer ser = new CDASerializer_IT();
    
String xml = ser.toXML(cda);
```

### Deserialize Clinical Document

```java
ICDADeserializer dser = new CDADeserializer();

cdaDeserializer.load(xml);

ClinicalDocument cda = dser.getClinicalDocument();
```

## Build

- `git clone https://github.com/giosil/hl7cda2.git`
- `mvn clean install`

## Contributors

* [Giorgio Silvestris](https://github.com/giosil)
