package net.acetheeldritchking.discerning_the_eldritch.mixins.recipes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeCraftingHolder.class)
public interface RecipeCraftingHolderMixin {
    @ModifyReturnValue(
            method = "setRecipeUsed(Lnet/minecraft/world/level/Level;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/crafting/RecipeHolder;)Z",
            at = @At("RETURN")
    )
    private boolean checkForLockedRecipes(boolean original, Level level, ServerPlayer players, RecipeHolder<?> recipe)
    {
        //List<ResourceLocation> recipes = new ArrayList<>();

        //recipes.add(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "data/discerning_the_eldritch/recipe/insanity/apple.json"));

        if (!players.getData(DTEAttachmentRegistry.IS_INSANE) && recipe.id().getPath().contains("insanity/"))
        {
            return false;
        }
        return original;
    }
}
