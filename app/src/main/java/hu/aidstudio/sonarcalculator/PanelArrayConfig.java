package hu.aidstudio.sonarcalculator;

/**
 * Configuration for the panel array (multiple panels, series/parallel wiring).
 */
public class PanelArrayConfig {
    public int panelsInSeries = 1;      // Number of panels in series (string)
    public int stringsInParallel = 1;   // Number of parallel strings
    public double singlePanelWp = 0;    // Single panel Wp
    public double singlePanelVoc = 0;   // Single panel Voc
    public double singlePanelIsc = 0;   // Single panel Isc
    public double singlePanelVmp = 0;   // Single panel Vmp (optional, for MPPT calc)
    public double singlePanelImp = 0;   // Single panel Imp (optional)

    // Cable parameters
    public double cableLengthMeters = 0;      // One-way cable length
    public double cableCrossSectionMm2 = 0;   // Cable cross-section (mm^2), 0 = auto-calculate

    public int getTotalPanels() {
        return panelsInSeries * stringsInParallel;
    }

    public double getTotalWp() {
        return singlePanelWp * getTotalPanels();
    }

    public double getArrayVoc() {
        // Voc adds in series, parallel strings don't increase voltage
        return singlePanelVoc * panelsInSeries * Constants.VOC_TEMP_COEFF_FACTOR; // Cold temp correction
    }

    public double getArrayIsc() {
        // Isc adds in parallel
        return singlePanelIsc * stringsInParallel * Constants.ISC_SAFETY_FACTOR;
    }

    public double getArrayVmp() {
        return singlePanelVmp * panelsInSeries;
    }

    public double getArrayImp() {
        return singlePanelImp * stringsInParallel;
    }
}