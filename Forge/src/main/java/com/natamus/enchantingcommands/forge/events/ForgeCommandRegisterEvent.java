package com.natamus.enchantingcommands.forge.events;

import com.natamus.enchantingcommands.cmds.CommandEc;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeCommandRegisterEvent {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent e) {
    	CommandEc.register(e.getDispatcher(), e.getBuildContext());
    }
}
