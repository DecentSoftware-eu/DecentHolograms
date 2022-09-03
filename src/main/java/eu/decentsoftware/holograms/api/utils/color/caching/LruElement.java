package eu.decentsoftware.holograms.api.utils.color.caching;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class LruElement {

    private final String input;
    private final String result;

    public LruElement(String input, String result) {
        this.input = input;
        this.result = result;
    }

}
