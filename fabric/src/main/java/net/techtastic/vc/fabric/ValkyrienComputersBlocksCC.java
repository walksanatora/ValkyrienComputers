package net.techtastic.vc.fabric;

import dan200.computercraft.ComputerCraft;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.techtastic.vc.ValkyrienComputersConfig;
import net.techtastic.vc.ValkyrienComputersConfig.Server.COMPUTERCRAFT;
import net.techtastic.vc.ValkyrienComputersMod;

public class ValkyrienComputersBlocksCC {

	public static Block RADAR;
	public static Block SHIP_READER;

	public static Block registerBlock(String name, Block block) {
		registerBlockItem(name, block);
		return Registry.register(Registry.BLOCK,
				new ResourceLocation(ValkyrienComputersMod.MOD_ID, name),
				block
		);
	}

	public static Item registerBlockItem(String name, Block block) {
		return Registry.register(Registry.ITEM,
				new ResourceLocation(ValkyrienComputersMod.MOD_ID, name),
				new BlockItem(block, new FabricItemSettings().group(ComputerCraft.MAIN_GROUP)));
	}
	public static void registerCCBlocks() {
		COMPUTERCRAFT config = ValkyrienComputersConfig.SERVER.getComputerCraft();
		if (!config.getDisableRadars()) {
			RADAR = registerBlock("radar_cc", new Block(FabricBlockSettings.of(Material.METAL)));
		}
		if (!config.getDisableShipReaders()) {
			SHIP_READER = registerBlock("reader_cc", new Block(FabricBlockSettings.of(Material.METAL)));
		}
	}
}
