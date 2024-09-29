package net.acetheeldritchking.discerning_the_eldritch;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.acetheeldritchking.discerning_the_eldritch.events.ServerEvents;
import net.acetheeldritchking.discerning_the_eldritch.registeries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registeries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registeries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.registeries.SpellRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DiscerningTheEldritch.MOD_ID)
public class DiscerningTheEldritch
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "discerning_the_eldritch";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public DiscerningTheEldritch()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Event Handler
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        // Items
        ItemRegistries.register(modEventBus);
        // Spells
        SpellRegistry.register(modEventBus);
        // Effects
        DTEPotionEffectRegistry.register(modEventBus);
        // Sounds
        DTESoundRegistry.register(modEventBus);
        // Entities


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
