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

## Day 25 single-pass schematics

Day 25 now reads the outer `Scanner` once, traverses the resulting text with `String.lines()`, and accumulates column heights directly. It no longer stores every schematic line, allocates and fills a transposed two-dimensional grid, or uses the default `fullSolve` path that creates two more `Scanner` instances. Width and height are derived from each schematic rather than fixed at 5×7; prompt-valid schematics are still compatible exactly when every pair of filled-column counts is at most the schematic height.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `617af5c` baseline then candidate (`B-C`), and even pairs reversed the order (`C-B`).

| Pair | Order | Baseline Day 25 (ms) | Candidate Day 25 (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 12.263 | 8.840 | -3.423 |
| 2 | C-B | 12.353 | 8.282 | -4.071 |
| 3 | B-C | 12.011 | 8.817 | -3.193 |
| 4 | C-B | 12.218 | 8.676 | -3.542 |
| 5 | B-C | 11.907 | 7.744 | -4.163 |
| 6 | C-B | 12.648 | 8.191 | -4.457 |
| 7 | B-C | 12.260 | 8.238 | -4.023 |
| 8 | C-B | 12.965 | 8.163 | -4.801 |
| 9 | B-C | 11.857 | 7.492 | -4.366 |
| 10 | C-B | 12.088 | 7.368 | -4.721 |

The excluded cold values were 11.700 ms baseline and 8.780 ms candidate. The measured means were **12.257 ms baseline** and **8.181 ms candidate**, a **4.076 ms (33.3%) reduction**. The paired-delta sample standard deviation was 0.545 ms and the t(9) 95% confidence interval was **[-4.47 ms, -3.69 ms]**.

The exact final sources also ran through the authoritative whole-suite harness. A counterbalanced 10-pair child comparison measured 191.999 ms baseline and 192.270 ms candidate solver means; its +0.272 ms paired delta had a wide 95% confidence interval of [-2.48 ms, +3.02 ms]. This aggregate is explicitly inconclusive under the nonuniform interactive load and is not used to claim a whole-suite improvement.

Separate standard cold-plus-10 runs retained the complete phase split:

| Variant | Cold wall (ms) | Cold main (ms) | Cold solver (ms) | Cold startup (ms) | Cold harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 264.431 | 222.253 | 192.057 | 26.997 | 30.195 |
| Candidate | 267.245 | 223.050 | 192.405 | 25.733 | 30.645 |

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 264.802 | 265.635 | +0.834 |
| Main | 221.971 | 222.707 | +0.736 |
| Solver | 192.314 | 193.298 | +0.984 |
| Startup | 24.699 | 25.052 | +0.353 |
| Harness | 29.657 | 29.409 | -0.248 |

The accepted evidence is the isolated, fully paired Day 25 solver result; the complete phase table is retained transparently rather than interpreted through unrelated day-to-day load. All 50 independent answers and all 25 combined solves retain checksum `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`. In addition, 100 deterministic generated cases checked independently computed fit counts, varying widths and heights, LF and CRLF separators, optional trailing blanks, and inputs with no locks or no keys.

## Day 12 shared region traversal

Day 12's `fullSolve` previously parsed the garden and flood-filled every crop region twice: once for area×perimeter and again for area×side count. It now performs one parse and one flood fill, accumulating the already-computed perimeter and the corner-derived side count for each region. The per-part `solve` paths still request only their own metric and remain separate entry-point checks.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `7a4f8ae` two-traversal baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Two traversals (ms) | Shared traversal (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 10.090 | 6.725 | -3.365 |
| 2 | C-B | 9.576 | 7.110 | -2.466 |
| 3 | B-C | 9.958 | 6.626 | -3.332 |
| 4 | C-B | 10.030 | 6.908 | -3.122 |
| 5 | B-C | 10.190 | 6.696 | -3.493 |
| 6 | C-B | 10.165 | 7.032 | -3.133 |
| 7 | B-C | 10.349 | 6.708 | -3.642 |
| 8 | C-B | 9.838 | 6.575 | -3.263 |
| 9 | B-C | 10.027 | 6.648 | -3.379 |
| 10 | C-B | 10.227 | 6.730 | -3.497 |

The excluded cold values were 10.009 ms baseline and 6.735 ms candidate. The measured means were **10.045 ms baseline** and **6.776 ms candidate**, a **3.269 ms (32.5%) reduction**. The paired-delta sample standard deviation was 0.325 ms and the t(9) 95% confidence interval was **[-3.50 ms, -3.04 ms]**.

The authoritative whole-suite child comparison was directionally consistent but noisier: its counterbalanced 10-pair solver means were 192.599 ms baseline and 191.846 ms candidate, a -0.753 ms delta with a 95% confidence interval of [-2.76 ms, +1.26 ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 271.853 | 225.504 | 195.591 | 27.564 | 29.913 |
| Candidate | 264.460 | 219.700 | 189.702 | 25.743 | 29.997 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 269.339 | 267.243 | -2.096 |
| Main | 224.305 | 221.620 | -2.685 |
| Solver | 193.981 | 191.895 | -2.086 |
| Startup | 26.958 | 26.770 | -0.188 |
| Harness | 30.324 | 29.725 | -0.598 |

The accepted evidence is the isolated paired Day 12 interval; the whole-suite paired interval is explicitly inconclusive under interactive load, while the separate phase means are retained as the latest current-source run. All 50 independent answers and 25 combined solves retain the established checksum. The official `140 / 80` sample and 500 deterministic rectangular gardens also matched both independent solves and the exact pre-change implementation.

## Day 6 shared guard traversal

Day 6's default `fullSolve` previously parsed the grid twice and walked the unobstructed guard route once for each part. The combined path now parses once and counts every first-entered route cell while the existing part 2 traversal tests that cell as an obstruction candidate. The independent part 1 entry point retains its cheaper visited-cell-only walk, and part 2 retains the same obstacle-state simulation.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `11f77ad` two-parse baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Two parses/walks (ms) | Shared traversal (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 19.814 | 17.657 | -2.157 |
| 2 | C-B | 19.914 | 17.961 | -1.953 |
| 3 | B-C | 19.783 | 17.810 | -1.972 |
| 4 | C-B | 19.624 | 17.613 | -2.011 |
| 5 | B-C | 19.410 | 17.513 | -1.897 |
| 6 | C-B | 19.872 | 17.355 | -2.516 |
| 7 | B-C | 19.659 | 17.607 | -2.052 |
| 8 | C-B | 19.697 | 17.895 | -1.802 |
| 9 | B-C | 19.639 | 17.632 | -2.006 |
| 10 | C-B | 19.563 | 17.656 | -1.907 |

Table deltas and summary statistics use the unrounded nanosecond records rather than the displayed three-decimal values.

The excluded cold values were 19.783 ms baseline and 17.470 ms candidate. The measured means were **19.697 ms baseline** and **17.670 ms candidate**, a **2.027 ms (10.3%) reduction**. The paired-delta sample standard deviation was 0.197 ms and the t(9) 95% confidence interval was **[-2.168 ms, -1.887 ms]**.

The authoritative whole-suite child comparison was directionally consistent but noisier: its counterbalanced 10-pair solver means were 195.355 ms baseline and 194.570 ms candidate, a -0.785 ms delta with a 95% confidence interval of [-3.320 ms, +1.749 ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 281.547 | 232.660 | 199.043 | 33.739 | 33.618 |
| Candidate | 288.747 | 242.386 | 207.934 | 29.879 | 34.451 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 279.885 | 281.043 | +1.158 |
| Main | 234.810 | 235.135 | +0.326 |
| Solver | 201.404 | 201.051 | -0.353 |
| Startup | 29.031 | 30.214 | +1.183 |
| Harness | 33.406 | 34.085 | +0.679 |

The accepted evidence is the isolated paired Day 6 interval; the whole-suite paired interval is explicitly inconclusive under the user's nonuniform interactive load, and the complete phase split is retained transparently. All 50 independent answers and 25 combined solves retain checksum `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`. The official `41 / 6` sample, the immediate-exit `1 / 0` edge case, and 500 deterministic exit-guaranteed rectangular grids also matched separate solves, combined solves, and the exact pre-change implementation.

## Day 2 primitive report checks

Day 2 previously stored split strings for every report, reparsed each retained level for every possible dampener removal, and allocated a copied `String[]` plus a parsed `int[]` per candidate. Its default combined solve repeated the entire process. It now parses each nonblank report once into an `int[]`, checks a removal by skipping its index in the original array, and counts both parts in one input pass. Repeated spaces and tabs are accepted, retained sequences shorter than two levels are handled directly, and differences use `long` arithmetic rather than overflowing at extreme signed levels.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `7aa5968` string-copy baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | String copies (ms) | Primitive skip (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 13.577 | 7.932 | -5.645 |
| 2 | C-B | 14.096 | 8.595 | -5.501 |
| 3 | B-C | 13.542 | 7.904 | -5.638 |
| 4 | C-B | 14.318 | 7.915 | -6.403 |
| 5 | B-C | 13.569 | 8.109 | -5.460 |
| 6 | C-B | 13.860 | 8.470 | -5.391 |
| 7 | B-C | 13.699 | 10.454 | -3.245 |
| 8 | C-B | 13.419 | 8.708 | -4.711 |
| 9 | B-C | 14.125 | 7.746 | -6.379 |
| 10 | C-B | 13.849 | 8.668 | -5.181 |

Table deltas and summary statistics use the unrounded nanosecond records. The excluded cold values were 13.674 ms baseline and 8.218 ms candidate. The measured means were **13.805 ms baseline** and **8.450 ms candidate**, a **5.355 ms (38.8%) reduction**. The paired-delta sample standard deviation was 0.897 ms and the t(9) 95% confidence interval was **[-5.997 ms, -4.714 ms]**.

The authoritative whole-suite child comparison was directionally consistent but noisier: its counterbalanced 10-pair solver means were 215.125 ms baseline and 212.809 ms candidate, a -2.316 ms delta with a 95% confidence interval of [-5.438 ms, +0.806 ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 311.893 | 255.970 | 218.075 | 38.246 | 37.895 |
| Candidate | 292.537 | 247.557 | 212.372 | 40.492 | 35.185 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 305.973 | 296.638 | -9.334 |
| Main | 254.047 | 251.795 | -2.253 |
| Solver | 217.598 | 215.214 | -2.384 |
| Startup | 36.002 | 34.370 | -1.632 |
| Harness | 36.449 | 36.580 | +0.131 |

The accepted evidence is the isolated paired Day 2 interval; the whole-suite paired interval is explicitly inconclusive under the user's nonuniform interactive load, and the complete phase split is retained transparently. All 50 independent answers and 25 combined solves retain checksum `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`. The official `2 / 4` sample plus 19,539 short, mixed-whitespace, extreme-integer, and exhaustive generated reports matched an independent copy-and-remove reference; separate and combined entry points agreed on all 19,545 reports.

## Day 3 direct corrupted-memory parser

Day 3 previously concatenated the input twice, compiled multiplication and enable-range regexes repeatedly, and rescanned enabled substrings for part 2. It now concatenates once and recognizes `do()`, `don't()`, and prompt-valid `mul(a,b)` instructions in a single character pass, accumulating both answers together. Operands remain limited to the prompt's one-to-three digits, line boundaries are removed exactly as before, malformed instructions are skipped, and `long` totals avoid the old 32-bit sum overflow on sufficiently many valid instructions.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `2b922d9` regex baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Regex passes (ms) | Direct parser (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 9.996 | 5.848 | -4.149 |
| 2 | C-B | 10.985 | 7.160 | -3.826 |
| 3 | B-C | 10.666 | 6.835 | -3.830 |
| 4 | C-B | 10.321 | 6.060 | -4.260 |
| 5 | B-C | 10.990 | 6.349 | -4.641 |
| 6 | C-B | 10.201 | 6.581 | -3.620 |
| 7 | B-C | 10.714 | 6.198 | -4.516 |
| 8 | C-B | 9.829 | 6.063 | -3.767 |
| 9 | B-C | 10.491 | 6.795 | -3.696 |
| 10 | C-B | 10.544 | 6.682 | -3.862 |

Table deltas and summary statistics use the unrounded nanosecond records. The excluded cold values were 11.131 ms baseline and 6.918 ms candidate. The measured means were **10.474 ms baseline** and **6.457 ms candidate**, a **4.017 ms (38.4%) reduction**. The paired-delta sample standard deviation was 0.355 ms and the t(9) 95% confidence interval was **[-4.270 ms, -3.763 ms]**.

The authoritative whole-suite child comparison was directionally consistent but noisy: its counterbalanced 10-pair solver means were 204.001 ms baseline and 203.189 ms candidate, a -0.811 ms delta with a 95% confidence interval of [-5.998 ms, +4.375 ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 291.085 | 242.081 | 209.664 | 32.461 | 32.416 |
| Candidate | 274.193 | 230.578 | 200.010 | 26.683 | 30.567 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 274.840 | 271.837 | -3.003 |
| Main | 232.795 | 232.830 | +0.035 |
| Solver | 201.235 | 202.390 | +1.155 |
| Startup | 27.207 | 26.818 | -0.389 |
| Harness | 31.560 | 30.440 | -1.120 |

The accepted evidence is the isolated paired Day 3 interval; the whole-suite paired interval is explicitly inconclusive under nonuniform interactive load, and the phase split is retained transparently. All 50 independent answers and 25 combined solves retain checksum `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`. Both official examples passed; 300 deterministic prompt-valid/malformed streams with LF/CRLF and cross-line instructions matched the exact regex implementation; four-digit operands were rejected per the prompt; and 3,000 valid `mul(999,999)` instructions produced the correct `2994003000` long total.

## Day 4 shared word-search traversal

Day 4's default combined solve previously materialized the input, created two additional Scanners, and parsed the grid twice. It now parses once and accumulates straight XMAS/SAMX lines plus diagonal MAS crosses in one grid traversal. Straight-line checks are skipped unless the starting cell is `X` or `S`; standalone parts retain selective counting paths.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `3aeb72b` duplicate-parse baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Duplicate parse (ms) | Shared traversal (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 8.175 | 4.602 | -3.574 |
| 2 | C-B | 7.225 | 4.933 | -2.291 |
| 3 | B-C | 7.282 | 4.615 | -2.667 |
| 4 | C-B | 7.606 | 4.880 | -2.726 |
| 5 | B-C | 8.018 | 4.918 | -3.100 |
| 6 | C-B | 7.473 | 4.573 | -2.899 |
| 7 | B-C | 7.422 | 4.666 | -2.756 |
| 8 | C-B | 7.708 | 5.093 | -2.615 |
| 9 | B-C | 7.693 | 4.943 | -2.750 |
| 10 | C-B | 7.669 | 4.569 | -3.100 |

Table deltas and summary statistics use the unrounded nanosecond records. The excluded cold values were 7.360 ms baseline and 4.710 ms candidate. The measured means were **7.627 ms baseline** and **4.779 ms candidate**, a **2.848 ms (37.3%) reduction**. The paired-delta sample standard deviation was 0.347 ms and the t(9) 95% confidence interval was **[-3.096 ms, -2.599 ms]**.

The authoritative whole-suite counterbalanced comparison also showed a statistically clear solver reduction: its 10-pair means were 206.300 ms baseline and 200.395 ms candidate, a -5.906 ms delta with a 95% confidence interval of **[-9.683 ms, -2.128 ms]**. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 272.796 | 229.357 | 200.156 | 26.909 | 29.201 |
| Candidate | 279.665 | 235.854 | 206.129 | 26.983 | 29.725 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 268.421 | 270.748 | +2.326 |
| Main | 228.205 | 227.289 | -0.916 |
| Solver | 198.130 | 196.917 | -1.212 |
| Startup | 27.844 | 27.027 | -0.817 |
| Harness | 30.076 | 30.372 | +0.296 |

All 50 independent answers and 25 combined solves retain checksum `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`. The official sample returned `18 / 9`, and 1,000 deterministic rectangular grids ranging down to one row or column matched the exact pre-change implementation with separate/combined agreement and optional final newlines.

## Day 8 bounded primitive antinode tracing

Day 8 previously parsed the map twice for a combined solve, allocated `Coordinate` objects in hash sets, and stopped harmonic tracing after 51 iterations. It now parses once, marks both parts in flat primitive arrays, reduces each antenna-pair delta by its absolute GCD, and traces to the actual rectangular grid boundary. This removes the input-size assumption while avoiding duplicate parsing and allocation.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs against baseline `9c40120`.

| Pair | Fixed-cap baseline (ms) | Bounded primitive trace (ms) | Delta (ms) |
| ---: | ---: | ---: | ---: |
| 1 | 9.601 | 3.283 | -6.318 |
| 2 | 9.025 | 2.946 | -6.079 |
| 3 | 9.167 | 3.585 | -5.582 |
| 4 | 9.431 | 3.285 | -6.146 |
| 5 | 10.313 | 3.295 | -7.018 |
| 6 | 9.926 | 3.508 | -6.418 |
| 7 | 10.019 | 3.245 | -6.774 |
| 8 | 10.708 | 3.119 | -7.589 |
| 9 | 11.296 | 3.869 | -7.426 |
| 10 | 9.539 | 4.886 | -4.653 |

The excluded cold values were 9.320 ms baseline and 3.334 ms candidate. The measured means were **9.903 ms baseline** and **3.502 ms candidate**, a **6.400 ms (64.6%) reduction**. The paired-delta sample standard deviation was 0.875 ms and the t(9) 95% confidence interval was **[-7.026 ms, -5.775 ms]**.

The authoritative whole-suite counterbalanced comparison also showed a statistically clear solver reduction: its 10-pair means were 202.273 ms baseline and 197.228 ms candidate, a -5.045 ms delta with a 95% confidence interval of **[-7.354 ms, -2.736 ms]**. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 283.825 | 234.863 | 201.117 | 32.284 | 33.747 |
| Candidate | 272.541 | 225.584 | 193.159 | 30.382 | 32.426 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 283.076 | 278.101 | -4.975 |
| Main | 235.894 | 230.241 | -5.653 |
| Solver | 201.686 | 197.288 | -4.398 |
| Startup | 30.681 | 31.679 | +0.998 |
| Harness | 34.207 | 32.953 | -1.255 |

All 50 independent answers and 25 combined solves retain the established checksum. The official sample returned `14 / 34`; 500 deterministic rectangular grids matched the exact pre-change implementation; and a candidate-only 1-by-120 collinear case returned `1 / 120`, demonstrating that tracing no longer truncates beyond 51 steps.

## Day 19 shared five-color trie DP

Day 19's default combined solve previously copied the full input into two new Scanners, built the towel trie twice with boxed-character `HashMap` edges, and ran separate reachability and arrangement DPs. It now builds one fixed-five-color trie—the complete color alphabet given by the prompt—and updates independent boolean reachability and checked-`long` arrangement arrays together in one reverse pass per design. An overflowing design is recomputed with `BigInteger`, and the total promotes on overflow, so arbitrary prompt-valid arrangement counts remain exact without charging the personal input for big-number arithmetic.

The original measurements for this change overlapped leaked background AoC JVMs and are withdrawn. The clean recovery first verified through the OS process table that no AoC Java/Javac, benchmark, timing, or watchdog process was active. Every recovery Java/Javac invocation then ran in a tracked process group with a hard deadline and a final descendant check. An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs against baseline `3b9d0e2`.

| Pair | Order | Duplicate map tries/DPs (ms) | Shared primitive trie DP (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 11.794 | 6.945 | -4.849 |
| 2 | C-B | 11.965 | 7.319 | -4.646 |
| 3 | B-C | 12.071 | 6.873 | -5.198 |
| 4 | C-B | 12.631 | 6.886 | -5.746 |
| 5 | B-C | 11.938 | 6.678 | -5.260 |
| 6 | C-B | 12.165 | 6.812 | -5.353 |
| 7 | B-C | 12.187 | 6.763 | -5.424 |
| 8 | C-B | 12.078 | 6.781 | -5.298 |
| 9 | B-C | 12.096 | 6.755 | -5.341 |
| 10 | C-B | 11.777 | 6.869 | -4.908 |

The excluded cold values were 12.046 ms baseline and 6.724 ms candidate. The measured means were **12.070 ms baseline** and **6.868 ms candidate**, a **5.202 ms (43.1%) reduction**. The paired-delta sample standard deviation was 0.320 ms and the t(9) 95% confidence interval was **[-5.431 ms, -4.974 ms]**.

The authoritative whole-suite counterbalanced comparison also showed a statistically clear solver reduction: its 10-pair means were 187.637 ms baseline and 185.086 ms candidate, a -2.551 ms delta with a 95% confidence interval of **[-4.363 ms, -0.739 ms]**. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 259.673 | 215.623 | 187.142 | 27.501 | 28.481 |
| Candidate | 262.121 | 216.753 | 187.128 | 27.026 | 29.624 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 260.059 | 258.953 | -1.106 |
| Main | 215.927 | 214.686 | -1.241 |
| Solver | 187.742 | 185.811 | -1.932 |
| Startup | 25.750 | 26.258 | +0.508 |
| Harness | 28.185 | 28.875 | +0.691 |

All 50 independent answers and 25 combined solves retain the established checksum. The official sample returned `6 / 16`; 300 deterministic prompt-alphabet pattern/design sets matched both the exact pre-change implementation and an independent `startsWith` DP oracle; and a 93-character overflow-shaped design returned the exact value from an independent `BigInteger` oracle beyond `long` range.

## Day 1 shared exact sorted lists

The default combined path previously copied the full input into two new Scanners, parsed both lists twice into boxed `ArrayList<Integer>` values, built a boxed frequency map even for part 1, and sorted separate copies. It also subtracted as `int` before widening, so extreme location IDs could overflow before `Math.abs`. Day 1 now parses flexible-whitespace pairs once into exact `BigInteger` arrays, sorts each array once, sums exact pairwise distances, and computes similarity by multiplying the run counts of equal values in the two sorted lists.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call. One excluded cold pair was followed by 10 counterbalanced pairs of separate JVM processes:

| Pair | Order | Duplicate boxed parse (ms) | Shared exact sort (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 10.271 | 9.407 | -0.865 |
| 2 | C-B | 10.200 | 8.358 | -1.842 |
| 3 | B-C | 10.465 | 9.834 | -0.631 |
| 4 | C-B | 10.271 | 9.434 | -0.837 |
| 5 | B-C | 10.086 | 8.865 | -1.221 |
| 6 | C-B | 11.170 | 9.416 | -1.755 |
| 7 | B-C | 11.242 | 10.082 | -1.160 |
| 8 | C-B | 10.247 | 8.936 | -1.311 |
| 9 | B-C | 10.625 | 8.987 | -1.639 |
| 10 | C-B | 9.919 | 8.820 | -1.099 |

The excluded cold values were 9.988ms baseline and 9.912ms candidate. The measured means were **10.450ms baseline** and **9.214ms candidate**, a **1.236ms (11.8%) reduction**. The paired-delta sample standard deviation was 0.407ms and the t(9) 95% confidence interval was **[-1.527ms, -0.945ms]**.

The candidate's standard phase run used one excluded cold JVM followed by 10 independent measured JVMs. The cold wall/main/solver/startup/harness values were 265.401/222.182/191.371/27.148/30.811ms. The measured means were:

| Metric | Mean (ms) | Median (ms) | Sample SD (ms) |
| --- | ---: | ---: | ---: |
| Wall | 266.346 | 266.553 | 2.126 |
| Main | 222.895 | 222.854 | 1.592 |
| Solver | 192.033 | 191.990 | 1.702 |
| Startup | 27.944 | 27.478 | 1.567 |
| Harness | 30.862 | 30.946 | 0.514 |

All 50 independent answers and 25 combined solves retain checksum `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`. The official sample returned `11 / 31`; 400 deterministic signed datasets matched an independent `BigInteger` sort/frequency oracle; and candidate-only whitespace, blank-line, negative, and beyond-`long` cases remained exact. Separate and combined entry points agreed throughout.

## Day 13 exact shared claw-machine solve

The default combined path previously copied the input into two Scanners, parsed each machine twice with twelve regex splits, and used absolute determinants that could turn negative press counts into false solutions. It also omitted the prompt's at-most-100-press bound for part 1 and used a heuristic collinear case. Day 13 now parses the six arbitrary-size integers once, applies signed Cramer's rule when the button vectors are independent, solves the collinear linear Diophantine optimization exactly, enforces nonnegative presses and the inclusive part-one bound, and validates both original equations before charging tokens.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call. One excluded cold pair was followed by 10 counterbalanced pairs of separate JVM processes:

| Pair | Order | Duplicate regex solve (ms) | Shared exact solve (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 14.879 | 10.131 | -4.748 |
| 2 | C-B | 15.022 | 10.456 | -4.566 |
| 3 | B-C | 14.958 | 10.562 | -4.396 |
| 4 | C-B | 15.665 | 10.437 | -5.228 |
| 5 | B-C | 14.850 | 10.552 | -4.297 |
| 6 | C-B | 14.903 | 10.258 | -4.645 |
| 7 | B-C | 14.646 | 10.278 | -4.369 |
| 8 | C-B | 14.657 | 10.453 | -4.204 |
| 9 | B-C | 14.926 | 10.201 | -4.725 |
| 10 | C-B | 14.366 | 10.246 | -4.120 |

The excluded cold values were 14.841ms baseline and 10.220ms candidate. The measured means were **14.887ms baseline** and **10.357ms candidate**, a **4.530ms (30.4%) reduction**. The paired-delta sample standard deviation was 0.327ms and the t(9) 95% confidence interval was **[-4.763ms, -4.296ms]**.

The candidate's standard phase run used one excluded cold JVM followed by 10 independent measured JVMs. The cold wall/main/solver/startup/harness values were 263.436/216.720/188.595/27.927/28.124ms. The measured means were:

| Metric | Mean (ms) | Median (ms) | Sample SD (ms) |
| --- | ---: | ---: | ---: |
| Wall | 260.992 | 260.482 | 2.682 |
| Main | 216.485 | 215.469 | 2.710 |
| Solver | 187.805 | 186.880 | 2.693 |
| Startup | 26.560 | 26.589 | 0.635 |
| Harness | 28.679 | 28.433 | 0.468 |

All 50 independent answers and 25 combined solves retain checksum `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`. The official sample returned `480 / 875318608908`; 300 deterministic independent-vector machines matched a signed `BigInteger` Cramer oracle; and targeted cases covered the inclusive 100-press bound, negative unique solutions, gcd misses, both collinear cost slopes, and bound-constrained collinear optima. Separate and combined entry points agreed throughout.

## Clean cumulative recovery comparison

After the leaked background JVMs were removed, pristine commit `88e8388` and pre-Day-1 source commit `12f75b8` were compiled with the identical current benchmark, solver factory, and `DayTemplate`. The OS process table was checked immediately before measurement, all Java/Javac commands ran in tracked process groups with hard deadlines, and no other AoC JVM ran concurrently. One excluded cold pair preceded 10 counterbalanced pairs of full 25-day child JVMs.

| Pair | Order | Pristine solver (ms) | Current solver (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 204.557 | 187.765 | -16.792 |
| 2 | C-B | 201.712 | 187.299 | -14.413 |
| 3 | B-C | 206.864 | 187.509 | -19.355 |
| 4 | C-B | 202.375 | 186.124 | -16.251 |
| 5 | B-C | 210.778 | 181.757 | -29.021 |
| 6 | C-B | 205.526 | 184.231 | -21.295 |
| 7 | B-C | 205.533 | 187.667 | -17.866 |
| 8 | C-B | 213.408 | 185.715 | -27.693 |
| 9 | B-C | 212.106 | 187.131 | -24.975 |
| 10 | C-B | 211.122 | 187.380 | -23.742 |

The excluded cold solver values were 213.871 ms pristine and 183.691 ms current. The paired means were **207.398 ms pristine** and **186.258 ms current**, a **21.140 ms (10.19%) reduction**. The paired-delta sample standard deviation was 5.039 ms and the t(9) 95% confidence interval was **[-24.745 ms, -17.536 ms]**.

Separate standard cold-plus-10 runs retained the complete phase split. Their cold processes were:

| Revision | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Pristine | 281.626 | 233.997 | 204.670 | 29.095 | 29.327 |
| Current | 262.121 | 216.753 | 187.128 | 27.026 | 29.624 |

Their 10-process means were:

| Metric | Pristine mean (ms) | Current mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 282.723 | 258.953 | -23.770 |
| Main | 237.287 | 214.686 | -22.601 |
| Solver | 207.821 | 185.811 | -22.011 |
| Startup | 27.059 | 26.258 | -0.802 |
| Harness | 29.466 | 28.875 | -0.591 |

This clean cumulative result supersedes the earlier cross-session 2024 headline. All 50 independent answers and 25 combined solves retain checksum `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`.

## Answer equivalence and correctness evidence

- The current 50-record length-framed checksum is exactly identical to the pre-change checksum from known-good pristine commit `88e8388`, and all 50 independent `solve` results match all 25 `fullSolve` pairs.
- Day 16 matches the official examples `7036 / 45` and `11048 / 64`.
- Day 16 also matched the pristine implementation on 40 deterministic generated rectangular mazes, with separate `solve` and combined `fullSolve` agreement.
- Day 20's ordered full solve matches its independent BFS solves on the personal input, the official sample at the 100-picosecond threshold, and a one-row corridor edge case.
- Day 25 matches independently calculated fit counts on 100 deterministic generalized schematic inputs, including alternate line endings and dimensions; the repository's prompt-shaped 5×7 puzzle input retains the established checksum.
- Day 12 matches its separate solve entry points, the `140 / 80` official example, and the pre-change implementation on 500 deterministic rectangular gardens.
- Day 6 matches its separate solve entry points, the `41 / 6` official example, an immediate-exit edge case, and the pre-change implementation on 500 deterministic exit-guaranteed rectangular grids with and without final newlines.
- Day 2 matches its separate solve entry points, the `2 / 4` official example, and an independent reference across 19,545 reports including exhaustive short sequences, mixed whitespace, and signed-integer extremes.
- Day 3 matches both official examples and the pre-change regex implementation on 300 deterministic streams, including toggles, malformed instructions, line-ending variants, and cross-line tokens; prompt-valid totals use `long` accumulation.
- Day 4 matches the official `18 / 9` sample and the pre-change implementation on 1,000 deterministic rectangular grids, including tiny dimensions, borders, overlaps, and reverse words.
- Day 8 matches the official `14 / 34` sample and the pre-change implementation on 500 deterministic rectangular grids; a 1-by-120 collinear case verifies boundary-driven tracing beyond the old fixed cap.
- Day 19 matches the official `6 / 16` sample, the pre-change implementation, and an independent DP on 300 deterministic pattern/design sets plus an overflow-shaped reachable design.
- Day 1 matches the official `11 / 31` sample and an independent exact oracle on 400 deterministic datasets plus arbitrary-size and whitespace variants.
- Day 13 matches the official `480 / 875318608908` sample, a signed exact oracle on 300 generated machines, and targeted bounded and degenerate systems.

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
