package org.dew.hl7;

import java.io.Serializable;

import java.util.Date;

public 
class Person implements Serializable
{
  private static final long serialVersionUID = -968637875488632886L;
  
  private String id;
  private String prefix;
  private String given;
  private String family;
  private String gender;
  private Date   birthTime;
  private String birthPlace;
  private String birthCountry;
  private String city;
  private String postalCode;
  private String streetName;
  private String houseNumber;
  private String country;
  private String email;
  private String phone;
  private Organization organization;
  
  public Person()
  {
  }
  
  public Person(String id)
  {
    this.id = id;
  }
  
  public Person(String id, String family, String given, String prefix)
  {
    this.id     = id;
    this.family = family;
    this.given  = given;
    this.prefix = prefix;
  }
  
  public Person(String id, String family, String given, String gender, Date birthTime)
  {
    this.id         = id;
    this.family     = family;
    this.given      = given;
    this.gender     = gender;
    this.birthTime  = birthTime;
  }
  
  public Person(String id, String family, String given, String gender, Date birthTime, String birthPlace)
  {
    this.id         = id;
    this.family     = family;
    this.given      = given;
    this.gender     = gender;
    this.birthTime  = birthTime;
    this.birthPlace = birthPlace;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getPrefix() {
    return prefix;
  }
  
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }
  
  public String getGiven() {
    return given;
  }
  
  public void setGiven(String given) {
    this.given = given;
  }
  
  public String getFamily() {
    return family;
  }
  
  public void setFamily(String family) {
    this.family = family;
  }
  
  public String getGender() {
    return gender;
  }
  
  public void setGender(String gender) {
    this.gender = gender;
  }
  
  public Date getBirthTime() {
    return birthTime;
  }
  
  public void setBirthTime(Date birthTime) {
    this.birthTime = birthTime;
  }
  
  public String getBirthPlace() {
    return birthPlace;
  }
  
  public void setBirthPlace(String birthPlace) {
    this.birthPlace = birthPlace;
  }
  
  public String getBirthCountry() {
    return birthCountry;
  }
  
  public void setBirthCountry(String birthCountry) {
    this.birthCountry = birthCountry;
  }
  
  public String getCity() {
    return city;
  }
  
  public void setCity(String city) {
    this.city = city;
  }
  
  public String getPostalCode() {
    return postalCode;
  }
  
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
  
  public String getStreetName() {
    return streetName;
  }
  
  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }
  
  public String getHouseNumber() {
    return houseNumber;
  }
  
  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }
  
  public String getCountry() {
    return country;
  }
  
  public void setCountry(String country) {
    this.country = country;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getPhone() {
    return phone;
  }
  
  public void setPhone(String phone) {
    this.phone = phone;
  }
  
  public Organization getOrganization() {
    return organization;
  }
  
  public void setOrganization(Organization organization) {
    this.organization = organization;
  }
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof Person) {
      String sId = ((Person) object).getId();
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
    return "Person(" + id + ")";
  }
}
