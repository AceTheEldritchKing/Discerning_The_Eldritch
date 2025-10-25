package net.acetheeldritchking.discerning_the_eldritch;

import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import mod.azure.azurelib.common.animation.cache.AzIdentityRegistry;
import mod.azure.azurelib.common.render.armor.AzArmorRendererRegistry;
import mod.azure.azurelib.common.render.item.AzItemRendererRegistry;
import net.acetheeldritchking.aces_spell_utils.entity.render.items.SheathCurioRenderer;
import net.acetheeldritchking.aces_spell_utils.items.curios.SheathCurioItem;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.armor.CrimsonStagArmorRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.armor.EldritchWarlockArmorRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.armor.EldritchWarlockHelmetRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.armor.EldritchWarlockMaskRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.items.*;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.DTEArmorMaterialRegistry;
import net.acetheeldritchking.discerning_the_eldritch.loot.DTELootModifiers;
import net.acetheeldritchking.discerning_the_eldritch.registries.*;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEConfig;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DiscerningTheEldritch.MOD_ID)
public class DiscerningTheEldritch
{
    public static final String MOD_ID = "discerning_the_eldritch";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DiscerningTheEldritch(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        // Creative Tab
        DTECreativeModeTabs.register(modEventBus);
        // Items
        ItemRegistries.register(modEventBus);
        // Spells
        SpellRegistries.register(modEventBus);
        // Effects
        DTEPotionEffectRegistry.register(modEventBus);
        // Sounds
        DTESoundRegistry.register(modEventBus);
        // Entities
        DTEEntityRegistry.register(modEventBus);
        // Armor Materials
        DTEArmorMaterialRegistry.register(modEventBus);
        // Loot Tables
        DTELootModifiers.register(modEventBus);
        // Attributes
        DTEAttributeRegistry.register(modEventBus);
        // Schools
        DTESchoolRegistry.register(modEventBus);
        // Attachment Types
        DTEAttachmentRegistry.register(modEventBus);
        // Particles
        DTEParticleRegistry.register(modEventBus);
        // Fluids
        DTEFluidRegistry.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, DTEConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Animation Registry
        AzIdentityRegistry.register(
                ItemRegistries.ELDRITCH_WARLOCK_HOOD.get(),
                ItemRegistries.ELDRITCH_WARLOCK_ROBES.get(),
                ItemRegistries.ELDRITCH_WARLOCK_LEGGINGS.get(),
                ItemRegistries.ELDRITCH_WARLOCK_GREAVES.get(),
                ItemRegistries.ELDRITCH_WARLOCK_MASK.get(),
                ItemRegistries.ELDRITCH_WARLOCK_HELMET.get(),
                ItemRegistries.STAFF_OF_VEHEMENCE.get(),
                ItemRegistries.KINGS_EFFIGY.get()
        );
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        //
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // curios
            event.enqueueWork(() -> {
                ItemRegistries.getDTEItems().stream().filter(item -> item.get() instanceof SpellBook).forEach((item) -> CuriosRendererRegistry.register(item.get(), SpellBookCurioRenderer::new));
                ItemRegistries.getDTEItems().stream().filter(item -> item.get() instanceof SheathCurioItem).forEach((item) -> CuriosRendererRegistry.register(item.get(), SheathCurioRenderer::new));
            });

            // Armor Rendering Registry
            AzArmorRendererRegistry.register(EldritchWarlockArmorRenderer::new,
                    ItemRegistries.ELDRITCH_WARLOCK_HOOD.get(),
                    ItemRegistries.ELDRITCH_WARLOCK_ROBES.get(),
                    ItemRegistries.ELDRITCH_WARLOCK_LEGGINGS.get(),
                    ItemRegistries.ELDRITCH_WARLOCK_GREAVES.get());
            AzArmorRendererRegistry.register(EldritchWarlockMaskRenderer::new, ItemRegistries.ELDRITCH_WARLOCK_MASK.get());
            AzArmorRendererRegistry.register(EldritchWarlockHelmetRenderer::new, ItemRegistries.ELDRITCH_WARLOCK_HELMET.get());
            AzArmorRendererRegistry.register(KingsEffigyCurioItemRenderer::new, ItemRegistries.KINGS_EFFIGY.get());
            AzArmorRendererRegistry.register(CastersMantleCurioItemRenderer::new, ItemRegistries.CASTERS_MANTLE.get());
            AzArmorRendererRegistry.register(CrimsonStagArmorRenderer::new,
                    ItemRegistries.CRIMSON_STAG_ANTLERS.get(),
                    ItemRegistries.CRIMSON_STAG_ROBES.get(),
                    ItemRegistries.CRIMSON_STAG_LEGGINGS.get(),
                    ItemRegistries.CRIMSON_STAG_BOOTS.get());

            // Item Rendering Registry
            AzItemRendererRegistry.register(StaffOfVehemenceRenderer::new, ItemRegistries.STAFF_OF_VEHEMENCE.get());

            // Curio Rendering
            CuriosRendererRegistry.register(
                    ItemRegistries.KINGS_EFFIGY.get(), KingsEffigyCurioRenderer::new
            );
            CuriosRendererRegistry.register(
                    ItemRegistries.CASTERS_MANTLE.get(), CastersMantleCurioRenderer::new
            );
        }
    }

    public static ResourceLocation id(@NotNull String path)
    {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, path);
    }
}
