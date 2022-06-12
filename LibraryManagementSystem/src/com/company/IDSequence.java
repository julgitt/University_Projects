package com.company;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class IDSequence implements Serializable {
    private final AtomicLong currentValue;
    public IDSequence(){
        this.currentValue = new AtomicLong(0L);
    }
    public IDSequence(AtomicLong current){
        this.currentValue = current;
    }
    public long getNextValue() {
        return currentValue.getAndIncrement();
    }
    @Override
    public String toString(){
        return currentValue + "";
    }
}
