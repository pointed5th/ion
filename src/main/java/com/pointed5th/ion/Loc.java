package com.pointed5th.ion;

public class Loc implements Comparable<Loc> {
    private final int line;
    private final int column;
    private final int length;


    public Loc(int line, int column, int length) {
        this.line = line;
        this.column = column;
        this.length = length;
    }

    public int line() {
        return line;
    }

    public int column() {
        return column;
    }

    public int length() {
        return length;
    }

    public int end() {
        return column + length;
    }

    public int compareTo(Loc other) {
        if (line < other.line) {
            return -1;
        } else if (line > other.line) {
            return 1;
        }

        if (column < other.column) {
            return -1;
        } else if (column > other.column) {
            return 1;
        }

        return 0;
    }

    public String toString() {
        return "line " + line + ", column " + column;
    }
}
