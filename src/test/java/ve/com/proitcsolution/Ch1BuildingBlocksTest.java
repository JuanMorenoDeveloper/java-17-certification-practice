package ve.com.proitcsolution;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class Ch1BuildingBlocksTest {

    private static final Predicate<String> REGEX_LEADING_WHITESPACE = Pattern.compile("\\s+.*")
        .asMatchPredicate();
    private static final Predicate<String> REGEX_TRAILING_WHITESPACE = Pattern.compile(".*\\s+")
        .asMatchPredicate();
    private static final String LINEBREAK_MATCHER = "\\R";

    @Test
    void verifyTextBlock() {
        var textBlock = """
            squirrel \s
            pigeon \
            termite""";
        var textBlock2 = """
            squirrel
              pigeon
            termite
            """;

        assertThat(textBlock).hasLineCount(2);
        assertThat(textBlock.split(LINEBREAK_MATCHER)).anyMatch(REGEX_TRAILING_WHITESPACE)
            .noneMatch(REGEX_LEADING_WHITESPACE);

        assertThat(textBlock2).hasLineCount(3);
        assertThat(textBlock2.split(LINEBREAK_MATCHER)).anyMatch(REGEX_LEADING_WHITESPACE)
            .noneMatch(REGEX_TRAILING_WHITESPACE);
    }

    @Test
    void verifyPrimitive() {
        int number1 = Integer.parseInt("15"); // Primitive, all .parse* methods return primitives.
        var number2 = Integer.valueOf("15");

        assertThat(number1).isEqualTo(15);
        assertThat(number2.getClass().isPrimitive()).isFalse();
    }
}
