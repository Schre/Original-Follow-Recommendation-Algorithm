package server.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserNetworkStatistics {
    public static Map<String, Double> computeFieldPercentages(Set<NetworkNode> users) {
        Map<String, Double> mappings = new HashMap<>();
        Map<String, Integer> professionalFrequencies = new HashMap<>();
        int totalFollowings = users.size();

        for (NetworkNode following : users) {
            mappings.putIfAbsent(following.getField(), 0.0);
            professionalFrequencies.putIfAbsent(following.getField(), 0);
            int newCount = professionalFrequencies.get(following.getField()) + 1;

            // update frequency count
            professionalFrequencies.put(following.getField(), newCount);
            // update percentage
            mappings.put(following.getField(), new Double(newCount) / totalFollowings);
        }

        return mappings;
    }
}
