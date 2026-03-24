package ca.bc.gov.farms.csv;

public class CSVParserException extends Exception {

    private static final long serialVersionUID = 1L;

    private int row;

    private int fileNum;

    public CSVParserException(int r, int f, Exception e) {
        super(e);
        this.row = r;
        this.fileNum = f;
    }

    public CSVParserException(int r, int f, String s) {
        super(s);
        this.row = r;
        this.fileNum = f;
    }

    public int getRowNumber() {
        return row;
    }

    public int getFileNumber() {
        return fileNum;
    }
}
