package kz.dpoj.tcriptobot.enumerate;

public enum ButtonNameEnum {
    GET_PRICE("Получить цену"),
    ADD_SUBSCRIBE("Подписаться на изменение цены"),
    HELP_BUTTON("Помощь"),
    SHOW_SUBSCRIBE("Просмотр моих подписок"),
    GET_CHART("Получить график изменения цены");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
