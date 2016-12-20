package com.jordanluke.egg;

/**
 * Created by jmattingley23 on 12/19/2016.
 */

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class BigNumber {
    int count;

    List<Integer> bigNumber = new ArrayList();

    public BigNumber(int num) {
        bigNumber.add(0, num);
    }

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
