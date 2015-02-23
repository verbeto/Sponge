package org.spongepowered.mod.mixin.plugin.entityactivation;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.mod.mixin.plugin.CoreMixinPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EntityActivationPlugin implements IMixinConfigPlugin {

    private List<String> mixins = new ArrayList<String>();

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!CoreMixinPlugin.getGlobalConfig().getRootNode().getNode("modules", "entity-activation-range").getBoolean()
                && mixinClassName.contains("mixin.entityactivation")) {
            return false;
        }
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

}
