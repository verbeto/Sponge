package org.spongepowered.mod.mixin.timings;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.apache.logging.log4j.Level;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.mod.SpongeMod;
import org.spongepowered.mod.command.CommandSponge;
import org.spongepowered.mod.configuration.SpongeConfig;
import org.spongepowered.mod.interfaces.IMixinWorld;
import org.spongepowered.mod.interfaces.IMixinWorldProvider;
import org.spongepowered.mod.mixin.plugin.CoreMixinPlugin;
import org.spongepowered.mod.mixin.plugin.timings.CustomTimingsHandler;
import org.spongepowered.mod.registry.SpongeGameRegistry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@NonnullByDefault
@Mixin(CommandSponge.class)
public abstract class MixinCommandSponge {

    private static long timingStart = 0;

    @Shadow
    private static final List<String> FLAG_COMMANDS = ImmutableList.of("save", "chunks", "conf", "reload");

    @Shadow
    private static final List<String> COMMANDS = ImmutableList.of("chunks", "conf", "heap", "help", "reload", "save", "timings", "version");

    @Shadow
    private static final List<String> ALIASES = ImmutableList.of("sp");

    @Shadow
    private static final String USAGE_CONF =
            EnumChatFormatting.WHITE + "Usage:\n" + EnumChatFormatting.GREEN + "/sponge conf [-g] [-d dim] [-w world] key value";

    @Shadow
    private static final String USAGE_RELOAD =
            EnumChatFormatting.WHITE + "Usage:\n" + EnumChatFormatting.GREEN + "/sponge reload [-g] [-d dim|*] [-w world|*]";

    @Shadow
    private static final String USAGE_SAVE =
            EnumChatFormatting.WHITE + "Usage:\n" + EnumChatFormatting.GREEN + "/sponge save [-g] [-d dim|*] [-w world|*]";

    @Shadow
    private static final String USAGE_CHUNKS =
            EnumChatFormatting.WHITE + "Usage:\n" + EnumChatFormatting.GREEN + "/sponge chunks [-g] [-d dim] [-w world]";

    @Shadow
    public abstract String getCommandUsage(ICommandSender par1ICommandSender);

    @Shadow
    public abstract String getCommandUsage(String command);

    @Shadow
    public abstract void processHeap(ICommandSender sender, String[] args);

    @Shadow
    public abstract void processChunks(SpongeConfig.Type type, WorldServer world, DimensionType dimensionType, ICommandSender sender, String[] args);

    @Shadow
    public abstract boolean getToggle(SpongeConfig config, ICommandSender sender, String key);

    @Shadow
    public abstract boolean setToggle(SpongeConfig config, ICommandSender sender, String key, String value);

    @Overwrite
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            String command = args[0];
            SpongeConfig config = CoreMixinPlugin.getGlobalConfig();

            if (COMMANDS.contains(command)) {
                if (FLAG_COMMANDS.contains(command)) {

                    String name = "";
                    WorldServer world = null;
                    DimensionType dimensionType = null;
                    if (sender instanceof EntityPlayer) {
                        world = (WorldServer) ((EntityPlayer) sender).worldObj;
                    }

                    if (command.equalsIgnoreCase("conf")) {
                        if (args.length < 3) {
                            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Improper conf syntax detected.\n"
                                    + USAGE_CONF));
                            return;
                        }

                        name = args[2];
                    } else {
                        if (args.length < 2) {
                            sender.addChatMessage(new ChatComponentText(
                                    EnumChatFormatting.RED + "Improper " + command + " syntax detected.\n" + getCommandUsage(command)));
                            return;
                        }

                        if (args.length > 2) {
                            name = args[2];
                        }
                    }

                    String flag = args[1];

