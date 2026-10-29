package hu.aidstudio.sonarcalculator;

public class SolarData {
    public double wp, voc, isc, batV, batAh, soh, dailyConsWh, manualHours;
    public boolean isWinter, isSpring, isSummer, isAutumn;
    public boolean isSunny, isCloudy, isVeryCloudy, isRainy;
    public boolean isDayLoad;
    public boolean isCheapPWM, isQualityPWM, isMPPT;
    // Itt tároljuk el a kijelzett nevet a UI-ról
    public String selectedControllerName = "";

    public boolean isValid() {
        return wp > 0 && batV > 0 && batAh > 0;
    }
}
