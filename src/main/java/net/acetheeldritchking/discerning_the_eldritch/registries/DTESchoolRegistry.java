package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTETags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.redspace.ironsspellbooks.api.registry.SchoolRegistry.SCHOOL_REGISTRY_KEY;

public class DTESchoolRegistry {
    private static final DeferredRegister<SchoolType> DTE_SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, DiscerningTheEldritch.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        DTE_SCHOOLS.register(eventBus);
    }

    private static Supplier<SchoolType> registerSchool(SchoolType type)
    {
        return DTE_SCHOOLS.register(type.getId().getPath(), () -> type);
    }

    public static final ResourceLocation RITUAL_RESOURCE = DiscerningTheEldritch.id("ritual");

    public static final Supplier<SchoolType> RITUAL = registerSchool(new SchoolType
            (
                    RITUAL_RESOURCE,
                    DTETags.RITUAL_FOCUS,
                    Component.translatable("school.discerning_the_eldritch.ritual").withStyle(Style.EMPTY.withColor(0x870b32)),
                    DTEAttributeRegistry.RITUAL_MAGIC_POWER,
                    DTEAttributeRegistry.RITUAL_MAGIC_RESIST,
                    SoundRegistry.EVOCATION_CAST,
                    DTEDamageTypes.RITUAL_MAGIC
            ));
}
