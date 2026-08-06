# Performance

## Methodology and current full suite

- Baseline: normal Java solvers at pre-speed commit `ff4e78e190876e460dc289329d118aa1d5616fe5`.
- Harness: `src.FreshJvmBenchmark`, with separate JVM processes and an unmeasured true-cold launch before measured runs.
- Two independent baseline JVMs each reached the 180-second hard deadline before completing Day 17, so the pre-speed baseline is right-censored and no numeric end-to-end CI is claimed.
- Current values are means of 100 paired fresh-JVM runs with seeded random within-pair order and the cold pair excluded (the checked-in n=10 counterbalanced mode remains the quick reproduction default). `solver` sums the 25 `fullSolve` calls; `startup` ends at the child marker; `harness = main - solver`; `wall` is parent-observed process time.
- Correctness: all 50 independent solves equal all 25 combined solves; personal outputs match the previously verified speed branch, and alternate-account outputs match frozen independent references.
- Environment: OpenJDK 23.0.1, macOS arm64, 14 available processors.
- Reproduce current timing with `java -Daoc.data.dir=data -Daoc.benchmark.runs=10 -cp <classes> src.FreshJvmBenchmark` on an idle machine.

| Revision | wall | main | solver | startup | harness |
| --- | ---: | ---: | ---: | ---: | ---: |
| pre-speed baseline | >180000 | — | — | — | — |
| current | 186.264 | 160.671 | 129.385 | 22.281 | 31.286 |

Both pre-speed baseline processes timed out; the current cold process and all measured processes completed with stable answers.

Latest aggregate gate against the preceding PR tip (current minus previous, n=100):

| Metric | Previous | Current | Delta | 95% CI |
| --- | ---: | ---: | ---: | ---: |
| wall | 189.522 | 186.264 | -3.258 | [-4.562, -1.955] |
| main | 164.025 | 160.671 | -3.354 | [-4.511, -2.197] |
| solver | 131.956 | 129.385 | -2.572 | [-3.580, -1.564] |
| startup | 22.064 | 22.281 | 0.217 | [-0.126, 0.560] |
| harness | 32.068 | 31.286 | -0.782 | [-1.083, -0.482] |

## Day 01

One slurped buffer and a sign-aware digit scan fill two primitive arrays, sorted with the intrinsic int sort; a zipped pass computes distance and a two-pointer group pass computes similarity, with no per-token objects or arbitrary-precision arithmetic anywhere.

| Previous | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 9.275 | 3.802 | -5.474 | [-5.541, -5.406] |

## Day 02

Reports are parsed once into primitive arrays. An allocation-free monotonic scan validates either the full report or a logical single-index removal for the dampener.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 7.224 | 3.248 | -3.976 | [-4.208, -3.744] |

## Day 03

A single character scan recognizes `do`, `don't`, and strictly formed one-to-three-digit `mul` instructions while accumulating both enabled and unconditional totals without regex rescans.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 3.616 | 2.296 | -1.320 | [-1.545, -1.094] |

## Day 04

The combined solve parses once and scans the grid once, counting `XMAS`/`SAMX` in four undirected orientations and `MAS` crosses without constructing directional strings.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 17.743 | 1.840 | -15.903 | [-16.248, -15.559] |

## Day 05

Signed page labels are mapped to compact IDs once; updates are validated directly and repaired with a stable Kahn topological ordering over only their applicable rules.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 29.352 | 5.757 | -23.595 | [-23.980, -23.211] |

## Day 06

The combined solve walks the flattened grid once, then tests obstruction candidates through a precomputed turn-to-turn ray graph with timestamped states instead of repeated cell-by-cell simulations.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 166.445 | 6.955 | -159.489 | [-168.431, -150.548] |

## Day 07

Equations are parsed once and solved backward from the target through inverse addition, multiplication, and concatenation. Checked `long` arithmetic has an exact `BigInteger` fallback.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 12.524 | 3.579 | -8.945 | [-9.383, -8.507] |

## Day 08

The grid is parsed once into antenna coordinates; primitive boolean arrays record antinodes while GCD-reduced line steps cover rectangular bounds without repeated set allocation.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 5.156 | 1.373 | -3.784 | [-4.071, -3.496] |

## Day 09

Both parts operate on parsed file/gap runs. Part 1 uses arithmetic-series checksum updates, while Part 2 relocates whole files through normalized free runs without expanding the disk.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 133.504 | 8.827 | -124.678 | [-126.275, -123.080] |

