package com.pointed5th.ion;

public class Loc {
    public final int start;
    public final int length;

    public Loc(int start, int length) {
        this.start = start;
        this.length = length;
    }
    public int end() {
        return start + length;
    }

    public String toString() {
        return "Loc(" + start + ", " + length + ")";
    }
}
