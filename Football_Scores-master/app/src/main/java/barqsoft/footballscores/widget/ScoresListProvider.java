package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.service.myFetchService;

/**
 * Created by deepanshugupta on 05/03/16.
 */
public class ScoresListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;
    Cursor scoresCursor = null;
    String[] strDate = new String[1];
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_ID = 8;

    public ScoresListProvider(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mContext.startService(new Intent(mContext, myFetchService.class));
    }

    @Override
    public void onDataSetChanged() {

        final long identityToken = Binder.clearCallingIdentity();
        Date date = new Date(System.currentTimeMillis()+((-2)*86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate_ = mformat.format(date);

        strDate[0] = strDate_;

        scoresCursor = mContext.getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(),
                null,
                null,
                strDate,
                null
        );
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (scoresCursor!= null) {
            scoresCursor.close();
            scoresCursor = null;
        }

    }

    @Override
    public int getCount() {
        return scoresCursor == null ? 0 : scoresCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                scoresCursor == null || !scoresCursor.moveToPosition(position)) {
            return null;
        }
        String home_team = scoresCursor.getString(COL_HOME);
        String away_team = scoresCursor.getString(COL_AWAY);
        String match_score = Utilies.getScores(scoresCursor.getInt(COL_HOME_GOALS), scoresCursor.getInt(COL_AWAY_GOALS));

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_score);

        rv.setTextViewText(R.id.home_team,home_team);
        rv.setTextViewText(R.id.away_team,away_team);
        rv.setTextViewText(R.id.match_score, match_score);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.widget_score);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (scoresCursor.moveToPosition(position))
            return scoresCursor.getLong(COL_ID);
        return position;

    }

    @Override
    public boolean hasStableIds() {

        return true;
    }
}
