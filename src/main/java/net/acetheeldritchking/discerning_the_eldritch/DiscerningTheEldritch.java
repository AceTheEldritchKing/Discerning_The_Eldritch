package net.acetheeldritchking.discerning_the_eldritch;

import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.acetheeldritchking.discerning_the_eldritch.events.ServerEvents;
import net.acetheeldritchking.discerning_the_eldritch.registries.*;
import net.minecraft.world.item.CreativeModeTabs;
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
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DiscerningTheEldritch.MOD_ID)
public class DiscerningTheEldritch
{
    public static final String MOD_ID = "discerning_the_eldritch";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DiscerningTheEldritch(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        // Creative Tab
        DTECreativeModeTabs.register(modEventBus);
        // Items
        ItemRegistries.register(modEventBus);
        // Spells
        SpellRegistry.register(modEventBus);
        // Effects
        DTEPotionEffectRegistry.register(modEventBus);
        // Sounds
        DTESoundRegistry.register(modEventBus);
        // Entities

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        //
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // Creative Menu
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        //
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // curios
            event.enqueueWork(() -> {
                ItemRegistries.getDTEItems().stream().filter(item -> item.get() instanceof SpellBook).forEach((item) -> CuriosRendererRegistry.register(item.get(), SpellBookCurioRenderer::new));
            });
        }
    }
}
