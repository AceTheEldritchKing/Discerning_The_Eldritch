package net.acetheeldritchking.discerning_the_eldritch.utils.boss_music;

import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.AscendedOneBoss;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(Dist.CLIENT)
public class AscendedOneMusicManager {
    @Nullable
    private static AscendedOneMusicManager INSTANCE;
    static final SoundSource SOUND_SOURCE = SoundSource.RECORDS;

    AscendedOneBoss ascendedOneBoss;
    final SoundManager manager;
    BossSoundInstance bossMusic;
    AscendedOneBoss.Phase phase;
    Set<BossSoundInstance> layers = new HashSet<>();
    boolean finishedPlaying = false;

    private AscendedOneMusicManager(AscendedOneBoss boss)
    {
        this.ascendedOneBoss = boss;
        this.manager = Minecraft.getInstance().getSoundManager();
        phase = AscendedOneBoss.Phase.values()[boss.getPhase()];
        bossMusic = new BossSoundInstance(DTESoundRegistry.TEST_BOSS_MUSIC.get(), SOUND_SOURCE, true);

        init();
    }

    private void init()
    {
        manager.stop(null, SoundSource.MUSIC);
        switch (phase)
        {
            case FirstPhase -> {
                addLayer(bossMusic);
            }
        }
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event)
    {
        if (INSTANCE != null && !Minecraft.getInstance().isPaused())
        {
            INSTANCE.tick();
        }
    }

    public static void createOrResumeInstance(AscendedOneBoss boss)
    {
        if (INSTANCE == null || INSTANCE.isDonePlaying())
        {
            INSTANCE = new AscendedOneMusicManager(boss);
        }
        else
        {
            INSTANCE.triggerResumeMusic(boss);
        }
    }

    public static void stop(AscendedOneBoss boss)
    {
        if (INSTANCE != null && INSTANCE.ascendedOneBoss.getUUID().equals(boss.getUUID()))
        {
            INSTANCE.stopLayers();
            INSTANCE.finishedPlaying = true;
        }
    }

    private void tick()
    {
        if (isDonePlaying() || finishedPlaying)
        {
            return;
        }
        if (ascendedOneBoss.isDeadOrDying() || ascendedOneBoss.isRemoved())
        {
            stopLayers();
            finishedPlaying = true;
            return;
        }

        var bossPhase = AscendedOneBoss.Phase.values()[ascendedOneBoss.getPhase()];
        switch (bossPhase)
        {
            case FirstPhase -> {
                if (!manager.isActive(bossMusic))
                {
                    playTheDamnMusic();
                }
            }
        }
    }

    private boolean isDonePlaying()
    {
        for (BossSoundInstance soundInstance : layers)
        {
            if (!soundInstance.isStopped() && manager.isActive(soundInstance))
            {
                return false;
            }
        }

        return true;
    }

    private void addLayer(BossSoundInstance instance)
    {
        layers.stream().filter((sound) -> sound.isStopped() || !manager.isActive(sound)).toList().forEach(layers::remove);
        manager.play(instance);
        layers.add(instance);
    }

    public void stopLayers()
    {
        layers.forEach(BossSoundInstance::triggerStop);
    }

    public static void hardStop()
    {
        if (INSTANCE != null)
        {
            INSTANCE.layers.forEach(INSTANCE.manager::stop);
            INSTANCE = null;
        }
    }

    public void triggerResumeMusic(AscendedOneBoss boss)
    {
        if (boss.getUUID().equals(this.ascendedOneBoss.getUUID()))
        {
            this.ascendedOneBoss = boss;
        }

        if (this.ascendedOneBoss.isRemoved())
        {
            layers.forEach((sound) -> {
                if (!manager.isActive(sound))
                {
                    manager.play(sound);
                }
            });
            finishedPlaying = false;
        }
    }

    private void playTheDamnMusic()
    {
        addLayer(bossMusic);
    }
}
