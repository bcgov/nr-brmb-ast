package ca.bc.gov.farms.csv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileCacheManager {

    private InputStream archive = null;
    private boolean extracted = false;
    private Map<String, InputStream> handles = new HashMap<>();

    public static final String FIPD_42_NAME_PREFIX = "FIPD_AS_42_";
    public static final int FIPD_42_NUMBER = 42;

    public static final String FIPD_02_NAME_PREFIX = "FIPD_AS_02_";
    public static final int FIPD_02_NUMBER = 2;

    public static final String FIPD_04_NAME_PREFIX = "FIPD_AS_04_";
    public static final int FIPD_04_NUMBER = 4;

    public static final String FIPD_03_NAME_PREFIX = "FIPD_AS_03_";
    public static final int FIPD_03_NUMBER = 3;

    public static final String FIPD_05_NAME_PREFIX = "FIPD_AS_05_";
    public static final int FIPD_05_NUMBER = 5;

    public static final String FIPD_21_NAME_PREFIX = "FIPD_AS_21_";
    public static final int FIPD_21_NUMBER = 21;

    public static final String FIPD_22_NAME_PREFIX = "FIPD_AS_22_";
    public static final int FIPD_22_NUMBER = 22;

    public static final String FIPD_23_NAME_PREFIX = "FIPD_AS_23_";
    public static final int FIPD_23_NUMBER = 23;

    public static final String FIPD_28_NAME_PREFIX = "FIPD_AS_28_";
    public static final int FIPD_28_NUMBER = 28;

    public static final String FIPD_29_NAME_PREFIX = "FIPD_AS_29_";
    public static final int FIPD_29_NUMBER = 29;

    public static final String FIPD_40_NAME_PREFIX = "FIPD_AS_40_";
    public static final int FIPD_40_NUMBER = 40;

    public static final String FIPD_50_NAME_PREFIX = "FIPD_AS_50_";
    public static final int FIPD_50_NUMBER = 50;

    public static final String FIPD_51_NAME_PREFIX = "FIPD_AS_51_";
    public static final int FIPD_51_NUMBER = 51;

    public static final String FIPD_99_NAME_PREFIX = "FIPD_AS_99_";
    public static final int FIPD_99_NUMBER = 99;

    public static final String FIPD_01_NAME_PREFIX = "FIPD_AS_01_";
    public static final int FIPD_01_NUMBER = 1;

    public FileCacheManager(InputStream archive) {
        this.archive = archive;
    }

    public final void extract() throws IOException {
        if (extracted) {
            return;
        }

        handles.clear();

        ZipInputStream zis = new ZipInputStream(archive);

        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry != null && !entry.isDirectory()) {
                String name = entry.getName();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zis.read(buffer)) >= 0) {
                    baos.write(buffer, 0, length);
                }

                handles.put(name, new ByteArrayInputStream(baos.toByteArray()));
            }
        }
        extracted = true;
    }

    public final FileHandle<?> read(final String namePrefix) throws CSVParserException {
        InputStream inputStream = find(namePrefix);
        if (inputStream == null) {
            return null;
        }

        // figure out which file handle to create
        if (FIPD_01_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd01.read(inputStream);
        }

        if (FIPD_02_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd02.read(inputStream);
        }

        if (FIPD_03_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd03.read(inputStream);
        }

        if (FIPD_04_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd04.read(inputStream);
        }

        if (FIPD_05_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd05.read(inputStream);
        }

        if (FIPD_21_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd21.read(inputStream);
        }

        if (FIPD_22_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd22.read(inputStream);
        }

        if (FIPD_23_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd23.read(inputStream);
        }

        if (FIPD_28_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd28.read(inputStream);
        }

        if (FIPD_29_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd29.read(inputStream);
        }

        if (FIPD_40_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd40.read(inputStream);
        }

        if (FIPD_42_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd42.read(inputStream);
        }

        if (FIPD_50_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd50.read(inputStream);
        }

        if (FIPD_51_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd51.read(inputStream);
        }

        if (FIPD_99_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
            return Fipd99.read(inputStream);
        }

        return null;
    }

    public final InputStream find(final String namePrefix) {
        if (!extracted || namePrefix == null) {
            return null;
        }

        InputStream inputStream = null;
        for (Iterator<String> i = handles.keySet().iterator(); inputStream == null && i.hasNext();) {
            String n = i.next();
            if (n != null && n.toUpperCase().startsWith(namePrefix.toUpperCase())) {
                inputStream = handles.get(n);
            }
        }

        return inputStream;
    }

    public final String[] list() {
        return handles.keySet().toArray(new String[handles.keySet().size()]);
    }
}
