package project_egg.project_egg;

/**
 * Created by jmattingley23 on 12/19/2016.
 */

import java.lang.String;

public class BigNumber {
    int count;

    public BigNumber() {
        count = 0;
    }

    public void add(int in) {
        count+=in;
    }

    public String toString() {
        return String.valueOf(count);
    }
}
