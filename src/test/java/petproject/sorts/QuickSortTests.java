package petproject.sorts;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class QuickSortTests {

	private static final Random rnd = new Random();
	private static final Logger log = Logger.getLogger(QuickSortTests.class.getName());

	private int[] getRandomArray(int size) {
		int[] array = new int[size];

		for (int i = 0; i < size; i++) {
			array[i] = rnd.nextInt(100);
		}

		return array;
	}

	private int getMin(int[] array) {
		if (array.length == 0)
			throw new IllegalArgumentException();
		int min = array[0];

		for (int u : array)
			if (u < min)
				min = u;

		return min;
	}

	private int getMax(int[] array) {
		if (array.length == 0)
			throw new IllegalArgumentException();
		int max = array[0];

		for (int u : array)
			if (u > max)
				max = u;

		return max;
	}

	@DisplayName("Проверка разбивки случайного массива")
	@RepeatedTest(100)
	void partitionTest() {
		var array = getRandomArray(10);
		int X = array[rnd.nextInt(10)];

		StringJoiner sj = new StringJoiner("\n");

		sj.add("Изначальный массив: " + Arrays.toString(array));
		sj.add("Разбивка по элементу: " + X);
		int p = QuickSort.partition(X, array);
		sj.add("Массив после разбивки: " + Arrays.toString(array));

		assertAll(
				"Сравнение левой части массива, <X\n" + sj.toString(),
				() -> {
					for (int i = 0; i < p; i++) {
						assertTrue(array[i] <= X, "Индекс: " + i + ". " + array[i] + "<" + X);
					}
				});
		assertAll(
				"Сравнение правой части массива, >=X\n" + sj.toString(),
				() -> {
					for (int i = p; i < array.length; i++) {
						assertTrue(array[i] >= X, "Индекс: " + i + ". " + array[i] + ">" + X);
					}
				});

	}

	@Test
	void emptyArrayPartitionTest() {
		var array = new int[0];
		assertThrows(
				IllegalArgumentException.class,
				() -> {
					QuickSort.partition(0, array);
				});
	}

	@Test
	void equalsElementPartitionTest() {
		// 1, 1, 1, 1, 1
		// E G
		var array = new int[] { 1, 1, 1, 1, 1 };
		int result = QuickSort.partition(1, array);
		assertEquals(5, result);
	}

	@RepeatedTest(1000)
	@Timeout(value = 2, unit = TimeUnit.SECONDS)
	void randomArraySort() {
		var array = getRandomArray(1000);
		var ethalon = array.clone();

		QuickSort.sort(array);
		Arrays.sort(ethalon);
		assertArrayEquals(ethalon, array);
	}
}
