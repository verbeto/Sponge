package org.spongepowered.mod.mixin.plugin.timings;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.mod.SpongeMod;

import java.io.PrintStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Provides custom timing sections for /timings merged.
 */
public class CustomTimingsHandler {

    private static Queue<CustomTimingsHandler> HANDLERS = new ConcurrentLinkedQueue<CustomTimingsHandler>();
    public static boolean USE_TIMINGS = false;
    private final String name;
    private final CustomTimingsHandler parent;
    private long count = 0;
    private long start = 0;
    private long timingDepth = 0;
    private long totalTime = 0;
    private long curTickTotal = 0;
    private long violations = 0;

    public CustomTimingsHandler(String name) {
        this(name, null);
    }

    public CustomTimingsHandler(String name, CustomTimingsHandler parent) {
        this.name = name;
        this.parent = parent;
        HANDLERS.add(this);
    }

    /**
     * Prints the timings and extra data to the given stream.
     * 
     * @param printStream
     */
    public static void printTimings(PrintStream printStream) {
        printStream.println("Minecraft");
        for (CustomTimingsHandler timings : HANDLERS) {
            long time = timings.totalTime;
            long count = timings.count;
            if (count == 0) {
                continue;
            }
            long avg = time / count;

            printStream
                    .println("    " + timings.name + " Time: " + time + " Count: " + count + " Avg: " + avg + " Violations: " + timings.violations);
        }
        printStream.println("# Version " + SpongeMod.instance.getGame().getMinecraftVersion());
        int entities = 0;
        //int livingEntities = 0;
        for (WorldServer world : DimensionManager.getWorlds()) {
            org.spongepowered.api.world.World spongeWorld = (org.spongepowered.api.world.World) world;
            entities += spongeWorld.getEntities().size();
            //livingEntities += spongeWorld.getLivingEntities().size();
        }
        printStream.println("# Entities " + entities);
        //printStream.println( "# LivingEntities " + livingEntities );
    }

    /**
     * Resets all timings.
     */
    public static void reload() {
        if (USE_TIMINGS) {
            for (CustomTimingsHandler timings : HANDLERS) {
                timings.reset();
            }
        }
        //CommandSponge.timingStart = System.nanoTime();
    }

    /**
     * Ticked every tick by CraftBukkit to count the number of times a timer
     * caused TPS loss.
     */
    public static void tick()
    {
        if (USE_TIMINGS) {
            for (CustomTimingsHandler timings : HANDLERS) {
                if (timings.curTickTotal > 50000000) {
                    timings.violations += Math.ceil(timings.curTickTotal / 50000000);
                }
                timings.curTickTotal = 0;
                timings.timingDepth = 0; // incase reset messes this up
            }
        }
    }

    /**
     * Starts timing to track a section of code.
     */
    public void startTiming() {
        // If second condtion fails we are already timing
        if (++this.timingDepth == 1) {
            this.start = System.nanoTime();
            if (this.parent != null && ++this.parent.timingDepth == 1) {
                this.parent.start = this.start;
            }
        }
    }

    /**
     * Stops timing a section of code.
     */
    public void stopTiming() {
        if (USE_TIMINGS) {
            if (--this.timingDepth != 0 || this.start == 0) {
                return;
            }
            long diff = System.nanoTime() - this.start;
            this.totalTime += diff;
            this.curTickTotal += diff;
            this.count++;
            this.start = 0;
            if (this.parent != null) {
                this.parent.stopTiming();
            }
        }
    }

    /**
     * Reset this timer, setting all values to zero.
     */
    public void reset() {
        this.count = 0;
        this.violations = 0;
        this.curTickTotal = 0;
        this.totalTime = 0;
        this.start = 0;
        this.timingDepth = 0;
    }
}
