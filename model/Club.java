package logic;

import java.io.Serializable;
import java.lang.*;
import java.lang.Object;


public class Club implements Serializable{

    private String name;
    private String address;
    private String phone;
    private int avgPriceCard;
    private int numberOfCards;
    private String descriptionClub;
    private String district;

    public Club(String name, String address, String phone, int avgPriceCard, int numberOfCards, String descriptionClub, String district) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.avgPriceCard = avgPriceCard;
        this.numberOfCards = numberOfCards;
        this.descriptionClub = descriptionClub;
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAvgPriceCard() {
        return avgPriceCard;
    }

    public void setAvgPriceCard(int avgPriceCard) {
        this.avgPriceCard = avgPriceCard;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public String getDescriptionClub() {
        return descriptionClub;
    }

    public void setDescriptionClub(String descriptionClub) {
        this.descriptionClub = descriptionClub;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}