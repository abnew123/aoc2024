package src.solutions;

import src.meta.DayTemplate;

import java.util.Arrays;
import java.util.Scanner;

public class Day08 extends DayTemplate {

    private int width;
    private int height;
    private long[] antennas;

    public String solve(boolean part1, Scanner in) {
        parse(in);
        boolean[] antinodes = new boolean[width * height];
        markAntinodes(part1 ? antinodes : null, part1 ? null : antinodes);
        return count(antinodes) + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        parse(in);
        int cells = width * height;
        boolean[] part1 = new boolean[cells];
        boolean[] part2 = new boolean[cells];
        markAntinodes(part1, part2);
        return new String[]{count(part1) + "", count(part2) + ""};
    }

    private void parse(Scanner in) {
        String raw = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int length = raw.length();
        int lastContent = -1;
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(raw.charAt(i))) {
                lastContent = i;
            }
        }
        if (lastContent < 0) {
            throw new IndexOutOfBoundsException(0);
        }

        int lineCount = 0;
        int firstLineLength = -1;
        int index = 0;
        while (index <= lastContent) {
            int lineStart = index;
            while (index < length && raw.charAt(index) != '\n' && raw.charAt(index) != '\r') {
                index++;
            }
            if (firstLineLength < 0) {
                firstLineLength = index - lineStart;
            }
            lineCount++;
            index = nextLineStart(raw, index);
        }

        width = firstLineLength;
        height = lineCount;
        char[] cells = new char[width * height];
        int row = 0;
        index = 0;
        while (index <= lastContent) {
            int lineStart = index;
            while (index < length && raw.charAt(index) != '\n' && raw.charAt(index) != '\r') {
                index++;
            }
            int lineLength = index - lineStart;
            for (int col = 0; col < lineLength; col++) {
                if (col >= width) {
                    throw new ArrayIndexOutOfBoundsException(col);
                }
                cells[col * height + row] = raw.charAt(lineStart + col);
            }
            row++;
            index = nextLineStart(raw, index);
        }

        long[] list = new long[16];
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                char c = cells[x * height + y];
                if (c != '.') {
                    if (count == list.length) {
                        list = Arrays.copyOf(list, count * 2);
                    }
                    list[count++] = ((long) c << 32) | (x * height + y);
                }
            }
        }
        Arrays.sort(list, 0, count);
        antennas = Arrays.copyOf(list, count);
    }

    private int nextLineStart(String raw, int separatorIndex) {
        if (separatorIndex >= raw.length()) {
            return separatorIndex;
        }
        if (raw.charAt(separatorIndex) == '\r' && separatorIndex + 1 < raw.length()
                && raw.charAt(separatorIndex + 1) == '\n') {
            return separatorIndex + 2;
        }
        return separatorIndex + 1;
    }

    private void markAntinodes(boolean[] part1, boolean[] part2) {
        long[] list = antennas;
        int groupStart = 0;
        while (groupStart < list.length) {
            int groupEnd = groupStart + 1;
            while (groupEnd < list.length && (list[groupEnd] >>> 32) == (list[groupStart] >>> 32)) {
                groupEnd++;
            }
            for (int i = groupStart; i < groupEnd; i++) {
                for (int j = i + 1; j < groupEnd; j++) {
                    int a = (int) list[i];
                    int b = (int) list[j];
                    int ax = a / height;
                    int ay = a % height;
                    int bx = b / height;
                    int by = b % height;
                    int diffX = ax - bx;
                    int diffY = ay - by;
                    if (part1 != null) {
                        mark(bx - diffX, by - diffY, part1);
                        mark(ax + diffX, ay + diffY, part1);
                    }
                    if (part2 != null) {
                        int divisor = Math.abs(gcd(diffX, diffY));
                        int stepX = diffX / divisor;
                        int stepY = diffY / divisor;
                        for (int x = ax, y = ay; safe(x, y); x += stepX, y += stepY) {
                            part2[x * height + y] = true;
                        }
                        for (int x = ax - stepX, y = ay - stepY;
                             safe(x, y); x -= stepX, y -= stepY) {
                            part2[x * height + y] = true;
                        }
                    }
                }
            }
            groupStart = groupEnd;
        }
    }

    private void mark(int x, int y, boolean[] antinodes) {
        if (safe(x, y)) {
            antinodes[x * height + y] = true;
        }
    }

    private boolean safe(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int next = a % b;
            a = b;
            b = next;
        }
        return a;
    }

    private int count(boolean[] antinodes) {
        int total = 0;
        for (boolean antinode : antinodes) {
            if (antinode) {
                total++;
            }
        }
        return total;
    }
}
