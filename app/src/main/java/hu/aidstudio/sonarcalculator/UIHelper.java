package hu.aidstudio.sonarcalculator;

import android.widget.*;

public class UIHelper {
    public static double parse(EditText e) {
        try {
            return Double.parseDouble(e.getText().toString());
        } catch (Exception ex) {
            return 0;
        }
    }

    public static void setupExclusive(CheckBox... bs) {
        for (CheckBox b : bs) {
            b.setOnClickListener(v -> {
                if (((CheckBox) v).isChecked())
                    for (CheckBox o : bs)
                        if (o != v)
                            o.setChecked(false);
            });
        }
    }
}
