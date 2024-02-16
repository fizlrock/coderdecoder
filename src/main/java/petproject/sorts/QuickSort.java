package petproject.sorts;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class QuickSort {

	public static void sort(int[] array) {
		sort(array, 0, array.length);
	}

	public static void sort(int[] array, int l, int r) {
		if (r - l < 2)
			return;
		int index = ThreadLocalRandom.current().nextInt(l, r);
		int X = array[index];
		int P = partition(X, array, l, r);

		if (P == r) {
			sort(array, l, P - 1);
		} else if (P == l + 1) {
			sort(array, P, r);
		} else {
			sort(array, P, r);
			sort(array, l, P - 1);
		}

	}

	public static int partition(int X, int[] array) {
		return partition(X, array, 0, array.length);
		
	}

	public static int partition(int X, int[] array, int l, int r) {
		// r);
		if (array.length == 0)
			throw new IllegalArgumentException("Пустой массив");

		int e = l, g = l;

		for (int n = l; n < r; n++) {
			if (array[n] == X) {
				swap(array, n, g);
				g++;
			} else if (array[n] < X) {
				swap(array, g, n);
				swap(array, g, e);
				e++;
				g++;
			}
		}

		return g;
	}

	/**
	 * Функция редактирует перданный массив, меняя элементы с индексами a и b
	 * местами
	 * 
	 * @param array - массив для редактированя
	 * @param a - индекс
	 * @param b - индекс
	 */
	private static void swap(int[] array, int a, int b) {
		int buf = array[a];
		array[a] = array[b];
		array[b] = buf;
	}

}
