package modtweaker.mods.mekanism.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import mekanism.api.AdvancedInput;
import mekanism.api.gas.GasRegistry;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.mekanism.util.AddMekanismRecipe;
import modtweaker.mods.mekanism.util.RemoveMekanismRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Purification")
public class Purification {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        AdvancedInput aInput = new AdvancedInput(toStack(input), GasRegistry.getGas("oxygen"));
        MineTweakerAPI.tweaker.apply(new AddMekanismRecipe("PURIFICATION_CHAMBER", aInput, toStack(output)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.tweaker.apply(new RemoveMekanismRecipe("PURIFICATION_CHAMBER", toStack(output)));
    }
}
