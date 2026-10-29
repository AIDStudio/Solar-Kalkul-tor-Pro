package hu.aidstudio.sonarcalculator;

import android.content.Context;

/**
 * Formats a SolarCalculationResult into a user-facing report string.
 */
public class ReportFormatter {

    public static String format(Context ctx, SolarCalculationResult r) {
        StringBuilder res = new StringBuilder();

        res.append(ctx.getString(R.string.res_header_solar));
        if (r.selectedControllerName != null && !r.selectedControllerName.isEmpty()) {
            res.append(ctx.getString(R.string.res_ctrl_selected, r.selectedControllerName));
        }
        res.append(ctx.getString(R.string.res_solar_power, r.nominalPanelPowerWp, r.currentPanelPowerW));
        res.append(ctx.getString(R.string.res_solar_psh, r.usefulSunHoursPsh, r.totalDaylightHours));
        res.append(ctx.getString(R.string.res_solar_daily_gen, r.dailyGenerationWh));
        res.append(ctx.getString(R.string.res_solar_controller, r.recommendedControllerAmps));

        res.append(ctx.getString(R.string.res_header_battery, r.batterySOH));
        res.append(ctx.getString(R.string.res_bat_cap, r.batteryVoltageV * r.batteryAmpHoursAh, r.realBatteryCapacityWh));
        res.append(ctx.getString(R.string.res_bat_safe, r.usableBatteryCapacityWh));

        // Only show consumption if we have data
        if (r.dailyConsumptionWh > 0) {
            res.append(ctx.getString(R.string.res_header_consumption));
            res.append(ctx.getString(R.string.res_cons_profile,
                    r.isDayLoadProfile ? ctx.getString(R.string.profile_day)
                            : ctx.getString(R.string.profile_night)));
            res.append(ctx.getString(R.string.res_cons_daily, r.dailyConsumptionWh));
            res.append(ctx.getString(R.string.res_cons_autonomy, r.usableBatteryCapacityWh / r.dailyConsumptionWh));

            res.append(ctx.getString(R.string.res_header_cycle));
            res.append(ctx.getString(R.string.res_cycle_balance, (r.dailyEnergyBalanceWh > 0 ? "+" : ""),
                    r.dailyEnergyBalanceWh));

            res.append(ctx.getString(R.string.res_header_diagnosis));
            // Determine diagnosis message based on balance
            int diagnosisResId = r.dailyEnergyBalanceWh < 0 ? R.string.diag_negative : R.string.diag_perfect;
            res.append(ctx.getString(diagnosisResId));
        }

        // Append newline character at the end for clean text
        res.append("\n");

        return res.toString();
    }
}