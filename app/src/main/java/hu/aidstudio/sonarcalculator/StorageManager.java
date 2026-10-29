package hu.aidstudio.sonarcalculator;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageManager {
    public static void save(Context c, SolarData d) {
        SharedPreferences.Editor e = c.getSharedPreferences("S", Context.MODE_PRIVATE).edit();
        e.putFloat("w", (float) d.wp);
        e.putFloat("v", (float) d.batV);
        e.putFloat("a", (float) d.batAh);
        e.putFloat("s", (float) d.soh);
        e.putFloat("c", (float) d.dailyConsWh);
        e.apply();
    }

    public static void load(Context c, SolarData d) {
        SharedPreferences p = c.getSharedPreferences("S", Context.MODE_PRIVATE);
        d.wp = p.getFloat("w", 0);
        d.batV = p.getFloat("v", 0);
        d.batAh = p.getFloat("a", 0);
        d.soh = p.getFloat("s", 100);
        d.dailyConsWh = p.getFloat("c", 0);
    }
}
