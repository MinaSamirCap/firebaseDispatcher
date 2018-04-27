package com.mmd.firebasedispature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

    private static String JOB_TAH_1 = "service1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new dispatcher using the Google Play driver.
        GooglePlayDriver driver = new GooglePlayDriver(this);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job myJob = dispatcher.newJobBuilder()
                // Select the service you want to work with.
                .setService(MyService.class)
                // set unique id to job
                .setTag(JOB_TAH_1)
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
        dispatcher.cancel(JOB_TAH_1);
        int cancel = driver.cancel(JOB_TAH_1);
        Toast.makeText(this, cancel + " canceled", Toast.LENGTH_LONG).show();
    }
}
