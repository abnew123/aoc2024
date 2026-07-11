2024 AoC repo. For more detailed thoughts about the problems, see https://abnew123.substack.com/

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=abnew123_aoc2024&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=abnew123_aoc2024)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=abnew123_aoc2024&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=abnew123_aoc2024)

<!-- AOC TILES BEGIN -->
<h1 align="center">
  2024 - 50 ⭐ - Java
</h1>
<a href="src/solutions/Day01.java">
  <img src=".aoc_tiles/tiles/2024/01.png" width="161px">
</a>
<a href="src/solutions/Day02.java">
  <img src=".aoc_tiles/tiles/2024/02.png" width="161px">
</a>
<a href="src/solutions/Day03.java">
  <img src=".aoc_tiles/tiles/2024/03.png" width="161px">
</a>
<a href="src/solutions/Day04.java">
  <img src=".aoc_tiles/tiles/2024/04.png" width="161px">
</a>
<a href="src/solutions/Day05.java">
  <img src=".aoc_tiles/tiles/2024/05.png" width="161px">
</a>
<a href="src/solutions/Day06.java">
  <img src=".aoc_tiles/tiles/2024/06.png" width="161px">
</a>
<a href="src/solutions/Day07.java">
  <img src=".aoc_tiles/tiles/2024/07.png" width="161px">
</a>
<a href="src/solutions/Day08.java">
  <img src=".aoc_tiles/tiles/2024/08.png" width="161px">
</a>
<a href="src/solutions/Day09.java">
  <img src=".aoc_tiles/tiles/2024/09.png" width="161px">
</a>
<a href="src/solutions/Day10.java">
  <img src=".aoc_tiles/tiles/2024/10.png" width="161px">
</a>
<a href="src/solutions/Day11.java">
  <img src=".aoc_tiles/tiles/2024/11.png" width="161px">
</a>
<a href="src/solutions/Day12.java">
  <img src=".aoc_tiles/tiles/2024/12.png" width="161px">
</a>
<a href="src/solutions/Day13.java">
  <img src=".aoc_tiles/tiles/2024/13.png" width="161px">
</a>
<a href="src/solutions/Day14.java">
  <img src=".aoc_tiles/tiles/2024/14.png" width="161px">
</a>
<a href="src/solutions/Day15.java">
  <img src=".aoc_tiles/tiles/2024/15.png" width="161px">
</a>
<a href="src/solutions/Day16.java">
  <img src=".aoc_tiles/tiles/2024/16.png" width="161px">
</a>
<a href="src/solutions/Day17.java">
  <img src=".aoc_tiles/tiles/2024/17.png" width="161px">
</a>
<a href="src/solutions/Day18.java">
  <img src=".aoc_tiles/tiles/2024/18.png" width="161px">
</a>
<a href="src/solutions/Day19.java">
  <img src=".aoc_tiles/tiles/2024/19.png" width="161px">
</a>
<a href="src/solutions/Day20.java">
  <img src=".aoc_tiles/tiles/2024/20.png" width="161px">
</a>
<a href="src/solutions/Day21.java">
  <img src=".aoc_tiles/tiles/2024/21.png" width="161px">
</a>
<a href="src/solutions/Day22.java">
  <img src=".aoc_tiles/tiles/2024/22.png" width="161px">
</a>
<a href="src/solutions/Day23.java">
  <img src=".aoc_tiles/tiles/2024/23.png" width="161px">
</a>
<a href="src/solutions/Day24.java">
  <img src=".aoc_tiles/tiles/2024/24.png" width="161px">
</a>
<a href="src/solutions/Day25.java">
  <img src=".aoc_tiles/tiles/2024/25.png" width="161px">
</a>
<!-- AOC TILES END -->

## Performance

The tracked [`FreshJvmBenchmark`](src/FreshJvmBenchmark.java) is the authoritative fresh-JVM benchmark and answer-consistency runner. It checks all 50 independent `solve` results against all 25 `fullSolve` pairs, computes a deterministic length-framed SHA-256 checksum, and launches one excluded cold JVM followed by 10 strictly sequential measured JVMs. It does not contain independent expected answers. [`timing-output.txt`](timing-output.txt) is retained as legacy output and is not a current benchmark source.

On a 2024 MacBook Pro running macOS 15.6 (build 24G84), OpenJDK 23.0.1, `aarch64`, and 14 processors, the latest current-source 10-process means are 296.638 ms wall time and 215.214 ms summed solver time under explicitly nonuniform interactive load. An earlier alternating comparison against pristine commit `88e8388` measured solver time improving from 207.799 ms to 202.477 ms (-2.56%); the paired 95% confidence interval for that solver delta is approximately [-7.36, -3.28] ms.

Day 16 produces the same answers with one forward Dijkstra followed by a reverse walk over optimal predecessor states, eliminating its second Dijkstra. Correctness evidence combines exact pre/post identity of the 50-record checksum against known-good pristine commit `88e8388`, both official examples, and differential tests against the pristine implementation on generated rectangular mazes. See the [performance notes](PERFORMANCE.md) for raw samples, metric definitions, validation, and the complete comparison.

