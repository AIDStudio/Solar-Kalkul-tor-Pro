package hu.aidstudio.sonarcalculator;

/**
 * Configuration for the battery bank (multiple batteries, series/parallel wiring).
 */
public class BatteryArrayConfig {
    public int batteriesInSeries = 1;   // Number of batteries in series
    public int stringsInParallel = 1;   // Number of parallel strings
    public double singleBatteryVoltageV = 12; // Single battery voltage (e.g., 12V)
    public double singleBatteryCapacityAh = 100; // Single battery capacity (Ah)
    public double sohPercent = 100;     // State of Health (%)

    public int getTotalBatteries() {
        return batteriesInSeries * stringsInParallel;
    }

    public double getBankVoltageV() {
        return singleBatteryVoltageV * batteriesInSeries;
    }

    public double getBankCapacityAh() {
        return singleBatteryCapacityAh * stringsInParallel;
    }

    public double getNominalCapacityWh() {
        return getBankVoltageV() * getBankCapacityAh();
    }

    public double getUsableCapacityWh() {
        return getNominalCapacityWh() * (sohPercent / 100.0) * Constants.MAX_DEPTH_OF_DISCHARGE;
    }
}