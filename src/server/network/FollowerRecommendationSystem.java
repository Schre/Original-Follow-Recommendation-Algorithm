package server.network;

import server.dto.dto.UserDTO;
import server.service.UserService;

import java.security.InvalidParameterException;
import java.util.*;

public class FollowerRecommendationSystem {
    private NetworkNode user;

    public FollowerRecommendationSystem(NetworkNode user) {
        this.user = user;

        /*if (RelatednessMatrix.matrix == null) {
            RelatednessMatrix.initialize();
        }*/
    }
    public NetworkNode getUser() {
        return this.user;
    }

    public NetworkNode loadNetworkForUser() {
        Map<String, NetworkNode> nodes = new HashMap<>();
        String user_id = user.getUID();
        Set<UserDTO> followers;
        UserService us = new UserService();
        try {
            // Fetch users user is following
            followers = us.getUserFollowings(user_id);
        }
        catch (NoSuchElementException nse) {
            System.out.println(nse.getMessage());
            return null;
        }

        NetworkNode root = user;

        for (UserDTO follower : followers) {
            nodes.putIfAbsent(follower.user_id, new NetworkNode(follower.user_id, follower.field));
            NetworkNode f = nodes.get(follower.user_id);

            root.addFollowing(f);

            Set<UserDTO> secondDegreeFollowers = us.getUserFollowings(f.getUID());
            for (UserDTO secondDegreeFollower : secondDegreeFollowers) {
                if (secondDegreeFollower.user_id.equals(this.user.getUID())) {
                    continue;
                }
                nodes.putIfAbsent(secondDegreeFollower.user_id, new NetworkNode(secondDegreeFollower.user_id, secondDegreeFollower.field));
                NetworkNode sdf = nodes.get(secondDegreeFollower.user_id);
                f.addFollowing(sdf);
            }
        }


        return root;
    }

    /* Assumes the user has already followed some users */
    private double calculateMutualFollowerScore(Map<NetworkNode, Integer> mutualFollowers, NetworkNode a) {
        return mutualFollowers.get(a) * RelatednessMatrix.getRelatedness(user.getField(), a.getField());
    }

    // Returns the top K most relevant users that our user should consider following
    public List<NetworkNode> getTopKRecommendations(int K) {
        List<NetworkNode> topK = new ArrayList<>();

        Map<NetworkNode, Integer> mutualFollowers = new HashMap<>();
        int prescision = 10000;

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
        Set<String> followedIds = new HashSet<>();
        for (NetworkNode followed : user.getUsersFollowed()) {
            followedIds.add(followed.getUID());
        }
        for (NetworkNode userFollowed : user.getUsersFollowed()) {
            for (NetworkNode mutualFollower : userFollowed.getUsersFollowed()) {
                if (mutualFollower == user || followedIds.contains(mutualFollower.getUID()))
                    continue;
                mutualFollowers.putIfAbsent(mutualFollower, 0);

                int oldCount = mutualFollowers.get(mutualFollower);

                // Increment mutual follower count
                mutualFollowers.put(mutualFollower, oldCount + 1);
                mutualFollower.incrementMutualFollowings();
            }
        }

        for (NetworkNode mutualFollower : mutualFollowers.keySet()) {
            if (pq.size() < K || pq.contains(mutualFollower)) {
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
