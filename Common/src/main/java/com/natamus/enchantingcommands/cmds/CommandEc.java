package com.natamus.enchantingcommands.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.enchantingcommands.config.ConfigHandler;
import com.natamus.enchantingcommands.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

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
				StringFunctions.sendMessage(source, "--- Enchanting Commands List ---", ChatFormatting.DARK_GREEN, true);
				StringFunctions.sendMessage(source, " " + joined, ChatFormatting.DARK_GREEN);
				return 1;
			}))
			.then(Commands.literal("enchant")
			.then(Commands.argument("enchantment", ResourceArgument.resource(commandBuildContext, Registries.ENCHANTMENT))
			.then(Commands.argument("level", IntegerArgumentType.integer(0, 127))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Entity entity = source.getEntity();
				if (!(entity instanceof ServerPlayer)) {
					StringFunctions.sendMessage(source, "This command can only be executed as a player.", ChatFormatting.RED);
					return 1;
				}

				Player player = (ServerPlayer)entity;
				ItemStack held = player.getMainHandItem();

				Enchantment enchantment = ResourceArgument.getEnchantment(command, "enchantment").value();
				int level = IntegerArgumentType.getInteger(command, "level");

				if (!player.hasItemInSlot(EquipmentSlot.MAINHAND)) {
					StringFunctions.sendMessage(player, "You do not have an enchantable item in your main hand.", ChatFormatting.RED);
					return 0;
				}

				ItemStack temp = new ItemStack(Item.byId(1));
				temp.enchant(enchantment, level);
				String estringtemp = temp.getEnchantmentTags().get(0).toString().split("id:")[1];

				boolean removed = false;
				for (Tag nbt : held.getEnchantmentTags()) {
					if (estringtemp.equals(nbt.toString().split("id:")[1])) {
						held.getEnchantmentTags().remove(nbt);
						removed = true;
						break;
					}
				}

				String enchantmentname = enchantment.getDescriptionId().replace("enchantment.", "");
				if (level != 0) {
					held.enchant(enchantment, level);
					StringFunctions.sendMessage(player, "The enchantment '" + enchantmentname + "' has been added to the item with a level of " + level + ".", ChatFormatting.DARK_GREEN);
				}
				else if (removed) {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantmentname + "' has been removed from the item.", ChatFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantmentname + "' does not exist on the item.", ChatFormatting.RED);
				}
				return 1;
			}))))
		);
	}

	public static void sendUsage(CommandSourceStack source) {
		StringFunctions.sendMessage(source, "--- Enchanting Commands Usage ---", ChatFormatting.DARK_GREEN, true);
		StringFunctions.sendMessage(source, " /" + ConfigHandler.enchantCommandString + " list", ChatFormatting.DARK_GREEN);
		StringFunctions.sendMessage(source, " /" + ConfigHandler.enchantCommandString + " enchant <enchant> <lvl>", ChatFormatting.DARK_GREEN);
	}

	public static void sendUsage(Player player) {
		StringFunctions.sendMessage(player, "--- Enchanting Commands Usage ---", ChatFormatting.DARK_GREEN, true);
		StringFunctions.sendMessage(player, " /" + ConfigHandler.enchantCommandString + " list", ChatFormatting.DARK_GREEN);
		StringFunctions.sendMessage(player, " /" + ConfigHandler.enchantCommandString + " enchant <enchant> <lvl>", ChatFormatting.DARK_GREEN);
	}
}