package extracells.blocks;

import java.lang.reflect.Method;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import appeng.api.me.tiles.IColoredMETile;

public abstract class ColorableRotatableECBlock extends BlockContainer
{

	public ColorableRotatableECBlock(int id, Material material)
	{
		super(id, material);
		isBlockContainer = true;
	}

	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
	{
		Boolean validDirection = isInValidRotations(worldObj, x, y, z, axis);

		if (ForgeDirection.getOrientation(worldObj.getBlockMetadata(x, y, z)) == axis && !worldObj.isRemote)
		{
			worldObj.destroyBlock(x, y, z, true);
		} else if (validDirection)
		{
			worldObj.setBlockMetadataWithNotify(x, y, z, axis.ordinal(), 3);
		}
		return true;
	}

	public boolean isInValidRotations(World worldObj, int x, int y, int z, ForgeDirection toCheck)
	{
		for (ForgeDirection currentDirection : getValidRotations(worldObj, x, y, z))
		{
			if (currentDirection == toCheck)
				return true;
		}
		return false;
	}

	public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
	{
		return ForgeDirection.VALID_DIRECTIONS;
	}

	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hiZ, int meta)
	{
		return side;
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack)
	{
		ForgeDirection orientation = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));

		TileEntity clickedOnTileEntity = world.getBlockTileEntity(x + orientation.getOpposite().offsetX, y + orientation.getOpposite().offsetY, z + orientation.getOpposite().offsetZ);
		TileEntity blockTileEntity = world.getBlockTileEntity(x, y, z);

		if (blockTileEntity instanceof IColoredMETile)
		{
			if (clickedOnTileEntity instanceof IColoredMETile)
			{

				// All this shit to make it compatible w/ AE 13 AND 14 :D
				Method isColoredMethod = null;
				boolean isColored = false;

				try
				{
					// Method for AE 14 (With forgeDireciton argument)
					isColoredMethod = ((IColoredMETile) clickedOnTileEntity).getClass().getDeclaredMethod("isColored", ForgeDirection.class);
					isColored = (Boolean) isColoredMethod.invoke((IColoredMETile) clickedOnTileEntity, orientation);
				} catch (Throwable ex)
				{
					try
					{
						// Method for AE 13 (W/O the argument)
						isColoredMethod = ((IColoredMETile) clickedOnTileEntity).getClass().getDeclaredMethod("isColored");
						isColored = (Boolean) isColoredMethod.invoke((IColoredMETile) clickedOnTileEntity);
					} catch (Throwable e)
					{
					}
				}

				if (isColored)
				{
					((IColoredMETile) blockTileEntity).setColor(((IColoredMETile) clickedOnTileEntity).getColor());
				} else
				{
					((IColoredMETile) blockTileEntity).setColor(-1);
				}
			} else
			{
				((IColoredMETile) blockTileEntity).setColor(-1);
			}
		}

		if (player.isSneaking())
		{
			world.setBlockMetadataWithNotify(x, y, z, orientation.getOpposite().ordinal(), 3);
		}
	}
}
