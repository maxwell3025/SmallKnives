package small_knives.misc;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class ModDamageSources {
    public static DamageSource knife(Entity thrower) {
        return new DamageSource("a flying knife"){
            Entity thrower_ = thrower;
            public Entity getTrueSource(){
                return thrower;
            }
        };
    }
}
