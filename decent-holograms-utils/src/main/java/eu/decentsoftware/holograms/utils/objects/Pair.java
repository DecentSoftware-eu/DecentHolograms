package eu.decentsoftware.holograms.utils.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<K, V> {

	private K key;
	private V value;

}
