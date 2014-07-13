package modtweaker.mods.tconstruct.handlers;

import static modtweaker.helpers.InputHelper.toStack;

import java.util.List;
import java.util.Map.Entry;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.tconstruct.TConstructHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import modtweaker.util.BaseSetVar;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.crafting.PatternBuilder.ItemKey;
import tconstruct.library.crafting.PatternBuilder.MaterialSet;
import tconstruct.library.tools.ToolCore;

@ZenClass("mods.tconstruct.Tweaks")
public class TiCTweaks {
    //Set the maximum RF
    @ZenMethod
    public static void setRFCapacity(int capacity) {
        MineTweakerAPI.tweaker.apply(new AdjustRF(capacity));
    }

    private static class AdjustRF extends BaseSetVar {
        public AdjustRF(int newValue) {
            super("RF Maximum for Tinkers Tools", ToolCore.class, "capacity", 40000, newValue);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Tweaks for enabling / disabling Patterns
    @ZenMethod
    public static void removePattern(ItemStack stack) {
        MineTweakerAPI.tweaker.apply(new DisablePattern(stack));
    }

    private static class DisablePattern implements IUndoableAction {
        private String key;
        private MaterialSet set;
        private List list;
        private ItemStack stack;
        private final ItemStack disable;

        public DisablePattern(ItemStack disable) {
            this.disable = disable;
        }

        //Loops through the pattern mappings to find an entry with the same material name
        @Override
        public void apply() {
            for (Entry<List, ItemStack> entry : TConstructRegistry.patternPartMapping.entrySet()) {
                ItemStack check = entry.getValue();
                if (check.isItemEqual(disable)) {
                    list = entry.getKey();
                    stack = entry.getValue();
                    break;
                }
            }

            TConstructRegistry.patternPartMapping.remove(list);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            TConstructRegistry.patternPartMapping.put(list, stack);
        }

        @Override
        public String describe() {
            return "Disabling creation of the pattern for: " + disable.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Enabling creation of the pattern for: " + disable.getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void addRepairMaterial(IItemStack stack, String material, int value) {
        ItemStack input = toStack(stack);
        MineTweakerAPI.tweaker.apply(new Add(TConstructHelper.getItemKey(input.getItem(), input.getItemDamage(), value, material)));
    }

    //Tweaks for setting repair materials
    private static class Add extends BaseListAddition {
        public Add(ItemKey recipe) {
            super("Repair Material", PatternBuilder.instance.materials, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return new ItemStack(((ItemKey) recipe).item, 1, ((ItemKey) recipe).damage).getDisplayName();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ZenMethod
    public static void removeRepairMaterial(IItemStack output, @Optional String material) {
        MineTweakerAPI.tweaker.apply(new Remove(toStack(output), material));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        private final String material;

        public Remove(ItemStack stack, String material) {
            super("Repair Material", PatternBuilder.instance.materials, stack);
            this.material = material;
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (ItemKey r : PatternBuilder.instance.materials) {
                ItemStack clone = new ItemStack(r.item, 1, r.damage);
                if ((material != null && material.equalsIgnoreCase(r.key)) || (material == null)) {
                    if (clone.isItemEqual(stack)) {
                        recipe = r;
                        break;
                    }
                }
            }

            PatternBuilder.instance.materials.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
