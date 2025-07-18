package com.natamus.enchantingcommands.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static List<String> getEnchantmentKeys(Level level) {
		List<String> output = new ArrayList<String>();
		for (ResourceLocation rl : level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).keySet()) {
			output.add(rl.toString().replaceAll("minecraft:", ""));
		}
		return output;
	}
}
