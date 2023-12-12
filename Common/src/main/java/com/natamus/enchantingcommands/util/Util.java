package com.natamus.enchantingcommands.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static List<String> getEnchantmentKeys() {
		List<String> output = new ArrayList<String>();
		for (ResourceLocation rl : Registry.ENCHANTMENT.keySet()) {
			output.add(rl.toString().replaceAll("minecraft:", ""));
		}
		return output;
	}
}
