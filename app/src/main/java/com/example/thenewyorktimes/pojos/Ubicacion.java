package com.example.thenewyorktimes.pojos;



public class Ubicacion {
    private String status;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private double lat;
    private double lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
    private String query;

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public String getCountry() { return country; }
    public void setCountry(String value) { this.country = value; }

    public String getMessageCountry() { return "Good Morning " + getCountry() + ", here are the most viewed articles for the last day!";}
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String value) { this.countryCode = value; }

    public String getRegion() { return region; }
    public void setRegion(String value) { this.region = value; }

    public String getRegionName() { return regionName; }
    public void setRegionName(String value) { this.regionName = value; }

    public String getCity() { return city; }
    public void setCity(String value) { this.city = value; }

    public String getZip() { return zip; }
    public void setZip(String value) { this.zip = value; }

    public double getLat() { return lat; }
    public void setLat(double value) { this.lat = value; }

    public double getLon() { return lon; }
    public void setLon(double value) { this.lon = value; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String value) { this.timezone = value; }

    public String getISP() { return isp; }
    public void setISP(String value) { this.isp = value; }

    public String getOrg() { return org; }
    public void setOrg(String value) { this.org = value; }

    public String getAs() { return as; }
    public void setAs(String value) { this.as = value; }

    public String getQuery() { return query; }
    public void setQuery(String value) { this.query = value; }

    @Override
    public String toString() {
        return "Ubicacion{" +
                "status='" + status + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", region='" + region + '\'' +
                ", regionName='" + regionName + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", timezone='" + timezone + '\'' +
                ", isp='" + isp + '\'' +
                ", org='" + org + '\'' +
                ", as='" + as + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
