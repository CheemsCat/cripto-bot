package kz.dpoj.tcriptobot.model;

public enum BirjaNameEnum {
    EXMO("EXMO"),
    BINANCE("BINANCE");

    private final String buttonName;

    BirjaNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
