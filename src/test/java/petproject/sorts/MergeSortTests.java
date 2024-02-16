package petproject.sorts;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class MergeSortTests {
	private int[] getRandomArray(int size) {
		int[] array = new int[size];

		for (int i = 0; i < size; i++) {
			array[i] = ThreadLocalRandom.current().nextInt(100);
		}

		return array;
	}

	@Test
	void getMiddleTests() {
		assertEquals(0, MergeSort.getMiddle(0, 0));
		assertEquals(1, MergeSort.getMiddle(0, 2));
		assertEquals(3, MergeSort.getMiddle(2, 4));
		assertEquals(4, MergeSort.getMiddle(3, 6));
	}

	@RepeatedTest(1000)
	void randomArrayMerge() {
		int size = ThreadLocalRandom.current().nextInt(1, 1000);
		int[] a1 = getRandomArray(size);
		int[] a2 = getRandomArray(size);
		Arrays.sort(a1);
		Arrays.sort(a2);

		int[] a1a2 = ArrayUtils.addAll(a1, a2);
		int[] expected = a1a2.clone();
		Arrays.sort(expected);

		MergeSort.mergeArrays(a1a2, 0, size, size * 2);

		assertArrayEquals(expected, a1a2);

	}

	@Test
	void twoElementsMerge() {
		int[] t1 = { 4, 4 };
		int[] t1_e = t1.clone();
		MergeSort.mergeArrays(t1, 0, 1, 2);
		assertArrayEquals(t1_e, t1);

		int[] t2 = { 9, 1 };
		int[] t2_e = { 1, 9 };
		MergeSort.mergeArrays(t2, 0, 1, 2);
		assertArrayEquals(t2_e, t2);
	}

	@RepeatedTest(1000)
	@Timeout(value = 2, unit = TimeUnit.SECONDS)
	void randomArraySort() {
		var array = getRandomArray(10);
		var ethalon = array.clone();

		MergeSort.sort(array);
		Arrays.sort(ethalon);
		assertArrayEquals(ethalon, array);
	}

}
