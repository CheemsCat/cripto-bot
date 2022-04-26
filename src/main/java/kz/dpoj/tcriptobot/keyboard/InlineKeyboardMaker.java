package kz.dpoj.tcriptobot.keyboard;

import kz.dpoj.tcriptobot.enumerate.BirjaNameEnum;
import kz.dpoj.tcriptobot.enumerate.ButtonNameEnum;
import kz.dpoj.tcriptobot.enumerate.ButtonTypeEnum;
import kz.dpoj.tcriptobot.enumerate.PeriodEnum;
import kz.dpoj.tcriptobot.model.Subscription;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardMaker{
    private static final int pageSize = 15;

    public ReplyKeyboard getKeyboard() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (BirjaNameEnum birjaNameEnum : BirjaNameEnum.values()) {
            rowList.add(getButton(
                    birjaNameEnum.getButtonName(),
                    ButtonTypeEnum.BIRJA + ":" + birjaNameEnum.name()
            ));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getPairKeyboard(List<String> pairs, int page){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        ButtonTypeEnum btnTypeEnum = ButtonTypeEnum.PAIR;
        ButtonTypeEnum btnPage = ButtonTypeEnum.PAGE;

        int offset = (page - 1) * pageSize;

        List<InlineKeyboardButton> row = null;
        for(int i = offset; i < offset + pageSize && i < pairs.size(); i++){
            if (i % 3 == 0 || i == offset) {
                if (row != null)
                    rowList.add(row);

                row = new ArrayList<>();
            }

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(pairs.get(i));
            button.setCallbackData(btnTypeEnum + ":" + pairs.get(i));

            row.add(button);
        }

        List<InlineKeyboardButton> paginationRow = new ArrayList<>();

        if (page != 1) {
            InlineKeyboardButton left = new InlineKeyboardButton();
            left.setText("<<");
            left.setCallbackData(btnPage + ":" + (page == 1 ? 1 : page - 1));
            paginationRow.add(left);
        }

        if (page != (pairs.size() / pageSize) + 1) {
            InlineKeyboardButton right = new InlineKeyboardButton();
            right.setText(">>");
            int pageQuantity = (int)Math.ceil((double) pairs.size() / (double)pageSize);
            right.setCallbackData(btnPage + ":" + (page == pageQuantity ? pageQuantity : page + 1));
            paginationRow.add(right);
        }

        rowList.add(paginationRow);

        List<InlineKeyboardButton> findButtonRow = new ArrayList<>();
        InlineKeyboardButton findButton = new InlineKeyboardButton();
        findButton.setText("Ввести валюту для поиска");
        findButton.setCallbackData(ButtonTypeEnum.FIND.toString());
        findButtonRow.add(findButton);
        rowList.add(findButtonRow);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }

    public ReplyKeyboard getSubscriptionKeyboard(List<Subscription> subscriptions){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for(int i = 0; i < subscriptions.size(); i++){
            if(i % 2 == 0 && i != 0) {
                rowList.add(row);
                row = new ArrayList<>();
            }

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(subscriptions.get(i).getPair() + "/" + subscriptions.get(i).getBirja());
            button.setCallbackData(ButtonTypeEnum.DELETE + ":" + subscriptions.get(i).getCronId());
            row.add(button);
        }

        if(row.size() != 0){
            rowList.add(row);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getPeriodKeyboard(){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        int i = 0;
        for(PeriodEnum periodEnum : PeriodEnum.values()){
            if(i % 2 == 0 && i != 0) {
                rowList.add(row);
                row = new ArrayList<>();
            }

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(periodEnum.getButtonName());
            button.setCallbackData(ButtonTypeEnum.CHART + ":" + periodEnum);
            row.add(button);
            i++;
        }

        if(row.size() != 0){
            rowList.add(row);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}