## Day 10

The rectangular height grid is parsed once. A descending dynamic program shares primitive path counts for ratings and per-cell peak-reachability bitsets for scores.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 4.467 | 0.528 | -3.939 | [-4.116, -3.762] |

## Day 11

The finite blink universe is closed once into dense integer ids with primitive open addressing, then both blink totals run as dense count-vector updates over precomputed successor pairs — no hashing, boxing, or recursion in the hot phase, with the exact overflow fallback preserved.

| Previous | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 8.229 | 3.499 | -4.730 | [-4.839, -4.620] |

## Day 12

The garden is flattened into primitive arrays and each crop region is flood-filled once. Area, perimeter, and corner-derived side count are accumulated together for the combined solve.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 179.314 | 3.840 | -175.474 | [-188.296, -162.652] |

## Day 13

Each machine is parsed once and solved exactly for both prize offsets. Signed `BigInteger` Cramer arithmetic handles regular systems, with a bounded Diophantine solution for collinear machines.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 6.630 | 3.595 | -3.035 | [-3.387, -2.684] |

## Day 14

The combined solve parses once, computes Part 1 directly, and replaces time stepping with independent dense-axis residue scans joined by the Chinese remainder theorem.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 25.013 | 2.810 | -22.203 | [-22.978, -21.429] |

## Day 15

The map and moves are parsed once into primitive narrow and wide grids. Wide vertical pushes discover the complete box dependency closure with a stamped queue before moving it atomically.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 6.435 | 3.231 | -3.203 | [-3.553, -2.854] |

## Day 16

A cyclic 1001-bucket Dijkstra exploits the exact 1/1000 edge weights over primitive orientation states. A reverse walk over exact distance-preserving predecessors marks the Part 2 tile union without storing paths.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 5315.551 | 6.693 | -5308.857 | [-5339.758, -5277.957] |

## Day 17

Part 2 builds register A one octal digit at a time while matching output suffixes. The combined solver parses the actual registers and program once, then shares that immutable input across both parts.

| Baseline | Current | Delta | Evidence |
| ---: | ---: | ---: | --- |
| timeout | 4.370 | censored | Both 180-second full-suite baseline runs stopped in Day 17; all current runs completed. |

## Day 18

The input is parsed once into primitive earliest-fall cells for reusable BFS; combined Part 2 activates the grid in reverse with union-find, including duplicate coordinates.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 26.957 | 12.294 | -14.663 | [-17.182, -12.144] |

## Day 19

Patterns are stored in a fixed five-color trie; reverse reachability and counting DPs share the parsed designs, with checked `long` arithmetic promoting to `BigInteger` when needed.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 66.926 | 3.842 | -63.084 | [-63.838, -62.329] |

## Day 20

The combined solve computes one distance-to-end field, walks the uniquely decreasing race route, and scans each route position's radius-20 neighborhood once to count both cheat limits while handling dead-end branches.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 374.984 | 13.573 | -361.411 | [-377.714, -345.107] |

## Day 21

Primitive keypad transition-cost matrices are composed for two and twenty-five robots. A tiny weighted shortest-path search explores every valid route around each keypad hole without materializing command strings.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 11.386 | 3.074 | -8.312 | [-8.687, -7.936] |

## Day 22

Each buyer's 2,000-step secret stream is evaluated once for both parts. Four price changes use a dense base-19 key with per-buyer stamps, replacing sparse boxed sequence maps.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 495.191 | 30.461 | -464.729 | [-485.439, -444.020] |

## Day 23

The network is parsed once into an indexed `BitSet` graph. Triangle intersections and a pivoted, pruned Bron–Kerbosch maximum-clique search share that graph.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 30.680 | 3.989 | -26.690 | [-27.640, -25.740] |

## Day 24

The circuit is parsed once into indexed immutable gates, evaluated in arbitrary order through a consumer queue, and checked with precomputed operation masks; z bits use BigInteger rather than long-width destructive rescans.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 11.056 | 8.501 | -2.555 | [-3.124, -1.987] |

## Day 25

Schematics stream directly into base-6 lock and key profile frequencies. A five-dimensional prefix sum counts every componentwise-compatible key profile without materializing grids or checking every lock/key pair.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 4.834 | 1.494 | -3.340 | [-3.379, -3.302] |
