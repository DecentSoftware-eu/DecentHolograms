package eu.decentsoftware.holograms.api.animations;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.animations.custom.CustomTextAnimation;
import eu.decentsoftware.holograms.api.animations.text.*;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.file.FileUtils;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.api.utils.tick.Ticked;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationManager extends Ticked {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
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

    public void destroy() {
        this.unregister();
        this.animationMap.clear();
    }

    public void reload() {
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

    public TextAnimation registerAnimation(@NonNull String name, @NonNull TextAnimation animation) {
        return this.animationMap.put(name, animation);
    }

    public TextAnimation registerAnimation(@NonNull TextAnimation animation) {
        return this.animationMap.put(animation.getName(), animation);
    }

    public TextAnimation unregisterAnimation(@NonNull String name) {
        return animationMap.remove(name);
    }

    public TextAnimation getAnimation(@NonNull String name) {
        return animationMap.get(name);
    }

    private void loadCustomAnimations() {
        String[] fileNames = FileUtils.getFileNames(DECENT_HOLOGRAMS.getDataFolder() + "/animations", "[a-zA-Z0-9_-]+\\.yml", true);
        if (fileNames == null || fileNames.length == 0) return;

        int counter = 0;
        Common.log("Loading animations...");
        for (String fileName : fileNames) {
            try {
                TextAnimation animation = CustomTextAnimation.fromFile(fileName);
                if (animation != null) {
                    this.registerAnimation(animation);
                    counter++;
                }
            } catch (Exception e) {
                Common.log(Level.WARNING, "Failed to load animation from file '%s'!", fileName);
                e.printStackTrace();
            }
        }
        Common.log("Loaded %d animations!", counter);
    }

}
