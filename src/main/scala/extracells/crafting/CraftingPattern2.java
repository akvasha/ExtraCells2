package extracells.crafting;

import appeng.api.AEApi;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;
import extracells.registries.ItemEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CraftingPattern2 extends CraftingPattern {

	private boolean needExtra = false;

	public CraftingPattern2(ICraftingPatternDetails _pattern) {
		super(_pattern);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		CraftingPattern other = (CraftingPattern) obj;
		if (this.pattern != null && other.pattern != null)
			return this.pattern.equals(other.pattern);
		return false;
	}

	@Override
	public IAEItemStack[] getCondensedInputs() {
		IAEItemStack[] s = super.getCondensedInputs();
		if (s.length == 0) {
			s = new IAEItemStack[1];
			s[0] = AEApi
					.instance()
					.storage()
					.createItemStack(
							new ItemStack(ItemEnum.FLUIDPATTERN.getItem()));
			this.needExtra = true;
		}
		return s;
	}

	@Override
	public IAEItemStack[] getCondensedOutputs() {
		getCondensedInputs();
		IAEItemStack[] s = super.getCondensedOutputs();
		if (this.needExtra) {
			IAEItemStack[] s2 = new IAEItemStack[s.length + 1];
			for (int i = 0; i < s.length; i++) {
				s2[i] = s[i];
			}
			s2[s.length] = AEApi
					.instance()
					.storage()
					.createItemStack(
							new ItemStack(ItemEnum.FLUIDPATTERN.getItem()));
			return s2;
		}
		return s;
	}

	@Override
	public IAEItemStack[] getInputs() {
		IAEItemStack[] in = super.getInputs();
		if (in.length == 0) {
			in = new IAEItemStack[1];
			in[0] = AEApi
					.instance()
					.storage()
					.createItemStack(
							new ItemStack(ItemEnum.FLUIDPATTERN.getItem()));
		} else {
			for (IAEItemStack s : in) {
				if (s != null)
					return in;
			}
			in[0] = AEApi
					.instance()
					.storage()
					.createItemStack(
							new ItemStack(ItemEnum.FLUIDPATTERN.getItem()));;
		}
		return in;
	}

	@Override
	public IAEItemStack[] getOutputs() {
		try {
			String filePath = "C:\\kek\\kekos.txt";
			FileWriter writer = new FileWriter(filePath, true);
			BufferedWriter bufferWriter = new BufferedWriter(writer);
			bufferWriter.write("i started crafting\n");
			IAEItemStack[] out = super.getOutputs();
			getCondensedInputs();
			if (!this.needExtra) {
				bufferWriter.write("case 1\n");
				return out;
			}
			if (out.length == 0) {
				out = new IAEItemStack[1];
				out[0] = AEApi
						.instance()
						.storage()
						.createItemStack(
								new ItemStack(ItemEnum.FLUIDPATTERN.getItem()));
				bufferWriter.write("case 2\n");
			} else {
				for (int i = 0; i < out.length; i++) {
					if (out[i] == null) {
						out[i] = AEApi
								.instance()
								.storage()
								.createItemStack(
										new ItemStack(ItemEnum.FLUIDPATTERN
												.getItem()));
						return out;
					}
				}
				IAEItemStack[] s2 = new IAEItemStack[out.length + 1];
				for (int i = 0; i < out.length; i++) {
					s2[i] = out[i];
				}
				s2[out.length] = AEApi
						.instance()
						.storage()
						.createItemStack(
								new ItemStack(ItemEnum.FLUIDPATTERN.getItem()));
				bufferWriter.write("case 3\n");
				return s2;
			}
			bufferWriter.write("output in crafting pattern 2\n");
			for (int i = 0; i < out.length; ++i) {
				String craft_name = out[i].toString();
				bufferWriter.write(craft_name + "\n");
			}
			bufferWriter.close();
			return out;
		} catch (IOException e) {
			System.out.println(e);
		}
		return new IAEItemStack[1];
	}

	@Override
	public ItemStack getPattern() {
		ItemStack p = this.pattern.getPattern();
		if (p == null)
			return null;
		ItemStack s = new ItemStack(ItemEnum.CRAFTINGPATTERN.getItem(), 1, 1);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("item", p.writeToNBT(new NBTTagCompound()));
		s.setTagCompound(tag);
		return s;
	}

}
