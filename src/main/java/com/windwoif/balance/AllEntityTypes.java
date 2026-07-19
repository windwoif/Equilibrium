package com.windwoif.balance;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.windwoif.balance.content.reactors.reactorEntity.ReactorEntity;
import net.minecraft.world.entity.MobCategory;

public class AllEntityTypes {
    public static void init() {}
    public static final EntityEntry<ReactorEntity> REACTOR_ENTITY =
            Balance.REGISTRATE.<ReactorEntity>entity("reactor", ReactorEntity::new, MobCategory.MISC)
                    .properties(b -> b
                            .sized(0.0F, 0.0F)
                            .clientTrackingRange(17)
                            .updateInterval(Integer.MAX_VALUE)
                    )
                    .register();
}
