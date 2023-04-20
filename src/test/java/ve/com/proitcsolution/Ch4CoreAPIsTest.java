package ve.com.proitcsolution;

import static org.assertj.core.api.Assertions.assertThat;

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
}
