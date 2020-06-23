package cfwz.skiti.go4lunch.utils.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


public class NotificationHelper {
        private Context mContext;
        private AlarmManager alarmManagerRTC;
        private PendingIntent alarmIntentRTC;
        public static int ALARM_TYPE_RTC = 100;


        public NotificationHelper(Context context) {
            mContext = context;
        }

        public void scheduleRepeatingNotification(){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY,12);
            Intent intent = new Intent(mContext, AlarmReceiver.class);
            alarmIntentRTC = PendingIntent.getBroadcast(mContext, ALARM_TYPE_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManagerRTC = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
            alarmManagerRTC.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 5, AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntentRTC);
            alarmManagerRTC.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentRTC);
        }

        public void cancelAlarmRTC() {
            if (alarmManagerRTC!= null) {
                alarmManagerRTC.cancel(alarmIntentRTC);
            }
        }
    }
