package net.kaupenjoe.mccourse.item.custom;

import net.kaupenjoe.mccourse.sound.ModSounds;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

public class ChainsawItem extends Item {
    public ChainsawItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if(!level.isClientSide()) {
            if(level.getBlockState(context.getClickedPos()).is(BlockTags.LOGS)) {
                level.destroyBlock(context.getClickedPos(), true, context.getPlayer());

                context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level),
                        ((ServerPlayer) context.getPlayer()), item ->
                        Objects.requireNonNull(context.getPlayer()).onEquippedItemBroken(item,  EquipmentSlot.MAINHAND));

                context.getLevel().playSound(null, context.getPlayer().blockPosition(), ModSounds.CHAINSAW_CUT.get(),
                        SoundSource.PLAYERS, 1f, 1f);

                // Server Particles (Via Server, Seen by all players)
                ((ServerLevel) context.getLevel()).sendParticles(ParticleTypes.SMOKE, context.getClickedPos().getX() + 0.5f, context.getClickedPos().getY() + 1.0f,
                        context.getClickedPos().getZ() + 0.5f, 25, 0.0, 0.05, 0.0, 0.15f);
            } else {
                context.getLevel().playSound(null, context.getPlayer().blockPosition(), ModSounds.CHAINSAW_PULL.get(),
                        SoundSource.PLAYERS, 1f, 1f);
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if(!Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.chainsaw.tooltip.shift"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.chainsaw.tooltip.1"));
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.chainsaw.tooltip.2"));
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
