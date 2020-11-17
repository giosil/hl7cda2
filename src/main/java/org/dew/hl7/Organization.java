package org.dew.hl7;

import java.io.Serializable;

public 
class Organization implements Serializable
{
  private static final long serialVersionUID = -5840454228753351137L;
  
  private String id;
  private String name;
  private String city;
  private String postalCode;
  private String streetName;
  private String houseNumber;
  private String country;
  private String email;
  private String phone;
  
  public Organization()
  {
  }
  
  public Organization(String id)
  {
    this.id = id;
  }
  
  public Organization(String id, String name)
  {
    this.id   = id;
    this.name = name;
  }
  
  public Organization(String id, String name, String city, String postalCode, String streetName, String houseNumber)
  {
    this.id          = id;
    this.name        = name;
    this.city        = city;
    this.postalCode  = postalCode;
    this.streetName  = streetName;
    this.houseNumber = houseNumber;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
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
  
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof Organization) {
      String sId = ((Organization) object).getId();
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
    return "Organization(" + id + ")";
  }
}
