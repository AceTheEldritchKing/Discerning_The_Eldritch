package net.acetheeldritchking.discerning_the_eldritch.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class SoulFireSlashParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final Vec3 forward;
    private final boolean mirror, vertical;
    private final Vector3f[] localVertices;

    SoulFireSlashParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xd, double yd, double zd, SoulFireSlashParticleOptions options) {
        super(level, x, y, z);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.lifetime = 4;
        this.gravity = 0;
        sprites = spriteSet;

        this.quadSize = options.scale * 3.25F;
        this.forward = new Vec3(options.xf, options.yf, options.zf);
        this.mirror = options.mirror;
        this.vertical = options.vertical;
        this.localVertices = calcVertices();

        this.friction = 1;
    }

    private Vec3 vec3Copy(Vector3f vec)
    {
        return new Vec3(vec.x, vec.y, vec.z);
    }

    @Override
    public void tick() {
        if (this.age == 0)
        {
            createSoulFireTrail();
        }
        if (this.age++ > this.lifetime)
        {
            this.remove();
        } else {
            this.setSpriteFromAge(sprites);
        }
    }

    private void createSoulFireTrail()
    {
        int count = (int) (9 * this.quadSize);
        for (int i = 1; i < count - 1; i++)
        {
            float t = (float) i / count;
            float u = 1 - t;
            // More math that has to do with 3D stuff...
            /*Vec3 localPos =
                    vec3Copy(localVertices[1]).scale(u * u * u)
                            .add(vec3Copy(localVertices[2]).scale(3 * u * u * t)
                                    .add(vec3Copy(localVertices[3]).scale(3 * u * t * t)
                                            .add(vec3Copy(localVertices[0]).scale(t * t * t)
                                            )
                                    )
                            ).scale(this.quadSize * 0.75F).add(Utils.getRandomVec3(0.3));*/

            Vec3 localPos =
                    vec3Copy(localVertices[1]).scale(u * u * u).add(
                            vec3Copy(localVertices[2]).scale(3 * u * u * t).add(
                                    vec3Copy(localVertices[3]).scale(3 * u * t * t).add(
                                            vec3Copy(localVertices[0]).scale(t * t * t)
                                    )
                            )
                    ).scale(this.quadSize * .75f).add(Utils.getRandomVec3(0.3));
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + localPos.x, y + localPos.y, z + localPos.z, 0, 0, 0);
            level.addParticle(ParticleTypes.SOUL, x + localPos.x, y + localPos.y, z + localPos.z, 0, 0, 0);
        }
    }

    private Vector3f[] calcVertices()
    {
        boolean vertical = this.vertical;
        Vec3 forward = this.forward;
        Vec3 up = new Vec3(0, 1, 0);
        if (forward.dot(up) > 0.999)
        {
            up = new Vec3(1, 0, 0);
        }
        Vec3 right = forward.cross(up);

        // Some crazy math Iron has that I am not qualified to understand
        // I really, really suck at math I am NOT qualified to be a coder </3
        up = up.subtract(proj(forward, up)).normalize();
        right = right.subtract(proj(forward, right)).subtract(proj(up, right)).normalize();
        Vec3 primary, secondary;

        if (!vertical)
        {
            primary = forward;
            secondary = right;
        } else
        {
            primary = forward;
            secondary = up;
        }

        Vector3f[] vertices = new Vector3f[]
                {
                        new Vector3f(-1.0F, -1.0F, 0.0F),
                        new Vector3f(-1.0F, 1.0F, 0.0F),
                        new Vector3f(1.0F, 1.0F, 0.0F),
                        new Vector3f(1.0F, -1.0F, 0.0F)
                };

        for (int i = 0; i < 4; i++)
        {
            float x = (float) (primary.x * vertices[i].x + secondary.x * vertices[i].y);
            float y = (float) (primary.y * vertices[i].x + secondary.y * vertices[i].y);
            float z = (float) (primary.z * vertices[i].x + secondary.z * vertices[i].y);
            vertices[i] = new Vector3f(x, y ,z);
        }

        return vertices;
    }

    public Vec3 proj(Vec3 u, Vec3 v)
    {
        return u.scale(v.dot(u) / u.lengthSqr());
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        boolean mirrored = !this.mirror; // If it's good enough for Big Man, it's good enough for me
        Vec3 vec3 = renderInfo.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        Vector3f[] vertices = new Vector3f[4];

        for (int i = 0; i < 4; i++)
        {
            var localVert = localVertices[i];
            vertices[i] = new Vector3f(localVert.x, localVert.y, localVert.z);
            vertices[i].mul(this.getQuadSize(partialTicks));
            vertices[i].add(f, f1, f2);
        }

        int j = this.getLightColor(partialTicks);

        // crying
        this.makeCornerVertex(buffer, vertices[0], this.getU1(), mirrored ? this.getV0() : this.getV1(), j);
        this.makeCornerVertex(buffer, vertices[1], this.getU1(), mirrored ? this.getV1() : this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[2], this.getU0(), mirrored ? this.getV1() : this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[3], this.getU0(), mirrored ? this.getV0() : this.getV1(), j);
        //backface
        this.makeCornerVertex(buffer, vertices[3], this.getU0(), mirrored ? this.getV0() : this.getV1(), j);
        this.makeCornerVertex(buffer, vertices[2], this.getU0(), mirrored ? this.getV1() : this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[1], this.getU1(), mirrored ? this.getV1() : this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[0], this.getU1(), mirrored ? this.getV0() : this.getV1(), j);
    }

    private void makeCornerVertex(VertexConsumer consumer, Vector3f vector3f, float u, float v, int light)
    {
        consumer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SoulFireSlashParticleOptions>
    {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite)
        {
            this.sprite = sprite;
        }

        @Override
        public @Nullable Particle createParticle(SoulFireSlashParticleOptions options, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            SoulFireSlashParticle slashParticle = new SoulFireSlashParticle(clientLevel, v, v1, v2, sprite, v3, v4, v5, options);
            slashParticle.setSpriteFromAge(this.sprite);
            slashParticle.setAlpha(1.0F);

            return slashParticle;
        }
    }
}
