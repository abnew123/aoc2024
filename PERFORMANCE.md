# Performance Notes

[`src/FreshJvmBenchmark.java`](src/FreshJvmBenchmark.java) is the authoritative fresh-JVM benchmark and answer-equivalence runner. The checked-in [`timing-output.txt`](timing-output.txt) is legacy `TimerRunner` output; it does not represent the current source or fresh-JVM methodology.

## Reproduce

Run from the repository root with the private inputs in `data/`:

```sh
mkdir -p /tmp/aoc2024-classes
javac -d /tmp/aoc2024-classes $(git ls-files '*.java')
java -cp /tmp/aoc2024-classes src.FreshJvmBenchmark --verify
java -cp /tmp/aoc2024-classes src.FreshJvmBenchmark
```

`--verify` runs all 50 parts independently, runs all 25 days through `fullSolve`, requires two non-null answers per day and exact pairwise equality, then hashes the UTF-8 answers with four-byte length framing. It has no embedded independent expected answers, so this mode checks consistency rather than proving correctness by itself. The resulting equivalence record is:

```text
VERIFY_OK solve_answers=50 full_solve_pairs=25 checksum=1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61
```

The default command repeats those consistency checks, launches one cold child JVM that is excluded from the summary, then launches 10 measured child JVMs strictly sequentially. Every child must emit the exact protocol version and expected checksum.

## Environment

- 2024 MacBook Pro
- `os.name=Mac OS X`; macOS 15.6, build 24G84
- `aarch64`, 14 processors
- OpenJDK 23.0.1

## Timing definitions

- **Wall**: parent time from immediately before launching the Java child through output drain and process exit.
- **Main**: child time from the first executable line of `main` through answer hashing, stopping immediately before the result record is formatted and emitted.
- **Solver**: the sum of the 25 timed `fullSolve` calls. Solver construction and outer input `Scanner` construction and closing are excluded; parsing and input consumption performed by `fullSolve` are included.
- **Startup**: parent time from child launch until the child's start marker is observed.
- **Harness**: child main time minus summed solver time, covering class/input setup, orchestration, and answer hashing.

These intervals are intentionally not additive. Child main timing begins before the start marker is emitted, so startup overlaps the beginning of main. Wall timing continues after main timing stops and includes result transport and JVM shutdown. “Cold” means a new JVM with no shared classes or JIT state; it does not claim cleared operating-system file caches.

## Pre-Day 20 fresh-JVM results

This retained snapshot measured the Day 16 candidate before the additional Day 20 change. The later Day 20 section reports the current branch result.

The excluded cold process was:

| Sample | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| cold | 279.812 | 232.526 | 202.416 | 28.519 | 30.111 |

The 10 measured fresh processes were:

| Sample | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| 1 | 277.780 | 235.066 | 204.416 | 23.896 | 30.650 |
| 2 | 277.602 | 235.176 | 204.789 | 23.588 | 30.387 |
| 3 | 278.212 | 235.067 | 203.554 | 24.399 | 31.512 |
| 4 | 273.159 | 230.672 | 201.448 | 23.680 | 29.224 |
| 5 | 275.170 | 231.017 | 200.579 | 25.435 | 30.437 |
| 6 | 277.490 | 234.489 | 204.248 | 24.594 | 30.241 |
| 7 | 271.186 | 227.947 | 198.544 | 24.585 | 29.403 |
| 8 | 278.665 | 235.172 | 204.504 | 24.694 | 30.668 |
| 9 | 278.251 | 235.133 | 204.364 | 24.389 | 30.768 |
| 10 | 272.699 | 230.216 | 200.673 | 23.846 | 29.543 |

| Metric | Mean (ms) | Median (ms) | Sample standard deviation (ms) |
| --- | ---: | ---: | ---: |
| Wall | 276.021 | 277.546 | 2.746 |
| Main | 232.995 | 234.777 | 2.737 |
| Solver | 202.712 | 203.901 | 2.209 |
| Startup | 24.310 | 24.394 | 0.566 |
| Harness | 30.283 | 30.412 | 0.708 |

## Alternating pristine/candidate comparison

The comparison compiled the identical `FreshJvmBenchmark` source with either the pristine `88e8388` solvers or the current solvers, then used the same Java executable, working directory, and `data/` inputs. It ran a cold pristine child followed by a cold candidate child; their wall/solver times were 276.545/203.135 ms and 269.145/195.346 ms, respectively. Ten measured pairs then ran strictly sequentially in counterbalanced order: odd pairs pristine then candidate (`B-C`), even pairs candidate then pristine (`C-B`).

