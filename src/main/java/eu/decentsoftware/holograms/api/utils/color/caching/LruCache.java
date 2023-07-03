package eu.decentsoftware.holograms.api.utils.color.caching;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@EqualsAndHashCode
@ToString
@ThreadSafe
public class LruCache {

    private final Deque<String> QUE = new LinkedList<>();
    private final Map<String, LruElement> MAP = new ConcurrentHashMap<>();

    private final int maxSize;

    public LruCache(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getResult(String input) {
        if (input != null && MAP.containsKey(input)) {
            LruElement curr = MAP.get(input);
            synchronized (QUE) {
                QUE.remove(input);
                QUE.addFirst(input);
            }
            return curr.getResult();
        }

        return null;
    }

    public void put(String input, String result) {
        if (input == null || result == null) {
            return;
        }
        synchronized (QUE) {
            if (MAP.containsKey(input)) {
                QUE.remove(input);
            } else {
                int size = QUE.size();
                if (size == maxSize && size > 0) {
                    String temp = QUE.removeLast();
                    MAP.remove(temp);
                }
            }
            LruElement newObj = new LruElement(input, result);
            QUE.addFirst(input);
            MAP.put(input, newObj);
        }
    }

}
