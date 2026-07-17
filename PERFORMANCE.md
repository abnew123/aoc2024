# Performance

## Methodology and current full suite

- Baseline: normal Java solvers at pre-speed commit `ff4e78e190876e460dc289329d118aa1d5616fe5`.
- Harness: `src.FreshJvmBenchmark`, with separate JVM processes and an unmeasured true-cold launch before measured runs.
- This enabling batch uses two measured candidate JVMs. Two independent baseline JVMs each reached the 180-second hard deadline before completing Day 17, so the baseline is right-censored and no numeric full-suite CI is claimed.
- Current values are two-run means in milliseconds. `solver` sums the 25 `fullSolve` calls; `startup` ends at the child marker; `harness = main - solver`; `wall` is parent-observed process time.
- Correctness: all 50 independent solves equal all 25 combined solves; personal outputs match the previously verified speed branch, and alternate-account outputs match frozen independent references.
- Environment: OpenJDK 23.0.1, macOS arm64, 14 available processors.
- Reproduce current timing with `java -Daoc.data.dir=data -Daoc.benchmark.runs=2 -cp <classes> src.FreshJvmBenchmark` on an idle machine.

| Revision | wall | main | solver | startup | harness |
| --- | ---: | ---: | ---: | ---: | ---: |
| pre-speed baseline | >180000 | — | — | — | — |
| current | 1539.881 | 1489.293 | 1454.367 | 30.073 | 34.926 |

Both baseline processes timed out; the cold and both measured candidate processes completed with stable answers.

## Day 01

Unchanged source. Current solver mean: 23.159 ms.

## Day 02

Unchanged source. Current solver mean: 6.094 ms.

## Day 03

Unchanged source. Current solver mean: 3.372 ms.

## Day 04

Unchanged source. Current solver mean: 17.300 ms.

## Day 05

Unchanged source. Current solver mean: 30.034 ms.

## Day 06

The combined solve walks the flattened grid once, then tests obstruction candidates through a precomputed turn-to-turn ray graph with timestamped states instead of repeated cell-by-cell simulations.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 166.445 | 6.955 | -159.489 | [-168.431, -150.548] |

## Day 07

Unchanged source. Current solver mean: 13.582 ms.

## Day 08

Unchanged source. Current solver mean: 5.167 ms.

## Day 09

Unchanged source. Current solver mean: 134.529 ms.

## Day 10

Unchanged source. Current solver mean: 2.641 ms.

## Day 11

Unchanged source. Current solver mean: 36.575 ms.

## Day 12

Unchanged source. Current solver mean: 161.582 ms.

## Day 13

Unchanged source. Current solver mean: 4.316 ms.

## Day 14

Unchanged source. Current solver mean: 17.260 ms.

## Day 15

Unchanged source. Current solver mean: 6.570 ms.

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

Unchanged source. Current solver mean: 66.111 ms.

## Day 20

Unchanged source. Current solver mean: 380.305 ms.

## Day 21

Unchanged source. Current solver mean: 10.794 ms.

## Day 22

Unchanged source. Current solver mean: 478.535 ms.

## Day 23

Unchanged source. Current solver mean: 26.612 ms.

## Day 24

The circuit is parsed once into indexed immutable gates, evaluated in arbitrary order through a consumer queue, and checked with precomputed operation masks; z bits use BigInteger rather than long-width destructive rescans.

| Baseline | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 11.056 | 8.501 | -2.555 | [-3.124, -1.987] |

## Day 25

Unchanged source. Current solver mean: 4.684 ms.
