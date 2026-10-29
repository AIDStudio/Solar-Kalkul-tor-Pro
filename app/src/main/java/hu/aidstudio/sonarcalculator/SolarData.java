package hu.aidstudio.sonarcalculator;

/**
 * A data model holding input values for the solar calculator.
 * This model holds the input values from the UI and is used by CalculatorEngine and ReportFormatter.
 * Extended to support multi-panel and multi-battery configurations.
 */
public class SolarData {
    // --- Panel Data ---
    public double wp;       // Peak power Wp (single panel or total, depending on mode)
    public double voc;      // Open circuit voltage Voc (single panel)
    public double isc;      // Short circuit current Isc (single panel)
    public double vmp;      // Voltage at max power Vmp (single panel)
    public double imp;      // Current at max power Imp (single panel)

    // --- Multi-panel configuration ---
    public int panelsInSeries = 1;
    public int panelStringsInParallel = 1;

    // --- Battery Data ---
    public double batV;     // Battery voltage V (single battery)
    public double batAh;    // Battery capacity Ah (single battery)
    public double soh;      // State of Health (%)

    // --- Multi-battery configuration ---
    public int batteriesInSeries = 1;
    public int batteryStringsInParallel = 1;

    // --- Operational Hours ---
    public double manualHours = 0; // Manually specified daylight hours (0 = use season-based)

    // --- Consumption Data ---
    public double dailyConsWh; // Daily consumption Wh

    // --- Operational Flags ---
    public boolean isWinter;       // Season flag
    public boolean isSpring;
    public boolean isSummer;
    public boolean isAutumn;
    public boolean isSunny;        // Weather flag
    public boolean isCloudy;
    public boolean isVeryCloudy;
    public boolean isRainy;
    public boolean isDayLoad;      // Day / Evening load profile

    // --- Controller Selection ---
    public boolean isCheapPWM;
    public boolean isQualityPWM;
    public boolean isMPPT;

    // --- Derived / UI State ---
    public String selectedControllerName = "";

    // --- Cable parameters ---
    public double cableLengthMeters = 0;      // One-way cable length (m)
    public double cableCrossSectionMm2 = 0;   // Cable cross-section (mm^2), 0 = auto-calculate

    // --- Inverter sizing ---
    public double inverterPowerW = 0;          // 0 = auto-size

    /**
     * Validates that the minimum required fields for a basic calculation are present.
     * Returns true if the system can attempt a calculation.
     */
    public boolean isValid() {
        // Minimum requirements: Panel Wp > 0, Battery V > 0, Battery Ah > 0
        return wp > 0 && batV > 0 && batAh > 0;
    }
}