| Pair | Order | Pristine wall (ms) | Candidate wall (ms) | Pristine solver (ms) | Candidate solver (ms) |
| ---: | :---: | ---: | ---: | ---: | ---: |
| 1 | B-C | 281.385 | 277.420 | 207.197 | 201.393 |
| 2 | C-B | 283.282 | 277.933 | 208.939 | 203.708 |
| 3 | B-C | 278.491 | 276.910 | 205.272 | 202.253 |
| 4 | C-B | 280.415 | 272.716 | 205.910 | 198.471 |
| 5 | B-C | 277.499 | 276.962 | 203.126 | 202.795 |
| 6 | C-B | 287.108 | 274.316 | 211.453 | 200.452 |
| 7 | B-C | 282.154 | 280.906 | 209.422 | 205.758 |
| 8 | C-B | 283.999 | 278.104 | 210.301 | 204.071 |
| 9 | B-C | 276.333 | 272.941 | 204.156 | 199.956 |
| 10 | C-B | 285.096 | 284.068 | 212.218 | 205.917 |

| Metric | Pristine mean (ms) | Candidate mean (ms) | Candidate change | Paired delta sample SD (ms) |
| --- | ---: | ---: | ---: | ---: |
| Wall | 281.576 | 277.228 | -4.348 ms (-1.54%) | 3.797 |
| Main | 239.021 | 233.555 | -5.466 ms (-2.29%) | 3.449 |
| Solver | 207.799 | 202.477 | -5.322 ms (-2.56%) | 2.848 |
| Startup | 24.762 | 24.906 | +0.144 ms | — |
| Harness | 31.222 | 31.077 | -0.145 ms | — |

For solver time, the approximate paired 95% confidence interval for candidate minus pristine is [-7.36, -3.28] ms. Day 16 is the code change under test: it retains the forward Dijkstra distances, starts from every optimally scored exit-direction state, and walks only predecessor states satisfying `distance[pred] + edgeCost == distance[current]`. Primitive state and cell markers produce the union of tiles on all optimal paths without a second reverse Dijkstra.

A separate Day 22 manual-parser/rolling-key experiment was rejected after it regressed the measured full run. Day 22 was restored exactly to `88e8388`, so that experiment is not included in these results.

## Day 20 ordered-track comparison

The prompt guarantees one route from `S` to `E`, but it does not rule out dead-end track branches. The full solve therefore computes one distance-to-end array, follows the uniquely decreasing route, and converts every reachable track cell's distance into progress toward the end. For a cheat starting at route step `i`, the saved time is exactly `targetProgress - i - ManhattanDistance`. This handles branch endpoints, evaluates both cheat radii in one radius-20 scan, and replaces the previous two BFS traversals plus two separate scans without adding an input assumption. The independent per-part `solve` path remains the general two-BFS implementation and serves as a separate correctness oracle.

Commit `d5bdcd3` and the final branch source were compiled into separate classpaths. Each classpath ran one excluded cold process and 10 measured fresh processes, all serially with the same Java executable, data, harness, and answer checksum.

| Sample | Baseline wall | Candidate wall | Baseline solver | Candidate solver |
| ---: | ---: | ---: | ---: | ---: |
| Cold | 275.419 | 270.803 | 199.012 | 192.871 |
| 1 | 272.907 | 273.840 | 197.946 | 197.734 |
| 2 | 273.975 | 275.744 | 200.520 | 202.227 |
| 3 | 280.558 | 273.618 | 206.640 | 197.480 |
| 4 | 278.818 | 283.514 | 202.698 | 200.961 |
| 5 | 278.274 | 275.240 | 203.842 | 198.805 |
| 6 | 274.199 | 267.041 | 203.891 | 192.539 |
| 7 | 276.837 | 267.330 | 203.532 | 195.858 |
| 8 | 275.356 | 265.047 | 201.161 | 191.458 |
| 9 | 272.234 | 269.503 | 202.435 | 193.148 |
| 10 | 276.686 | 262.900 | 201.915 | 188.838 |

| Metric | Baseline mean | Candidate mean | Change | Baseline SD | Candidate SD |
| --- | ---: | ---: | ---: | ---: | ---: |
| Wall | 275.984 | 271.378 | -4.607 ms (-1.67%) | 2.720 | 6.173 |
| Main | 232.678 | 227.439 | -5.239 ms (-2.25%) | 2.230 | 5.515 |
| Solver | 202.458 | 195.905 | -6.553 ms (-3.24%) | 2.328 | 4.326 |
| Startup | 24.869 | 25.843 | +0.973 ms | 0.897 | 1.643 |
| Harness | 30.219 | 31.534 | +1.314 ms | 0.670 | 1.350 |

