package cam11062.strange.berries.mixin;

import cam11062.strange.berries.StrangeBerries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HuskEntity.class)
public class HuskEntityMixin extends HostileEntity {

    // This is the same as the ZombieEntityMixin class

    public HuskEntityMixin(EntityType<? extends HuskEntity> entityType, World world) {
        super(entityType, world);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(StrangeBerries.ROTTEN_BERRIES) && player.getItemCooldownManager().isCoolingDown(StrangeBerries.ROTTEN_BERRIES)) {
            return ActionResult.FAIL;
        }
        else if (itemStack.isOf(StrangeBerries.ROTTEN_BERRIES)) {
            player.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1.0f, 1.0f);
            itemStack.useOnEntity(player, this, hand);
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
                player.getItemCooldownManager().set(StrangeBerries.ROTTEN_BERRIES, 60);
            }
            setAttributes();
            return ActionResult.success(this.world.isClient);
        }
        return super.interactMob(player, hand);
    }

    public void setAttributes() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.17f);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(8);
    }
}