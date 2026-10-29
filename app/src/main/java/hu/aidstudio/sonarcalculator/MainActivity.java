package hu.aidstudio.sonarcalculator;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etWp = findViewById(R.id.etWp), etBatV = findViewById(R.id.etBatV),
                etBatAh = findViewById(R.id.etBatAh);
        EditText etSoh = findViewById(R.id.etBatSoh), etCons = findViewById(R.id.etConsWh),
                etHours = findViewById(R.id.etManualHours);
        CheckBox cbWinter = findViewById(R.id.cbWinter), cbSpring = findViewById(R.id.cbSpring),
                cbSummer = findViewById(R.id.cbSummer), cbAutumn = findViewById(R.id.cbAutumn);
        CheckBox cbSunny = findViewById(R.id.cbSunny), cbCloudy = findViewById(R.id.cbCloudy),
                cbDark = findViewById(R.id.cbVeryCloudy), cbRain = findViewById(R.id.cbRainy);
        CheckBox cbCheap = findViewById(R.id.cbCheapPWM),
                cbQuality = findViewById(R.id.cbQualityPWM), cbMPPT = findViewById(R.id.cbMPPT);
        CheckBox cbDay = findViewById(R.id.Day), cbNight = findViewById(R.id.Night);
        Button btn = findViewById(R.id.btnCalculate);
        TextView tvRes = findViewById(R.id.tvResult);

        UIHelper.setupExclusive(cbWinter, cbSpring, cbSummer, cbAutumn);
        UIHelper.setupExclusive(cbSunny, cbCloudy, cbDark, cbRain);
        UIHelper.setupExclusive(cbCheap, cbQuality, cbMPPT);
        UIHelper.setupExclusive(cbDay, cbNight);

        btn.setOnClickListener(v -> {
            SolarData d = new SolarData();
            d.wp = UIHelper.parse(etWp);
            d.batV = UIHelper.parse(etBatV);
            d.batAh = UIHelper.parse(etBatAh);
            d.soh = UIHelper.parse(etSoh);
            d.dailyConsWh = UIHelper.parse(etCons);
            d.manualHours = UIHelper.parse(etHours);

            d.isCheapPWM = cbCheap.isChecked();
            d.isQualityPWM = cbQuality.isChecked();
            d.isMPPT = cbMPPT.isChecked();

            // Beolvassuk a feliratot közvetlenül a kijelzőről
            if (d.isCheapPWM)
                d.selectedControllerName = cbCheap.getText().toString();
            else if (d.isQualityPWM)
                d.selectedControllerName = cbQuality.getText().toString();
            else if (d.isMPPT)
                d.selectedControllerName = cbMPPT.getText().toString();

            d.isWinter = cbWinter.isChecked();
            d.isSpring = cbSpring.isChecked();
            d.isSummer = cbSummer.isChecked();
            d.isAutumn = cbAutumn.isChecked();
            d.isSunny = cbSunny.isChecked();
            d.isCloudy = cbCloudy.isChecked();
            d.isVeryCloudy = cbDark.isChecked();
            d.isRainy = cbRain.isChecked();
            d.isDayLoad = cbDay.isChecked();

            // Validáció: legalább a három alapvető mezőt kell feltölteni
            boolean valid = d.wp > 0 && d.batV > 0 && d.batAh > 0
                    && d.soh >= 0 && d.soh <= 100; // SOH érvényes tartománynak kell lennie

            if (valid) {
                // Új módszer: a számítást elválasztjuk a formázástól
                SolarCalculationResult result = CalculatorEngine.calculate(d);
                String report = ReportFormatter.format(this, result);
                tvRes.setText(report);
                StorageManager.save(this, d);
            } else {
                tvRes.setText(getString(R.string.error_missing_data));
            }
        });
    }
}