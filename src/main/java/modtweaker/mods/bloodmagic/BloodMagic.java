package modtweaker.mods.bloodmagic;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.bloodmagic.handlers.Alchemy;
import modtweaker.mods.bloodmagic.handlers.Binding;
import modtweaker.mods.bloodmagic.handlers.BloodAltar;

public class BloodMagic {
    public BloodMagic() {
        MineTweakerAPI.registerClass(Alchemy.class);
        MineTweakerAPI.registerClass(Binding.class);
        MineTweakerAPI.registerClass(BloodAltar.class);
    }
}
