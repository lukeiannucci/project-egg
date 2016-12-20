package com.jordanluke.egg;

/**
 * Created by jmattingley23 on 12/19/2016.
 */

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BigNumber {
    int count;

    List<Integer> bigNumberList = new ArrayList();

    public BigNumber(int num) {
        bigNumberList.add(0, num);
    }

    public BigNumber addList(BigNumber addToEnd) {
        BigNumber sum = new BigNumber();
        int first = 0;
        int second = 0;
        int result = 0;
        int carry = 0;

        int size1 = bigNumberList.size();
        int size2 = addToEnd.bigNumberList.size();

        ListIterator<Integer> it1 = bigNumberList.listIterator(size1 - 1);
        ListIterator<Integer> it2 = addToEnd.bigNumberList.listIterator(size2 - 1);

        while ((it1 != bigNumberList.listIterator(size1 - 1)) || (it2 != addToEnd.bigNumberList.listIterator(size2 - 1))) {
            if (it1 != bigNumberList.listIterator(size1 - 1)) {
                first = Integer.parseInt(it1.toString());
                it1.previousIndex();
            } else {
                first = 0;
            }

            if (it2 != addToEnd.bigNumberList.listIterator(size1 - 1)) {
                second = Integer.parseInt(it2.toString());
                it2.previousIndex();
            } else {
                second = 0;
            }

            int temp = first + second + carry;

            result = temp % 1000;

            carry = temp / 1000;

            sum.bigNumberList.add(0, result);
        }

        if (carry > 0) {
            sum.bigNumberList.add(0, carry);
        }

        return sum;
    }

    public void display() {
        int blockCount = 0;

        ListIterator<Integer> it = bigNumberList.listIterator(0);
        int size = bigNumberList.size();
        int counter = 0;

        while(true){
            //System.out.println('0');
            if(blockCount == 0){
                if (counter == 0)
                System.out.print("%.1" + it.toString());
            }
            else{
                System.out.print(".1" + it.next());
            }
            if(it == bigNumberList.listIterator(size-1)){
                return;
            }
            if(blockCount != 0) {
                System.out.print(".3" + it.toString());
                it.next();
            }

            if(it != bigNumberList.listIterator(size-1)){
                System.out.print(",");
            }
        }
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
