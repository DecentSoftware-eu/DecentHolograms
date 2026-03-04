/*
 * This file is part of DecentHolograms, licensed under the GNU GPL v3.0 License.
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.display;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.api.animations.compile.AnimationCompiler;
import eu.decentsoftware.holograms.display.attribute.AttributeCommandService;
import eu.decentsoftware.holograms.display.attribute.AttributeConfigMapper;
import eu.decentsoftware.holograms.display.attribute.DisplayAttributeService;
import eu.decentsoftware.holograms.display.attribute.command.handler.AttributeCommandHandlerRegistry;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultBooleanHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultBrightnessHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultColorHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultEnumHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultLineWidthHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultOpacityHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultPitchHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultScaleHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultShadowRadiusHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultShadowStrengthHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultSkullTextureHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultTranslationHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.DefaultYawHandler;
import eu.decentsoftware.holograms.display.attribute.defaults.AttributeDefaultRegistry;
import eu.decentsoftware.holograms.display.attribute.defaults.AttributeDefaultRepository;
import eu.decentsoftware.holograms.display.attribute.defaults.AttributeDefaultService;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.display.attribute.definition.BillboardAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.BrightnessAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.GlowColorAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.ItemDisplayTypeAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.ItemEnchantedAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.ItemLeatherColorAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.ItemSkullTextureAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.PitchAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.ScaleAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.ShadowRadiusAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.ShadowStrengthAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.TextAlignmentAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.TextBackgroundColorAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.TextLineWidthAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.TextOpacityAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.TextSeeThroughAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.TextShadowAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.TranslationAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.YawAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueSerializer;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueTypeRegistry;
import eu.decentsoftware.holograms.display.attribute.value.color.RgbaValueType;
import eu.decentsoftware.holograms.display.attribute.value.display.BillboardConstraintsValue;
import eu.decentsoftware.holograms.display.attribute.value.display.BillboardConstraintsValueType;
import eu.decentsoftware.holograms.display.attribute.value.display.BrightnessValueType;
import eu.decentsoftware.holograms.display.attribute.value.display.ItemDisplayTypeValue;
import eu.decentsoftware.holograms.display.attribute.value.display.ItemDisplayTypeValueType;
import eu.decentsoftware.holograms.display.attribute.value.display.TextAlignmentValue;
import eu.decentsoftware.holograms.display.attribute.value.display.TextAlignmentValueType;
import eu.decentsoftware.holograms.display.attribute.value.primitives.BooleanValueType;
import eu.decentsoftware.holograms.display.attribute.value.primitives.FloatValueType;
import eu.decentsoftware.holograms.display.attribute.value.primitives.IntegerValueType;
import eu.decentsoftware.holograms.display.attribute.value.primitives.StringValueFactory;
import eu.decentsoftware.holograms.display.attribute.value.primitives.StringValueType;
import eu.decentsoftware.holograms.display.attribute.value.primitives.Vector3fValueType;
import eu.decentsoftware.holograms.display.command.DisplaysCommand;
import eu.decentsoftware.holograms.display.config.DisplayConfigMapper;
import eu.decentsoftware.holograms.display.config.DisplayPersistenceService;
import eu.decentsoftware.holograms.display.config.DisplayRepository;
import eu.decentsoftware.holograms.display.config.YamlConfigurationLoaderFactory;
import eu.decentsoftware.holograms.display.config.dto.ConfigAttribute;
import eu.decentsoftware.holograms.display.config.dto.ConfigDefaultAttribute;
import eu.decentsoftware.holograms.display.config.serializer.ConfigAttributeSerializer;
import eu.decentsoftware.holograms.display.config.serializer.ConfigDefaultAttributeSerializer;
import eu.decentsoftware.holograms.display.config.serializer.DecentLocationSerializer;
import eu.decentsoftware.holograms.display.config.serializer.DisplayBrightnessSerializer;
import eu.decentsoftware.holograms.display.config.serializer.DisplayColorSerializer;
import eu.decentsoftware.holograms.display.config.serializer.DisplayVector3fSerializer;
import eu.decentsoftware.holograms.display.render.DisplayRenderCoordinator;
import eu.decentsoftware.holograms.display.render.DisplayRenderIntentMaterializer;
import eu.decentsoftware.holograms.display.render.DisplayRenderService;
import eu.decentsoftware.holograms.display.render.DisplayVisibilityService;
import eu.decentsoftware.holograms.display.render.TextPostProcessor;
import eu.decentsoftware.holograms.display.render.placeholder.DisplayPlaceholderService;
import eu.decentsoftware.holograms.display.render.DisplayPostProcessor;
import eu.decentsoftware.holograms.display.render.state.LogicalRenderStateService;
import eu.decentsoftware.holograms.display.render.state.LogicalRenderStateManager;
import eu.decentsoftware.holograms.display.render.state.PresentedRenderStateManager;
import eu.decentsoftware.holograms.display.type.BlockDisplayTypeDefinition;
import eu.decentsoftware.holograms.display.type.DisplayTypeRegistry;
import eu.decentsoftware.holograms.display.type.ItemDisplayTypeDefinition;
import eu.decentsoftware.holograms.display.type.TextDisplayTypeDefinition;
import eu.decentsoftware.holograms.platform.api.PlatformAdapter;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayAlignment;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import eu.decentsoftware.holograms.platform.bukkit.text.LegacyCachingBukkitTextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

/**
 * Manages the display module, including initialization, reloading, and shutdown.
 *
 * @author d0by
 * @since 2.10.0
 */
