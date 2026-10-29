package hu.aidstudio.sonarcalculator;

/**
 * Engine for solar system calculations.
 * Separated from UI formatting: no Context or string resources used here.
 * Returns a SolarCalculationResult object with all calculated values.
 * This makes the calculation logic testable and ready for future technical extensions
 * (multi-panel arrays, multi-battery banks, real-world PSH estimation, etc.).
 */
public class CalculatorEngine {

    /**
     * Performs all calculations based on input SolarData.
     * Does not depend on Android Context (testable).
     */
    public static SolarCalculationResult calculate(SolarData data) {
        SolarCalculationResult r = new SolarCalculationResult();

        // --- Copy input values needed for reporting ---
        r.selectedControllerName = data.selectedControllerName;
        r.nominalPanelPowerWp = data.wp;
        r.panelVoc = data.voc;
        r.panelIsc = data.isc;
        r.batteryVoltageV = data.batV;
        r.batteryAmpHoursAh = data.batAh;
        r.batterySOH = data.soh > 0 ? data.soh : Constants.DEFAULT_SOH_PERCENT;
        r.dailyConsumptionWh = data.dailyConsWh;
        r.isDayLoadProfile = data.isDayLoad;

        // --- 1. Controller Efficiency ---
        double ctrlEff = Constants.EFF_DEFAULT;
        if (data.isCheapPWM)
            ctrlEff = Constants.EFF_CHEAP_PWM;
        else if (data.isQualityPWM)
            ctrlEff = Constants.EFF_QUALITY_PWM;
        else if (data.isMPPT)
            ctrlEff = Constants.EFF_MPPT;
        r.controllerEfficiency = ctrlEff;

        // --- 2. Environmental Factors (PSH) ---
        double totalDaylight = Constants.DAYLIGHT_DEFAULT;
        double psh = Constants.PSH_DEFAULT;
        double weatherMult = Constants.WEATHER_DEFAULT;

        if (data.manualHours > 0) {
            totalDaylight = data.manualHours;
            psh = data.manualHours * Constants.MANUAL_PSH_FACTOR;
        } else {
            if (data.isWinter) {
                totalDaylight = Constants.DAYLIGHT_WINTER;
                psh = Constants.PSH_WINTER;
            } else if (data.isSpring) {
                totalDaylight = Constants.DAYLIGHT_SPRING;
                psh = Constants.PSH_SPRING;
            } else if (data.isSummer) {
                totalDaylight = Constants.DAYLIGHT_SUMMER;
                psh = Constants.PSH_SUMMER;
            } else if (data.isAutumn) {
                totalDaylight = Constants.DAYLIGHT_AUTUMN;
                psh = Constants.PSH_AUTUMN;
            }
        }

        if (data.isRainy) {
            weatherMult = Constants.WEATHER_RAINY;
        } else if (data.isVeryCloudy) {
            weatherMult = Constants.WEATHER_VERY_CLOUDY;
        } else if (data.isCloudy) {
            weatherMult = Constants.WEATHER_CLOUDY;
        } else if (data.isSunny) {
            weatherMult = Constants.WEATHER_SUNNY;
        }

        double finalPsh = psh * weatherMult;

        r.totalDaylightHours = totalDaylight;
        r.usefulSunHoursPsh = finalPsh;
        r.weatherMultiplier = weatherMult;

        // --- 3. Calculations ---
        // Current panel power (accounting for derating, weather, cable loss)
        double currentPowerW = data.wp * Constants.PANEL_DERATING_FACTOR * weatherMult * ctrlEff * Constants.CABLE_LOSS_FACTOR;
        // Daily generation: nominal Wp * PSH * controller eff * cable loss
        double dailyGenWh = data.wp * finalPsh * ctrlEff * Constants.CABLE_LOSS_FACTOR;

        // Recommended controller amps (safety factor on max current)
        // For multi-panel arrays, Isc scales with parallel strings, so use array current
        double arrayIsc = data.isc * data.panelStringsInParallel * Constants.ISC_SAFETY_FACTOR;
        double maxCurrentForSizing = (arrayIsc * data.voc) / data.batV; // Rough: Isc * Voc / Vbat is not right; use simple: current from power
        double recAmps = (data.wp / data.batV) * Constants.CONTROLLER_SIZING_FACTOR;
        if (data.panelStringsInParallel > 1) {
            // More parallel strings means higher peak current
            recAmps = ((data.wp / data.panelsInSeries) / data.batV) * Constants.CONTROLLER_SIZING_FACTOR * data.panelStringsInParallel;
        }
        r.recommendedControllerAmps = recAmps;

        // Battery capacities
        double nominalBatWh = data.batV * data.batAh;
        double realCapWh = nominalBatWh * (r.batterySOH / 100.0);
        double safeCapWh = realCapWh * Constants.MAX_DEPTH_OF_DISCHARGE;

        r.currentPanelPowerW = currentPowerW;
        r.dailyGenerationWh = dailyGenWh;
        r.nominalBatteryCapacityWh = nominalBatWh;
        r.realBatteryCapacityWh = realCapWh;
        r.usableBatteryCapacityWh = safeCapWh;

        // --- 4. Consumption & Cycle ---
        r.dailyEnergyBalanceWh = dailyGenWh - data.dailyConsWh;

        // --- 5. Multi-Battery Configuration ---
        // For simplicity, use single battery values as the bank values when no explicit
        // multi-battery config is provided (batteriesInSeries/batteriesInParallel default to 1).
        // The calculation logic is ready to be extended here.
        r.morningBatteryLevelPercent = 50.0; // Placeholder for future detailed analysis
        r.eveningBatteryLevelPercent = 50.0;
        r.rechargeTimeHours = safeCapWh / (dailyGenWh > 0 ? dailyGenWh : 1.0);
        r.isChargeAbsorbed = false; // Placeholder
        r.isBatteryFullOvernight = false; // Placeholder

        // --- 6. Diagnostics ---
        if (r.dailyEnergyBalanceWh < 0) {
            r.diagnosisMessageKey = "diag_negative";
        } else {
            r.diagnosisMessageKey = "diag_perfect";
        }

        return r;
    }
}