                    if (flag.equalsIgnoreCase("-d")) {
                        if (name.equals("") || name.equals("*")) {
                            if (sender instanceof EntityPlayer) {
                                config = ((IMixinWorldProvider) ((EntityPlayer) sender).worldObj.provider).getDimensionConfig();
                            } else {
                                sender.addChatMessage(
                                        new ChatComponentText(EnumChatFormatting.RED + "Console requires a valid dimension name.\n"
                                                + USAGE_CONF));
                                return;
                            }
                        } else {
                            for (DimensionType dimType : ((SpongeGameRegistry) SpongeMod.instance.getGame().getRegistry()).getDimensionTypes()) {
                                if (dimType.getName().equalsIgnoreCase(name)) {
                                    config = SpongeGameRegistry.dimensionConfigs.get(dimType.getDimensionClass());
                                    dimensionType = dimType;
                                    break;
                                }
                            }
                            if (config == CoreMixinPlugin.getGlobalConfig()) {
                                sender.addChatMessage(new ChatComponentText("Dimension '" + EnumChatFormatting.RED + name + EnumChatFormatting.WHITE
                                        + "' does not exist. Please enter a valid dimension."));
                                return;
                            }
                        }
                    } else if (flag.equalsIgnoreCase("-w")) {
                        if (name.equals("") || name.equals("*")) {
                            if (sender instanceof EntityPlayer) {
                                config = ((IMixinWorld) ((EntityPlayer) sender).worldObj).getWorldConfig();
                            } else {
                                sender.addChatMessage(
                                        new ChatComponentText(EnumChatFormatting.RED + "Console requires a valid world name.\n"
                                                + USAGE_CONF));
                                return;
                            }
                        } else {
                            for (WorldServer worldserver : DimensionManager.getWorlds()) {
                                if (worldserver.provider.getSaveFolder() == null && name.equalsIgnoreCase("DIM0")) {
                                    config = ((IMixinWorld) worldserver).getWorldConfig();
                                    world = worldserver;
                                    break;
                                }
                                if (worldserver.provider.getSaveFolder() != null && worldserver.provider.getSaveFolder().equalsIgnoreCase(name)) {
                                    config = ((IMixinWorld) worldserver).getWorldConfig();
                                    world = worldserver;
                                    break;
                                }
                            }
                            if (config == CoreMixinPlugin.getGlobalConfig()) {
                                sender.addChatMessage(new ChatComponentText(
                                        EnumChatFormatting.RED + "World " + EnumChatFormatting.AQUA + name + EnumChatFormatting.RED
                                                + " does not exist. Please enter a valid world."));
                                return;
                            }
                        }
                    } else if (flag.equalsIgnoreCase("-g")) {
                        config = CoreMixinPlugin.getGlobalConfig();
                    } else {
                        sender.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.AQUA + flag + EnumChatFormatting.RED + " is not recognized as a valid flag.\n"
                                        + USAGE_CONF));
                        return;
                    }

                    if (command.equalsIgnoreCase("save")) {
                        String configName = config.getConfigName();
                        if (name.equals("")) {
                            config.save();
                        } else if (name.equals("*") && config.getType() == SpongeConfig.Type.WORLD) {
                            for (WorldServer worldserver : DimensionManager.getWorlds()) {
                                SpongeConfig worldConfig = ((IMixinWorld) worldserver).getWorldConfig();
                                worldConfig.save();
                            }
                            configName = "ALL";
                        } else if (name.equals("*") && config.getType() == SpongeConfig.Type.DIMENSION) {
                            for (SpongeConfig dimensionConfig : SpongeGameRegistry.dimensionConfigs.values()) {
                                dimensionConfig.save();
                            }
                            configName = "ALL";
                        }
                        sender.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.GREEN + "Saved " + EnumChatFormatting.GOLD + config.getType() + EnumChatFormatting.GREEN
                                        + " configuration: " + EnumChatFormatting.AQUA + configName));
                    } else if (command.equalsIgnoreCase("reload")) {
                        config.reload();
                        String configName = config.getConfigName();
                        if (name.equals("")) {
                            config.reload();
                        } else if (name.equals("*") && config.getType() == SpongeConfig.Type.WORLD) {
                            for (WorldServer worldserver : DimensionManager.getWorlds()) {
                                SpongeConfig worldConfig = ((IMixinWorld) worldserver).getWorldConfig();
                                worldConfig.reload();
                            }
                            configName = "ALL";
                        } else if (name.equals("*") && config.getType() == SpongeConfig.Type.DIMENSION) {
                            for (SpongeConfig dimensionConfig : SpongeGameRegistry.dimensionConfigs.values()) {
                                dimensionConfig.reload();
                            }
                            configName = "ALL";
                        }

                        sender.addChatMessage(new ChatComponentText(
                                EnumChatFormatting.GREEN + "Reloaded " + EnumChatFormatting.GOLD + config.getType() + EnumChatFormatting.GREEN
                                        + " configuration: " + EnumChatFormatting.AQUA + configName));
                    } else if (command.equalsIgnoreCase("chunks")) {
                        processChunks(config.getType(), world, dimensionType, sender, args);
                    } else {
                        if (config.getSetting(args[args.length - 1]) != null) {
                            getToggle(config, sender, args[args.length - 1]);
                        } else {
                            setToggle(config, sender, args[args.length - 2], args[args.length - 1]);
                        }
                    }

                } else if ("version".equalsIgnoreCase(command)) {
                    sender.addChatMessage(new ChatComponentText(
                            "SpongeMod : " + EnumChatFormatting.GREEN + SpongeMod.instance.getGame().getImplementationVersion() + "\n" +
                                    "SpongeAPI : " + EnumChatFormatting.GREEN + SpongeMod.instance.getGame().getApiVersion()));
                } else if (command.equalsIgnoreCase("heap")) {
                    processHeap(sender, args);
                } else if (command.equalsIgnoreCase("help")) {
                    sender.addChatMessage(new ChatComponentText("commands:\n" +
                            "    " + EnumChatFormatting.GREEN + "chunks   " + EnumChatFormatting.WHITE + "     "
                            + "Prints chunk data for a specific dimension or world(s)\n" +
                            "    " + EnumChatFormatting.GREEN + "conf   " + EnumChatFormatting.WHITE + "     " + "Configure sponge settings\n" +
                            "    " + EnumChatFormatting.GREEN + "heap   " + EnumChatFormatting.WHITE + "     " + "Dump live JVM heap\n" +
                            "    " + EnumChatFormatting.GREEN + "reload   " + EnumChatFormatting.WHITE + "     "
                            + "Reloads a global, dimension, or world config\n" +
                            "    " + EnumChatFormatting.GREEN + "save   " + EnumChatFormatting.WHITE + "     "
                            + "Saves a global, dimension, or world config\n" +
                            "    " + EnumChatFormatting.GREEN + "version" + EnumChatFormatting.WHITE + "     " + "Prints current sponge version"));
                } else if (command.equalsIgnoreCase("timings") && args.length > 1) {
                    String arg = args[1];
                    if (arg.equalsIgnoreCase("on")) {
                        CustomTimingsHandler.USE_TIMINGS = true;
                        CustomTimingsHandler.reload();
                        sender.addChatMessage(new ChatComponentText("Enabled Timings & Reset"));
                        return;
                    } else if (arg.equalsIgnoreCase("off")) {
                        CustomTimingsHandler.USE_TIMINGS = false;
                        sender.addChatMessage(new ChatComponentText("Disabled Timings"));
                        return;
                    }

                    if (!CustomTimingsHandler.USE_TIMINGS) {
                        sender.addChatMessage(new ChatComponentText("Please enable timings by typing /timings on"));
                        return;
                    }

                    boolean paste = arg.equalsIgnoreCase("paste");
                    if (arg.equalsIgnoreCase("reset")) {
                        CustomTimingsHandler.reload();
                        sender.addChatMessage(new ChatComponentText("Timings reset"));
                    } else if (arg.equalsIgnoreCase("merged") || arg.equalsIgnoreCase("report") || paste) {
                        long sampleTime = System.nanoTime() - timingStart;
                        int index = 0;
                        File timingFolder = new File("timings");
                        timingFolder.mkdirs();
                        File timings = new File(timingFolder, "timings.txt");
                        ByteArrayOutputStream bout = (paste) ? new ByteArrayOutputStream() : null;
                        while (timings.exists()) {
                            timings = new File(timingFolder, "timings" + (++index) + ".txt");
                        }
                        PrintStream fileTimings = null;
                        try {
                            fileTimings = (paste) ? new PrintStream(bout) : new PrintStream(timings);

                            CustomTimingsHandler.printTimings(fileTimings);
                            fileTimings.println("Sample time " + sampleTime + " (" + sampleTime / 1E9 + "s)");

                            if (paste) {
                                new PasteThread(sender, bout).start();
                                return;
                            }

                            sender.addChatMessage(new ChatComponentText("Timings written to " + timings.getPath()));
                            sender.addChatMessage(new ChatComponentText(
                                    "Paste contents of file into form at http://timings.aikar.co/go/timings to read results."));

                        } catch (IOException e) {
                        } finally {
                            if (fileTimings != null) {
                                fileTimings.close();
                            }
                        }
                    }
                }
            } else { // invalid command
                sender.addChatMessage(new ChatComponentText("'" + EnumChatFormatting.RED + command + EnumChatFormatting.WHITE + "'"
                        + " is not recognized as a valid command.\nAvailable commands are: \n" + EnumChatFormatting.GREEN
                        + COMMANDS.toString()));
                return;
            }
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Usage: " + getCommandUsage(sender)));
        }
    }

    private static class PasteThread extends Thread {

        private final ICommandSender sender;
        private final ByteArrayOutputStream bout;

        public PasteThread(ICommandSender sender, ByteArrayOutputStream bout) {
            super("Timings paste thread");
            this.sender = sender;
            this.bout = bout;
        }

        @Override
        public synchronized void start() {
            if (this.sender instanceof RConConsoleSource) {
                run();
            } else {
                super.start();
            }
        }

        @Override
        public void run() {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL("http://paste.ubuntu.com/").openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setInstanceFollowRedirects(false);

                OutputStream out = con.getOutputStream();
                out.write("poster=Sponge&syntax=text&content=".getBytes("UTF-8"));
                out.write(URLEncoder.encode(this.bout.toString("UTF-8"), "UTF-8").getBytes("UTF-8"));
                out.close();
                con.getInputStream().close();

                String location = con.getHeaderField("Location");
                String pasteID = location.substring("http://paste.ubuntu.com/".length(), location.length() - 1);
                this.sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
                        + "View timings results can be viewed at http://timings.aikar.co/?url=" + pasteID));
            } catch (IOException ex) {
                this.sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED
                        + "Error pasting timings, check your console for more information"));
                SpongeMod.instance.getLogger().log(Level.WARN, "Could not paste timings", ex);
            }
        }
    }
}
