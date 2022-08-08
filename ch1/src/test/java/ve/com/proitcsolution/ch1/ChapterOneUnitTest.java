package ve.com.proitcsolution.ch1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class ChapterOneUnitTest {

    static Pattern REGEX_LEADING_WHITESPACE = Pattern.compile("\\s+.*");
    static Predicate<String> REGEX_TRAILING_WHITESPACE = Pattern.compile(".*\\s+")
        .asMatchPredicate();
    static String LINEBREAK_MATCHER = "\\R";

    @Test
    void verifyTextBlock() {
        var textBlock = """
            squirrel \s
            pigeon \
            termite""";

        assertThat(textBlock).hasLineCount(2);
        assertThat(textBlock.split(LINEBREAK_MATCHER)).anyMatch(REGEX_TRAILING_WHITESPACE);
    }

    @Test
    void verifyPrimitive() {
        int number1 = Integer.parseInt("15"); // Primitive
        var number2 = Integer.valueOf("15");

        assertThat(number2.getClass().isPrimitive()).isFalse();
    }
}
