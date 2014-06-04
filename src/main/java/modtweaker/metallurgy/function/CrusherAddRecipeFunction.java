package modtweaker.metallurgy.function;

import static modtweaker.helpers.TweakerHelper.getItem;
import static modtweaker.helpers.TweakerHelper.throwException;
import modtweaker.metallurgy.action.CrusherAddRecipeAction;
import net.minecraft.item.ItemStack;
import stanhebben.minetweaker.api.Tweaker;
import stanhebben.minetweaker.api.TweakerNameSpace;
import stanhebben.minetweaker.api.value.TweakerFunction;
import stanhebben.minetweaker.api.value.TweakerValue;

public class CrusherAddRecipeFunction extends TweakerFunction {
	public static final CrusherAddRecipeFunction INSTANCE = new CrusherAddRecipeFunction();
	
	private CrusherAddRecipeFunction() {}

	@Override
	public TweakerValue call(TweakerNameSpace namespace, TweakerValue... arguments) {
		if(arguments.length != 2) throwException(toString(), 2); 
		ItemStack input = getItem(0, arguments).get();
		ItemStack output = getItem(1, arguments).get();
		Tweaker.apply(new CrusherAddRecipeAction(input, output));
		return null;
	}

	@Override
	public String toString() {
		return "metallurgy.crusher.addRecipe";
	}
}
