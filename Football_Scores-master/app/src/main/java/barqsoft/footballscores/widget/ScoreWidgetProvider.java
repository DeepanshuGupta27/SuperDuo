package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import barqsoft.footballscores.service.ScoreWidgetIntentService;

/**
 * Created by deepanshugupta on 05/03/16.
 */
public class ScoreWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        context.startService(new Intent(context, ScoreWidgetIntentService.class));
    }
}
