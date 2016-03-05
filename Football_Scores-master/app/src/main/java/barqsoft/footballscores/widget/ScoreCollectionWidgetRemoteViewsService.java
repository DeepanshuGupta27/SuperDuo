package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/**
 * Created by deepanshugupta on 05/03/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoreCollectionWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return (new ScoresListProvider(this.getApplicationContext(), intent));
    }
}
