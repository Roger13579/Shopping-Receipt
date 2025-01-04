package org.example.yxinterview;

import org.example.yxinterview.constant.State;
import org.example.yxinterview.model.Order;
import org.example.yxinterview.model.PurchaseItem;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

public class MainFunction {

    private static final List<String> foodItems = List.of("potato","tomato","banana");

    private static final List<String> clothingItems = List.of("skirt","shirt","hat");

    private static State state;

    public static void main(String[] args) {
        List<PurchaseItem> purchaseItems = getPurchaseItems();
        Order order = calculateOrder(state, purchaseItems);
        System.out.println("item price qty");
        for (PurchaseItem item : order.getPurchaseItems()) {
            System.out.println(item.item() + " $" + item.price() + " " + item.qty());
        }
        System.out.println("subtotal: $" + order.getSubTotal());
        System.out.println("tax: $" + order.getTax());
        System.out.println("total: $" + order.getTotal());
    }

    public static Order calculateOrder(State state, List<PurchaseItem> purchaseItems) {
        Order order = new Order(purchaseItems, state);
        switch (order.getState()) {
            case CA -> order.setTax(calculateTax(State.CA, order.getPurchaseItems()));
            case NY -> order.setTax(calculateTax(State.NY, order.getPurchaseItems()));
            default -> order.setTax(0.00);
        }
        order.setTotal(BigDecimal.valueOf(order.getTax() + order.getSubTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue());
       return order;
    }

    public static List<PurchaseItem> getPurchaseItems() {
        List<PurchaseItem> purchaseItems = new ArrayList<>();
        try(FileInputStream fis = new FileInputStream("./test.txt");
                BufferedReader br =
                 new BufferedReader( new InputStreamReader(fis, StandardCharsets.UTF_8))){
            String line;
            while(( line = br.readLine()) != null ) {
                if (line.contains(",")) {
                    String[] texts = line.trim().split(",");
                    purchaseItems.add(new PurchaseItem(
                            texts[0],
                            Integer.parseInt(texts[1]),
                            Double.parseDouble(texts[2]),
                            checkItemCategory(texts[0]))
                    );
                }else {
                    try {
                        state = State.valueOf(line);
                    }catch (IllegalArgumentException e) {
                        state = State.valueOf("OTHER");
                        System.out.println("Not CA, NY, set Other");
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return purchaseItems;
    }

    public static double calculateTax(State state, List<PurchaseItem> purchaseItems) {
        List<PurchaseItem> notExemptionItems = purchaseItems.stream()
                .filter((o) -> (!Arrays.asList(state.getExemptions()).contains(o.category()))).toList();
        if (!notExemptionItems.isEmpty()) {
            return roundTax(notExemptionItems.stream()
                    .flatMapToDouble((s) ->
                            DoubleStream.of(s.price() * s.qty() * state.getRate()))
                    .sum());
        }

        return 0.0;
    }
    public static double roundTax(double tax) {
        return BigDecimal.valueOf(Math.ceil(tax * 20) / 20).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static String checkItemCategory(String item) {
         if (foodItems.contains(item)) {
             return "Food";
         }else if (clothingItems.contains(item)) {
             return "Clothing";
         }else {
             return "Other";
         }
    }
}
