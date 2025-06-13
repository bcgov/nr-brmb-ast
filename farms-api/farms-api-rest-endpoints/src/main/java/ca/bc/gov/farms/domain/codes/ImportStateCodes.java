package ca.bc.gov.farms.domain.codes;

/**
 * @author $author$
 * @version $Revision: 1694 $, $Date: 2011-11-07 15:45:42 -0800 (Mon, 07 Nov
 *          2011) $
 */
public final class ImportStateCodes {

    /**  */
    private ImportStateCodes() {
    }

    public static final String SCHEDULED_FOR_STAGING = "SS";

    public static final String STAGING_IN_PROGRESS = "SP";

    public static final String STAGING_FAILED = "SF";

    public static final String STAGING_COMPLETE = "SC";

    public static final String SCHEDULED_FOR_IMPORT = "SI";

    public static final String IMPORT_IN_PROGRESS = "IP";

    public static final String IMPORT_FAILED = "IF";

    public static final String IMPORT_COMPLETE = "IC";

    public static final String IMPORT_COMPLETE_PARTIALLY = "IPC";

    public static final String CANCELLED = "CAN";

    /**
     * @param code a state code
     *
     * @return true if the state is not a final state
     */
    public static boolean isInProgress(final String code) {
        return (SCHEDULED_FOR_STAGING.equals(code)
                || STAGING_IN_PROGRESS.equals(code)
                || STAGING_COMPLETE.equals(code)
                || SCHEDULED_FOR_IMPORT.equals(code)
                || IMPORT_IN_PROGRESS.equals(code));
    }

    /**
     * @param code a state code
     *
     * @return true if staging is done
     */
    public static boolean isStagingDone(final String code) {
        return (STAGING_COMPLETE.equals(code)
                || STAGING_FAILED.equals(code)
                || CANCELLED.equals(code));
    }

    /**
     * @param code a state code
     *
     * @return true if staging is scheduled
     */
    public static boolean isScheduledForStaging(final String code) {
        return SCHEDULED_FOR_STAGING.equals(code);
    }

    /**
     * @param code a state code
     *
     * @return true if staging is scheduled
     */
    public static boolean isScheduledForImport(final String code) {
        return SCHEDULED_FOR_IMPORT.equals(code);
    }

    /**
     * @param code a state code
     *
     * @return true if importing (staging to operational) is done
     */
    public static boolean isImportDone(final String code) {
        return (IMPORT_COMPLETE.equals(code) ||
                IMPORT_FAILED.equals(code) ||
                IMPORT_COMPLETE_PARTIALLY.equals(code));
    }

    /**
     * @param importClassCode        importClassCode
     * @param importStateCode        importStateCode
     * @param importStateDescription importStateDescription
     * @return String
     */
    public static String translateImportStateDescription(
            String importClassCode, String importStateCode, String importStateDescription) {
        if (ImportClassCodes.isEnrolment(importClassCode)) {
            if (ImportStateCodes.SCHEDULED_FOR_IMPORT.equals(importStateCode)) {
                return "Scheduled for Confirmation";
            } else if (ImportStateCodes.IMPORT_COMPLETE.equals(importStateCode)) {
                return "Enrolment Completed";
            } else if (ImportStateCodes.IMPORT_FAILED.equals(importStateCode)) {
                return "Enrolment Failed";
            } else if (ImportStateCodes.IMPORT_IN_PROGRESS.equals(importStateCode)) {
                return "Enrolment in Progress";
            }
        } else if (ImportClassCodes.isTransfer(importClassCode)) {
            if (ImportStateCodes.SCHEDULED_FOR_STAGING.equals(importStateCode)) {
                return "Scheduled for Transfer";
            } else if (ImportStateCodes.IMPORT_COMPLETE.equals(importStateCode)) {
                return "Transfer Completed";
            } else if (ImportStateCodes.IMPORT_FAILED.equals(importStateCode)) {
                return "Transfer Failed";
            } else if (ImportStateCodes.IMPORT_IN_PROGRESS.equals(importStateCode)) {
                return "Transfer in Progress";
            }
        }
        return importStateDescription;
    }

}
