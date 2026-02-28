package eu.decentsoftware.holograms.api.animations;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.animations.custom.CustomTextAnimation;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.file.FileUtils;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.api.utils.tick.Ticked;
import eu.decentsoftware.holograms.display.render.content.CompiledAnimation;
import lombok.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationManager extends Ticked {

    public static final Pattern ANIMATION_PATTERN = Pattern.compile("[<{]#?ANIM:(\\w+)(:\\S+)?[}>](.*?)[<{]/#?ANIM[}>]");
    private final DecentHolograms decentHolograms;
    private final Map<String, TextAnimation> animationMap = new HashMap<>();
    private final AtomicLong step;

    public AnimationManager(DecentHolograms decentHolograms) {
        super(1L);
        this.decentHolograms = decentHolograms;
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
        this.registerAnimation(BuiltInAnimations.TYPEWRITER);
        this.registerAnimation(BuiltInAnimations.WAVE);
        this.registerAnimation(BuiltInAnimations.BURN);
        this.registerAnimation(BuiltInAnimations.SCROLL);
        this.registerAnimation(BuiltInAnimations.COLORS);
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
            string = string.replace("&u", BuiltInAnimations.COLORS.animate("", getStep()));
        }

        return string;
    }

    public String applyAnimations(String string, List<CompiledAnimation> animations) {
        if (animations.isEmpty()) {
            return string;
        }

        StringBuilder stringBuilder = new StringBuilder(string.length());
        for (CompiledAnimation compiledAnimation : animations) {
            TextAnimation animation = getAnimation(compiledAnimation.getAnimation());
            if (animation == null) {
                continue;
            }
            String body = compiledAnimation.getBody();
            String animationFrame = animation.animate(body == null ? "" : body, getStep(), compiledAnimation.getArgs());
            int position = compiledAnimation.getPosition();

            stringBuilder
                    .append(string, 0, position)
                    .append(animationFrame)
                    .append(string.substring(position));
        }
        return stringBuilder.toString();
    }

    public boolean containsAnimations(@NonNull String string) {
        return string.contains("&u") || ANIMATION_PATTERN.matcher(string).find();
    }

    public TextAnimation registerAnimation(@NonNull String name, @NonNull TextAnimation animation) {
        return animationMap.put(name, animation);
    }

    public TextAnimation registerAnimation(@NonNull TextAnimation animation) {
        return animationMap.put(animation.getName(), animation);
    }

    public TextAnimation unregisterAnimation(@NonNull String name) {
        return animationMap.remove(name);
    }

    public TextAnimation getAnimation(@NonNull String name) {
        return animationMap.get(name);
    }

    private void loadCustomAnimations() {
        File folder = new File(decentHolograms.getDataFolder(), "animations");
        List<File> files = FileUtils.getFilesFromTree(folder, Common.NAME_REGEX + "\\.yml", true);
        if (files.isEmpty()) {
            return;
        }

        int counter = 0;
        Log.info("Loading animations...");
        for (File file : files) {
            String fileName = FileUtils.getRelativePath(file, folder);
            try {
                TextAnimation animation = CustomTextAnimation.fromFile(fileName);
                registerAnimation(animation);
                counter++;
            } catch (Exception e) {
                Log.warn("Failed to load animation from file '%s'!", e, fileName);
            }
        }
        Log.info("Loaded %d animations!", counter);
    }

}