public class DisplayModule {

    private final JavaPlugin plugin;
    private final DisplayService displayService;
    private final DisplayUpdateScheduler displayUpdateScheduler;
    private final DisplayListener displayListener;
    private final DisplaysCommand displaysCommand;
    private final AttributeDefaultService attributeDefaultService;
    private final LegacyCachingBukkitTextFormatter textFormatter;

    public DisplayModule(JavaPlugin plugin, AnimationManager animationManager, PlatformAdapter platformAdapter) {
        this.plugin = plugin;
        DisplayVisibilityService visibilityService = new DisplayVisibilityService();
        DisplayRenderIntentMaterializer renderDiffService = new DisplayRenderIntentMaterializer();
        PresentedRenderStateManager renderStateManager = new PresentedRenderStateManager();
        DisplayPlaceholderService displayPlaceholderService = new DisplayPlaceholderService(platformAdapter);
        AnimationCompiler animationCompiler = new AnimationCompiler(animationManager);
        this.textFormatter = new LegacyCachingBukkitTextFormatter();
        DisplayTypeRegistry displayTypeRegistry = createDisplayTypeRegistry(displayPlaceholderService, animationCompiler);
        AttributeDefinitionRegistry attributeDefinitionRegistry = new AttributeDefinitionRegistry();
        TextPostProcessor textPostProcessor = new TextPostProcessor(animationManager, textFormatter);
        DisplayPostProcessor postProcessingService = new DisplayPostProcessor(attributeDefinitionRegistry, textPostProcessor);
        DisplayRenderService renderService = new DisplayRenderService(renderDiffService, platformAdapter.getRenderService(), renderStateManager, postProcessingService);
        LogicalRenderStateService stateService = new LogicalRenderStateService(displayTypeRegistry);
        LogicalRenderStateManager logicalRenderStateManager = new LogicalRenderStateManager();
        PlatformPlayerService playerService = platformAdapter.getPlayerService();
        DisplayRenderCoordinator renderCoordinator = new DisplayRenderCoordinator(
                visibilityService, playerService, stateService, renderService, logicalRenderStateManager);
        AttributeValueTypeRegistry attributeValueTypeRegistry = createAttributeValueTypeRegistry(displayPlaceholderService);
        AttributeValueSerializer attributeValueSerializer = new AttributeValueSerializer(attributeValueTypeRegistry);
        YamlConfigurationLoaderFactory yamlConfigurationLoaderFactory = new YamlConfigurationLoaderFactory(createTypeSerializers(attributeValueSerializer));
        DisplayRepository configService = new DisplayRepository(plugin, yamlConfigurationLoaderFactory);
        AttributeConfigMapper attributeConfigMapper = new AttributeConfigMapper(attributeDefinitionRegistry, attributeValueTypeRegistry);
        DisplayConfigMapper configMapper = new DisplayConfigMapper(attributeConfigMapper, platformAdapter.getMaterialService());
        DisplayPersistenceService persistenceService = new DisplayPersistenceService(configService, configMapper);
        DisplayCloneService displayCloneService = new DisplayCloneService();
        this.displayService = new DisplayService(persistenceService, renderCoordinator, platformAdapter.getEventListener());
        this.displayListener = new DisplayListener(displayService, playerService);
        AttributeCommandHandlerRegistry commandHandlerRegistry = createCommandHandlerRegistry(displayPlaceholderService);
        AttributeDefaultRegistry attributeDefaultRegistry = new AttributeDefaultRegistry();
        AttributeDefaultRepository attributeDefaultRepository = new AttributeDefaultRepository(
                yamlConfigurationLoaderFactory, attributeDefinitionRegistry, attributeValueTypeRegistry, plugin.getDataFolder().toPath());
        this.attributeDefaultService = new AttributeDefaultService(attributeDefaultRegistry, attributeDefinitionRegistry, attributeDefaultRepository);
        AttributeCommandService attributeCommandService = new AttributeCommandService(
                attributeDefinitionRegistry, commandHandlerRegistry, attributeDefaultService);
        DisplayAttributeService displayAttributeService = new DisplayAttributeService(attributeDefinitionRegistry);
        this.displaysCommand = new DisplaysCommand(
                displayService, displayCloneService, attributeCommandService, attributeDefaultService, displayAttributeService, platformAdapter.getMaterialService());
        this.displayUpdateScheduler = new DisplayUpdateScheduler(displayService, renderCoordinator);
    }

