package ve.com.proitcsolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

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
      var storeProcedures =
          Files.readString(Path.of("src/test/resources/ch15jdbc/store_procedure_invoicedb.sql"));
      statement.execute(storeProcedures);

      assertThat(isResultSetCreate).isFalse();
      assertThat(isResultSetData).isFalse();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

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

  @Test
  void verifyProductWithIdZero() {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var statement = connection.prepareStatement("SELECT * FROM Product where Id = ?")) {
      statement.setInt(1, 0);
      var result = statement.executeQuery();
      result.next();
      var productName = result.getString("Name");

      assertThat(productName).isEqualTo("Iron Iron");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void verifyProductWithoutBindParameterThrowsException() {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var statement = connection.prepareStatement("SELECT * FROM Product where Id = ?")) {

      var thrown = catchThrowable(statement::executeQuery);

      assertThat(thrown).isInstanceOf(SQLException.class).hasMessageContaining("Parameter not set");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void verifyProductWithInvalidBindParameterThrowsException() {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL);
        var statement = connection.prepareStatement("SELECT * FROM Product where Id = ?")) {
      statement.setInt(1, 0);
      statement.setInt(2, 0);

      var thrown = catchThrowable(statement::executeQuery);

      assertThat(thrown)
          .isInstanceOf(SQLException.class)
          .hasMessageContaining("parameter index out of range");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void verifyStoreProcedures() throws SQLException {
    try (var connection = DriverManager.getConnection(JDBC_HSQLDB_URL)) {
      // store procedure without parameters
      try (var callableStatement = connection.prepareCall("{call read_t_names()}");
          var namesStartingWithTResulSet = callableStatement.executeQuery()) {

        while (namesStartingWithTResulSet.next()) {
          assertThat(namesStartingWithTResulSet.getString("Name")).startsWith("T");
        }
      }

      // store procedure with input parameter
      try (var callableStatement = connection.prepareCall("{call read_names_by_letter(?)}")) {
        callableStatement.setString("prefix", "C");
        var namesStartingWithCResulSet = callableStatement.executeQuery();

        while (namesStartingWithCResulSet.next()) {
          assertThat(namesStartingWithCResulSet.getString("Name")).startsWith("C");
        }
      }

      // store procedure with an out parameter
      try (var callableStatement = connection.prepareCall("{? = call magic_number(?)}")) {
        callableStatement.registerOutParameter(1, Types.INTEGER);
        callableStatement.execute();

        assertThat(callableStatement.getInt(1)).isEqualTo(42);
      }

      // store procedure with an inout parameter
      try (var callableStatement = connection.prepareCall("{call double_number(?)}")) {
        callableStatement.setInt(1, 5);
        callableStatement.registerOutParameter(1, Types.INTEGER);
        callableStatement.execute();

        assertThat(callableStatement.getInt(1)).isEqualTo(10);
      }
    }
  }
}
