/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.text.title;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S45PacketTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.mod.text.SpongeText;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SpongeTitles {

    public static int DEFAULT_FADE_IN = 20;
    public static int DEFAULT_STAY = 20;
    public static int DEFAULT_FADE_OUT = 20;

    private static LoadingCache<Title, S45PacketTitle[]> titlePackets = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build(new CacheLoader<Title, S45PacketTitle[]>() {
                @Override
                public S45PacketTitle[] load(Title title) throws Exception {
                    Optional<Integer> fadeIn = title.getFadeIn();
                    Optional<Integer> stay = title.getStay();
                    Optional<Integer> fadeOut = title.getFadeOut();
                    Optional<Text> subtitle = title.getSubtitle();
                    Optional<Text> textTitle = title.getTitle();

                    S45PacketTitle[] packets = new S45PacketTitle[5];
                    int i = 0;

                    if (title.isClear()) {
                        packets[i++] = new S45PacketTitle(S45PacketTitle.Type.CLEAR, null);
                    }
                    if (title.isReset()) {
                        packets[i++] = new S45PacketTitle(S45PacketTitle.Type.RESET, null);
                    }
                    if (fadeIn.isPresent() || stay.isPresent() || fadeOut.isPresent()) {
                        packets[i++] = new S45PacketTitle(
                                title.getFadeIn().or(DEFAULT_FADE_IN),
                                title.getStay().or(DEFAULT_STAY),
                                title.getFadeOut().or(DEFAULT_FADE_OUT));
                    }
                    if (subtitle.isPresent()) {
                        packets[i++] = new S45PacketTitle(S45PacketTitle.Type.SUBTITLE, ((SpongeText) subtitle.get()).toComponent());
                    }
                    if (textTitle.isPresent()) {
                        packets[i++] = new S45PacketTitle(S45PacketTitle.Type.TITLE, ((SpongeText) textTitle.get()).toComponent());
                    }

                    packets = i == packets.length ? packets : Arrays.copyOf(packets, i);
                    return packets;
                }
            });

    public static void send(Title title, EntityPlayerMP player) {
        for (S45PacketTitle packet : titlePackets.getUnchecked(title)) {
            player.playerNetServerHandler.sendPacket(packet);
        }
    }

}
