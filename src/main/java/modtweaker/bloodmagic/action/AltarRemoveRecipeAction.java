package modtweaker.bloodmagic.action;

import static modtweaker.helpers.ItemHelper.areEqual;
import stanhebben.minetweaker.api.IUndoableAction;
import stanhebben.minetweaker.api.value.TweakerItemStack;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;

public class AltarRemoveRecipeAction implements IUndoableAction {
	private final TweakerItemStack result;
	private AltarRecipe recipe;
	
	public AltarRemoveRecipeAction(TweakerItemStack output) {
		this.result = output;
	}

	@Override
	public void apply() {		
		for(AltarRecipe r: AltarRecipeRegistry.altarRecipes) {
			if(areEqual(result.get(), r.result)) {
				recipe = r;
				break;
			}
		}
		
		if(recipe != null) {
			AltarRecipeRegistry.altarRecipes.remove(recipe);
		}
	}

	@Override
	public boolean canUndo() {
		return AltarRecipeRegistry.altarRecipes != null;
	}

	@Override
	public void undo() {
		AltarRecipeRegistry.altarRecipes.add(recipe);
	}

	@Override
	public String describe() {
		return "Removing Altar Recipe: " + result.getDisplayName();
	}

	@Override
	public String describeUndo() {
		return "Restoring Altar Recipe: " + result.getDisplayName();
	}
}
