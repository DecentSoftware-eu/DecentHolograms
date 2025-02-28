package eu.decentsoftware.holograms.api.utils.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<K, V> {

	private K key;
	private V value;

}