The approximate unpaired 95% confidence interval for the solver difference is **[-9.89, -3.21] ms**, excluding zero. A preceding independent baseline set measured a 202.620 ms solver mean, consistent with the reported baseline.

## Direct solver factory

The authoritative fresh-JVM harness now constructs each speed solver through a direct switch-based factory instead of formatting a class name and using `Class.forName`, constructor lookup, and reflective instantiation. Solver construction remains before the outer input `Scanner` and outside the timed `fullSolve` call, so no work moved across the solver boundary.

The exact final factory and reflective `dccd8db` baseline first ran one cold child each, followed by 10 counterbalanced pairs of fresh child JVMs. Odd pairs ran reflection then factory (`B-C`); even pairs ran factory then reflection (`C-B`). Every process returned the established checksum. The cold harness values (`main - solver`) were 39.861 ms for reflection and 31.811 ms for the factory.

| Pair | Order | Reflection harness (ms) | Factory harness (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 33.379 | 30.346 | -3.033 |
| 2 | C-B | 34.629 | 30.717 | -3.912 |
| 3 | B-C | 33.806 | 33.655 | -0.150 |
| 4 | C-B | 34.122 | 31.427 | -2.696 |
| 5 | B-C | 35.112 | 31.762 | -3.351 |
| 6 | C-B | 35.005 | 31.655 | -3.349 |
| 7 | B-C | 34.192 | 31.713 | -2.479 |
| 8 | C-B | 34.227 | 32.873 | -1.354 |
| 9 | B-C | 35.136 | 32.059 | -3.077 |
| 10 | C-B | 32.540 | 32.860 | +0.319 |

The paired harness means were **34.215 ms reflection** and **31.907 ms factory**, a 2.308 ms reduction. The paired-delta sample standard deviation was 1.434 ms; using Student's t with 9 degrees of freedom gives an approximate 95% confidence interval of **[-3.33 ms, -1.28 ms]**.

Separate standard cold-plus-10 parent runs recorded the complete phase split. Their excluded cold processes were:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Reflection | 274.207 | 235.812 | 198.985 | 34.665 | 36.826 |
| Factory | 296.259 | 247.965 | 214.558 | 31.890 | 33.407 |

The 10-process means were:

| Metric | Reflection mean (ms) | Factory mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 283.460 | 290.115 | +6.655 |
| Main | 242.400 | 245.556 | +3.156 |
| Solver | 207.476 | 212.493 | +5.017 |
| Startup | 29.607 | 29.635 | +0.028 |
| Harness | 34.924 | 33.063 | **-1.862** |

The full phase run independently confirms the targeted harness reduction. Interactive machine load was explicitly nonuniform during this burst, so the wall, main, and solver movements are not treated as evidence for this harness-only change. All 50 independent answers and all 25 combined solves remain unchanged.

## Answer equivalence and correctness evidence

- The current 50-record length-framed checksum is exactly identical to the pre-change checksum from known-good pristine commit `88e8388`, and all 50 independent `solve` results match all 25 `fullSolve` pairs.
- Day 16 matches the official examples `7036 / 45` and `11048 / 64`.
- Day 16 also matched the pristine implementation on 40 deterministic generated rectangular mazes, with separate `solve` and combined `fullSolve` agreement.
- Day 20's ordered full solve matches its independent BFS solves on the personal input, the official sample at the 100-picosecond threshold, and a one-row corridor edge case.

## Historical July warm visual examples

The README per-part table and the examples below preserve earlier July warm measurements using `DayTemplate.timer`. They show useful solver-level history but are not directly comparable to the fresh-process results above.

Bars are scaled to combined part 1 + part 2 time. Lower is better.

### Day 7: Bridge Repair

The solver still searches left-to-right operator choices, but the optimized version parses into primitive arrays and prunes recursive branches without extra collections.

```text
Before   9.4 ms | #########
After    4.4 ms | ####
```

### Day 14: Restroom Redoubt

The solver still projects robot positions on the toroidal grid, but the optimized version keeps positions and velocities in arrays and avoids rebuilding intermediate objects during the search.

```text
Before  10.1 ms | ##########
After    7.4 ms | #######
```

### Day 16: Reindeer Maze

The July version flattened position/direction state into primitive arrays. The current change additionally replaces its reverse Dijkstra with the optimal-predecessor walk described above; the bars remain the historical July comparison.

```text
Before  17.0 ms | #################
After   10.3 ms | ##########
```

### Day 22: Monkey Market

The solver still evaluates each buyer's secret sequence, but it now stores four price changes as base-19 digits, shrinking the sequence arrays from 1,048,576 sparse slots to 130,321 dense slots.

```text
Before  31.4 ms | ###############################
After   30.7 ms | ###############################
```
