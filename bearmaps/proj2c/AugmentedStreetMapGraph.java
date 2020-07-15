package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;
import bearmaps.proj2c.utils.Trie;
import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private WeirdPointSet ds;
    private HashMap<Point, Long> pointLongHashMap;
    private Trie trieDS;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        pointLongHashMap = new HashMap<>();
        ArrayList<Point> lstPoints = new ArrayList<>();
        trieDS = new Trie();
        for (Node node : getNodes()) {
            if (neighbors(node.id()).size() > 0) {
                Point point = new Point(node.lon(), node.lat());
                lstPoints.add(point);
                pointLongHashMap.put(point, node.id());
            }
            if (node.name() != null){
                String cleanedString = cleanString(node.name());
                HashMap<String, Object> location = new HashMap<>();
                location.put("id", node.id());
                location.put("name", node.name());
                location.put("lat", node.lat());
                location.put("lon", node.lon());
                trieDS.add(cleanedString, location);
            }
        }
        ds = new WeirdPointSet(lstPoints);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        return pointLongHashMap.get(ds.nearest(lon, lat));
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return trieDS.getPrefix(cleanString(prefix));
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        return trieDS.getLocation(cleanString(locationName));
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
