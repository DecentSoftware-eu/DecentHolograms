package eu.decentsoftware.holograms.api.animations;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.animations.custom.CustomTextAnimation;
import eu.decentsoftware.holograms.api.animations.text.*;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.file.FileUtils;
import eu.decentsoftware.holograms.api.utils.scheduler.ConsumerTask;
import lombok.Getter;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationManager {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private static final Pattern ANIMATION_PATTERN = Pattern.compile("[<{]#?ANIM:(\\w+)(:\\S+)?[}>](.*?)[<{]/#?ANIM[}>]");
    private final Map<String, TextAnimation> animationMap = new HashMap<>();
    private final ConsumerTask<AnimationManager> animationTask;

    @Getter
    private long step = 0;

    public AnimationManager() {
        this.reload();

        this.animationTask = new ConsumerTask<>(DECENT_HOLOGRAMS.getPlugin(), this, 0L, 1L);
        this.animationTask.addPart("animation_update", (animationManager) -> animationManager.step++);
        this.animationTask.restart();
    }

    public void destroy() {
        this.animationTask.stop();
        this.animationMap.clear();
    }

    public void reload() {
        this.animationMap.clear();
        this.registerAnimation(new TypewriterAnimation());
        this.registerAnimation(new WaveAnimation());
        this.registerAnimation(new BurnAnimation());
        this.registerAnimation(new ScrollAnimation());
        this.registerAnimation(new ColorsAnimation());
        this.loadCustomAnimations();
    }

    public String parseTextAnimations(String string) {
        Validate.notNull(string);

        Matcher matcher = ANIMATION_PATTERN.matcher(string);
        while (matcher.find()) {
            String animationName = matcher.group(1);
            String args = matcher.group(2);
            String text = matcher.group(3);

            TextAnimation animation = getAnimation(animationName);
            if (animation != null) {
                string = string.replace(matcher.group(), animation.animate(text, step, args == null ? null : args.substring(1).split(",")));
                matcher = ANIMATION_PATTERN.matcher(string);
            }
        }

        if (string.contains("&u")) {
            TextAnimation animation = getAnimation("colors");
            if (animation != null) {
                string = string.replace("&u", animation.animate("", step));
            }
        }

        return string;
    }

    public boolean containsAnimations(String string) {
        Matcher matcher = ANIMATION_PATTERN.matcher(string);
        return matcher.find() || string.contains("&u");
    }

    public TextAnimation registerAnimation(String name, TextAnimation animation) {
        return this.animationMap.put(name, animation);
    }

    public TextAnimation registerAnimation(TextAnimation animation) {
        return this.animationMap.put(animation.getName(), animation);
    }

    public TextAnimation unregisterAnimation(String name) {
        return animationMap.remove(name);
    }

    public TextAnimation getAnimation(String name) {
        return animationMap.get(name);
    }

    private void loadCustomAnimations() {
        String[] fileNames = FileUtils.getFileNames(DECENT_HOLOGRAMS.getDataFolder() + "/animations", "\\w+\\.yml", true);
        if (fileNames == null || fileNames.length == 0) return;

        int counter = 0;
        Common.log("Loading animations...");
        for (String fileName : fileNames) {
            TextAnimation animation = CustomTextAnimation.fromFile(fileName);
            if (animation != null) {
                this.registerAnimation(animation);
                counter++;
            }
        }
        Common.log("Loaded %d animations!", counter);
    }

}
