package com.darkgran.farstar.gui;

import java.util.Objects;

public class SimpleVector2 {
    public float x;
    public float y;

    public SimpleVector2() {
    }

    public SimpleVector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleVector2 that = (SimpleVector2) o;
        return Float.compare(that.x, x) == 0 && Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
