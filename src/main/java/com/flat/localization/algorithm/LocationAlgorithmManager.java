package com.flat.localization.algorithm;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jacob Phillips (01/2015, jphilli85 at gmail)
 */
public class LocationAlgorithmManager {
    /**
     * Location algorithms that can be applied to each node.
     */
    private final Map<LocationAlgorithm, Criteria.AlgorithmMatchCriteria> algorithms =
            Collections.synchronizedMap(new LinkedHashMap<LocationAlgorithm, Criteria.AlgorithmMatchCriteria>());

    public int getAlgorithmCount() {
        return algorithms.size();
    }

    public Set<LocationAlgorithm> getAlgorithms() {
        return algorithms.keySet();
    }

    public Criteria.AlgorithmMatchCriteria getCriteria(LocationAlgorithm la) {
        return algorithms.get(la);
    }

    public void addAlgorithm(LocationAlgorithm la, Criteria.AlgorithmMatchCriteria amc) {
        algorithms.put(la, amc);
    }
}
