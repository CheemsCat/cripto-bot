package kz.dpoj.tcriptobot.model;

public enum ButtonNameEnum {
    GET_PRICE("Получить цену"),
    ADD_SUBSCRIBE("Подписаться на изменение цены"),
    HELP_BUTTON("Помощь");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
