package server.TrieHard;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutoComplete {
    private Map<String, Trie<String, String>> userSearchTrieMap;

    public AutoComplete() {
        userSearchTrieMap = new HashMap<>();
    }

    public synchronized  boolean containsUser(String uid) {
        return userSearchTrieMap.containsKey(uid);
    }
    public synchronized void add(String uid, String rec_id, String s) {
        userSearchTrieMap.putIfAbsent(uid, new Trie<>(new TrieSequencerCharSequenceCaseInsensitive<>()));
        Trie t = userSearchTrieMap.get(uid);
        t.put(s, rec_id);
    }

    public synchronized Set<TrieNode<String, String>> getNodesMatchingPattern(String uid, String s) {
        userSearchTrieMap.putIfAbsent(uid, new Trie<>(new TrieSequencerCharSequenceCaseInsensitive<>()));
        Trie t = userSearchTrieMap.get(uid);
        return t.nodeSet(s, TrieMatch.PARTIAL);
    }
}
