package com.mmd.firebasedispature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                // Select the service you want to work with.
                .setService(MyService.class)
                // set unique id to job
                .setTag("MyService")
                // Repeat
                .setRecurring(true)
                // to run from 0 to 60 seconds (with SetRecurring)
                .setTrigger(Trigger.executionWindow(30, 90))
                // will give this job until the new boot.
                .setLifetime(Lifetime.FOREVER)
                // do not overwrite job with the same tag name
                .setReplaceCurrent(true)
                // will only work when connected to the network
                //.setConstraints(Constraint.ON_ANY_NETWORK, Constraint.DEVICE_CHARGING)
                // retry the matric fail
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();

        dispatcher.mustSchedule(myJob);



                /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 *
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))*/

    }
}
