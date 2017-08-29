package xyz.jmatt.models;

import java.math.BigDecimal;

public class TransactionModel {
    private String transactionId;
    private String name;
    private String category;
    private BigDecimal amount;
    private long date;

    public TransactionModel(String Name, String Category, BigDecimal Amount, long Date){
        name = Name;
        category = Category;
        amount = Amount;
        date = Date;
    }

    public TransactionModel() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
