package modtweaker.mods.pneumaticraft.handlers;

import static modtweaker.helpers.InputHelper.toStacks;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import pneumaticCraft.api.recipe.PressureChamberRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.pneumaticraft.Pressure")
public class Pressure {
    @ZenMethod
    public static void addRecipe(IItemStack[] input, double pressure, IItemStack[] output, boolean asBlock) {
        MineTweakerAPI.tweaker.apply(new Add(new PressureChamberRecipe(toStacks(input), (float) pressure, toStacks(output), asBlock)));
    }

    private static class Add extends BaseListAddition {
        private ItemStack stack;

        public Add(PressureChamberRecipe recipe) {
            super("Pneumaticraft Pressure Chamber", PressureChamberRecipe.chamberRecipes, recipe);
            this.stack = recipe.output[0];
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack[] output) {
        MineTweakerAPI.tweaker.apply(new Remove(toStacks(output)));
    }

    private static class Remove extends BaseListRemoval {
        private final ItemStack[] stacks;

        public Remove(ItemStack[] stacks) {
            super("Pneumaticraft Pressure Chamber", PressureChamberRecipe.chamberRecipes, stacks[0]);
            this.stacks = stacks;
        }

        @Override
        public void apply() {
            for (PressureChamberRecipe r : PressureChamberRecipe.chamberRecipes) {
                boolean matches = true;
                for (int i = 0; i < stacks.length; i++) {
                    if (!stacks[i].isItemEqual(r.output[i])) {
                        matches = false;
                        break;
                    }
                }

                if (matches) {
                    recipe = r;
                    break;
                }
            }

            PressureChamberRecipe.chamberRecipes.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}