package com.natamus.enchantingcommands.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static List<String> getEnchantmentKeys() {
		List<String> output = new ArrayList<String>();
		for (ResourceLocation rl : BuiltInRegistries.ENCHANTMENT.keySet()) {
			output.add(rl.toString().replaceAll("minecraft:", ""));
		}
		return output;
	}
}
