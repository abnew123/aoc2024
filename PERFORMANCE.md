# Performance

## Methodology and current full suite

- Baseline: normal Java solvers at pre-speed commit `ff4e78e190876e460dc289329d118aa1d5616fe5`.
- Harness: `src.FreshJvmBenchmark`, with separate JVM processes and an unmeasured true-cold launch before measured runs.
- Two independent baseline JVMs each reached the 180-second hard deadline before completing Day 17, so the pre-speed baseline is right-censored and no numeric end-to-end CI is claimed.
- Current values are ten-run means in milliseconds. `solver` sums the 25 `fullSolve` calls; `startup` ends at the child marker; `harness = main - solver`; `wall` is parent-observed process time.
- Correctness: all 50 independent solves equal all 25 combined solves; personal outputs match the previously verified speed branch, and alternate-account outputs match frozen independent references.
- Environment: OpenJDK 23.0.1, macOS arm64, 14 available processors.
- Reproduce current timing with `java -Daoc.data.dir=data -Daoc.benchmark.runs=10 -cp <classes> src.FreshJvmBenchmark` on an idle machine.

| Revision | wall | main | solver | startup | harness |
| --- | ---: | ---: | ---: | ---: | ---: |
| pre-speed baseline | >180000 | — | — | — | — |
| current | 1345.530 | 1297.073 | 1264.124 | 29.041 | 32.949 |

Both baseline processes timed out; the cold and both measured candidate processes completed with stable answers.

## Day 01

Unchanged source. Current solver mean: 23.003 ms.

## Day 02

Unchanged source. Current solver mean: 6.056 ms.

## Day 03

Unchanged source. Current solver mean: 3.654 ms.

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

Unchanged source. Current solver mean: 14.435 ms.

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

Unchanged source. Current solver mean: 3.808 ms.

## Day 11

Unchanged source. Current solver mean: 39.943 ms.

## Day 12

Unchanged source. Current solver mean: 167.302 ms.

## Day 13

Unchanged source. Current solver mean: 3.783 ms.

## Day 14

Unchanged source. Current solver mean: 18.827 ms.

## Day 15

The map and moves are parsed once into primitive narrow and wide grids. Wide vertical pushes discover the complete box dependency closure with a stamped queue before moving it atomically.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 6.435 | 3.231 | -3.203 | [-3.553, -2.854] |

## Day 16

A primitive orientation-state Dijkstra computes Part 1; a reverse walk over exact distance-preserving predecessors marks the Part 2 tile union, replacing path-string enumeration.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 5387.412 | 11.098 | -5376.314 | [-5413.581, -5339.047] |

## Day 17

Part 2 builds register A one octal digit at a time while matching output suffixes, and the VM parses the actual initial registers instead of using a hardcoded A and two brute-force chunks.

| Baseline | Current | Delta | Evidence |
| ---: | ---: | ---: | --- |
| timeout | 6.266 | censored | Both 180-second full-suite baseline runs stopped in Day 17; both current day runs completed. |

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

Unchanged source. Current solver mean: 380.253 ms.

## Day 21

Unchanged source. Current solver mean: 11.091 ms.

## Day 22

Unchanged source. Current solver mean: 508.685 ms.

## Day 23

Unchanged source. Current solver mean: 28.284 ms.

## Day 24

The circuit is parsed once into indexed immutable gates, evaluated in arbitrary order through a consumer queue, and checked with precomputed operation masks; z bits use BigInteger rather than long-width destructive rescans.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 11.056 | 8.501 | -2.555 | [-3.124, -1.987] |

## Day 25

Unchanged source. Current solver mean: 4.585 ms.
