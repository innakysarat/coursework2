package com.example.coursework.options;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class PriceDao {

    Set<Integer> prices = new LinkedHashSet<>();

    public void addPrice(Integer price) {
        prices.add(price);
    }

    /*    @OneToMany(mappedBy = "price", fetch = FetchType.EAGER)
        private Set<Internship> internships = new HashSet<>();
        public void addInternship(Internship internship){
            internships.add(internship);
        }*/
    Double asDouble(Object o) {
        Double val = null;
        if (o instanceof Number) {
            val = ((Number) o).doubleValue();
        }
        return val;
    }
}
