package com.natamus.enchantingcommands.neoforge.events;

import com.natamus.enchantingcommands.cmds.CommandEc;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class NeoForgeCommandRegisterEvent {
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent e) {
		CommandEc.register(e.getDispatcher(), e.getBuildContext());
	}
}
