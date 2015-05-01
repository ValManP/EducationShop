package logic;

import java.io.Serializable;


public class ClubFilter implements Serializable {
    private boolean popularity;
    private String districtName; // Сделать массив String, для фильтра по нескольким районам
    private String clubName; // Аналогично здесь

    public ClubFilter() {
        this.clubName = "";
        this.districtName = "";
        this.popularity = false;
    }

    public ClubFilter(boolean popularity, String districtName, String clubName) {
        this.popularity = popularity;
        this.districtName = districtName;
        this.clubName = clubName;
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

    @Override
    public String toString() {

        String sqlFilter = "";

        if (!this.districtName.equals(""))
            sqlFilter += " AND a.name = 'Район' AND p.text_value = '"+ this.districtName + "' ";

        if (!this.clubName.equals(""))
            sqlFilter += " AND o.name = '"+ this.clubName + "' ";

        return sqlFilter;
    }
}