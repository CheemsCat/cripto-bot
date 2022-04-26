package kz.dpoj.tcriptobot.keyboard;

import kz.dpoj.tcriptobot.enumerate.ButtonNameEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker{
    public ReplyKeyboard getKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        int i = 0;
        for(ButtonNameEnum buttonNameEnum : ButtonNameEnum.values()){
            if(i % 2 == 0 && i != 0) {
                keyboard.add(row);
                row = new KeyboardRow();
            }
            row.add(buttonNameEnum.getButtonName());
            i++;
        }
        if(!row.isEmpty()){
            keyboard.add(row);
        }

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}

