package syntheticdata;

import server.network.NetworkNode;
import server.network.RelatednessMatrix;
import shared.SharedObjects;
import org.json.*;

import java.util.*;

/***
 * This will generate synthetic data, upload the data to our database,
 * and output it to the syntheticdata.json file
 */

// TODO: Output our network to syntheticdata.json

/** TODO: Maybe we should not use NetworkNode and create a network
 *  that creates all of the attributes of a user to make adding them
 *  to the database easier. The idea of a network node is that the uid
 *  corresponds to an already existing user in our database. If we are
 *  creating data then this case does not hold true.
 */
public class SyntheticDataGenerator {
    // arg1 = number of nodes to create in our network (parameter nodeCount)
    // arg2 = density of network D (Probability of connecting two users if the relatedness was 1)
    public static void Main(String[] args) {

        // Set up connection pool so that we can execute queries
        SharedObjects.initialize();

        // TODO: Clear all of the tables in the database!!!


        List<String> fields = new ArrayList<>(RelatednessMatrix.getSupportedFields());
        Set<NetworkNode> data = new HashSet<>();

        int nodeCount = Integer.parseInt(args[0]);
        int D = Integer.parseInt(args[1]);
        /***
         * Step 1. Generate all of the nodes in our network, randomly assigning each of the nodes
         * a profession according to our set of fields.
         */

        Random r = new Random();

        for (int i = 0; i < nodeCount; ++i) {

            // Generate a unique uid
            String uid = UUID.randomUUID().toString();

            // Generate a random field
            int randomIndex = r.nextInt() % fields.size();

            String randField = fields.get(randomIndex);

            // create node
            NetworkNode node = new NetworkNode(uid, randField);
            data.add(node);

        }

        /**
         * Step 2. Randomly connect C nodes to other nodes in the network with a probability for each
         * connection determined by our RelatednessMatrix (and mutual followers?)
         */

        for (NetworkNode node : data) {

            // See if we should connect this node based on Density D
            if ( r.nextFloat() > D) {
                continue;
            }

            for (NetworkNode adj : data) {
                double connectProbability = RelatednessMatrix.getRelatedness(node.getField(), adj.getField());

                /* Do not connect */
                if (r.nextFloat() > connectProbability) {
                    continue;
                }

                // Add follower
                node.addFollow(adj);
            }
        }

        /***
         * Step 3. Insert all of the nodes into the database
         * and randomly make up names for required fields
         */

        /***
         * Step 4. Add all of the follower information to the
         * database
         */
    }
}
