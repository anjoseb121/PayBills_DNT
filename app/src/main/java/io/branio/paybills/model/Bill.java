package io.branio.paybills.model;

import java.util.Date;

/**
 * Created by anjose on 6/10/17.
 */

public class Bill {
    private String name;
    private int month;
    private Double value;
    private String company;
    private int type;
    private Date dueDate;
    private boolean paid;

    public Bill() {
    }

    public Bill(String name, int month, String company, Double value, int type, Date due, boolean paid) {
        this.name = name;
        this.month = month;
        this.company = company;
        this.value = value;
        this.type = type;
        this.dueDate = due;
        this.paid = paid;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
