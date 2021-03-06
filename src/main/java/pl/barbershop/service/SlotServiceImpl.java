package pl.barbershop.service;


import org.springframework.stereotype.Service;
import pl.barbershop.model.Slot;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

@Service
public class SlotServiceImpl implements SlotService {


    @Override
    public List<Slot> setSlot(String open, String close, String duration) {
        double openParsed = Double.parseDouble(open);
        double closeParsed = Double.parseDouble(close);
        double durationParsed = Double.parseDouble(duration);
        List<Slot> slots = new ArrayList<>();
        for (double i = openParsed; i < closeParsed; i = i + (durationParsed / 60)) {
            Slot slot = new Slot();
            double minutes = i * 60;
            Duration result = Duration.ofMinutes((long) minutes);
            String parsedResult = result.toString().replace("PT", "").replace("H", ":").replace("M", "");
            if (Double.parseDouble(parsedResult.replace(":", ".")) + (durationParsed / 60) < closeParsed) {

              if(parsedResult.length() == 2 || parsedResult.length() == 3) {
                  slot.setTime(parsedResult + "00");
                  slot.setAvaible(true);
                  slots.add(slot);
              }else{
                  slot.setTime(parsedResult);
                  slot.setAvaible(true);
                  slots.add(slot);
              }
            }
        }

        return slots;
    }

    @Override
    public List<Slot> checkIsAvaible(List<Slot> slots) {
        for (Slot slot : slots) {
            if (!slot.isAvaible()) {
                slots.remove(slot);
                return slots;
            }
        }
        return slots;
    }

    @Override
    public List<Slot> updateSlot(String open, String close, String duration) throws ParseException {

            String[] openTable = open.split(":");
            String finalClose = close + ":00";
            String[] closeTable = finalClose.split(":");
            List<Slot> slots = new ArrayList<>();

            LocalTime opened = LocalTime.of(Integer.parseInt(openTable[0]), Integer.parseInt(openTable[1]));
            double openParsed = opened.getHour()*60 + opened.getMinute();

            LocalTime closed = LocalTime.of(Integer.parseInt(closeTable[0]), Integer.parseInt(closeTable[1]));
            double closeParsed = closed.getHour()*60 + closed.getMinute();
            double durationParsed = Double.parseDouble(duration);

            for (double i = openParsed; i < closeParsed; i = i + Double.parseDouble(duration)) {
                Slot slot = new Slot();
                Duration result = Duration.ofMinutes((long) i);
                System.out.println(result.toString());
                String parsedResult = result.toString().replace("PT", "").replace("H", ":").replace("M", "");
                if (Double.parseDouble(parsedResult.replace(":", ".")) + (durationParsed / 60) < closeParsed) {
                    if(parsedResult.length() == 2 || parsedResult.length() == 3) {
                        slot.setTime(parsedResult + "00");
                        slot.setAvaible(true);
                        slots.add(slot);
                    }else{
                        slot.setTime(parsedResult);
                        slot.setAvaible(true);
                        slots.add(slot);
                    }
                }
            }
            return slots;
    }

}