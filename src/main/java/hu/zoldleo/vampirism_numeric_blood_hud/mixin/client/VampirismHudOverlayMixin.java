package hu.zoldleo.vampirism_numeric_blood_hud.mixin.client;

import de.teamlapen.vampirism.api.entity.IBiteableEntity;
import de.teamlapen.vampirism.api.entity.IExtendedCreatureVampirism;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.client.gui.overlay.VampirismHUDOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VampirismHUDOverlay.class)
public class VampirismHudOverlayMixin {
    @Shadow
    @Final
    private Minecraft mc;

    @Inject(method = "lambda$onRenderCrosshair$1", at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/client/gui/overlay/VampirismHUDOverlay;renderBloodFangs(Lnet/minecraft/client/gui/GuiGraphics;IIFI)V"))
    private void renderNumeric(Entity entity, RenderGuiLayerEvent.Pre event, IBiteableEntity biteable, CallbackInfo ci) {
        int blood = -1;
        int maxBlood = -1;
        if (biteable instanceof IExtendedCreatureVampirism creature) {
            blood = creature.getBlood();
            maxBlood = creature.getMaxBlood();
        } else if (biteable instanceof IVampirePlayer vampirePlayer) {
            if (vampirePlayer.getLevel() == 0) {
                blood = vampirePlayer.asEntity().getFoodData().getFoodLevel();
                maxBlood = 20;
            } else {
                blood = vampirePlayer.getBloodLevel();
                maxBlood = vampirePlayer.getBloodStats().getMaxBlood();
            }
        }
        if (blood != -1) {
            event.getGuiGraphics().pose().pushPose();
            event.getGuiGraphics().pose().scale(.5f, .5f, .5f);
            event.getGuiGraphics().drawCenteredString(mc.font, blood + "/" + maxBlood, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight() - 16, 16711680);
            event.getGuiGraphics().pose().popPose();
        }
    }
}