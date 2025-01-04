package org.example.yxinterview.constant;

public enum State {
    CA("CA", 0.0975, new String[]{"Food"}),

    NY("NY", 0.08875, new String[]{"Food", "Clothing"}),

    OTHER("Other", 0.00, new String[]{});

    private final String name;
    private final double rate;
    private final String[] exemptions;

    State(String name, double rate, String[] exemptions) {
        this.name = name;
        this.rate = rate;
        this.exemptions = exemptions;
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }

    public String[] getExemptions() {
        return exemptions;
    }
}
