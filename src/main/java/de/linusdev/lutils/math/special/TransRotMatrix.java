package de.linusdev.lutils.math.special;

import de.linusdev.lutils.math.VMath;
import de.linusdev.lutils.math.matrix.abstracts.floatn.Float4x4;
import de.linusdev.lutils.math.matrix.abstracts.floatn.min.MinFloat3x3;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float3;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float4;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat3;
import org.jetbrains.annotations.NotNull;

public class TransRotMatrix {

    protected final static @NotNull Float3 DEFAULT_UP = new ABFloat3(0,1,0);

    protected final @NotNull Float4x4 backingMatrix;

    protected final @NotNull Float4 translationView;
    protected final @NotNull Float4 positionView;

    protected float yaw = 0f;
    protected float pitch = 0f;
    protected float roll = 0f;

    public TransRotMatrix(@NotNull Float4x4 backingMatrix) {
        this.backingMatrix = backingMatrix;
        this.backingMatrix.put(3, 3, 1f);

        this.translationView = backingMatrix.createFloat4View(
                0, 3,
                1, 3,
                2, 3,
                3, 3
        );

        this.positionView = translationView.createFactorizedView(-1f, -1f, -1f, 1f);
    }

    /**
     * The translation contained in this matrix. Changes to the returned float4 will be visible in {@link #backingMatrix}.
     */
    public @NotNull Float4 translation() {
        return translationView;
    }

    /**
     * The position (inverse translation) contained in this matrix. Changes to the returned float4 will be visible in {@link #backingMatrix}.
     */
    public @NotNull Float4 position() {
        return positionView;
    }

    public @NotNull Float4x4 backingMatrix() {
        return backingMatrix;
    }

    /**
     * Set {@link #yaw}, {@link #pitch} and {@link #roll} rotation.
     */
    public void setRotation(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;

        VMath.rotationMatrix(yaw, pitch, roll, backingMatrix);
    }

    /**
     * Sets the rotation using {@link VMath#rotationMatrix(float, Float3, MinFloat3x3)}. Note:
     * {@link #yaw}, {@link #pitch} and {@link #roll} will not be updated and will contain wrong values.
     * @param axis normalized rotation axis.
     */
    public void setRotation(float angle, @NotNull Float3 axis) {
        VMath.rotationMatrix(angle, axis, backingMatrix);
    }

    /**
     * Add to current {@link #yaw}, {@link #pitch} and {@link #roll} rotation.
     */
    public void addRotation(float yaw, float pitch, float roll) {
        this.yaw += yaw;
        this.pitch += pitch;
        this.roll += roll;

        VMath.rotationMatrix(this.yaw, this.pitch, this.roll, backingMatrix);
    }

}
