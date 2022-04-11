package kz.dpoj.tcriptobot.buttons;

import kz.dpoj.tcriptobot.model.BirjaNameEnum;
import kz.dpoj.tcriptobot.model.ButtonNameEnum;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class InlineKeyboardMaker{
    private static final int pageSize = 15;

    public ReplyKeyboard getKeyboard(boolean isSubscribe) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        ButtonTypeEnum buttonTypeEnum = null;

        if(isSubscribe){
            buttonTypeEnum = ButtonTypeEnum.SUBSCRIBE_BIRJA;
        } else {
            buttonTypeEnum = ButtonTypeEnum.BIRJA;
        }

        for (BirjaNameEnum birjaNameEnum : BirjaNameEnum.values()) {
            rowList.add(getButton(
                    birjaNameEnum.getButtonName(),
                    buttonTypeEnum + ":" + birjaNameEnum.name()
            ));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getPairKeyboard(List<String> pairs, int page, boolean isSubscribe){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        ButtonTypeEnum btnTypeEnum = null;
        ButtonTypeEnum btnPage = null;
        if(isSubscribe){
            btnTypeEnum = ButtonTypeEnum.SUBSCRIBE_PAIR;
            btnPage = ButtonTypeEnum.SUBSCRIBE_PAGE;
        } else {
            btnTypeEnum = ButtonTypeEnum.PAIR;
            btnPage = ButtonTypeEnum.PAGE;
        }

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
}

