package bearmaps.proj2c.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Trie {
    private class Node {
        HashMap<Character, Node> children;
        List<Map<String, Object>> locations;
        public Node() {
            children = new HashMap<>();
            locations = new ArrayList<>();
        }
        public Node addChild(char ch) {
            Node node = new Node();
            if (children.containsKey(ch)) {
                return children.get(ch);
            }
            children.put(ch, node);
            return node;
        }
    }

    private Node root;

    public Trie() {
        root = new Node();
    }

    public void add(String s, Map<String, Object> location) {
        Node pointer = root;
        for (int i = 0; i < s.length(); i += 1) {
            char ch = s.charAt(i);
            pointer = pointer.addChild(ch);
        }
        pointer.locations.add(location);
    }

    public List<String> getPrefix(String s) {
        Node pointer = root;
        List<String> lstLocationNames = new ArrayList<>();
        for (int i = 0 ; i < s.length(); i += 1) {
            char ch = s.charAt(i);
            pointer = pointer.children.getOrDefault(ch, null);
            if (pointer == null) {
                return lstLocationNames;
            }
        }
        getAllNodes(pointer, lstLocationNames);
        return lstLocationNames;
    }

    private void getAllNodes(Node pointer, List<String> lstLocationNames) {
        for (Map<String, Object> location : pointer.locations) {
            lstLocationNames.add((String) location.get("name"));
        }
        for (Node child : pointer.children.values()) {
            getAllNodes(child, lstLocationNames);
        }
    }

    public List<Map<String, Object>> getLocation(String s) {
        Node pointer = root;
        for (int i = 0 ; i < s.length(); i += 1) {
            char ch = s.charAt(i);
            pointer = pointer.children.getOrDefault(ch, null);
            if (pointer == null) {
                return new ArrayList<>();
            }
        }
        return pointer.locations;
    }
}
