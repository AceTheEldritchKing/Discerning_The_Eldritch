package net.acetheeldritchking.discerning_the_eldritch.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class VeinRipperVerticalParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final Vec3 forward, up;
    private final Vector3f[] localVertices;

    public VeinRipperVerticalParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double xd, double yd, double zd, VeinRipperVerticalParticleOptions options) {
        super(pLevel, pX, pY, pZ, 0, 0, 0);

        this.lifetime = 5;
        this.gravity = 0;
        this.sprites = spriteSet;

        this.quadSize = options.scale * 3.25f;
        this.forward = new Vec3(options.xf, options.yf, options.zf).normalize();
        this.up = new Vec3(options.xu, options.yu, options.zu).normalize();
        this.localVertices = calculateVertices();
        if (new Vec3(xd, yd, zd).lengthSqr() > 0) {
            this.xd = xd;
            this.yd = yd;
            this.zd = zd;
        } else {
            this.xd = this.forward.x * .1;
            this.yd = this.forward.y * .1;
            this.zd = this.forward.z * .1;
        }
        this.friction = 1;
    }

    private Vec3 vec3Copy(Vector3f vector3f)
    {
        return new Vec3(vector3f.x, vector3f.y, vector3f.z);
    }

    @Override
    public void tick() {
        if (this.age == 0) {
            createBloodTrail();
        }
        this.move(xd, yd, zd);
        if (this.age++ > this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(sprites);
        }
    }

    private void createBloodTrail()
    {
        int count = (int) (9 * this.quadSize);
        for (int i = 1; i < count - 1; i++)
        {
            float t = (float) i / count;
            float u = 1 - t;

            Vec3 localPos =
                    vec3Copy(localVertices[1]).scale(0.4).scale(u * u * u).add(
                            vec3Copy(localVertices[2]).scale(3 * u * u * t).add(
                                    vec3Copy(localVertices[3]).scale(3 * u * t * t).add(
                                            vec3Copy(localVertices[0]).scale(0.85).scale(t * t * t)
                                    )
                            )
                    ).scale(this.quadSize * .85);
            Vec3 pos = localPos.add(Utils.getRandomVec3(0.2 + i * .01f));
            Vec3 motion = new Vec3(xd, yd, zd).scale(random.nextDouble() * 6);
            if (random.nextFloat() < .5f) {
                level.addParticle(ParticleHelper.BLOOD, x + pos.x, y + pos.y, z + pos.z, motion.x * 1.5, motion.y * 1.5, motion.z * 1.5);
            }
        }
    }

    private Vector3f[] calculateVertices()
    {
        Vec3 forward = this.forward;
        Vec3 up = this.up;
        Vec3 right = forward.cross(up);

        Vector3f[] vertices = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        for (int i = 0; i < 4; i++) {
            float x = (float) (forward.x * vertices[i].x + right.x * vertices[i].y);
            float y = (float) (forward.y * vertices[i].x + right.y * vertices[i].y);
            float z = (float) (forward.z * vertices[i].x + right.z * vertices[i].y);
            vertices[i] = new Vector3f(x, y, z);
        }
        return vertices;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vec3 = renderInfo.getPosition();

        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());

        Vector3f[] vertices = new Vector3f[4];

        for (int i = 0; i < 4; i++)
        {
            var localVertex = localVertices[i];
            vertices[i] = new Vector3f(localVertex.x, localVertex.y, localVertex.z);
            vertices[i].mul(this.getQuadSize(partialTicks));
            vertices[i].add(f, f1, f2);
        }

        int j = this.getLightColor(partialTicks);
        this.makeCornerVertex(buffer, vertices[0], this.getU1(), this.getV1(), j);
        this.makeCornerVertex(buffer, vertices[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[3], this.getU0(), this.getV1(), j);

        this.makeCornerVertex(buffer, vertices[3], this.getU0(), this.getV1(), j);
        this.makeCornerVertex(buffer, vertices[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[0], this.getU1(), this.getV1(), j);
    }

    private void makeCornerVertex(VertexConsumer consumer, Vector3f vector3f, float u, float v, int light)
    {
        consumer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<VeinRipperVerticalParticleOptions>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet)
        {
            this.sprites = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull VeinRipperVerticalParticleOptions options, @NotNull ClientLevel clientLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            VeinRipperVerticalParticle veinRipperVerticalParticle = new VeinRipperVerticalParticle(clientLevel, pX, pY, pZ, sprites, pXSpeed, pYSpeed, pZSpeed, options);
            veinRipperVerticalParticle.setSpriteFromAge(this.sprites);
            veinRipperVerticalParticle.setAlpha(1.0F);

            return veinRipperVerticalParticle;
        }
    }
}
