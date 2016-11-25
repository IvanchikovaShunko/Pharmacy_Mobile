package bsu.fpmi.pharmacy.pharmacy_mobile.entity;

import java.util.Date;

/**
 * Created by annashunko on 25-Nov-16.
 */
public class Medicine {
    private int id;
    private String name;
    private String state;
    private Date expireDate;
    private int weight;
    private int gramInOne;
    private String description;


    public Medicine(int id, String name, String state, Date expireDate, int weight, int gramInOne, String description) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.expireDate = expireDate;
        this.weight = weight;
        this.gramInOne = gramInOne;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getGramInOne() {
        return gramInOne;
    }

    public void setGramInOne(int gramInOne) {
        this.gramInOne = gramInOne;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
