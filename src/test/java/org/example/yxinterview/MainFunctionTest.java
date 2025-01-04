package org.example.yxinterview;

import org.example.yxinterview.constant.State;
import org.example.yxinterview.model.Order;
import org.example.yxinterview.model.PurchaseItem;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainFunctionTest {

    @Test
    void testCalculateOrder_CAState() {
        // Mock data
        State state = State.CA;
        List<PurchaseItem> items = List.of(
                new PurchaseItem("potato", 1, 3.99, "Food"),
                new PurchaseItem("phone", 1, 100.99, "electronics")
        );

        // Invoke the method
        Order order = MainFunction.calculateOrder(state, items);

        // Assertions
        assertEquals(State.CA, order.getState());
        assertTrue(order.getTax() > 0);
        assertEquals(BigDecimal.valueOf(order.getTax() + order.getSubTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue(), order.getTotal());
    }

    @Test
    void testCalculateOrder_NYState() {
        State state = State.NY;
        List<PurchaseItem> items = List.of(
                new PurchaseItem("potato", 1, 3.99, "Food"),
                new PurchaseItem("phone", 1, 100.99, "electronics")
        );

        Order order = MainFunction.calculateOrder(state, items);

        assertTrue(order.getTax() > 0);
        assertEquals(BigDecimal.valueOf(order.getTax() + order.getSubTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue(), order.getTotal());
    }

    @Test
    void testCalculateOrder_OtherState() {
        // Mock data
        State state = State.OTHER;
        List<PurchaseItem> items = List.of(
                new PurchaseItem("phone", 1, 300.0, "electronics"),
                new PurchaseItem("shirt", 3, 50.0, "clothing")
        );

        // Invoke the method
        Order order = MainFunction.calculateOrder(state, items);

        // Assertions
        assertEquals(0.0, order.getTax());
        assertEquals(order.getSubTotal(), order.getTotal());
    }

    @Test
    void testCalculateOrder_EmptyPurchaseItems() {
        // Mock data
        State state = State.CA;
        List<PurchaseItem> items = Collections.emptyList();

        // Invoke the method
        Order order = MainFunction.calculateOrder(state, items);

        // Assertions
        assertEquals(State.CA, order.getState());
        assertEquals(0.0, order.getTax());
        assertEquals(0.0, order.getTotal());
    }

    @Test
    void testCalculateTax_WithExemptions() {
        // Mock state and items
        State state = State.CA;
        List<PurchaseItem> items = List.of(
                new PurchaseItem("potato", 1, 3.99, "Food")
        );

        double tax = MainFunction.calculateTax(state, items);
        assertEquals(0.0, tax);
    }

    @Test
    void testCalculateTax_WithoutExemptions() {
        // Mock state and items
        State state = State.NY;
        List<PurchaseItem> items = List.of(
                new PurchaseItem("electronics", 1, 100.0, "phone")
        );

        double tax = MainFunction.calculateTax(state, items);
        assertTrue(tax > 0);
    }

    @Test
    void testRoundTax() {
        assertEquals(1.05, MainFunction.roundTax(1.048));
        assertEquals(1.20, MainFunction.roundTax(1.151));
    }
}