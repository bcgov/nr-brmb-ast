package ca.bc.gov.srm.farm.ui.domain.dataimport;

/**
 * Bean used by screen 210.
 */
public class FileLineMessage {

  /** DOCUMENT ME! */
  private String fileNumber;

  /** DOCUMENT ME! */
  private String lineNumber;

  /** DOCUMENT ME! */
  private String message;

  /**
   * @return  the fileNumber
   */
  public String getFileNumber() {
    return fileNumber;
  }

  /**
   * @param  fileNumber  the fileNumber to set
   */
  public void setFileNumber(String fileNumber) {
    this.fileNumber = fileNumber;
  }

  /**
   * @return  the lineNumber
   */
  public String getLineNumber() {
    return lineNumber;
  }

  /**
   * @param  lineNumber  the lineNumber to set
   */
  public void setLineNumber(String lineNumber) {
    this.lineNumber = lineNumber;
  }

  /**
   * @return  the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param  message  the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

}
