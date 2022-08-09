package ve.com.proitcsolution.ch1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class ChapterTwoUnitTest {

    @Test
    void verifyBitwiseNotOperator() {
        int number1 = 0b101; // 5
        int numberExpected = -1 * number1 - 1; // -6

        assertThat(~number1).isEqualTo(numberExpected);
    }
}
