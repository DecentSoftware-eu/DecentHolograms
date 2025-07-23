package eu.decentsoftware.holograms.api.animations;

import eu.decentsoftware.holograms.api.animations.text.BurnAnimation;
import eu.decentsoftware.holograms.api.animations.text.ColorsAnimation;
import eu.decentsoftware.holograms.api.animations.text.ScrollAnimation;
import eu.decentsoftware.holograms.api.animations.text.TypewriterAnimation;
import eu.decentsoftware.holograms.api.animations.text.WaveAnimation;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.api.utils.tick.Ticked;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationManager extends Ticked {

    private static final Pattern ANIMATION_PATTERN = Pattern.compile("[<{]#?ANIM:(\\w+)(:\\S+)?[}>](.*?)[<{]/#?ANIM[}>]");
    private final Map<String, TextAnimation> animationMap = new HashMap<>();
    private final AtomicLong step;

    public AnimationManager() {
        super(1L);
        this.step = new AtomicLong(0);
        this.reload();
    }

    @Override
    public void tick() {
        step.incrementAndGet();
    }

    public synchronized void destroy() {
        this.unregister();
        this.animationMap.clear();
    }

    public synchronized void reload() {
        this.animationMap.clear();
        this.registerAnimation(new TypewriterAnimation());
        this.registerAnimation(new WaveAnimation());
        this.registerAnimation(new BurnAnimation());
        this.registerAnimation(new ScrollAnimation());
        this.registerAnimation(new ColorsAnimation());
        this.step.set(0);
        this.register();

        // Load custom animations asynchronously
        S.async(this::loadCustomAnimations);
    }

    public long getStep() {
        return step.get();
    }

    @NonNull
    public String parseTextAnimations(@NonNull String string) {
        Matcher matcher = ANIMATION_PATTERN.matcher(string);
        while (matcher.find()) {
            String animationName = matcher.group(1);
            String args = matcher.group(2);
            String text = matcher.group(3);

            TextAnimation animation = getAnimation(animationName);
            if (animation != null) {
                string = string.replace(matcher.group(), animation.animate(text, getStep(), args == null ? null : args.substring(1).split(",")));
            }
        }

        if (string.contains("&u")) {
            TextAnimation animation = getAnimation("colors");
            if (animation != null) {
                string = string.replace("&u", animation.animate("", getStep()));
            }
        }

        return string;
    }

    public boolean containsAnimations(@NonNull String string) {
        Matcher matcher = ANIMATION_PATTERN.matcher(string);
        return matcher.find() || string.contains("&u");
    }

    public TextAnimation registerAnimation(@NonNull TextAnimation animation) {
        return animationMap.put(animation.getName(), animation);
    }

    public TextAnimation getAnimation(@NonNull String name) {
        return animationMap.get(name);
    }

    private void loadCustomAnimations() {


        int counter = 0;
        Log.info("Loading animations...");

        Log.info("Loaded %d animations!", counter);
    }

}
