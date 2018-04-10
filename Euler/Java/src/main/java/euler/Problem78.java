package euler;

public class Problem78 {
    private final int modulo;
	private final int[] p;

    public Problem78(int modulo) {
        this.modulo = modulo;
        this.p = new int[modulo];
    }

    public int solve() {
        p[0] = p[1] = 1;
        for (int n = 2; ; n++) {
            if (partition(n) == 0) {
                return n;
            }
        }
    }

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

		return p[n] = sum % modulo;
    }

	public static void main(String[] args) {
        Problem78 solver = new Problem78(1_000_000);
		System.out.println(solver.solve());
	}
}