/*
 * Copyright (c) 2024-2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.math.special;

import de.linusdev.lutils.math.VMath;
import de.linusdev.lutils.math.matrix.abstracts.floatn.Float4x4;
import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float3;
import de.linusdev.lutils.math.vector.abstracts.floatn.FloatN;
import org.jetbrains.annotations.NotNull;

public class CameraMatrix extends TransRotMatrix {

    protected final @NotNull Float4x4 backingViewMatrix;

    /**
     * {@link VMath#normalize(FloatN, FloatN) normalized} world up vector
     */
    protected @NotNull Float3 worldUp = DEFAULT_UP;

    protected final @NotNull Float3 viewDirectionBacked;
    protected final @NotNull Float3 screenUpBacked;
    protected final @NotNull Float3 screenRightBacked;


    public CameraMatrix(@NotNull Float4x4 backingMatrix, @NotNull Float4x4 backingViewMatrix) {
        super(backingMatrix);
        this.backingViewMatrix = backingViewMatrix;
        this.backingViewMatrix.put(3, 3, 1f);

        this.viewDirectionBacked = backingMatrix.createFloat3View(
                0, 2,
                1, 2,
                2, 2
        );

        this.screenUpBacked = backingMatrix.createFloat3View(
                0, 1,
                1, 1,
                2, 1
        );

        this.screenRightBacked = backingMatrix.createFloat3View(
                0, 0,
                1, 0,
                2, 0
        );
    }

    /**
     *
     * @param worldUp must be {@link VMath#normalize(FloatN, FloatN) normalized}
     * @see #worldUp
     */
    public void setWorldUp(@NotNull Float3 worldUp) {
        assert Vector.isNormalized(worldUp, 0.00001f);
        this.worldUp = worldUp;
    }

    public void lookAt(@NotNull Float3 position) {
        VMath.subtract(positionView.xyz(), position, viewDirectionBacked);
        VMath.normalize(viewDirectionBacked, viewDirectionBacked);
        VMath.cross(worldUp, viewDirectionBacked, screenRightBacked);
        VMath.normalize(screenRightBacked, screenRightBacked);
        VMath.cross(viewDirectionBacked, screenRightBacked, screenUpBacked);
        VMath.normalize(screenUpBacked, screenUpBacked);
    }

    public void calculateViewMatrix() {
        VMath.transpose3x3(backingMatrix, backingViewMatrix);
        backingViewMatrix.put(0, 3, VMath.dot(screenRightBacked, positionView.xyz()));
        backingViewMatrix.put(1, 3, VMath.dot(screenUpBacked, positionView.xyz()));
        backingViewMatrix.put(2, 3, VMath.dot(viewDirectionBacked, positionView.xyz()));

        assert backingViewMatrix.get(3,3) == 1f;
        assert backingViewMatrix.get(3,0) == 0f;
        assert backingViewMatrix.get(3,1) == 0f;
        assert backingViewMatrix.get(3,2) == 0f;
    }

    public @NotNull Float4x4 viewMatrix() {
        return backingViewMatrix;
    }

    @Override
    public void setRotation(float angle, @NotNull Float3 axis) {
        super.setRotation(angle, axis);
    }

    @Override
    public void setRotation(float yaw, float pitch, float roll) {
        super.setRotation(yaw, pitch, roll);
    }

    @Override
    public void addRotation(float yaw, float pitch, float roll) {
        super.addRotation(yaw, pitch, roll);
    }


}
