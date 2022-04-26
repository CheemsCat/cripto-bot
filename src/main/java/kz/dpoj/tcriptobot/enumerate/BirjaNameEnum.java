package kz.dpoj.tcriptobot.enumerate;

public enum BirjaNameEnum {
    EXMO("EXMO"),
    GATE("GATE"),
    KRAKEN("KRAKEN");

    private final String buttonName;

    BirjaNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