Day 25 parses schematic column heights in one pass and bypasses the default duplicate-`Scanner` combined-solve path. A counterbalanced isolated fresh-process comparison reduced its mean from 12.257 ms to 8.181 ms (-33.3%), with a paired 95% confidence interval of [-4.47, -3.69] ms; the whole-suite comparison remained inconclusive under interactive load.

Day 12 now computes perimeter and side prices from one region traversal. Its counterbalanced isolated mean fell from 10.045 ms to 6.776 ms (-32.5%), with a paired 95% confidence interval of [-3.50, -3.04] ms; the whole-suite paired interval remained inconclusive, although its point estimate favored the candidate.

Day 6 now parses once and derives the visited-cell count while walking the same guard route used to test obstruction candidates. Its counterbalanced isolated `fullSolve` mean fell from 19.697 ms to 17.670 ms (-10.3%), with a paired 95% confidence interval of [-2.168, -1.887] ms; the whole-suite paired interval remained inconclusive under interactive load.

Day 2 now parses every report once into primitive levels and tests dampener candidates by skipping an index instead of copying and reparsing arrays. Its counterbalanced isolated `fullSolve` mean fell from 13.805 ms to 8.450 ms (-38.8%), with a paired 95% confidence interval of [-5.997, -4.714] ms; the whole-suite paired interval remained inconclusive under interactive load.

Run from the repository root with the private inputs in `data/`:

```sh
mkdir -p /tmp/aoc2024-classes
javac -d /tmp/aoc2024-classes $(git ls-files '*.java')
java -cp /tmp/aoc2024-classes src.FreshJvmBenchmark --verify
java -cp /tmp/aoc2024-classes src.FreshJvmBenchmark
```

The answer-equivalence checksum for this revision is `1e220b27c702727a86e8e599b50487350230b8b33794c16f8f987759a4215e61`.

### Historical July warm per-part timings

The table below preserves the July warm 10-run averages recorded with the older `DayTemplate.timer` convention. These values remain useful historical context for individual solvers, but they are not directly comparable to the fresh-process results above.

