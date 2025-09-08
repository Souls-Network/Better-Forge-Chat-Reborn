package com.rvt.bfcrmod.utils;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class Utils {
    public static String parseText(ServerPlayer player, String text) {
        TagResolver resolver = MiniPlaceholders.getGlobalPlaceholders();

        if(player != null) {
            resolver = TagResolver.resolver(resolver, MiniPlaceholders.getAudiencePlaceholders((Audience) player));
        }

        var server = ServerLifecycleHooks.getCurrentServer();

//        System.out.println("Server is " + (server != null ? " not" : "") + " null");

        if (server != null)


            return MinecraftServerAudiences.of(server).asNative(MiniMessage.miniMessage().deserialize(text, resolver)).getString();
        return text;
    }

    public static Component parseTextAsComponent(ServerPlayer player, String text) {
        TagResolver resolver = MiniPlaceholders.getGlobalPlaceholders();

        if(player != null) {
            resolver = TagResolver.resolver(resolver, MiniPlaceholders.getAudiencePlaceholders((Audience) player));
        }

        var server = ServerLifecycleHooks.getCurrentServer();

//        System.out.println("Server is " + (server != null ? " not" : "") + " null");

        if (server != null)


            return MinecraftServerAudiences.of(server).asNative(MiniMessage.miniMessage().deserialize(text, resolver));
        else {
            return Component.literal("[What?] " + text);
        }
    }
}
