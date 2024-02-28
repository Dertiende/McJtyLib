package mcjty.lib.container;

import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BaseSlot extends SlotItemHandler {

    private final GenericTileEntity te;

    public BaseSlot(IItemHandler inventory, GenericTileEntity te, int index, int x, int y) {
        super(inventory, index, x, y);
        this.te = te;
    }

    @Override
    public void set(@Nonnull ItemStack stack) {
        if (te != null) {
            te.onSlotChanged(getSlotIndex(), stack);
        }
        super.set(stack);
    }

    public GenericTileEntity getTe() {
        return te;
    }
}
