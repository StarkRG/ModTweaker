package modtweaker.mods.mekanism.util;

import java.util.Iterator;
import java.util.Map;

import mekanism.api.ChanceOutput;
import mekanism.api.ChemicalPair;
import mekanism.api.PressurizedProducts;
import mekanism.api.PressurizedReactants;
import mekanism.api.PressurizedRecipe;
import mekanism.api.gas.GasStack;
import mekanism.api.infuse.InfusionOutput;
import modtweaker.mods.mekanism.MekanismHelper;
import modtweaker.util.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RemoveMekanismRecipe extends BaseMapRemoval {
    private Object tmp;

    public RemoveMekanismRecipe(String string, Object key) {
        super(string.toLowerCase(), MekanismHelper.get(string), key, null);
        this.tmp = key;
    }

    @Override
    public void apply() {
        try {
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                Object value = pairs.getValue();
                if (value != null) {
                    if (tmp instanceof ItemStack && value instanceof ItemStack) {
                        if (((ItemStack) tmp).isItemEqual((ItemStack) value)) {
                            key = pairs.getKey();
                            break;
                        }
                    }

                    if (tmp instanceof FluidStack && value instanceof FluidStack) {
                        if (((FluidStack) tmp).isFluidEqual((FluidStack) value)) {
                            key = pairs.getKey();
                            break;
                        }
                    }

                    if (tmp instanceof GasStack && value instanceof GasStack) {
                        if (((GasStack) tmp).isGasEqual((GasStack) value)) {
                            key = pairs.getKey();
                            break;
                        }
                    }

                    if (tmp instanceof ItemStack && value instanceof InfusionOutput) {
                        if (((ItemStack) tmp).isItemEqual(((InfusionOutput) value).resource)) {
                            key = pairs.getKey();
                            break;
                        }
                    }

                    if (tmp instanceof ChemicalPair && value instanceof ChemicalPair) {
                        ChemicalPair par1 = (ChemicalPair) tmp;
                        ChemicalPair par2 = (ChemicalPair) value;
                        if (par1.leftGas.isGasEqual(par2.leftGas) && par1.rightGas.isGasEqual(par2.rightGas)) {
                            key = pairs.getKey();
                            break;
                        }
                    }

                    if (tmp instanceof ChanceOutput && value instanceof ChanceOutput) {
                        ChanceOutput par1 = (ChanceOutput) tmp;
                        ChanceOutput par2 = (ChanceOutput) value;
                        if (par1.primaryOutput.isItemEqual(par2.primaryOutput)) {
                            if (par1.secondaryOutput == null || (par1.secondaryOutput != null && par2.secondaryOutput != null && par1.secondaryOutput.isItemEqual(par2.secondaryOutput))) {
                                key = pairs.getKey();
                                break;
                            }
                        }
                    }

                    if (tmp instanceof PressurizedProducts && value instanceof PressurizedRecipe) {
                        PressurizedProducts par1 = (PressurizedProducts) tmp;
                        PressurizedProducts par2 = ((PressurizedRecipe) value).products;
                        if (par1.getItemOutput().isItemEqual(par2.getItemOutput())) {
                            if (par1.getGasOutput().isGasEqual(par2.getGasOutput())) {
                                key = pairs.getKey();
                                break;

                            }
                        }
                    }
                }

                it.remove();
            }

            recipe = map.get(key);

        } catch (Exception e) {
            e.printStackTrace();
        }

        map.remove(key);
    }

    @Override
    public String getRecipeInfo() {
        if (tmp instanceof ItemStack) return "Adding " + description + " Recipe for : " + ((ItemStack) tmp).getDisplayName();
        else if (tmp instanceof FluidStack) return "Adding " + description + " Recipe for : " + ((FluidStack) tmp).getFluid().getLocalizedName();
        else if (tmp instanceof ChemicalPair) return "Adding " + description + " Recipe for : " + ((ChemicalPair) tmp).leftGas.getGas().getLocalizedName();
        else if (tmp instanceof ChanceOutput) return "Adding " + description + " Recipe for : " + ((ChanceOutput) tmp).primaryOutput.getDisplayName();
        else if (tmp instanceof GasStack) return "Adding " + description + " Recipe for : " + ((GasStack) tmp).getGas().getLocalizedName();
        else if (tmp instanceof PressurizedReactants) return "Adding " + description + " Recipe for : " + ((PressurizedReactants) tmp).getSolid().getDisplayName();
        else return super.getRecipeInfo();
    }
}