    private AttributeCommandHandlerRegistry createCommandHandlerRegistry(DisplayPlaceholderService placeholderService) {
        AttributeCommandHandlerRegistry registry = new AttributeCommandHandlerRegistry();
        registry.register(BillboardAttributeDefinition.KEY, new DefaultEnumHandler<>(DisplayBillboardConstraints.class, BillboardConstraintsValue::new));
        registry.register(BrightnessAttributeDefinition.KEY, new DefaultBrightnessHandler());
        DefaultColorHandler defaultColorHandler = new DefaultColorHandler();
        registry.register(GlowColorAttributeDefinition.KEY, defaultColorHandler);
        registry.register(ItemDisplayTypeAttributeDefinition.KEY, new DefaultEnumHandler<>(ItemDisplayType.class, ItemDisplayTypeValue::new));
        registry.register(ItemEnchantedAttributeDefinition.KEY, new DefaultBooleanHandler());
        registry.register(ItemLeatherColorAttributeDefinition.KEY, defaultColorHandler);
        registry.register(ItemSkullTextureAttributeDefinition.KEY, new DefaultSkullTextureHandler(new StringValueFactory(placeholderService)));
        registry.register(PitchAttributeDefinition.KEY, new DefaultPitchHandler());
        registry.register(ScaleAttributeDefinition.KEY, new DefaultScaleHandler());
        registry.register(ShadowRadiusAttributeDefinition.KEY, new DefaultShadowRadiusHandler());
        registry.register(ShadowStrengthAttributeDefinition.KEY, new DefaultShadowStrengthHandler());
        registry.register(TextAlignmentAttributeDefinition.KEY, new DefaultEnumHandler<>(TextDisplayAlignment.class, TextAlignmentValue::new));
        registry.register(TextBackgroundColorAttributeDefinition.KEY, defaultColorHandler);
        registry.register(TextLineWidthAttributeDefinition.KEY, new DefaultLineWidthHandler());
        registry.register(TextOpacityAttributeDefinition.KEY, new DefaultOpacityHandler());
        registry.register(TextSeeThroughAttributeDefinition.KEY, new DefaultBooleanHandler());
        registry.register(TextShadowAttributeDefinition.KEY, new DefaultBooleanHandler());
        registry.register(TranslationAttributeDefinition.KEY, new DefaultTranslationHandler());
        registry.register(YawAttributeDefinition.KEY, new DefaultYawHandler());
        return registry;
    }

    private AttributeValueTypeRegistry createAttributeValueTypeRegistry(DisplayPlaceholderService placeholderService) {
        AttributeValueTypeRegistry registry = new AttributeValueTypeRegistry();
        registry.register(new BooleanValueType());
        registry.register(new FloatValueType());
        registry.register(new IntegerValueType());
        registry.register(new StringValueType(new StringValueFactory(placeholderService)));
        registry.register(new Vector3fValueType());
        registry.register(new BillboardConstraintsValueType());
        registry.register(new BrightnessValueType());
        registry.register(new ItemDisplayTypeValueType());
        registry.register(new TextAlignmentValueType());
        registry.register(new RgbaValueType());
        return registry;
    }

    private TypeSerializerCollection createTypeSerializers(AttributeValueSerializer attributeValueSerializer) {
        return TypeSerializerCollection.builder()
                .register(DecentLocation.class, new DecentLocationSerializer())
                .register(DecentVector3f.class, new DisplayVector3fSerializer())
                .register(DecentColor.class, new DisplayColorSerializer())
                .register(DisplayBrightness.class, new DisplayBrightnessSerializer())
                .register(ConfigAttribute.class, new ConfigAttributeSerializer(attributeValueSerializer))
                .register(ConfigDefaultAttribute.class, new ConfigDefaultAttributeSerializer(attributeValueSerializer))
                .build();
    }

    private DisplayTypeRegistry createDisplayTypeRegistry(DisplayPlaceholderService displayPlaceholderService,
                                                          AnimationCompiler animationCompiler) {
        DisplayTypeRegistry registry = new DisplayTypeRegistry();
        registry.registerDisplayType(DisplayType.TEXT, initializeTextDisplayType(displayPlaceholderService, animationCompiler));
        registry.registerDisplayType(DisplayType.ITEM, new ItemDisplayTypeDefinition());
        registry.registerDisplayType(DisplayType.BLOCK, new BlockDisplayTypeDefinition());
        return registry;
    }

    private TextDisplayTypeDefinition initializeTextDisplayType(DisplayPlaceholderService displayPlaceholderService,
                                                                AnimationCompiler animationCompiler) {
        return new TextDisplayTypeDefinition(displayPlaceholderService, animationCompiler);
    }

    public void initialize() {
        this.attributeDefaultService.reload();
        this.displayService.reload();
        this.displayUpdateScheduler.start();
        Bukkit.getPluginManager().registerEvents(displayListener, plugin);
    }

    public void reload() {
        this.attributeDefaultService.reload();
        this.displayService.reload();
    }

    public void shutdown() {
        HandlerList.unregisterAll(displayListener);
        this.displayUpdateScheduler.shutdown();
        this.displayService.shutdown();
        this.attributeDefaultService.shutdown();
        this.textFormatter.invalidate();
    }

    public DisplayService getDisplayService() {
        return displayService;
    }

    public DisplaysCommand getDisplaysCommand() {
        return displaysCommand;
    }
}