| Day | Problem | Solution | Part 1 (ms) | Part 2 (ms) |
| --- | --- | --- |------------:|------------:|
| 1 | [Historian Hysteria](https://adventofcode.com/2024/day/1) | [Source](src/solutions/Day01.java) | 1.996 | 1.674 |
| 2 | [Red-Nosed Reports](https://adventofcode.com/2024/day/2) | [Source](src/solutions/Day02.java) | 1.888 | 2.953 |
| 3 | [Mull It Over](https://adventofcode.com/2024/day/3) | [Source](src/solutions/Day03.java) | 1.914 | 2.226 |
| 4 | [Ceres Search](https://adventofcode.com/2024/day/4) | [Source](src/solutions/Day04.java) | 1.793 | 1.304 |
| 5 | [Print Queue](https://adventofcode.com/2024/day/5) | [Source](src/solutions/Day05.java) | 2.794 | 3.055 |
| 6 | [Guard Gallivant](https://adventofcode.com/2024/day/6) | [Source](src/solutions/Day06.java) | 1.408 | 10.201 |
| 7 | [Bridge Repair](https://adventofcode.com/2024/day/7) | [Source](src/solutions/Day07.java) | 2.142 | 2.216 |
| 8 | [Resonant Collinearity](https://adventofcode.com/2024/day/8) | [Source](src/solutions/Day08.java) | 0.913 | 3.578 |
| 9 | [Disk Fragmenter](https://adventofcode.com/2024/day/9) | [Source](src/solutions/Day09.java) | 2.860 | 3.263 |
| 10 | [Hoof It](https://adventofcode.com/2024/day/10) | [Source](src/solutions/Day10.java) | 1.514 | 1.344 |
| 11 | [Plutonian Pebbles](https://adventofcode.com/2024/day/11) | [Source](src/solutions/Day11.java) | 0.488 | 5.716 |
| 12 | [Garden Groups](https://adventofcode.com/2024/day/12) | [Source](src/solutions/Day12.java) | 1.703 | 2.575 |
| 13 | [Claw Contraption](https://adventofcode.com/2024/day/13) | [Source](src/solutions/Day13.java) | 2.524 | 2.572 |
| 14 | [Restroom Redoubt](https://adventofcode.com/2024/day/14) | [Source](src/solutions/Day14.java) | 2.396 | 4.969 |
| 15 | [Warehouse Woes](https://adventofcode.com/2024/day/15) | [Source](src/solutions/Day15.java) | 1.724 | 2.872 |
| 16 | [Reindeer Maze](https://adventofcode.com/2024/day/16) | [Source](src/solutions/Day16.java) | 3.931 | 6.352 |
| 17 | [Chronospatial Computer](https://adventofcode.com/2024/day/17) | [Source](src/solutions/Day17.java) | 0.363 | 1.992 |
| 18 | [RAM Run](https://adventofcode.com/2024/day/18) | [Source](src/solutions/Day18.java) | 2.545 | 3.138 |
| 19 | [Linen Layout](https://adventofcode.com/2024/day/19) | [Source](src/solutions/Day19.java) | 2.035 | 2.705 |
| 20 | [Race Condition](https://adventofcode.com/2024/day/20) | [Source](src/solutions/Day20.java) | 2.444 | 17.196 |
| 21 | [Keypad Conundrum](https://adventofcode.com/2024/day/21) | [Source](src/solutions/Day21.java) | 1.991 | 2.814 |
| 22 | [Monkey Market](https://adventofcode.com/2024/day/22) | [Source](src/solutions/Day22.java) | 15.592 | 15.070 |
| 23 | [LAN Party](https://adventofcode.com/2024/day/23) | [Source](src/solutions/Day23.java) | 3.077 | 2.886 |
| 24 | [Crossed Wires](https://adventofcode.com/2024/day/24) | [Source](src/solutions/Day24.java) | 2.145 | 1.906 |
| 25 | [Code Chronicle](https://adventofcode.com/2024/day/25) | [Source](src/solutions/Day25.java) | 7.522 | 0.002 |

<!-- CHAR COUNTS BEGIN -->

## Character counts

The table below counts non-whitespace characters in each normal solution file and its golfed sibling. The golfed files are intentionally separate from the readable/performance-oriented `DayXX.java` files so the PR tracks can merge independently.

| Day | Solution | Golfed solution | Source chars | Golfed chars |
| --- | --- | --- |-------------:|-------------:|
| 1 | [Source](src/solutions/Day01.java) | [Golfed](src/solutions/Day01Golfed.java) | 926 | 407 |
| 2 | [Source](src/solutions/Day02.java) | [Golfed](src/solutions/Day02Golfed.java) | 1,078 | 436 |
| 3 | [Source](src/solutions/Day03.java) | [Golfed](src/solutions/Day03Golfed.java) | 862 | 347 |
| 4 | [Source](src/solutions/Day04.java) | [Golfed](src/solutions/Day04Golfed.java) | 1,584 | 455 |
| 5 | [Source](src/solutions/Day05.java) | [Golfed](src/solutions/Day05Golfed.java) | 2,127 | 568 |
| 6 | [Source](src/solutions/Day06.java) | [Golfed](src/solutions/Day06Golfed.java) | 2,324 | 512 |
| 7 | [Source](src/solutions/Day07.java) | [Golfed](src/solutions/Day07Golfed.java) | 1,276 | 437 |
| 8 | [Source](src/solutions/Day08.java) | [Golfed](src/solutions/Day08Golfed.java) | 1,220 | 504 |
| 9 | [Source](src/solutions/Day09.java) | [Golfed](src/solutions/Day09Golfed.java) | 2,221 | 747 |
| 10 | [Source](src/solutions/Day10.java) | [Golfed](src/solutions/Day10Golfed.java) | 1,582 | 465 |
| 11 | [Source](src/solutions/Day11.java) | [Golfed](src/solutions/Day11Golfed.java) | 2,963 | 450 |
| 12 | [Source](src/solutions/Day12.java) | [Golfed](src/solutions/Day12Golfed.java) | 2,405 | 728 |
| 13 | [Source](src/solutions/Day13.java) | [Golfed](src/solutions/Day13Golfed.java) | 1,303 | 276 |
| 14 | [Source](src/solutions/Day14.java) | [Golfed](src/solutions/Day14Golfed.java) | 2,336 | 697 |
| 15 | [Source](src/solutions/Day15.java) | [Golfed](src/solutions/Day15Golfed.java) | 3,268 | 838 |
| 16 | [Source](src/solutions/Day16.java) | [Golfed](src/solutions/Day16Golfed.java) | 5,356 | 724 |
| 17 | [Source](src/solutions/Day17.java) | [Golfed](src/solutions/Day17Golfed.java) | 2,459 | 733 |
| 18 | [Source](src/solutions/Day18.java) | [Golfed](src/solutions/Day18Golfed.java) | 1,506 | 666 |
| 19 | [Source](src/solutions/Day19.java) | [Golfed](src/solutions/Day19Golfed.java) | 1,459 | 393 |
| 20 | [Source](src/solutions/Day20.java) | [Golfed](src/solutions/Day20Golfed.java) | 2,760 | 644 |
| 21 | [Source](src/solutions/Day21.java) | [Golfed](src/solutions/Day21Golfed.java) | 10,068 | 727 |
| 22 | [Source](src/solutions/Day22.java) | [Golfed](src/solutions/Day22Golfed.java) | 2,079 | 515 |
| 23 | [Source](src/solutions/Day23.java) | [Golfed](src/solutions/Day23Golfed.java) | 3,581 | 931 |
| 24 | [Source](src/solutions/Day24.java) | [Golfed](src/solutions/Day24Golfed.java) | 2,621 | 1,081 |
| 25 | [Source](src/solutions/Day25.java) | [Golfed](src/solutions/Day25Golfed.java) | 1,111 | 411 |
| Total |  |  | 60,475 | 14,692 |

<!-- CHAR COUNTS END -->
