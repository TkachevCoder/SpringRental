package com.example.rental.util;

import com.example.rental.model.Rental;

import java.util.Comparator;

public class RentalDateComparator implements Comparator<Rental> {
    @Override
    public int compare(Rental o1, Rental o2) {
        if(o1.getRentalDate().compareTo(o2.getRentalDate())>0) return 1;
        else if(o1.getRentalDate().compareTo(o2.getRentalDate())<0) return -1;
        else return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
