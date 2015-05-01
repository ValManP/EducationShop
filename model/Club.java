package logic;

import java.io.Serializable;
import java.lang.*;
import java.lang.Object;


public class Club implements Serializable, Comparable{

    private String name;
    private String address;
    private String phone;
    private int avgPriceCard;
    private int numberOfCards;
    private String descriptionClub;

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

    public Club(String name, String address, String phone, int avgPriceCard, int numberOfCards, String descriptionClub) {
        this.name = name;

        this.address = address;
        this.phone = phone;
        this.avgPriceCard = avgPriceCard;
        this.numberOfCards = numberOfCards;
        this.descriptionClub = descriptionClub;
    }

    @Override
    public int compareTo(Object o) {
        Club tmp = (Club)o;

        if (this.getNumberOfCards() < tmp.getNumberOfCards())
            return -1;
        if (this.getNumberOfCards() > tmp.getNumberOfCards())
            return 1;
        if (this.getNumberOfCards() == tmp.getNumberOfCards()){
            if (this.getAvgPriceCard() < tmp.getAvgPriceCard())
                return -1;
            if (this.getAvgPriceCard() > tmp.getAvgPriceCard())
                return 1;

        }
          return 0;
    }
}