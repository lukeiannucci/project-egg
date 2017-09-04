package xyz.jmatt.models;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class SortableDate extends Date {
    private long date;
    SortableDate(long date) {
        super(date);
        this.date = date;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("MM/dd/yyyy").format(date * 1000);
    }
}
