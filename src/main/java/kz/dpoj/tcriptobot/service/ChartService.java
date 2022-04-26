package kz.dpoj.tcriptobot.service;

import com.image.charts.ImageCharts;
import kz.dpoj.tcriptobot.enumerate.PeriodEnum;
import kz.dpoj.tcriptobot.model.birja.ChartMessage;
import kz.dpoj.tcriptobot.model.birja.exmo.Period;
import kz.dpoj.tcriptobot.service.birja.BirjaFabrica;
import kz.dpoj.tcriptobot.service.birja.BirjaInterface;
import kz.dpoj.tcriptobot.service.birja.ExmoBirjaService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChartService {
    private BirjaFabrica birjaFabrica;

    private ChartService(BirjaFabrica birjaFabrica){
        this.birjaFabrica = birjaFabrica;
    }

    public ChartMessage getChartByPair(String period, Long chatId, String pair){
        BirjaInterface birjaType = birjaFabrica.getBirjaByChatId(chatId);
        Period per = getPeriod(period, birjaType);

        List<String> candles = birjaType.getCandlesHistory(pair, per.getFrom(), per.getTo(), per.getResolution());

        if(candles.size()>24) {
            candles.subList(24, candles.size()).clear();
        }

        String chdString = String.join(",", candles);
        String chlString = String.join("|", candles);

        ImageCharts pie = new ImageCharts().cht("bvg")
                .chd("a:" + chdString)
                .chf("b0,lg,90,EA469EFF,1,03A9F47C,0.4")
                .chxt("y")
                .chl(chlString)
                .chlps("align,center|color,000000|rotation,270")
                .chs("900x500");
        SendPhoto sendPhoto = new SendPhoto();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            BufferedImage bi = pie.toBuffer();
            ImageIO.write(bi, "jpeg", os);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        InputFile inputFile = new InputFile();
        inputFile.setMedia(is, pair);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId.toString());

        return new ChartMessage(sendPhoto);
    }

    private Period getPeriod(String period, BirjaInterface birjaType){
        Period per = new Period();

        LocalDateTime to = LocalDateTime.now();
        long toTimestamp = Timestamp.valueOf(to).getTime()/1000;
        per.setTo(String.valueOf(toTimestamp));
        LocalDateTime from = null;

        PeriodEnum periodEnum = PeriodEnum.valueOf(period);
        per.setResolution(getResolution(birjaType, periodEnum));

        switch (periodEnum){
            case DAY:
                from = to.minusDays(1);
                break;
            case WEEK:
                from = to.minusWeeks(1);
                break;
            case MONTH:
                from = to.minusMonths(1);
                break;
            case YEAR:
                from = to.minusYears(1);
        }

        long fromTimestamp = Timestamp.valueOf(from).getTime()/1000;
        per.setFrom(String.valueOf(fromTimestamp));

        return per;
    }

    private String getResolution(BirjaInterface birjaType, PeriodEnum period){
        if(birjaType instanceof ExmoBirjaService) {
            return switch (period) {
                case DAY -> "60";
                case WEEK -> "240";
                case MONTH -> "1D";
                case YEAR -> "1M";
            };
        } else{
            return switch (period){
                case DAY -> "1h";
                case WEEK -> "8h";
                case MONTH -> "1d";
                case YEAR -> "7d";
            };
        }
    }
}
