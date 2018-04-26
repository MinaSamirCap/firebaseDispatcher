package com.mmd.firebasedispature;

import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.text.DateFormat;
import java.util.Date;

//// job service runs on main thread ..
public class MyService extends JobService {

    /// in the most cases the job service will do a long time operation and because it works in main thread
    /// we will move the effort to a background thread using Async Task ..
    private AsyncTask mBackgroundTask;
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
                Log.d(" CURRENT ", currentDateTime);
                NotificationUtils.showNofivation(MyService.this, R.string.noti_title, R.string.noti_details);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

                /// job service needs to tell the system when the job is actually done by calling job finished ..
                /// and the job is done when the async task is done so we call .. jobFinished in the onPostExecute()
                jobFinished(jobParameters, false);
                // false here means the job not need to be reschedule ..
                // because the job successfully completed ..
            }
        };
        mBackgroundTask.execute();
        // passing true because we still do some work and we will pass false in the job completed ..
        return true; // Answers the question: "Is there still work going on?"
    }

    /*
    * onStopJob will called when your requirements no longer met */
    @Override
    public boolean onStopJob(JobParameters JobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true; // Answers the question: "Should this job be retried?"
    }
}
