package com.example.coursework.options;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class PriceDao {

    private final Set<Integer> prices;

    public PriceDao() {
        prices = new LinkedHashSet<>();
    }

    public void addPrice(Integer price) {
        prices.add(price);
    }

    /*    @OneToMany(mappedBy = "price", fetch = FetchType.EAGER)
        private Set<Internship> internships = new HashSet<>();
        public void addInternship(Internship internship){
            internships.add(internship);
        }*/

    public Set<Integer> getPrices() {
        return prices;
    }
}
