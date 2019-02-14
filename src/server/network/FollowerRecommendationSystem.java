package server.network;

import java.security.InvalidParameterException;
import java.util.*;

public class FollowerRecommendationSystem {
    private NetworkNode user;

    public FollowerRecommendationSystem(NetworkNode user) {
        this.user = user;
    }
    public NetworkNode getUser() {
        return this.user;
    }


    /* Assumes the user has already followed some users */
    private double calculateMutualFollowerScore(Map<NetworkNode, Integer> mutualFollowers, NetworkNode a) {
        return mutualFollowers.get(a) * RelatednessMatrix.getRelatedness(a.getField(), user.getField());
    }
    // Returns the top K most relevent users that our user should consider following
    public List<NetworkNode> getTopKRecommendations(int K) {
        List<NetworkNode> topK = new ArrayList<>();

        Map<NetworkNode, Integer> mutualFollowers = new HashMap<>();
        int prescision = 1000;

        // maintain a minheap of maximum depth K
        Queue<NetworkNode> pq = new PriorityQueue<>((a, b) -> {
            // Formula: Mutalfriends * (relatedness between a's field and user's field)
            try {
                double aRelatedness = calculateMutualFollowerScore(mutualFollowers, a);
                double bRelatedness = calculateMutualFollowerScore(mutualFollowers, b);

                return (int) ((aRelatedness - bRelatedness) * prescision);
            }
            catch (InvalidParameterException e) {
                /* Invalid field comparison */
                return 0;
            }
        });

        /**
            Algorithm:
            For each person user is following E, increment all of the users E is following's
            mutual follower count (unless the mutual follower is the user

            After this, go through all of those mutual followers and return the top K most related
            by utilizing the min heap

         **/

        for (NetworkNode userFollowed : user.getUsersFollowed()) {
            for (NetworkNode mutualFollower : userFollowed.getUsersFollowed()) {
                if (mutualFollower == user)
                    continue;
                mutualFollowers.putIfAbsent(mutualFollower, 0);

                int oldCount = mutualFollowers.get(mutualFollower);

                // Increment mutual follower count
                mutualFollowers.put(mutualFollower, oldCount + 1);
            }
        }

        for (NetworkNode mutualFollower : mutualFollowers.keySet()) {
            if (pq.size() < K) {
                pq.add(mutualFollower);
                continue;
            }

            // Heap is full
            NetworkNode minMax = pq.peek();

            if (calculateMutualFollowerScore(mutualFollowers, mutualFollower) > calculateMutualFollowerScore(mutualFollowers, minMax)) {
                pq.poll();
                pq.add(mutualFollower);
            }

        }

        while (!pq.isEmpty()) {
            topK.add(pq.poll());
        }

        return topK;
    }
}
