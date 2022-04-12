package com.findyourfamily.app.models.domain;

/**
 * Type for storing location information related to a person or media.
 */
public class Location {

    //Stores the location name
    private String locationName;

    //Stores the city
    private String city;

    //Stores the province
    private String province;

    //Stores the country
    private String country;

    public Location() {
        this.locationName = "";
        this.city = "";
        this.province = "";
        this.country = "";
    }

    /**
     * Gets the location name for the location.
     * @return location name
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Sets the location name for the location.
     * @param locationName for the location.
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * Gets the city for the location.
     * @return for the location.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the location name for the location.
     * @param city for the location.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the location name for the location.
     * @return for the location.
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets the location name for the location.
     * @param province for the location.
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Gets the country for the location.
     * @return country for the location.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country for the location.
     * @param country for the location.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return locationName + ", " + city + ", " + province + ", " + country;
    }
}
