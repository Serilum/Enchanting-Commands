package com.serilum.enchantingcommands.cmds;
import com.serilum.enchantingcommands.util.Reference;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.natamus.collective.functions.MessageFunctions;
import com.serilum.enchantingcommands.config.ConfigHandler;
import com.serilum.enchantingcommands.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CommandEc {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
		dispatcher.register(Commands.literal(ConfigHandler.enchantCommandString)
			.requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				sendUsage(command.getSource());
				return 1;
			})
			.then(Commands.literal("list")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();

				String joined = String.join(", ", Util.getEnchantmentKeys());
				MessageFunctions.sendTranslatableMessage(source, "collective.enchantingcommands.message.list", true, ChatFormatting.DARK_GREEN, Reference.NAME);
				MessageFunctions.sendMessage(source, " " + joined, ChatFormatting.DARK_GREEN);
				return 1;
			}))
			.then(Commands.literal("enchant")
			.then(Commands.argument("enchantment", ResourceArgument.resource(commandBuildContext, Registries.ENCHANTMENT))
			.then(Commands.argument("level", IntegerArgumentType.integer(0, 255))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Entity entity = source.getEntity();
				if (!(entity instanceof ServerPlayer)) {
					MessageFunctions.sendTranslatableMessage(source, "collective.shared.message.playeronly", ChatFormatting.RED);
					return 1;
				}

				Player player = (ServerPlayer)entity;
				ItemStack held = player.getMainHandItem();

				Enchantment enchantment = ResourceArgument.getEnchantment(command, "enchantment").value();
				int level = IntegerArgumentType.getInteger(command, "level");

				if (!player.hasItemInSlot(EquipmentSlot.MAINHAND)) {
					MessageFunctions.sendTranslatableMessage(player, "collective.enchantingcommands.message.enchantableitemmain", ChatFormatting.RED);
					return 0;
				}

				ResourceLocation enchantmentId = EnchantmentHelper.getEnchantmentId(enchantment);
				ListTag enchantmentTags = held.getEnchantmentTags();

				boolean removed = false;
				for (int i = enchantmentTags.size() - 1; i >= 0; i--) {
					if (enchantmentId.equals(EnchantmentHelper.getEnchantmentId(enchantmentTags.getCompound(i)))) {
						enchantmentTags.remove(i);
						removed = true;
					}
				}

				String enchantmentname = enchantment.getDescriptionId().replace("enchantment.", "");
				if (level != 0) {
					enchantmentTags.add(EnchantmentHelper.storeEnchantment(enchantmentId, level));
					held.addTagElement(ItemStack.TAG_ENCH, enchantmentTags);
					MessageFunctions.sendTranslatableMessage(player, "collective.enchantingcommands.message.enchantmentaddeditem", ChatFormatting.DARK_GREEN, enchantmentname, level);
				}
				else if (removed) {
					MessageFunctions.sendTranslatableMessage(player, "collective.enchantingcommands.message.enchantmentremovedfrom", ChatFormatting.DARK_GREEN, enchantmentname);
				}
				else {
					MessageFunctions.sendTranslatableMessage(player, "collective.enchantingcommands.message.enchantmentexistitem", ChatFormatting.RED, enchantmentname);
				}
				return 1;
			}))))
		);
	}

	public static void sendUsage(CommandSourceStack source) {
		MessageFunctions.sendTranslatableMessage(source, "collective.enchantingcommands.message.usage", true, ChatFormatting.DARK_GREEN, Reference.NAME);
		MessageFunctions.sendMessage(source, " /" + ConfigHandler.enchantCommandString + " list", ChatFormatting.DARK_GREEN);
		MessageFunctions.sendMessage(source, " /" + ConfigHandler.enchantCommandString + " enchant <enchant> <lvl>", ChatFormatting.DARK_GREEN);
	}

	public static void sendUsage(Player player) {
		MessageFunctions.sendTranslatableMessage(player, "collective.enchantingcommands.message.usage", true, ChatFormatting.DARK_GREEN, Reference.NAME);
		MessageFunctions.sendMessage(player, " /" + ConfigHandler.enchantCommandString + " list", ChatFormatting.DARK_GREEN);
		MessageFunctions.sendMessage(player, " /" + ConfigHandler.enchantCommandString + " enchant <enchant> <lvl>", ChatFormatting.DARK_GREEN);
	}
}