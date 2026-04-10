package com.natamus.enchantingcommands.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.enchantingcommands.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static String enchantCommandString = "ec";

	public static void initConfig() {
		configMetaData.put("enchantCommandString", Arrays.asList(
			"The default command to use the features of this mod. By default /ec"
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}