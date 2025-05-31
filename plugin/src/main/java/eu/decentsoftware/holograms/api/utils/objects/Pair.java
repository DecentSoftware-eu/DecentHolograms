package eu.decentsoftware.holograms.api.utils.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @deprecated For removal with {@link eu.decentsoftware.holograms.api.nms.NMS}.
 */
@Deprecated
@Data
@AllArgsConstructor
public class Pair<K, V> {

    private K key;
    private V value;

}
