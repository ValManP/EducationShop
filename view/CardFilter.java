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

    private int countOnPage;
    private int page;
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

    public int getCountOnPage() {
        return countOnPage;
    }

    public void setCountOnPage(int countOnPage) {
        this.countOnPage = countOnPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public CardFilter() {
        this.page = 1;
        this.popularity = false;
        this.priceFrom = "";
        this.priceTo = "";
        this.districtName = "";
        this.clubName = "";
        this.validity = "";
        this.countOnPage = 6;
        this.havePool = false;
    }

    public CardFilter(boolean popularity, String priceFrom, String priceTo, String districtName, String clubName, String validity, boolean havePool, int page, int countOnPage) {
        this.popularity = popularity;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.districtName = districtName;
        this.clubName = clubName;
        this.validity = validity;
        this.havePool = havePool;
        this.countOnPage = countOnPage;
        this.page = page;
    }

    @Override
    public String toString() {
        String sqlFilter = "";

        if (!this.priceFrom.equals(""))
            sqlFilter += " AND cost.number_value >= "+ this.priceFrom + " ";
        if (!this.priceTo.equals(""))
            sqlFilter += " AND cost.number_value <=  "+ this.priceTo + " ";
        if (!this.districtName.equals(""))
            sqlFilter += " AND district.text_value = '"+ this.getDistrictName() + "' ";
        if (!this.clubName.equals(""))
            sqlFilter += " AND club.name = '"+ this.clubName + "' ";
        if (this.havePool == true)
            sqlFilter += " AND pool.text_value = 'Да' ";

        return sqlFilter;
    }
}