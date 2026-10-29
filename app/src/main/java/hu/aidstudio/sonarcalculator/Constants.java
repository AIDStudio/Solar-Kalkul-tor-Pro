package hu.aidstudio.sonarcalculator;

public class Constants {
    // --- Panel & System Losses ---
    public static final double PANEL_DERATING_FACTOR = 0.85; // 15% loss (temp, dirt, mismatch)
    public static final double CABLE_LOSS_FACTOR = 0.98; // ~2% cable loss (placeholder for future cable calc)

    // --- Battery ---
    public static final double MAX_DEPTH_OF_DISCHARGE = 0.80; // 80% DoD -> usable is 80%
    public static final double DEFAULT_SOH_PERCENT = 100.0;

    // --- Controller Efficiencies ---
    public static final double EFF_CHEAP_PWM = 0.62;
    public static final double EFF_QUALITY_PWM = 0.82;
    public static final double EFF_MPPT = 0.97;
    public static final double EFF_DEFAULT = 0.75; // Fallback if none selected

    // --- Controller Sizing Safety Factor ---
    public static final double CONTROLLER_SIZING_FACTOR = 1.25; // 125% of max current

    // --- Environmental / PSH ---
    // Base PSH values for seasons (approximate for Central Europe ~47° lat)
    public static final double PSH_WINTER = 1.5;
    public static final double PSH_SPRING = 3.8;
    public static final double PSH_SUMMER = 5.5;
    public static final double PSH_AUTUMN = 2.5;
    public static final double DAYLIGHT_WINTER = 8.5;
    public static final double DAYLIGHT_SPRING = 13.0;
    public static final double DAYLIGHT_SUMMER = 15.5;
    public static final double DAYLIGHT_AUTUMN = 11.5;
    public static final double PSH_DEFAULT = 4.0;
    public static final double DAYLIGHT_DEFAULT = 12.0;

    // Manual PSH estimation factor (rough rule of thumb: PSH ~ 45% of daylight hours)
    public static final double MANUAL_PSH_FACTOR = 0.45;

    // Weather multipliers
    public static final double WEATHER_SUNNY = 1.0;
    public static final double WEATHER_CLOUDY = 0.6;
    public static final double WEATHER_VERY_CLOUDY = 0.3;
    public static final double WEATHER_RAINY = 0.15;
    public static final double WEATHER_DEFAULT = 1.0; // Default to sunny if none selected

    // --- Voc/Isc Safety Factors (for future expansion) ---
    public static final double VOC_TEMP_COEFF_FACTOR = 1.15; // Rough factor for cold temp Voc rise (STC -20C)
    public static final double ISC_SAFETY_FACTOR = 1.25; // NEC 690.8(A) 125% Isc

    // --- Cable Calculation ---
    public static final double MAX_VOLTAGE_DROP_PERCENT = 3.0; // 3% max drop
    public static final double COPPER_RESISTIVITY = 0.0172; // Ohm * mm^2 / m at 20C

    // --- Inverter Sizing ---
    public static final double INVERTER_SIZING_RATIO_MIN = 0.8;  // DC/AC ratio min
    public static final double INVERTER_SIZING_RATIO_MAX = 1.3;  // DC/AC ratio max (clipping)
}