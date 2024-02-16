package petproject.sorts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;

public class CoderDecoderTest {

  @Test
  void countLettersTest() {
    var result = ShannonFanoCode.countLetter("aaabbcd");

    Map<Character, Integer> expected = Map.of(
        'a', 3,
        'b', 2,
        'c', 1,
        'd', 1);

    assertEquals(expected, result);
  }

  @ParameterizedTest(name = "{index} ==> split '{0}' is {1}")
  @MethodSource()
  void shannonSpliterTest(List<Integer> appriority, int expected_index) {

   int index = ShannonFanoCode.splitSection(appriority);
    
    assertEquals(expected_index, index,
       String.format("expected %d but was %d in %s", expected_index, index, appriority));
  }

  private static Stream<Arguments> shannonSpliterTest() {
    return Stream.of(
        Arguments.of(Arrays.asList(1, 2, 3), 2),
        Arguments.of(Arrays.asList(1, 8), 1),
        Arguments.of(Arrays.asList(3, 3), 1),
        Arguments.of(Arrays.asList(1, 2, 3, 10), 3));
  }
}
