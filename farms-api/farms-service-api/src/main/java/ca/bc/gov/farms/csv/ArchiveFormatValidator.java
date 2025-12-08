package ca.bc.gov.farms.csv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z99ExtractFileCtl;

public class ArchiveFormatValidator {

    private FileCacheManager cache;

    private List<CSVParserException> errors = new ArrayList<>();
    private List<CSVParserException> warnings = new ArrayList<>();

    public ArchiveFormatValidator(final FileCacheManager fcm) {
        this.cache = fcm;
    }

    public final List<CSVParserException> getErrors() {
        return errors;
    }

    public final List<CSVParserException> getWarnings() {
        return warnings;
    }

    private void addError(final CSVParserException e) {
        errors.add(e);
    }

    private void addWarning(final CSVParserException e) {
        warnings.add(e);
    }

    public final boolean validate() {
        validateFileNames();
        if (errors != null && errors.size() > 0) {
            return false;
        }

        List<Z99ExtractFileCtl> z99 = checkAndLoad99();
        if (z99 != null) {
            validateF99FileNumbers(z99);
        }
        if (errors != null && errors.size() > 0) {
            return false;
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_01_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_02_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_03_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_04_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_05_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_21_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_22_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_23_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_28_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_29_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_40_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_42_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_50_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        try {
            FileHandle<?> fh = cache.read(FileCacheManager.FIPD_51_NAME_PREFIX);
            validate(fh, z99);
        } catch (CSVParserException e) {
            addError(e);
        }

        return errors == null || errors.size() == 0;
    }

    private void validate(final FileHandle<?> fh, final List<Z99ExtractFileCtl> z99) {
        int count = findCount(fh.getFileNumber(), z99);

        try {
            String[] errs = fh.validate();

            if (errs != null) {
                for (String err : errs) {
                    addError(new CSVParserException(fh.getRowsRead(), fh.getFileNumber(), err));
                }
            }

            int c = fh.getRecords().size();
            if (c != count) {
                addError(new CSVParserException(0, fh.getFileNumber(),
                        "Row count in file does not match declared count."));
            }
        } catch (RuntimeException re) {
            addError(new CSVParserException(0, fh.getFileNumber(), re));
        }
    }

    private int findCount(final Integer fileNumber, final List<Z99ExtractFileCtl> pZ99) {
        if (pZ99 == null) {
            return -1;
        }

        for (Z99ExtractFileCtl z : pZ99) {
            if (z.getExtractFileNumber() != null && z.getExtractFileNumber().equals(fileNumber)) {
                if (z.getRowCount() != null) {
                    return z.getRowCount();
                }
            }
        }

        return 0;
    }

    private void validateF99FileNumbers(final List<Z99ExtractFileCtl> pZ99) {
        List<Integer> l = new ArrayList<>();

        for (Z99ExtractFileCtl p : pZ99) {
            l.add(p.getExtractFileNumber());
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_01_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_01_NUMBER,
                    "File 01 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_02_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_02_NUMBER,
                    "File 02 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_03_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_03_NUMBER,
                    "File 03 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_04_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_04_NUMBER,
                    "File 04 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_05_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_05_NUMBER,
                    "File 05 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_21_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_21_NUMBER,
                    "File 21 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_22_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_22_NUMBER,
                    "File 22 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_23_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_23_NUMBER,
                    "File 23 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_28_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_28_NUMBER,
                    "File 28 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_29_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_29_NUMBER,
                    "File 29 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_40_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_40_NUMBER,
                    "File 40 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_42_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_42_NUMBER,
                    "File 42 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_50_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_50_NUMBER,
                    "File 50 not found in File 99"));
        }

        if (!l.remove(Integer.valueOf(FileCacheManager.FIPD_51_NUMBER))) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_51_NUMBER,
                    "File 51 not found in File 99"));
        }

        for (Iterator<Integer> i = l.iterator(); i.hasNext();) {
            addWarning(new CSVParserException(0, 0,
                    "File " + i.next() + " found in File 99"));
        }
    }

    private void validateFileNames() {
        if (cache.find(FileCacheManager.FIPD_01_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_01_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_01_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_02_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_02_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_02_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_03_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_03_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_03_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_04_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_04_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_04_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_05_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_05_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_05_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_21_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_21_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_21_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_22_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_22_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_22_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_23_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_23_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_23_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_28_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_28_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_28_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_29_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_29_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_29_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_40_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_40_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_40_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_42_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_42_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_42_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_50_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_50_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_50_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_51_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_51_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_51_NAME_PREFIX + "YYYYMMDD.csv"));
        }

        if (cache.find(FileCacheManager.FIPD_99_NAME_PREFIX) == null) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_99_NUMBER,
                    "Missing File: " + FileCacheManager.FIPD_99_NAME_PREFIX + "YYYYMMDD.csv"));
        }
    }

    private List<Z99ExtractFileCtl> checkAndLoad99() {
        List<Z99ExtractFileCtl> z99 = new ArrayList<>();
        FileHandle<Z99ExtractFileCtl> fh = null;

        try {

            try {
                fh = Fipd99.read(cache.find(FileCacheManager.FIPD_99_NAME_PREFIX));
            } catch (CSVParserException e) {
                addError(e);
                return z99;
            }

            String[] errs = fh.validate();
            if (errs != null) {
                for (String err : errs) {
                    addError(new CSVParserException(fh.getRowsRead(), fh.getFileNumber(), err));
                }
            }

            z99.addAll(fh.getRecords());
        } catch (RuntimeException re) {
            addError(new CSVParserException(0, FileCacheManager.FIPD_99_NUMBER, re));
        }

        return z99;
    }
}
