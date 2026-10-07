package ca.bc.gov.srm.farm.report;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/** Renders the packaged COB using the application's connection, not a Studio data adapter. */
public class CobReportRenderer {
  static final String TEMPLATE = "/reports/BenefitNotice2023-postgresql.jrxml";
  private static final Logger LOG = LoggerFactory.getLogger(CobReportRenderer.class);
  private static volatile JasperReport compiledReport;

  public byte[] render(Connection connection, Integer scenarioId) throws JRException, IOException {
    if (connection == null || scenarioId == null || scenarioId <= 0) {
      throw new IllegalArgumentException("A database connection and positive COB scenario ID are required.");
    }

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("IN_SCENARIO_ID", scenarioId);
    parameters.put(JRParameter.REPORT_LOCALE, Locale.CANADA);
    parameters.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getTimeZone("America/Vancouver"));

    // Each request owns its print/virtualizer; only the immutable compiled design is shared.
    Path workDirectory = Files.createTempDirectory("farm-cob-");
    JRFileVirtualizer virtualizer = new JRFileVirtualizer(25, workDirectory.toString());
    parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
    try {
      JasperPrint print = JasperFillManager.fillReport(getCompiledReport(), parameters, connection);
      if (print.getPages().isEmpty()) {
        throw new JRException("No COB data found for scenario " + scenarioId
            + ". Check its claim and program-year configuration before generating a notice.");
      }
      return JasperExportManager.exportReportToPdf(print);
    } finally {
      virtualizer.cleanup();
      try {
        Files.deleteIfExists(workDirectory);
      } catch (IOException e) {
        LOG.warn("Could not remove COB temporary directory {}", workDirectory, e);
      }
    }
  }

  static JasperReport getCompiledReport() throws JRException, IOException {
    JasperReport report = compiledReport;
    if (report == null) {
      synchronized (CobReportRenderer.class) {
        report = compiledReport;
        if (report == null) {
          try (InputStream source = CobReportRenderer.class.getResourceAsStream(TEMPLATE)) {
            if (source == null) {
              throw new IOException("Missing packaged COB template: " + TEMPLATE);
            }
            JasperDesign design = JRXmlLoader.load(source);
            design.setProperty("net.sf.jasperreports.jdbc.query.timeout", "180");
            design.setProperty("net.sf.jasperreports.governor.max.pages.enabled", "true");
            design.setProperty("net.sf.jasperreports.governor.max.pages", "500");
            report = JasperCompileManager.compileReport(design);
            compiledReport = report;
          }
        }
      }
    }
    return report;
  }
}
