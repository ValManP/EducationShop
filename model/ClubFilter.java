package logic;

import java.io.Serializable;


public class ClubFilter implements Serializable {
    private boolean popularity;
    private String districtName; // Сделать массив String, для фильтра по нескольким районам
    private String clubName; // Аналогично здесь
    private int countOnPage;
    private int page;

    public ClubFilter() {
        this.clubName = "";
        this.districtName = "";
        this.popularity = false;
        this.countOnPage = 6;
        this.page = 1;
    }

    public ClubFilter(boolean popularity, String districtName, String clubName, int countOnPage, int page) {
        this.popularity = popularity;
        this.districtName = districtName;
        this.clubName = clubName;
        this.countOnPage = countOnPage;
        this.page = page;
    }

    public boolean isPopularity() {
        return popularity;
    }

    public void setPopularity(boolean popularity) {
        this.popularity = popularity;
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

    @Override
    public String toString() {

        String sqlFilter = "";

        if (!this.districtName.equals(""))
            sqlFilter += " AND district.text_value = '"+ this.districtName + "' ";

        if (!this.clubName.equals(""))
            sqlFilter += " AND o.name = '"+ this.clubName + "' ";

        return sqlFilter;
    }
}