package io.branio.paybills;

/**
 * Created by anjose on 6/10/17.
 */

public class Bill {
    private String month;
    private Double value;
    private String company;
    private String type;
    private String dueDate;
    private boolean paid;

    public Bill(String month, String company, Double value, String type, String due, boolean paid) {
        this.month = month;
        this.company = company;
        this.value = value;
        this.type = type;
        this.dueDate = due;
        this.paid = paid;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
