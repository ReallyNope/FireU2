package net.myapp.Organize4U;
import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context k1, Intent k2) {
        Vibrator vibrator = (Vibrator) k1.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
        Toast.makeText(k1, "Alarm received!", Toast.LENGTH_LONG).show();

    }

}


