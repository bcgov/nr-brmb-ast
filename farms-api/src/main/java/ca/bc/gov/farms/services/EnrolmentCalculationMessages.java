package ca.bc.gov.farms.services;

public final class EnrolmentCalculationMessages {

    public static final String BENEFIT_NO_MARGIN_DATA =
            "Benefit calculation has no margin data.";
    public static final String BENEFIT_MISSING_COMBINED_FARM_PERCENT =
            "Benefit calculation is missing combined farm percent.";
    public static final String BENEFIT_MARGIN_OUTSIDE_SUPPORTED_RANGE =
            "Benefit calculation has a margin outside the supported range.";
    public static final String BENEFIT_ZERO_MARGIN_DATA =
            "Benefit calculation has zero margin data in one of the three most recent years.";
    public static final String BENEFIT_INSUFFICIENT_MARGIN_DATA =
            "Benefit calculation has insufficient margin data.";
    public static final String BENEFIT_TOO_MANY_REFERENCE_MARGINS =
            "Benefit calculation has too many reference margins.";

    private EnrolmentCalculationMessages() {
    }
}
