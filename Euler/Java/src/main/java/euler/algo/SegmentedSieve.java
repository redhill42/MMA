package euler.algo;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static euler.algo.Library.isqrt;

@SuppressWarnings("unused")
public class SegmentedSieve {
    private final long limit;
    private final int segmentSize;
    private final PrimeSieve initialSieve;

    public SegmentedSieve(long limit) {
        if (limit > Long.MAX_VALUE / 2)
            throw new IllegalArgumentException("limit is too large");

        this.limit = limit;
        this.segmentSize = (int)isqrt(limit);
        this.initialSieve = new PrimeSieve(segmentSize);
    }

    void fillSegment(BitSet segment, long lowest, long highest) {
        highest = Math.min(highest, lowest + segmentSize);

        segment.clear();
        segment.set(0, Math.min(segmentSize, (int)(highest - lowest + 1)));

        int crossto = (int)isqrt(highest) + 1;
        for (int p = 2; p > 0 && p <= crossto; p = initialSieve.nextPrime(p)) {
            for (long n = (lowest + p - 1) / p * p; n <= highest; n += p) {
                segment.clear((int)(n - lowest));
            }
        }
    }

    /**
     * A segment in the segmented prime sieve.
     */
    public class Segment implements Iterable<Long> {
        private final BitSet segment = new BitSet(segmentSize);
        private long lowest, highest;
        private long current;
        private int next;

        Segment(long lowest, long highest) {
            reset(lowest, highest);
        }

        /**
         * Returns the lowest bound in the segment.
         *
         * @return the lowest bound in the segment.
         */
        public long getLowest() {
            return lowest;
        }

        /**
         * Returns the highest bound in the segment.
         *
         * @return the highest bound in the segment.
         */
        public long getHighest() {
            return highest;
        }

        /**
         * Returns {@code true} if the segment has more primes.
         * (In other words, returns {@code true} if {@link #next}
         * would return a prime rather than -1.
         *
         * @return {@code true} if the iteration has more elements.
         */
        public boolean hasNext() {
            return next >= 0;
        }

        /**
         * Returns the next prime in the segment.
         *
         * @return the next prime in the segment.
         */
        public long next() {
            if (next < 0)
                return -1;
            long res = current + next;
            if ((next = segment.nextSetBit(next + 1)) < 0)
                fill();
            return res;
        }

        /**
         * Reset the segment to initial state.
         *
         * @return this segment
         */
        public Segment reset() {
            return reset(lowest, highest);
        }

        /**
         * Reset the segment to the given bounds.
         *
         * @param lowest the new lowest bound of the segment
         * @param highest the new highest bound of the segment
         * @return this segment
         */
        public Segment reset(long lowest, long highest) {
            if (lowest <= 0)
                lowest = 1;
            if (highest > limit)
                highest = limit;

            this.lowest = lowest;
            this.highest = highest;

            if (lowest > highest) {
                this.next = -1; // return empty segment
                return this;
            }

            if (lowest <= segmentSize) {
                segment.clear();

                // copy primes from initial sieve
                int p = initialSieve.nextPrime((int)lowest - 1);
                while (p > 0 && p <= highest) {
                    segment.set(p - 1); // 0-based in segment
                    p = initialSieve.nextPrime(p);
                }

                // fill segment if no primes found in the initial sieve
                current = 1;
                next = segment.nextSetBit(0);
            } else {
                // fill segment with the given range
                current = lowest - segmentSize;
                next = -1;
            }

            fill();
            return this;
        }

        private void fill() {
            while (next < 0 && current <= highest - segmentSize) {
                current += segmentSize;
                fillSegment(segment, current, highest);
                next = segment.nextSetBit(0);
            }
        }

        /**
         * Returns the number of primes in the segment.
         *
         * @return the number of primes in the segment.
         */
        public long cardinality() {
            long result = 0;
            while (next >= 0) {
                result += segment.cardinality();
                next = -1;
                fill();
            }
            return result;
        }

        @Override
        public Iterator<Long> iterator() {
            return new Iterator<Long>() {
                @Override
                public boolean hasNext() {
                    return Segment.this.hasNext();
                }

                @Override
                public Long next() {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    return Segment.this.next();
                }
            };
        }
    }

    public Segment segment() {
        return new Segment(1, limit);
    }

    public Segment segment(long lowest) {
        return segment(lowest, limit);
    }

    public Segment segment(long lowest, long highest) {
        return new Segment(lowest, highest);
    }

    public Segment[] partition() {
        int cores = Runtime.getRuntime().availableProcessors();
        long blocksize = Math.round((double)limit / segmentSize / cores) * segmentSize;
        if (blocksize == 0) {
            return new Segment[] {segment()};
        }

        int numblocks = (int)Math.min(cores, (limit + blocksize - 1) / blocksize);
        Segment[] blocks = new Segment[numblocks];
        long lowest = 1;

        for (int i = 0; i < numblocks - 1; i++) {
            long highest = lowest + blocksize;
            blocks[i] = new Segment(lowest, highest - 1);
            lowest = highest;
        }
        blocks[numblocks - 1] = new Segment(lowest, limit);

        return blocks;
    }
}
