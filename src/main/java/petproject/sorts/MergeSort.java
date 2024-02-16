package petproject.sorts;

import java.util.Arrays;

public class MergeSort {

	private static final int[] buffer = new int[2048];

	public static void sort(int[] array) {
		sort(array, 0, array.length);
	}

	public static void sort(int[] array, int l, int r) {

		String slice = Arrays.toString(Arrays.copyOfRange(array, l, r));
		// System.out.printf("Sorting %d to %d. Array: %s\n", l, r, slice);
		if (r - l == 1)
			return;

		int m = getMiddle(l, r);

		sort(array, l, m);
		sort(array, m, r);
		mergeArrays(array, l, m, r);
	}

	public static void mergeArrays(int[] array, int l, int m, int r) {

		for (int i = l; i < r; i++)
			buffer[i] = array[i];

		int a1 = l, a2 = m;
		for (int i = l; i < r; i++) {

			if (a1 < m && a2 < r) {
				if (buffer[a1] <= buffer[a2]) {
					array[i] = buffer[a1];
					a1++;
				} else {
					array[i] = buffer[a2];
					a2++;
				}
			} else if (a1 < m) {
				array[i] = buffer[a1];
				a1++;
			} else {
				array[i] = buffer[a2];
				a2++;
			}
		}
	}

	public static int getMiddle(int l, int r) {
		return (r - l) / 2 + l;
	}
}
