package util.Calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

    public static String getCurrentDateBR() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
        Date dateObj = null;
        try {
            dateObj = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finalDate = simpleDateFormat.format(dateObj);
        return finalDate;
    }

    public static String getCurrentHourBR(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
    }
}
