package eu.decentsoftware.holograms.api.animations.text;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.animations.TextAnimation;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

public class ColorsAnimation extends TextAnimation {

    private static final List<String> DEFAULT_COLORS = Lists.newArrayList("&c", "&6", "&e", "&a", "&b", "&d");

    public ColorsAnimation() {
        super("colors", 4, 0, "colours");
    }

    @Override
    public String animate(@NonNull String string, long step, String... args) {
        List<String> colors = DEFAULT_COLORS;
        if (args != null && args.length > 0) {
            colors.clear();
            colors.addAll(Arrays.asList(args));
        }
        int currentStep = getCurrentStep(step, colors.size());
        return colors.get(currentStep) + string;
    }
}
