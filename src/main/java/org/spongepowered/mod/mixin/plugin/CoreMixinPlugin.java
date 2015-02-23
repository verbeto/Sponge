package org.spongepowered.mod.mixin.plugin;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.mod.configuration.SpongeConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CoreMixinPlugin implements IMixinConfigPlugin {

    private List<String> mixins = new ArrayList<String>();
    private static SpongeConfig GLOBAL_CONFIG;
    private static File SPONGE_CONFIG_DIR = new File("." + File.separator + "config" + File.separator + "sponge" + File.separator);

    @Override
    public void onLoad(String mixinPackage) {
        try {
            GLOBAL_CONFIG = new SpongeConfig(SpongeConfig.Type.GLOBAL, new File(SPONGE_CONFIG_DIR, "global.conf"), "sponge");
        } catch (Throwable t) {
            LogManager.getLogger().error(ExceptionUtils.getStackTrace(t));
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName,
            String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return this.mixins;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass,
            String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass,
            String mixinClassName, IMixinInfo mixinInfo) {
    }

    public static SpongeConfig getGlobalConfig() {
        return GLOBAL_CONFIG;
    }

    public static File getConfigDir() {
        return SPONGE_CONFIG_DIR;
    }

}
