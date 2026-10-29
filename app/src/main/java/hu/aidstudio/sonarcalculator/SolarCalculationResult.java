package hu.aidstudio.sonarcalculator;

public class SolarCalculationResult {
    // Vezérlő
    public double controllerEfficiency;
    public String selectedControllerName;

    // Környezeti tényezők
    public double totalDaylightHours;
    public double usefulSunHoursPsh; // Peak Sun Hours
    public double weatherMultiplier;

    // Napelem
    public double nominalPanelPowerWp;
    public double currentPanelPowerW;
    public double dailyGenerationWh;
    public double recommendedControllerAmps;
    public double panelVoc; // Hozzáadva a Voc/Isc bővítéshez
    public double panelIsc; // Hozzáadva a Voc/Isc bővítéshez

    // Akkumulátor
    public double nominalBatteryCapacityWh;
    public double realBatteryCapacityWh;
    public double usableBatteryCapacityWh; // Safe capacity (e.g., 80% DOD)
    public double batteryVoltageV;
    public double batteryAmpHoursAh;
    public double batterySOH; // State of Health

    // Fogyasztás
    public double dailyConsumptionWh;
    public boolean isDayLoadProfile;
    public double nightLoadWh; // Ezt majd később bővítjük a fogyasztási ciklus analízishez

    // Ciklus mérleg
    public double dailyEnergyBalanceWh;
    public double morningBatteryLevelPercent;
    public double eveningBatteryLevelPercent;
    public double rechargeTimeHours;
    public boolean isChargeAbsorbed;
    public boolean isBatteryFullOvernight;

    // Diagnózis
    public String diagnosisMessageKey; // Pl. R.string.diag_negative
}
