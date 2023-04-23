package ve.com.proitcsolution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.jupiter.api.Test;

class Ch4CoreAPIsTest {

  @Test
  void verifyStringMethods() {
    assertThat("\t test\u2000".strip()).isEqualTo("test"); // strip() support unicode
    assertThat("\t test ".stripLeading())
        .isEqualTo("test "); // stripLeading() removes spaces at the beginning
    assertThat("\t test ".stripTrailing())
        .isEqualTo("\t test"); // stripTrailing() removes spaces at the end
    assertThat("\t test\u2000".trim()).isEqualTo("test\u2000"); // trim() doesn't support unicode
    assertThat("test".indexOf("e")).isEqualTo(1);
    assertThat("test".indent(4)).isEqualTo("    test\n");
    assertThat("\t test".indent(-2)).isEqualTo("test\n");
  }

  @Test
  void verifyPeriodVsDuration() {
    var date = LocalDate.of(2022, 1, 20);
    var period = Period.ofDays(2); // Works with LocalDate, DateTime, ZonedDateTime
    assertThat(date.plus(period)).isEqualTo(LocalDate.of(2022, 1, 22));

    var time = LocalTime.of(6, 15);
    var duration = Duration.ofHours(2); // Works with Time, DateTime, ZonedDateTime
    assertThat(time.plus(duration)).isEqualTo(LocalTime.of(8, 15));
    assertThatThrownBy(() -> time.plus(period))
        .isInstanceOf(UnsupportedTemporalTypeException.class);
  }
}
