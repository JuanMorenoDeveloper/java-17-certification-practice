package ve.com.proitcsolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class Ch15JdbcTest {

  public static final String JDBC_HSQLDB_URL = "jdbc:hsqldb:mem:invoice";

  @BeforeEach
  void setup() throws IOException {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var statement = connection.createStatement()) {
      var create = Files.readString(Path.of("src/test/resources/ch15jdbc/create_invoicedb.sql"));
      statement.execute(create);
      var data = Files.readString(Path.of("src/test/resources/ch15jdbc/data_invoicedb.sql"));
      statement.execute(data);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void verifyCount() {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var statement = connection.prepareStatement("SELECT count(*) FROM Product");
        var result = statement.executeQuery()) {

      result.next();
      int productCount = result.getInt(1);

      assertThat(productCount).isEqualTo(50);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
