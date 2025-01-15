package com.example.addon.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
F
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private static Identifier CROSSHAIR_TEXTURE;
    @Shadow
    @Final
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE;
    @Shadow
    @Final
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;
    @Shadow
    @Final
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE;
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract boolean shouldRenderSpectatorCrosshair(@Nullable HitResult hitResult);

    //    @Inject(method = "renderCrosshair", at = @At(value = "HEAD"),cancellable = true)
//    public void onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
//        ci.cancel();
//        GameOptions gameOptions = this.client.options;
//        if (gameOptions.getPerspective().isFirstPerson()) {
//            if (this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR || this.shouldRenderSpectatorCrosshair(this.client.crosshairTarget)) {
//                if ( !this.client.player.hasReducedDebugInfo() && !gameOptions.getReducedDebugInfo().getValue()) {
//                    Camera camera = this.client.gameRenderer.getCamera();
//                    Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
//                    matrix4fStack.pushMatrix();
//                    matrix4fStack.mul(context.getMatrices().peek().getPositionMatrix());
//                    matrix4fStack.translate((float) (context.getScaledWindowWidth() / 2), (float) (context.getScaledWindowHeight() / 2), 0.0F);
//                    matrix4fStack.rotateX(-camera.getPitch() * (float) (Math.PI / 180.0));
//                    matrix4fStack.rotateY(camera.getYaw() * (float) (Math.PI / 180.0));
//                    matrix4fStack.scale(-1.0F, -1.0F, -1.0F);
//                    RenderSystem.renderCrosshair(10);
//                    matrix4fStack.popMatrix();
//
//                    if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
//                        float f = this.client.player.getAttackCooldownProgress(0.0F);
//                        boolean bl = false;
//                        if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && f >= 1.0F) {
//                            bl = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
//                            bl &= this.client.targetedEntity.isAlive();
//                        }
//
//                        int j = context.getScaledWindowHeight() / 2 - 7 + 16;
//                        int k = context.getScaledWindowWidth() / 2 - 8;
//                        if (bl) {
//                            context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE, k, j, 16, 16);
//                        } else if (f < 1.0F) {
//                            int l = (int) (f * 17.0F);
//                            context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, k, j, 16, 4);
//                            context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 16, 4, 0, 0, k, j, l, 4);
//                        }
//                    }
//
//                } else {
//                    int i = 15;
//                    context.drawGuiTexture(
//                        RenderLayer::getCrosshair, CROSSHAIR_TEXTURE, (context.getScaledWindowWidth() - 15) / 2, (context.getScaledWindowHeight() - 15) / 2, 15, 15
//                    );
//                    if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
//                        float f = this.client.player.getAttackCooldownProgress(0.0F);
//                        boolean bl = false;
//                        if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && f >= 1.0F) {
//                            bl = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
//                            bl &= this.client.targetedEntity.isAlive();
//                        }
//
//                        int j = context.getScaledWindowHeight() / 2 - 7 + 16;
//                        int k = context.getScaledWindowWidth() / 2 - 8;
//                        if (bl) {
//                            context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE, k, j, 16, 16);
//                        } else if (f < 1.0F) {
//                            int l = (int) (f * 17.0F);
//                            context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, k, j, 16, 4);
//                            context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 16, 4, 0, 0, k, j, l, 4);
//                        }
//                    }
//                }
//            }
//        }
//    }
    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/hud/DebugHud.shouldShowDebugHud ()Z"))
    public boolean onInvokeShouldShowDebugHud(DebugHud instance) {
        return true;
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.hasReducedDebugInfo ()Z"))
    public void onInvoke(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
            float f = this.client.player.getAttackCooldownProgress(0.0F);
            boolean bl = false;
            if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && f >= 1.0F) {
                bl = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
                bl &= this.client.targetedEntity.isAlive();
            }

            int j = context.getScaledWindowHeight() / 2 - 7 + 16;
            int k = context.getScaledWindowWidth() / 2 - 8;
            if (bl) {
                context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE, k, j, 16, 16);
            } else if (f < 1.0F) {
                int l = (int) (f * 17.0F);
                context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, k, j, 16, 4);
                context.drawGuiTexture(RenderLayer::getCrosshair, CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 16, 4, 0, 0, k, j, l, 4);
            }
        }
    }
}
