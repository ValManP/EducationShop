package logic;

import java.io.Serializable;


public class Club implements Serializable{

    private String name;
    private String address;
    private String phone;
    private String avgPriceCard;
    private String numberOfCards;
    private String descriptionClub;

    public Club(String name, String address, String phone, String avgPriceCard, String numberOfCards, String descriptionClub) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.avgPriceCard = avgPriceCard;
        this.numberOfCards = numberOfCards;
        this.descriptionClub = descriptionClub;
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

    public String getAvgPriceCard() {
        return avgPriceCard;
    }

    public void setAvgPriceCard(String avgPriceCard) {
        this.avgPriceCard = avgPriceCard;
    }

    public String getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(String numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public String getDescriptionClub() {
        return descriptionClub;
    }

    public void setDescriptionClub(String descriptionClub) {
        this.descriptionClub = descriptionClub;
    }
}