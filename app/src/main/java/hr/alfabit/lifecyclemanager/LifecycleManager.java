package hr.alfabit.lifecyclemanager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karim on 25.5.2015..
 */
public class LifecycleManager
{
    private WeakReference<Activity> activity;
    private List<Activity> activityList;

    public LifecycleManager(Activity activity)
    {
        watchActivity(activity);
    }

    @CallSuper
    protected void watchActivity(Activity activity)
    {
        if (activity == null) return;
        this.activity = new WeakReference<>(activity);
        if (activityList == null) activityList = new ArrayList<>();
        if (!activityList.contains(activity))
        {
            activityList.add(activity);
            activity.getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks());
        }
    }

    @CallSuper
    protected void ignoreActivity(Activity activity)
    {
        if (activity == null) return;
        activity.getApplication().unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks());
        if (activityList != null) activityList.remove(activity);
        this.activity = null;
    }

    @CallSuper
    protected boolean shouldHandleActivity(Activity activity)
    {
        if (activityList == null) return false;
        return activityList.contains(activity);
    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks()
    {
        return new Application.ActivityLifecycleCallbacks()
        {
            @Override
            @CallSuper
            public void onActivityCreated(Activity activity, Bundle savedInstanceState)
            {
                if (shouldHandleActivity(activity))
                {
                    LifecycleManager.this.activity = new WeakReference<>(activity);
                    onCreate(activity, savedInstanceState);
                } else return;
            }

            @Override
            @CallSuper
            public void onActivityStarted(Activity activity)
            {
                if (shouldHandleActivity(activity)) onStart(activity);
                else return;
            }

            @Override
            @CallSuper
            public void onActivityResumed(Activity activity)
            {
                if (shouldHandleActivity(activity))
                {
                    LifecycleManager.this.activity = new WeakReference<>(activity);
                    onResume(activity);
                } else return;
            }

            @Override
            @CallSuper
            public void onActivityPaused(Activity activity)
            {
                if (shouldHandleActivity(activity)) onPause(activity);
                else return;
            }

            @Override
            @CallSuper
            public void onActivityStopped(Activity activity)
            {
                if (shouldHandleActivity(activity)) onStop(activity);
                else return;
            }

            @Override
            @CallSuper
            public void onActivitySaveInstanceState(Activity activity, Bundle outState)
            {
                if (shouldHandleActivity(activity)) onSaveInstanceState(activity, outState);
                else return;
            }

            @Override
            @CallSuper
            public void onActivityDestroyed(Activity activity)
            {
                if (shouldHandleActivity(activity)) onDestroy(activity);
                else return;
                ignoreActivity(activity);
            }
        };
    }

    @CallSuper
    public void passActivityResult(Activity activity, int requestCode, int resultCode, Intent data)
    {
        if (shouldHandleActivity(activity)) onActivityResult(activity, requestCode, resultCode, data);
        else return;
    }

    protected void onCreate(Activity activity, Bundle savedInstanceState)
    {
    }

    protected void onStart(Activity activity)
    {

    }

    protected void onResume(Activity activity)
    {
    }

    protected void onPause(Activity activity)
    {

    }

    protected void onStop(Activity activity)
    {

    }

    protected void onSaveInstanceState(Activity activity, Bundle outState)
    {

    }

    protected void onDestroy(Activity activity)
    {

    }

    protected void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data)
    {

    }

    public Activity getActivity()
    {
        if (activity == null) return null;
        return activity.get();
    }
}
