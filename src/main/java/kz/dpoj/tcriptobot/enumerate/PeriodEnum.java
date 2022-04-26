package kz.dpoj.tcriptobot.enumerate;

public enum PeriodEnum {
    DAY("День"),
    WEEK("Неделя"),
    MONTH("Месяц"),
    YEAR("Год");

    private final String buttonName;

    PeriodEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
