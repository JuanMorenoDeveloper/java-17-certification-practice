package ve.com.proitcsolution;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class Ch3MakingDecisionsTest {

  @Test
  void verifySwitchExpression() {
    int fish = 5;
    int length = 12;
    var name =
        switch (fish) {
          case 1 -> "Goldfish";
          case 2 -> {
            yield "Trout";
          }
          case 3 -> {
            if (length > 10) yield "Blobfish";
            else yield "Green";
          }
          default -> "Swordfish";
        };

    assertThat(name).isEqualTo("Swordfish");
  }
}
