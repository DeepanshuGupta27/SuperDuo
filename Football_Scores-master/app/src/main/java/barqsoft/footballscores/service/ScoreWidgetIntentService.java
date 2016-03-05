package barqsoft.footballscores.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.widget.ScoreWidgetProvider;

/**
 * Created by deepanshugupta on 05/03/16.
 */
public class ScoreWidgetIntentService extends IntentService {

    String[] strDate = new String[1];
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;

    public ScoreWidgetIntentService()
    {
        super("ScoreWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        getApplicationContext().startService(new Intent(getApplicationContext(), myFetchService.class));

        Date date = new Date(System.currentTimeMillis()+((-2)*86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate_ = mformat.format(date);

        strDate[0] = strDate_;

        Cursor scoresCursor = getApplicationContext().getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(),
                null,
                null,
                strDate,
                null
        );

        if (scoresCursor == null) {
            return;
        }
        if (!scoresCursor.moveToFirst()) {
            scoresCursor.close();
            return;
        }

        String home_team = scoresCursor.getString(COL_HOME);
        String away_team = scoresCursor.getString(COL_AWAY);
        String match_score = Utilies.getScores(scoresCursor.getInt(COL_HOME_GOALS),scoresCursor.getInt(COL_AWAY_GOALS));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ScoreWidgetProvider.class));

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_score);

            views.setTextViewText(R.id.home_team,home_team);
            views.setTextViewText(R.id.away_team,away_team);
            views.setTextViewText(R.id.match_score,match_score);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        return;
    }
}
