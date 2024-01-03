package ve.com.proitcsolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class Ch15JdbcTest {

  public static final String JDBC_HSQLDB_URL = "jdbc:hsqldb:mem:invoice";

  @BeforeEach
  void setup() throws IOException {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var statement = connection.createStatement()) {
      var create = Files.readString(Path.of("src/test/resources/ch15jdbc/create_invoicedb.sql"));
      boolean isResultSetCreate = statement.execute(create);
      var data = Files.readString(Path.of("src/test/resources/ch15jdbc/data_invoicedb.sql"));
      boolean isResultSetData = statement.execute(data);

      assertThat(isResultSetCreate).isFalse();
      assertThat(isResultSetData).isFalse();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Order(0)
  @Test
  void verifyProductCount() {
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

  @Order(1)
  @Test
  void verifyDeleteCustomer() {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var countStatement = connection.prepareStatement("SELECT count(*) FROM Customer");
        var initialResult = countStatement.executeQuery()) {

      initialResult.next();
      int initialCustomerCount = initialResult.getInt(1);

      var deleteStatement = connection.prepareStatement("DELETE FROM Customer WHERE id=49");
      int rowUpdated = deleteStatement.executeUpdate();

      var finalResult = countStatement.executeQuery();
      finalResult.next();
      int finalCustomerCount = finalResult.getInt(1);

      assertThat(rowUpdated).isEqualTo(1);
      assertThat(initialCustomerCount).isEqualTo(50);
      assertThat(finalCustomerCount).isEqualTo(49);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Order(2)
  @Test
  void verifyExecuteUpdateThrowsExceptionWithInvalidStatement() {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var countStatement = connection.prepareStatement("SELECT count(*) FROM Customer")) {

      var thrown = catchThrowable(countStatement::executeUpdate);

      assertThat(thrown)
          .isInstanceOf(SQLException.class)
          .hasMessage("statement does not generate a row count");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Order(3)
  @Test
  void verifyExecuteQueryThrowsExceptionWithInvalidStatement() {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var deleteStatement = connection.prepareStatement("DELETE FROM Customer where id=49")) {

      var thrown = catchThrowable(deleteStatement::executeQuery);

      assertThat(thrown)
          .isInstanceOf(SQLException.class)
          .hasMessage("statement does not generate a result set");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
