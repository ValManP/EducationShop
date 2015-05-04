package view;

import java.io.Serializable;
import java.lang.*;
import java.lang.Object;

public class Card implements Serializable, Comparable {

    private int id;
    private String name;
    private int price;
    private int validity;
    private boolean havePool;
    private String type;
    private String description;

    public Card(int id, String name, int price, int validity, boolean havePool, String type, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.validity = validity;
        this.havePool = havePool;
        this.type = type;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public boolean isHavePool() {
        return havePool;
    }

    public void setHavePool(boolean havePool) {
        this.havePool = havePool;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int compareTo(Object o) {
        Card tmp = (Card)o;

        if (this.getPrice() < tmp.getPrice())
            return -1;
        if (this.getPrice() > tmp.getPrice())
            return 1;
        if (this.getPrice() == tmp.getPrice()){
            if (this.getValidity() < tmp.getValidity())
                return -1;
            if (this.getValidity() > tmp.getValidity())
                return 1;
        }

        return 0;
    }
}