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

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 24.543 | 3.670 | -20.873 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.028 [-0.086, 0.030].*


## Day 02

Reports are parsed once into primitive arrays. An allocation-free monotonic scan validates either the full report or a logical single-index removal for the dampener.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 7.224 | 1.556 | -5.668 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -5.153 [-5.206, -5.101].*

## Day 03

A single character scan recognizes `do`, `don't`, and strictly formed one-to-three-digit `mul` instructions while accumulating both enabled and unconditional totals without regex rescans.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 3.616 | 3.189 | -0.427 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 0.679 [0.632, 0.725].*

## Day 04

The combined solve parses once and scans the grid once, counting `XMAS`/`SAMX` in four undirected orientations and `MAS` crosses without constructing directional strings.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 17.743 | 3.100 | -14.643 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 1.217 [1.178, 1.257].*

## Day 05

Signed page labels are mapped to compact IDs once; updates are validated directly and repaired with a stable Kahn topological ordering over only their applicable rules.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 29.352 | 5.585 | -23.767 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 0.011 [-0.107, 0.129].*

## Day 06

The combined solve walks the flattened grid once, then tests obstruction candidates through a precomputed turn-to-turn ray graph with timestamped states instead of repeated cell-by-cell simulations.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 166.445 | 4.682 | -161.763 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 0.803 [0.726, 0.881].*

## Day 07

Equations are parsed once and solved backward from the target through inverse addition, multiplication, and concatenation. Checked `long` arithmetic has an exact `BigInteger` fallback.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 12.524 | 4.109 | -8.415 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 0.250 [0.194, 0.305].*

## Day 08

The grid is parsed once into antenna coordinates; primitive boolean arrays record antinodes while GCD-reduced line steps cover rectangular bounds without repeated set allocation.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 5.156 | 1.053 | -4.103 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.263 [-0.303, -0.223].*

## Day 09

Both parts operate on parsed file/gap runs. Part 1 uses arithmetic-series checksum updates, while Part 2 relocates whole files through normalized free runs without expanding the disk.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 133.504 | 4.446 | -129.058 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -3.964 [-4.021, -3.908].*

## Day 10

The rectangular height grid is parsed once. A descending dynamic program shares primitive path counts for ratings and per-cell peak-reachability bitsets for scores.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 4.467 | 0.527 | -3.940 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.029 [-0.045, -0.013].*

## Day 11

The finite blink universe is closed once into dense integer ids with primitive open addressing, then both blink totals run as dense count-vector updates over precomputed successor pairs — no hashing, boxing, or recursion in the hot phase, with the exact overflow fallback preserved.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 39.342 | 3.317 | -36.025 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.163 [-0.203, -0.124].*


## Day 12

The garden is flattened into primitive arrays and each crop region is flood-filled once. Area, perimeter, and corner-derived side count are accumulated together for the combined solve.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 179.314 | 4.509 | -174.805 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 0.537 [0.496, 0.577].*

## Day 13

Each machine is parsed once and solved exactly for both prize offsets. Signed `BigInteger` Cramer arithmetic handles regular systems, with a bounded Diophantine solution for collinear machines.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 6.630 | 4.776 | -1.854 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.189 [-0.242, -0.136].*

## Day 14

The combined solve parses once, computes Part 1 directly, and replaces time stepping with independent dense-axis residue scans joined by the Chinese remainder theorem.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 25.013 | 2.532 | -22.481 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.173 [-0.214, -0.132].*

## Day 15

The map and moves are parsed once into primitive narrow and wide grids. Wide vertical pushes discover the complete box dependency closure with a stamped queue before moving it atomically.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 6.435 | 3.383 | -3.052 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 0.210 [0.159, 0.262].*

## Day 16

A cyclic 1001-bucket Dijkstra exploits the exact 1/1000 edge weights over primitive orientation states. A reverse walk over exact distance-preserving predecessors marks the Part 2 tile union without storing paths.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 5315.551 | 7.069 | -5308.482 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 0.262 [0.146, 0.378].*

## Day 17

Part 2 builds register A one octal digit at a time while matching output suffixes. The combined solver parses the actual registers and program once, then shares that immutable input across both parts.

| Baseline | Current | Delta | Evidence |
| ---: | ---: | ---: | --- |
| timeout | 1.415 | censored | Both 180-second full-suite baseline runs stopped in Day 17; all current runs completed. |

*Current is the 2026-08-07 n=100 confirming run; the measured step versus the preceding tip (n=100) was -3.392 [-3.457, -3.326].*

## Day 18

The input is parsed once into primitive earliest-fall cells for reusable BFS; combined Part 2 activates the grid in reverse with union-find, including duplicate coordinates.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 26.957 | 4.750 | -22.207 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.593 [-0.689, -0.498].*

## Day 19

Patterns are stored in a fixed five-color trie; reverse reachability and counting DPs share the parsed designs, with checked `long` arithmetic promoting to `BigInteger` when needed.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 66.926 | 3.951 | -62.975 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was 0.580 [0.471, 0.689].*

## Day 20

The combined solve computes one distance-to-end field, walks the uniquely decreasing race route, and scans each route position's radius-20 neighborhood once to count both cheat limits while handling dead-end branches.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 374.984 | 13.349 | -361.635 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -2.478 [-3.022, -1.933].*

## Day 21

Primitive keypad transition-cost matrices are composed for two and twenty-five robots. A tiny weighted shortest-path search explores every valid route around each keypad hole without materializing command strings.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 11.386 | 2.748 | -8.638 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.275 [-0.304, -0.247].*

## Day 22

Each buyer's 2,000-step secret stream is evaluated once for both parts. Four price changes use a dense base-19 key with per-buyer stamps, replacing sparse boxed sequence maps.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 495.191 | 16.834 | -478.357 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -3.890 [-4.394, -3.386].*

## Day 23

The network is parsed once into an indexed `BitSet` graph. Triangle intersections and a pivoted, pruned Bron–Kerbosch maximum-clique search share that graph.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 30.680 | 4.139 | -26.541 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.630 [-0.722, -0.538].*

## Day 24

The circuit is parsed once into indexed immutable gates, evaluated in arbitrary order through a consumer queue, and checked with precomputed operation masks; z bits use BigInteger rather than long-width destructive rescans.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 11.056 | 2.025 | -9.031 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -2.731 [-2.809, -2.654].*

## Day 25

Schematics stream directly into base-6 lock and key profile frequencies. A five-dimensional prefix sum counts every componentwise-compatible key profile without materializing grids or checking every lock/key pair.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 4.834 | 1.531 | -3.303 | — |

*Baseline is the documented pre-speed measurement (commit `ff4e78e1`, n=10, at the 2026-07-18 freeze); Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -0.065 [-0.088, -0.043].*
