package org.example.yxinterview.model;

import org.example.yxinterview.constant.State;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private State state;
    private List<PurchaseItem> purchaseItems;
    private double subTotal;
    private double tax;
    private double total;

    public Order(List<PurchaseItem> purchaseItems, State state) {
        this.state = state;
        this.purchaseItems = purchaseItems;
        this.subTotal = BigDecimal.valueOf(purchaseItems.stream().mapToDouble(s -> s.price() * s.qty()).sum()).doubleValue();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "state=" + state +
                ", purchaseItems=" + purchaseItems +
                ", subTotal=" + subTotal +
                ", tax=" + tax +
                ", total=" + total +
                '}';
    }
}
