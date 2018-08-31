package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.CommandUtils.ChatType;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

public class GiveWarpCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "givewarp";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "givewarp";
	}
	
	//@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		WarpWorldStorage storage = WarpWorldStorage.get(sender.getEntityWorld());
		String name;
		EntityPlayer player;
		switch (args.length) {
		case 1:
			name = args[0];
			if (sender instanceof EntityPlayer) {
				player = (EntityPlayer)sender;
			}
			else {
				CommandUtils.showError(sender, I18n.translateToLocal("help.noplayerspecified").trim());
				return;
			}
			break;
		case 2:
			name = args[0];
			try {
				player = CommandBase.getPlayer(server, sender, args[1]);
			}
			catch (PlayerNotFoundException e) {
				CommandUtils.showError(sender, ChatType.TYPE_player, args[1]);
				return;
			}
			break;
		default:
			CommandUtils.printUsage(sender, this);
			return;
		}
		if (!storage.waypointExists(name)) {
			CommandUtils.showError(sender, String.format(I18n.translateToLocal("help.waypointdoesnotexist").trim(), name));
			return;
		}
		ItemStack hyperStack = new ItemStack(WarpBookMod.items.hyperWarpPotionItem);
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("hypername", name);
		hyperStack.setTagCompound(compound);
		player.inventory.addItemStackToInventory(hyperStack);
	}
	
	public int compareTo(ICommand command) {
		return this.getName().compareTo(command.getName());
	}

}
