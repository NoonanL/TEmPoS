package TEmPoS.Model;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Customer {

    /**
     * declare variables
     */
    private String id;
    private String title;
    private String firstname;
    private String surname;
    private String street;
    private String town;
    private String postcode;
    private String city;
    private String country;
    private String mobile;
    private String email;
    private String marketingStatus;

    /**
     * constructors
     */
    public Customer(){
        this.id = "";
        this.title="";
        this.firstname = "";
        this.surname = "";
        this.street="";
        this.town="";
        this.postcode="";
        this.city="";
        this.country="";
        this.mobile="";
        this.email="";
        this.marketingStatus="";
    }

    public Customer(String firstname, String surname){
        this.firstname = firstname;
        this.surname = surname;
    }

    /**
     * getters.setters
     * NOTE - getters return a stringproperty for the use of FX
     */

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getFirstname(){
        return this.firstname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getSurname(){
        return this.surname;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMarketingStatus() {
        return marketingStatus;
    }

    public void setMarketingStatus(String marketingStatus) {
        this.marketingStatus = marketingStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONObject toJson(){
        JSONObject json;
        Map<String, String> user = new LinkedHashMap<>();

        user.put("id", this.getId());
        user.put("title", this.getTitle());
        user.put("firstname" , this.getFirstname());
        user.put("surname" , this.getSurname());
        user.put("street", this.getStreet());
        user.put("town", this.getTown());
        user.put("postcode", this.getPostcode());
        user.put("city", this.getCity());
        user.put("country", this.getCountry());
        user.put("mobile", this.getMobile());
        user.put("email", this.getEmail());
        user.put("marketingStatus",this.getMarketingStatus());
        json = new JSONObject(user);
        return json;
    }
}
