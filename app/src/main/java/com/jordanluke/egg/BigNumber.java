package com.jordanluke.egg;

/**
 * Created by jmattingley23 on 12/19/2016.
 */

import android.widget.TextView;

import java.lang.String;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BigNumber {
    int count;

    BigInteger largeCount = new BigInteger(Integer.toString(Integer.MAX_VALUE));

    public BigNumber() {
        count = 0;
    }

    public void add(int in) {
        count+=in;
    }

    public void addB(BigInteger in){
        largeCount = largeCount.add(in);
    }

    public String toString() {
        return String.valueOf(count);
    }
}
