package ca.bc.gov.srm.farm.report;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;

/** No live database: compile the real template and exercise PDF export with JDBC test doubles. */
class CobReportRendererTest {
  @Test
  void compilesPackagedTemplateAndCachesOnlyTheDesign() throws Exception {
    JasperReport report = CobReportRenderer.getCompiledReport();
    assertSame(report, CobReportRenderer.getCompiledReport());
    assertEquals(18, report.getDatasets().length);
    assertEquals(Integer.class, Arrays.stream(report.getParameters())
        .filter(p -> p.getName().equals("IN_SCENARIO_ID")).findFirst().get().getValueClass());
  }

  @Test
  void allListsUseTheSelectedScenarioAndApplicationConnection() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    try (InputStream source = getClass().getResourceAsStream(CobReportRenderer.TEMPLATE)) {
      NodeList runs = factory.newDocumentBuilder().parse(source)
          .getElementsByTagNameNS("*", "datasetRun");
      assertEquals(18, runs.getLength());
      for (int i = 0; i < runs.getLength(); i++) {
        Element run = (Element) runs.item(i);
        assertEquals("$P{REPORT_CONNECTION}", run.getElementsByTagNameNS("*", "connectionExpression")
            .item(0).getTextContent().trim());
        boolean hasScenario = false;
        NodeList parameters = run.getElementsByTagNameNS("*", "datasetParameter");
        for (int j = 0; j < parameters.getLength(); j++) {
          Element parameter = (Element) parameters.item(j);
          if (parameter.getAttribute("name").equals("IN_SCENARIO_ID")) {
            hasScenario = true;
            assertEquals("$P{IN_SCENARIO_ID}", parameter.getTextContent().trim());
          }
        }
        assertTrue(hasScenario, run.getAttribute("subDataset") + " must not use a preview scenario");
      }
    }
  }

  @Test
  void exportsActualTemplateWithTheRequestedScenarioAndPortableFonts() throws Exception {
    JdbcFixture fixture = new JdbcFixture(true);
    byte[] pdf = new CobReportRenderer().render(fixture.connection(), 1081568);
    assertEquals("%PDF-", new String(pdf, 0, 5, StandardCharsets.US_ASCII));
    Files.createDirectories(Paths.get("target", "cob-test-output"));
    try (PDDocument document = PDDocument.load(pdf)) {
      String text = new PDFTextStripper().getText(document);
      assertTrue(text.contains("JASPER INTEGRATION TEST"));
      assertTrue(text.contains("2024 AgriStability"));
      assertTrue(text.contains("4375671"));
      assertTrue(text.contains("339,947.00"));
      assertTrue(text.contains("\u2713"), "The existing checkmark must survive UTF-8 loading and PDF export");
      assertTrue(text.contains("Combined Farm Participant Share"));
      assertTrue(document.getNumberOfPages() > 1);
      PDFRenderer renderer = new PDFRenderer(document);
      for (int page : new int[] {0, document.getNumberOfPages() - 1}) {
        ImageIO.write(renderer.renderImageWithDPI(page, 120), "png",
            Paths.get("target", "cob-test-output", "synthetic-cob-page-" + (page + 1) + ".png").toFile());
      }
      for (org.apache.pdfbox.pdmodel.PDPage page : document.getPages()) {
        for (org.apache.pdfbox.cos.COSName font : page.getResources().getFontNames()) {
          assertTrue(page.getResources().getFont(font).isEmbedded(), "PDF fonts must be portable");
        }
      }
    }
    assertFalse(fixture.parameters.isEmpty());
    assertEquals(1081568, fixture.parameters.get(0).get(1));
    assertTrue(fixture.parameters.stream().allMatch(p -> !p.containsValue(1126209)));
    assertFalse(fixture.closed, "The renderer must not close the application's connection");
    Files.createDirectories(Paths.get("target", "cob-test-output"));
    Files.write(Paths.get("target", "cob-test-output", "synthetic-cob.pdf"), pdf);
  }

  @Test
  void noMainRowIsAnErrorRatherThanAnEmptySavedPdf() throws Exception {
    JdbcFixture fixture = new JdbcFixture(false);
    JRException error = assertThrows(JRException.class,
        () -> new CobReportRenderer().render(fixture.connection(), 1081568));
    assertTrue(error.getMessage().contains("No COB data"));
    assertFalse(fixture.closed);
  }

  @Test
  void missingOrInvalidScenarioCannotFallBackToAPreviewDefault() {
    CobReportRenderer renderer = new CobReportRenderer();
    assertThrows(IllegalArgumentException.class, () -> renderer.render(null, 1081568));
    assertThrows(IllegalArgumentException.class, () -> renderer.render(new JdbcFixture(false).connection(), null));
    assertThrows(IllegalArgumentException.class, () -> renderer.render(new JdbcFixture(false).connection(), 0));
  }

  private static class JdbcFixture {
    private final boolean mainRow;
    private final List<Map<Integer, Object>> parameters = new ArrayList<>();
    private boolean closed;

    JdbcFixture(boolean mainRow) {
      this.mainRow = mainRow;
    }

    Connection connection() {
      return proxy(Connection.class, (object, method, args) -> {
        if (method.getName().equals("getMetaData")) {
          return proxy(DatabaseMetaData.class, (o, m, a) -> m.getName().equals("getDatabaseProductName")
              ? "PostgreSQL" : defaultValue(m.getReturnType()));
        }
        if (method.getName().equals("prepareStatement")) {
          boolean mainQuery = parameters.isEmpty();
          Map<Integer, Object> bindings = new HashMap<>();
          parameters.add(bindings);
          return proxy(PreparedStatement.class, (statement, operation, values) -> {
            if (operation.getName().equals("setInt") || operation.getName().equals("setObject")) {
              bindings.put((Integer) values[0], values[1]);
            }
            if (operation.getName().equals("executeQuery")) {
              return resultSet(mainQuery && mainRow);
            }
            return defaultValue(operation.getReturnType());
          });
        }
        if (method.getName().equals("close")) {
          closed = true;
        }
        if (method.getName().equals("commit") || method.getName().equals("rollback")) {
          fail("The renderer must not change transaction ownership");
        }
        return defaultValue(method.getReturnType());
      });
    }

    ResultSet resultSet(boolean hasMainRow) throws Exception {
      JRField[] fields = CobReportRenderer.getCompiledReport().getFields();
      Map<String, Object> row = new HashMap<>();
      for (JRField field : fields) {
        row.put(field.getName(), field.getValueClass() == BigDecimal.class ? BigDecimal.ZERO : null);
      }
      row.put("CORP_NAME", "JASPER INTEGRATION TEST");
      row.put("REGION", "Fraser Valley");
      row.put("CASH_MARGINS_IND", "Y");
      row.put("YEAR", new BigDecimal("2024"));
      row.put("PARTICIPANT_PIN", new BigDecimal("4375671"));
      row.put("COMBINED_FARM_NUMBER", new BigDecimal("13385"));
      row.put("APPLIED_BENEFIT_PERCENT", new BigDecimal("35.8"));
      row.put("BENEFIT_TOTAL", new BigDecimal("339947.00"));
      row.put("BENEFIT_AFTER_APPL_BENEFIT_PCT", new BigDecimal("300783.00"));
      row.put("BENEFIT_BEFORE_COMBIND_FRM_PCT", new BigDecimal("840177.04"));
      row.put("BNFT_AFTER_LATE_ENROL_PENALTY", new BigDecimal("840177.04"));
      row.put("ADJUSTED_REFERENCE_MARGIN", new BigDecimal("1289210.87"));
      row.put("PROGRAM_YEAR_MARGIN", new BigDecimal("-1285495.00"));
      row.put("TIER3_TRIGGER", new BigDecimal("902447.61"));
      ResultSetMetaData metadata = proxy(ResultSetMetaData.class, (o, m, a) -> {
        if (m.getName().equals("getColumnCount")) return fields.length;
        if (m.getName().equals("getColumnLabel") || m.getName().equals("getColumnName")) {
          return fields[(Integer) a[0] - 1].getName();
        }
        return defaultValue(m.getReturnType());
      });
      int[] position = {0};
      boolean[] wasNull = {false};
      return proxy(ResultSet.class, (o, m, a) -> {
        if (m.getName().equals("next")) return hasMainRow && position[0]++ == 0;
        if (m.getName().equals("getMetaData")) return metadata;
        if (m.getName().equals("wasNull")) return wasNull[0];
        if (m.getName().equals("getBigDecimal") || m.getName().equals("getString")
            || m.getName().equals("getTimestamp") || m.getName().equals("getDate")
            || m.getName().equals("getObject")) {
          String name = a[0] instanceof Integer ? fields[(Integer) a[0] - 1].getName() : (String) a[0];
          Object value = row.get(name);
          wasNull[0] = value == null;
          return value;
        }
        return defaultValue(m.getReturnType());
      });
    }
  }

  static <T> T proxy(Class<T> type, InvocationHandler handler) {
    return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, handler));
  }

  static Object defaultValue(Class<?> type) {
    if (type == boolean.class) return false;
    if (type == int.class) return 0;
    if (type == long.class) return 0L;
    return null;
  }
}
