package euler;

public class Problem78 {
	private final int[] p = new int[1000000];

	private int partition(int n) {
		int k = 4;
		int a = 2;
		int b = 1;
		int s = 1;
		int sum = 0;

		while (n >= a) {
	   		sum += s * (p[n - a] + p[n - b]);
			a += k + 1;
			b += k;
			k += 3;
			s = -s;
		}
		if (n >= b) {
			sum += s * p[n - b];
		}
		return p[n] = sum % 1000000;
    }

	public int solve() {
		p[0] = p[1] = 1;
		for (int n = 2; ; n++) {
			if (partition(n) == 0) {
				return n;
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(new Problem78().solve());
	}
}