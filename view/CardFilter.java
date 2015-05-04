package view;

import java.io.Serializable;

public class CardFilter implements Serializable{

    private boolean popularity;
    private String priceFrom;
    private String priceTo;
    private String districtName;
    private String clubName;
    private String validity;
    private boolean havePool;
    //private boolean groupProgram;
    //private boolean


    public boolean isPopularity() {
        return popularity;
    }

    public void setPopularity(boolean popularity) {
        this.popularity = popularity;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public boolean isHavePool() {
        return havePool;
    }

    public void setHavePool(boolean havePool) {
        this.havePool = havePool;
    }

    public CardFilter() {
        this.popularity = false;
        this.priceFrom = "";
        this.priceTo = "";
        this.districtName = "";
        this.clubName = "";
        this.validity = "";
        this.havePool = false;
    }

    public CardFilter(boolean popularity, String priceFrom, String priceTo, String districtName, String clubName, String validity, boolean havePool) {

        this.popularity = popularity;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.districtName = districtName;
        this.clubName = clubName;
        this.validity = validity;
        this.havePool = havePool;
    }

    @Override
    public String toString() {
        String sqlFilter = "";

        if (!this.priceFrom.equals(""))
            sqlFilter += " AND a.name = 'Цена(руб.)' AND p.number_value >= "+ this.priceFrom + " ";
        if (!this.priceTo.equals(""))
            sqlFilter += " AND a.name = 'Цена(руб.)' AND p.number_value <=  "+ this.priceTo + " ";
        if (!this.districtName.equals(""))
            sqlFilter += " AND a.name = 'Район' AND p.text_value < "+ this.priceTo + " ";
        if (!this.clubName.equals(""))
            sqlFilter += " AND o.parent_id = (SELECT oo.object_id FROM objects oo WHERE oo.name = '"+ this.clubName + "') ";
        if (this.havePool == true)
            sqlFilter += " AND a.name = 'Бассейн' AND p.text_value = 'Да' ";

        return sqlFilter;
    }
}