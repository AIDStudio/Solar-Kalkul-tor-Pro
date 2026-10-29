package hu.aidstudio.sonarcalculator;

import android.content.Context;

public class CalculatorEngine {

    public static String generateReport(Context ctx, SolarData data) {
        StringBuilder res = new StringBuilder();

        // 1. VEZÉRLŐ HATÁSFOK
        double ctrlEff = 0.75;
        if (data.isCheapPWM)
            ctrlEff = 0.62;
        else if (data.isQualityPWM)
            ctrlEff = 0.82;
        else if (data.isMPPT)
            ctrlEff = 0.97;

        // 2. KÖRNYEZETI TÉNYEZŐK (PSH)
        double totalDaylight = 12.0;
        double psh = 4.0;
        if (data.manualHours > 0) {
            totalDaylight = data.manualHours;
            psh = data.manualHours * 0.45;
        } else {
            if (data.isWinter) {
                totalDaylight = 8.5;
                psh = 1.5;
            } else if (data.isSpring) {
                totalDaylight = 13.0;
                psh = 3.8;
            } else if (data.isSummer) {
                totalDaylight = 15.5;
                psh = 5.5;
            } else if (data.isAutumn) {
                totalDaylight = 11.5;
                psh = 2.5;
            }
        }

        double wMult = data.isSunny ? 1.0 : data.isCloudy ? 0.6 : data.isVeryCloudy ? 0.3 : 0.15;
        double finalPsh = psh * wMult;

        // 3. SZÁMÍTÁSOK
        double currentPowerW = data.wp * 0.85 * wMult * ctrlEff;
        double dailyGenWh = data.wp * finalPsh * ctrlEff;
        double recAmps = (data.wp / data.batV) * 1.25;
        double realCapWh = (data.batV * data.batAh) * (data.soh / 100.0);
        double safeCapWh = realCapWh * 0.8;

        // 4. JELENTÉS (CSAK A TE EREDETI STRINGJEIDDEL)
        res.append(ctx.getString(R.string.res_header_solar));
        if (!data.selectedControllerName.isEmpty()) {
            res.append("• Vezérlő: ").append(data.selectedControllerName).append("\n");
        }
        res.append(ctx.getString(R.string.res_solar_power, data.wp, currentPowerW));
        res.append(ctx.getString(R.string.res_solar_psh, finalPsh, totalDaylight));
        res.append(ctx.getString(R.string.res_solar_daily_gen, dailyGenWh));
        res.append(ctx.getString(R.string.res_solar_controller, recAmps));

        res.append(ctx.getString(R.string.res_header_battery, data.soh));
        res.append(ctx.getString(R.string.res_bat_cap, data.batV * data.batAh, realCapWh));
        res.append(ctx.getString(R.string.res_bat_safe, safeCapWh));

        if (data.dailyConsWh > 0) {
            res.append(ctx.getString(R.string.res_header_consumption));
            res.append(ctx.getString(R.string.res_cons_profile,
                    data.isDayLoad ? ctx.getString(R.string.profile_day)
                            : ctx.getString(R.string.profile_night)));
            res.append(ctx.getString(R.string.res_cons_daily, data.dailyConsWh));
            res.append(ctx.getString(R.string.res_cons_autonomy, safeCapWh / data.dailyConsWh));

            double balanceWh = dailyGenWh - data.dailyConsWh;
            res.append(ctx.getString(R.string.res_header_cycle));
            res.append(ctx.getString(R.string.res_cycle_balance, (balanceWh > 0 ? "+" : ""),
                    balanceWh));

            res.append(ctx.getString(R.string.res_header_diagnosis));
            if (balanceWh < 0)
                res.append(ctx.getString(R.string.diag_negative));
            else
                res.append(ctx.getString(R.string.diag_perfect));
        }

        return res.toString();
    }
}
