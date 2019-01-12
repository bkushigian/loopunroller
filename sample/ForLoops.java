/**
 * A class using for loops
 */
class ForLoops {
  public static void main(String[] args) {
    final int depth = Integer.parseInt(args[0]);
    final ForLoops fl = new ForLoops();
    System.out.println("sum:       " + fl.sum(depth));
    System.out.println("factorial: " + fl.factorial(depth));
    System.out.println("power:     " + fl.power(2, depth));
    System.out.println("isPrime:   " + fl.isPrime(depth));
    System.out.println("mnestedMult:" + fl.nestedMultiply(4,5));
  }

  int countToFive() {
    int x = 0;
    for (int i = 0; i < 5; ++i) {
      ++x;
    }
    return x;     // returns 5, or thereabouts
  }

  int nestedMultiply(int m, int n) {
    int total = 0;
    for (int i = 0; i < m; ++i) {
      for (int j = 0; j < n; ++j) {
        ++total;
      }
    }
    return total;
  }

  /**
   * compute the sum from 1 to n
   */
  int sum(int n) {
    int total = 0;
    for (int i = 1; i <= n; ++i) {
      total += i;
    }
    return total;
  }

  /**
   * compute n!
   */
  long factorial(int n) {
    long total = 1;
    for (long i = 1; i <= n; ++i)
      total *= i;
    return total;
  }

  /**
   * compute b^p
   */
  long power(int b, int p) {
    long total = 1;
    for (int i = 0; i < p; ++i) {
      total *= b;
    }
    return total;
  }

  /**
   * compute if n is prime
   */
  int isPrime(int n) {
    for (int i = 2; i < n; ++i) {
      if (n % i == 0) return 0;
    }
    return 1;
  }

}
