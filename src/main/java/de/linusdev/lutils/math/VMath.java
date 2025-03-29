/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.math;

import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.abstracts.floatn.Float3x3;
import de.linusdev.lutils.math.matrix.abstracts.floatn.Float4x4;
import de.linusdev.lutils.math.matrix.abstracts.floatn.FloatMxN;
import de.linusdev.lutils.math.matrix.abstracts.floatn.min.MinFloat3x3;
import de.linusdev.lutils.math.matrix.abstracts.floatn.min.MinFloat4x4;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import de.linusdev.lutils.math.vector.UnsignedVector;
import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float3;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float4;
import de.linusdev.lutils.math.vector.abstracts.floatn.FloatN;
import de.linusdev.lutils.math.vector.abstracts.intn.IntN;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static de.linusdev.lutils.math.vector.Vector.View.doesMappingCollide;
import static de.linusdev.lutils.math.vector.Vector.View.isMappingSpecial;

@SuppressWarnings({"UnusedReturnValue", "ForLoopReplaceableByForEach"})
public class VMath {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *                        Vector Functions                       *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Contract("_, _, _ -> param3")
    public static <V extends FloatN> @NotNull V add(@NotNull V left, @NotNull V right, @UniqueView @NotNull V store) {
        assert matchingDimensions(left, right, store);
        assert uniqueViewVector(store, left, right);

        for(int i = 0; i < left.getMemberCount(); i++)
            store.put(i, left.get(i) + right.get(i));

        return store;
    }

    @Contract("_, _, _ -> param3")
    public static <V extends FloatN> @NotNull V subtract(@NotNull V left, @NotNull V right, @UniqueView @NotNull V store) {
        assert matchingDimensions(left, right, store);
        assert uniqueViewVector(store, left, right);

        for(int i = 0; i < left.getMemberCount(); i++)
            store.put(i, left.get(i) - right.get(i));

        return store;
    }

    @Contract("_, _, _ -> param3")
    public static <V extends FloatN> @NotNull V multiply(@NotNull V left, @NotNull V right, @UniqueView @NotNull V store) {
        assert matchingDimensions(left, right, store);
        assert uniqueViewVector(store, left, right);

        for(int i = 0; i < left.getMemberCount(); i++)
            store.put(i, left.get(i) * right.get(i));

        return store;
    }

    @Contract("_, _, _ -> param3")
    public static <V extends FloatN> @NotNull V divide(@NotNull V left, @NotNull V right, @UniqueView @NotNull V store) {
        assert matchingDimensions(left, right, store);
        assert uniqueViewVector(store, left, right);

        for(int i = 0; i < left.getMemberCount(); i++)
            store.put(i, left.get(i) / right.get(i));

        return store;
    }

    @Contract("_, _, _ -> param3")
    public static <V extends FloatN> @NotNull V scale(@NotNull V toScale, float factor, @UniqueView @NotNull V store) {
        assert matchingDimensions(toScale, store);
        assert uniqueViewVector(store, toScale);

        for(int i = 0; i < toScale.getMemberCount(); i++)
            store.put(i, toScale.get(i) * factor);

        return store;
    }

    public static float dot(@NotNull FloatN left, @NotNull FloatN right) {
        assert matchingDimensions(left, right);

        float dot = 0.0f;
        for(int i = 0; i < left.getMemberCount(); i++)
            dot += left.get(i) * right.get(i);

        return dot;
    }

    public static float length(@NotNull FloatN vector) {
        float length = 0;
        for(int i = 0; i < vector.getMemberCount(); i++)
            length += vector.get(i) * vector.get(i);

        return (float) Math.sqrt(length);
    }

    @Contract("_, _, _ -> param3")
    public static @NotNull Float3 cross(@NotNull Float3 left, @NotNull Float3 right, @NotNull Float3 store) {
        float storeX, storeY, storeZ;

        storeX = (left.get(1) * right.get(2)) - (left.get(2) * right.get(1));
        storeY = (left.get(2) * right.get(0)) - (left.get(0) * right.get(2));
        storeZ = (left.get(0) * right.get(1)) - (left.get(1) * right.get(0));

        store.xyz(storeX, storeY, storeZ);
        return store;
    }

    /**
     * Normalizes given vector {@code toNormalize} so that the vector has a {@link VMath#length(FloatN) length}  of {@code 1.0f}.
     * This is archived by dividing each component by the length of the vector. If the vector has a length of {@code 0.0}, the
     * returned vector will also have a length of {@code 0.0}.
     * @param toNormalize vector to normalize
     * @param store vector to store the result in
     * @return {@code store}
     * @see Vector#isNormalized(FloatN, float)
     */
    @Contract("_, _ -> param2")
    public static <V extends FloatN> @NotNull V normalize(@NotNull V toNormalize, @UniqueView @NotNull V store) {
        assert matchingDimensions(toNormalize, store);
        assert uniqueViewVector(store, toNormalize);

        float length = length(toNormalize);
        if(length == 0f || length == 1f) {
            for(int i = 0; i < toNormalize.getMemberCount(); i++)
                store.put(i, toNormalize.get(i));
            return store;
        }

        for(int i = 0; i < toNormalize.getMemberCount(); i++)
            store.put(i, toNormalize.get(i) / length);

        return store;
    }

    @Contract("_, _ -> param2")
    public static <V extends FloatN> @NotNull V absolute(@NotNull V toAbsolute, @UniqueView @NotNull V store) {
        assert matchingDimensions(toAbsolute, store);
        assert uniqueViewVector(store, toAbsolute);

        for(int i = 0; i < toAbsolute.getMemberCount(); i++)
            store.put(i, Math.abs(toAbsolute.get(i)));

        return store;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *               Vector Min / Max / Clamp Functions              *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Component wise {@link Math#min(int, int)}.
     */
    public static <V extends IntN> @NotNull V min(@NotNull V first, @NotNull V second, @UniqueView @NotNull V store) {
        assert matchingDimensions(first, second, store);
        assert uniqueViewVector(store, first, second);

        for (int i = 0; i < first.getMemberCount(); i++) {
            store.put(i, Math.min(first.get(i), second.get(i)));
        }

        return store;
    }

    /**
     * Component wise {@link Math#max(int, int)}.
     */
    public static <V extends IntN> @NotNull V max(@NotNull V first, @NotNull V second, @UniqueView @NotNull V store) {
        assert matchingDimensions(first, second, store);
        assert uniqueViewVector(store, first, second);

        for (int i = 0; i < first.getMemberCount(); i++) {
            store.put(i, Math.max(first.get(i), second.get(i)));
        }

        return store;
    }

    /**
     * Component wise {@link LMath#clamp(int, int, int)}}.
     */
    public static <V extends IntN> @NotNull V clamp(@NotNull V value, @NotNull V min, @NotNull V max, @UniqueView @NotNull V store) {
        assert matchingDimensions(value, min, max, store);
        assert uniqueViewVector(store, value, min, max);

        for (int i = 0; i < value.getMemberCount(); i++) {
            store.put(i, LMath.clamp(value.get(i), min.get(i), max.get(i)));
        }

        return store;
    }

    /**
     * Component wise {@link Math#min(int, int)}.
     */
    public static <V extends FloatN> @NotNull V min(@NotNull V first, @NotNull V second, @UniqueView @NotNull V store) {
        assert matchingDimensions(first, second, store);
        assert uniqueViewVector(store, first, second);

        for (int i = 0; i < first.getMemberCount(); i++) {
            store.put(i, Math.min(first.get(i), second.get(i)));
        }

        return store;
    }

    /**
     * Component wise {@link Math#max(int, int)}.
     */
    public static <V extends FloatN> @NotNull V max(@NotNull V first, @NotNull V second, @UniqueView @NotNull V store) {
        assert matchingDimensions(first, second, store);
        assert uniqueViewVector(store, first, second);

        for (int i = 0; i < first.getMemberCount(); i++) {
            store.put(i, Math.max(first.get(i), second.get(i)));
        }

        return store;
    }

    /**
     * Component wise {@link LMath#clamp(int, int, int)}}.
     */
    public static <V extends FloatN> @NotNull V clamp(@NotNull V value, @NotNull V min, @NotNull V max, @UniqueView @NotNull V store) {
        assert matchingDimensions(value, min, max, store);
        assert uniqueViewVector(store, value, min, max);

        for (int i = 0; i < value.getMemberCount(); i++) {
            store.put(i, LMath.clamp(value.get(i), min.get(i), max.get(i)));
        }

        return store;
    }

    /**
     * Component wise {@link LMath#minUnsigned(int, int)}.
     */
    public static <V extends IntN & UnsignedVector> @NotNull V minUnsigned(@NotNull V first, @NotNull V second, @UniqueView @NotNull V store) {
        assert matchingDimensions(first, second, store);
        assert uniqueViewVector(store, first, second);

        for (int i = 0; i < first.getMemberCount(); i++) {
            store.put(i, LMath.minUnsigned(first.get(i), second.get(i)));
        }

        return store;
    }

    /**
     * Component wise {@link LMath#maxUnsigned(int, int)}.
     */
    public static <V extends IntN & UnsignedVector> @NotNull V maxUnsigned(@NotNull V first, @NotNull V second, @UniqueView @NotNull V store) {
        assert matchingDimensions(first, second, store);
        assert uniqueViewVector(store, first, second);

        for (int i = 0; i < first.getMemberCount(); i++) {
            store.put(i, LMath.maxUnsigned(first.get(i), second.get(i)));
        }

        return store;
    }

    /**
     * Component wise {@link LMath#clampUnsigned(int, int, int)}.
     */
    public static <V extends IntN & UnsignedVector> @NotNull V clampUnsigned(@NotNull V value, @NotNull V min, @NotNull V max, @UniqueView @NotNull V store) {
        assert matchingDimensions(value, min, max, store);
        assert uniqueViewVector(store, value, min, max);

        for (int i = 0; i < value.getMemberCount(); i++) {
            store.put(i, LMath.clampUnsigned(value.get(i), min.get(i), max.get(i)));
        }

        return store;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *                        Matrix Functions                       *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Contract("_, _, _ -> param3")
    public static <M extends FloatMxN> @NotNull M scale(@NotNull M toScale, float factor, @UniqueView @NotNull M store) {
        assert matchingDimensions(toScale, store);

        for(int x = 0; x < toScale.getWidth(); x++) {
            for(int y = 0; y < toScale.getHeight(); y++) {
                store.put(y, x, toScale.get(y, x) * factor);
            }
        }

        return store;
    }

    public static float determinant(@NotNull Float4x4 mat) {
        return 0
                + mat.get(0, 0) * mat.get(1, 1) * mat.get(2, 2) * mat.get(3, 3)
                + mat.get(0, 0) * mat.get(1, 2) * mat.get(2, 3) * mat.get(3, 1)
                + mat.get(0, 0) * mat.get(1, 3) * mat.get(2, 1) * mat.get(3, 2)
                - mat.get(0, 0) * mat.get(1, 3) * mat.get(2, 2) * mat.get(3, 1)
                - mat.get(0, 0) * mat.get(1, 2) * mat.get(2, 1) * mat.get(3, 3)
                - mat.get(0, 0) * mat.get(1, 1) * mat.get(2, 3) * mat.get(3, 2)
                - mat.get(0, 1) * mat.get(1, 0) * mat.get(2, 2) * mat.get(3, 3)
                - mat.get(0, 2) * mat.get(1, 0) * mat.get(2, 3) * mat.get(3, 1)
                - mat.get(0, 3) * mat.get(1, 0) * mat.get(2, 1) * mat.get(3, 2)
                + mat.get(0, 3) * mat.get(1, 0) * mat.get(2, 2) * mat.get(3, 1)
                + mat.get(0, 2) * mat.get(1, 0) * mat.get(2, 1) * mat.get(3, 3)
                + mat.get(0, 1) * mat.get(1, 0) * mat.get(2, 3) * mat.get(3, 2)
                + mat.get(0, 1) * mat.get(1, 2) * mat.get(2, 0) * mat.get(3, 3)
                + mat.get(0, 2) * mat.get(1, 3) * mat.get(2, 0) * mat.get(3, 1)
                + mat.get(0, 3) * mat.get(1, 1) * mat.get(2, 0) * mat.get(3, 2)
                - mat.get(0, 3) * mat.get(1, 2) * mat.get(2, 0) * mat.get(3, 1)
                - mat.get(0, 2) * mat.get(1, 1) * mat.get(2, 0) * mat.get(3, 3)
                - mat.get(0, 1) * mat.get(1, 3) * mat.get(2, 0) * mat.get(3, 2)
                - mat.get(0, 1) * mat.get(1, 2) * mat.get(2, 3) * mat.get(3, 0)
                - mat.get(0, 2) * mat.get(1, 3) * mat.get(2, 1) * mat.get(3, 0)
                - mat.get(0, 3) * mat.get(1, 1) * mat.get(2, 2) * mat.get(3, 0)
                + mat.get(0, 3) * mat.get(1, 2) * mat.get(2, 1) * mat.get(3, 0)
                + mat.get(0, 2) * mat.get(1, 1) * mat.get(2, 3) * mat.get(3, 0)
                + mat.get(0, 1) * mat.get(1, 3) * mat.get(2, 2) * mat.get(3, 0)
                ;
    }

    @Contract("_, _ -> param2")
    public static <M extends Float4x4> @NotNull M adjugate(@NotNull M mat, @Unique @NotNull M store) {
        assert uniqueMatrix(store, mat);
        // x = 0:

        store.put(0, 0, 0
                + mat.get(1, 1) * mat.get(2, 2) * mat.get(3, 3)
                + mat.get(1, 2) * mat.get(2, 3) * mat.get(3, 1)
                + mat.get(1, 3) * mat.get(2, 1) * mat.get(3, 2)
                - mat.get(1, 3) * mat.get(2, 2) * mat.get(3, 1)
                - mat.get(1, 2) * mat.get(2, 1) * mat.get(3, 3)
                - mat.get(1, 1) * mat.get(2, 3) * mat.get(3, 2)
        );

        store.put(1, 0, 0
                - mat.get(1, 0) * mat.get(2, 2) * mat.get(3, 3)
                - mat.get(1, 2) * mat.get(2, 3) * mat.get(3, 0)
                - mat.get(1, 3) * mat.get(2, 0) * mat.get(3, 2)
                + mat.get(1, 3) * mat.get(2, 2) * mat.get(3, 0)
                + mat.get(1, 2) * mat.get(2, 0) * mat.get(3, 3)
                + mat.get(1, 0) * mat.get(2, 3) * mat.get(3, 2)
        );

        store.put(2, 0, 0
                + mat.get(1, 0) * mat.get(2, 1) * mat.get(3, 3)
                + mat.get(1, 1) * mat.get(2, 3) * mat.get(3, 0)
                + mat.get(1, 3) * mat.get(2, 0) * mat.get(3, 1)
                - mat.get(1, 3) * mat.get(2, 1) * mat.get(3, 0)
                - mat.get(1, 1) * mat.get(2, 0) * mat.get(3, 3)
                - mat.get(1, 0) * mat.get(2, 3) * mat.get(3, 1)
        );

        store.put(3, 0, 0
                - mat.get(1, 0) * mat.get(2, 1) * mat.get(3, 2)
                - mat.get(1, 1) * mat.get(2, 2) * mat.get(3, 0)
                - mat.get(1, 2) * mat.get(2, 0) * mat.get(3, 1)
                + mat.get(1, 2) * mat.get(2, 1) * mat.get(3, 0)
                + mat.get(1, 1) * mat.get(2, 0) * mat.get(3, 2)
                + mat.get(1, 0) * mat.get(2, 2) * mat.get(3, 1)
        );

        // x = 1:

        store.put(0, 1, 0
                - mat.get(0, 1) * mat.get(2, 2) * mat.get(3, 3)
                - mat.get(0, 2) * mat.get(2, 3) * mat.get(3, 1)
                - mat.get(0, 3) * mat.get(2, 1) * mat.get(3, 2)
                + mat.get(0, 3) * mat.get(2, 2) * mat.get(3, 1)
                + mat.get(0, 2) * mat.get(2, 1) * mat.get(3, 3)
                + mat.get(0, 1) * mat.get(2, 3) * mat.get(3, 2)
        );

        store.put(1, 1, 0
                + mat.get(0, 0) * mat.get(2, 2) * mat.get(3, 3)
                + mat.get(0, 2) * mat.get(2, 3) * mat.get(3, 0)
                + mat.get(0, 3) * mat.get(2, 0) * mat.get(3, 2)
                - mat.get(0, 3) * mat.get(2, 2) * mat.get(3, 0)
                - mat.get(0, 2) * mat.get(2, 0) * mat.get(3, 3)
                - mat.get(0, 0) * mat.get(2, 3) * mat.get(3, 2)
        );

        store.put(2, 1, 0
                - mat.get(0, 0) * mat.get(2, 1) * mat.get(3, 3)
                - mat.get(0, 1) * mat.get(2, 3) * mat.get(3, 0)
                - mat.get(0, 3) * mat.get(2, 0) * mat.get(3, 1)
                + mat.get(0, 3) * mat.get(2, 1) * mat.get(3, 0)
                + mat.get(0, 1) * mat.get(2, 0) * mat.get(3, 3)
                + mat.get(0, 0) * mat.get(2, 3) * mat.get(3, 1)
        );

        store.put(3, 1, 0
                + mat.get(0, 0) * mat.get(2, 1) * mat.get(3, 2)
                + mat.get(0, 1) * mat.get(2, 2) * mat.get(3, 0)
                + mat.get(0, 2) * mat.get(2, 0) * mat.get(3, 1)
                - mat.get(0, 2) * mat.get(2, 1) * mat.get(3, 0)
                - mat.get(0, 1) * mat.get(2, 0) * mat.get(3, 2)
                - mat.get(0, 0) * mat.get(2, 2) * mat.get(3, 1)
        );

        // x = 2:

        store.put(0, 2, 0
                + mat.get(0, 1) * mat.get(1, 2) * mat.get(3, 3)
                + mat.get(0, 2) * mat.get(1, 3) * mat.get(3, 1)
                + mat.get(0, 3) * mat.get(1, 1) * mat.get(3, 2)
                - mat.get(0, 3) * mat.get(1, 2) * mat.get(3, 1)
                - mat.get(0, 2) * mat.get(1, 1) * mat.get(3, 3)
                - mat.get(0, 1) * mat.get(1, 3) * mat.get(3, 2)
        );

        store.put(1, 2, 0
                - mat.get(0, 0) * mat.get(1, 2) * mat.get(3, 3)
                - mat.get(0, 2) * mat.get(1, 3) * mat.get(3, 0)
                - mat.get(0, 3) * mat.get(1, 0) * mat.get(3, 2)
                + mat.get(0, 3) * mat.get(1, 2) * mat.get(3, 0)
                + mat.get(0, 2) * mat.get(1, 0) * mat.get(3, 3)
                + mat.get(0, 0) * mat.get(1, 3) * mat.get(3, 2)
        );

        store.put(2, 2, 0
                + mat.get(0, 0) * mat.get(1, 1) * mat.get(3, 3)
                + mat.get(0, 1) * mat.get(1, 3) * mat.get(3, 0)
                + mat.get(0, 3) * mat.get(1, 0) * mat.get(3, 1)
                - mat.get(0, 3) * mat.get(1, 1) * mat.get(3, 0)
                - mat.get(0, 1) * mat.get(1, 0) * mat.get(3, 3)
                - mat.get(0, 0) * mat.get(1, 3) * mat.get(3, 1)
        );

        store.put(3, 2, 0
                - mat.get(0, 0) * mat.get(1, 1) * mat.get(3, 2)
                - mat.get(0, 1) * mat.get(1, 2) * mat.get(3, 0)
                - mat.get(0, 2) * mat.get(1, 0) * mat.get(3, 1)
                + mat.get(0, 2) * mat.get(1, 1) * mat.get(3, 0)
                + mat.get(0, 1) * mat.get(1, 0) * mat.get(3, 2)
                + mat.get(0, 0) * mat.get(1, 2) * mat.get(3, 1)
        );

        // x = 3:

        store.put(0, 3, 0
                - mat.get(0, 1) * mat.get(1, 2) * mat.get(2, 3)
                - mat.get(0, 2) * mat.get(1, 3) * mat.get(2, 1)
                - mat.get(0, 3) * mat.get(1, 1) * mat.get(2, 2)
                + mat.get(0, 3) * mat.get(1, 2) * mat.get(2, 1)
                + mat.get(0, 2) * mat.get(1, 1) * mat.get(2, 3)
                + mat.get(0, 1) * mat.get(1, 3) * mat.get(2, 2)
        );

        store.put(1, 3, 0
                + mat.get(0, 0) * mat.get(1, 2) * mat.get(2, 3)
                + mat.get(0, 2) * mat.get(1, 3) * mat.get(2, 0)
                + mat.get(0, 3) * mat.get(1, 0) * mat.get(2, 2)
                - mat.get(0, 3) * mat.get(1, 2) * mat.get(2, 0)
                - mat.get(0, 2) * mat.get(1, 0) * mat.get(2, 3)
                - mat.get(0, 0) * mat.get(1, 3) * mat.get(2, 2)
        );

        store.put(2, 3, 0
                - mat.get(0, 0) * mat.get(1, 1) * mat.get(2, 3)
                - mat.get(0, 1) * mat.get(1, 3) * mat.get(2, 0)
                - mat.get(0, 3) * mat.get(1, 0) * mat.get(2, 1)
                + mat.get(0, 3) * mat.get(1, 1) * mat.get(2, 0)
                + mat.get(0, 1) * mat.get(1, 0) * mat.get(2, 3)
                + mat.get(0, 0) * mat.get(1, 3) * mat.get(2, 1)
        );

        store.put(3, 3, 0
                + mat.get(0, 0) * mat.get(1, 1) * mat.get(2, 2)
                + mat.get(0, 1) * mat.get(1, 2) * mat.get(2, 0)
                + mat.get(0, 2) * mat.get(1, 0) * mat.get(2, 1)
                - mat.get(0, 2) * mat.get(1, 1) * mat.get(2, 0)
                - mat.get(0, 1) * mat.get(1, 0) * mat.get(2, 2)
                - mat.get(0, 0) * mat.get(1, 2) * mat.get(2, 1)
        );

        return store;
    }

    @Contract("_, _ -> param2")
    public static <M extends Float4x4> @NotNull M inverse(@NotNull M mat, @Unique @NotNull M store) {
        assert uniqueMatrix(store, mat);
        return VMath.scale(VMath.adjugate(mat, store), 1f / VMath.determinant(mat), store);
    }

    @Contract("_, _ -> param2")
    public static <M extends MinFloat3x3> @NotNull M transpose3x3(@NotNull M mat, @UniqueView @NotNull M store) {
        assert uniqueViewVector(store, mat);

        if(store != mat) {
            store.put(0,0, mat.get(0, 0));
            store.put(1,1, mat.get(1, 1));
            store.put(2,2, mat.get(2, 2));
        }

        swapWithBuf(mat, store, 0, 1, 1, 0);
        swapWithBuf(mat, store, 0, 2, 2, 0);
        swapWithBuf(mat, store, 1, 2, 2, 1);

        return store;
    }

    /**
     * stores read(ya, xa) at store(yb, xb) and <br>
     * stores read(yb, xb) at store(ya, xa) <br>
     * This functions uses a buffer value, to ensure that it works even if read and store are the same matrix.
     */
    private static void swapWithBuf(@NotNull FloatMxN read, @NotNull FloatMxN store, int ya, int xa, int yb, int xb) {
        float buf = read.get(ya, xa);
        store.put(ya, xa, read.get(yb, xb));
        store.put(yb, xb, buf);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *               Special Matrix Creation Functions               *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Creates a rotation matrix, which can rotate a 3D vector around given {@code axis}
     * @param angle angle in radians. Describes, how much to rotate. (reminder: PI = 180°)
     * @param axis axis to rotate around. Must be {@link #normalize(FloatN, FloatN) normalized}.
     * @param store matrix to store the result
     * @return {@code store}
     * @see <a href="https://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle">Formula</a>
     */
    @Contract("_, _, _ -> param3")
    public static <M extends MinFloat3x3> @NotNull M rotationMatrix(float angle, @NotNull Float3 axis, @NotNull M store) {
        assert Vector.isNormalized(axis, 0.0001f);

        store.put(0,0, (float) (
                ((double) (axis.x() * axis.x())) * (1d - Math.cos(angle)) + (Math.cos(angle))
        ));

        store.put(0,1, (float) (
                ((double) (axis.x() * axis.y())) * (1d - Math.cos(angle)) - (axis.z() * Math.sin(angle))
        ));

        store.put(0,2, (float) (
                ((double) (axis.x() * axis.z())) * (1d - Math.cos(angle)) + (axis.y() * Math.sin(angle))
        ));

        store.put(1,0, (float) (
                ((double) (axis.x() * axis.y())) * (1d - Math.cos(angle)) + (axis.z() * Math.sin(angle))
        ));

        store.put(1,1, (float) (
                ((double) (axis.y() * axis.y())) * (1d - Math.cos(angle)) + (Math.cos(angle))
        ));

        store.put(1,2, (float) (
                ((double) (axis.y() * axis.z())) * (1d - Math.cos(angle)) - (axis.x() * Math.sin(angle))
        ));

        store.put(2,0, (float) (
                ((double) (axis.x() * axis.z())) * (1d - Math.cos(angle)) - (axis.y() * Math.sin(angle))
        ));

        store.put(2,1, (float) (
                ((double) (axis.y() * axis.z())) * (1d - Math.cos(angle)) + (axis.x() * Math.sin(angle))
        ));

        store.put(2,2, (float) (
                ((double) (axis.z() * axis.z())) * (1d - Math.cos(angle)) + (Math.cos(angle))
        ));

        return store;
    }

    /**
     * Creates a rotation matrix, which can rotate a 3D vector.
     * @param yaw angle in radians. Describes, how much to rotate. (reminder: PI = 180°)
     * @param pitch angle in radians. Describes, how much to rotate. (reminder: PI = 180°)
     * @param roll angle in radians. Describes, how much to rotate. (reminder: PI = 180°)
     * @param store matrix to store the result
     * @return {@code store}
     * @see <a href="https://en.wikipedia.org/wiki/Rotation_matrix#General_3D_rotations">Formula</a>
     */
    @Contract("_, _, _, _ -> param4")
    public static <M extends MinFloat3x3> @NotNull M rotationMatrix(float yaw, float pitch, float roll, @NotNull M store) {
        store.put(0,0, (float) (Math.cos(yaw) * Math.cos(pitch)));
        store.put(0,1, (float) (Math.cos(yaw) * Math.sin(pitch) * Math.sin(roll) - (Math.sin(yaw) * Math.cos(roll))));
        store.put(0,2, (float) (Math.cos(yaw) * Math.sin(pitch) * Math.cos(roll) + (Math.sin(yaw) * Math.sin(roll))));

        store.put(1,0, (float) (Math.sin(yaw) * Math.cos(pitch)));
        store.put(1,1, (float) (Math.sin(yaw) * Math.sin(pitch) * Math.sin(roll) + (Math.cos(yaw) * Math.cos(roll))));
        store.put(1,2, (float) (Math.sin(yaw) * Math.sin(pitch) * Math.cos(roll) - (Math.cos(yaw) * Math.sin(roll))));

        store.put(2,0, (float) (-Math.sin(pitch)));
        store.put(2,1, (float) (Math.cos(pitch) * Math.sin(roll)));
        store.put(2,2, (float) (Math.cos(pitch) * Math.cos(roll)));

        return store;
    }

    /**
     * Puts given {@code translation} into the first three fields of the last column of given matrix {@code store}.
     * All other fields will not be changed.
     * @param translation the translation vector
     * @param store the matrix to store the translation
     * @return {@code store}
     */
    public static <M extends Float4x4> @NotNull M translationMatrix(@NotNull Float3 translation, @NotNull M store) {
        store.put(0, 3, translation.x());
        store.put(1, 3, translation.y());
        store.put(2, 3, translation.z());

        return store;
    }

    /**
     * Create a diagonal matrix with given {@code value}.
     * @param value the value to set on the diagonals
     * @param fillZeros whether to put zeros in all fields, that a diagonal matrix requires to be zero. This is useful
     *                  if a new matrix was created which is initialized with zeros anyway.
     * @param store the matrix to store in
     * @return {@code store}
     */
    public static <M extends FloatMxN> @NotNull M diagonalMatrix(float value, boolean fillZeros, @NotNull M store) {
        for (int i = 0; i < store.getHeight() && i < store.getWidth(); i++)
            store.put(i, i, value);

        if(fillZeros) {
            for (int i = 0; i < store.getHeight(); i++) {
                for (int j = 0; j < store.getWidth(); j++) {
                    if(i == j) continue;
                    store.put(i, j, 0f);
                }
            }
        }

        return store;
    }

    /**
     * Projection matrix
     */
    static Float4x4 projectionMatrixExplained(
            float aspect,
            float width,
            float height,
            float near,
            float far,
            boolean perspective,
            float fudgeFactor
    ) {
        ABFloat4x4 mat = new ABFloat4x4();

        // map x from [-width/2, +width/2] to [-1, 1]. E.G divide by width/2,
        // but also ensure, that all aspect ratios work -> multiply width with aspect or divide height by aspect
        // -> the factor for x is: 1f/((aspect*width)/2), which is the same as 2f/(aspect*width)
        mat.put(0,0, 2f/(aspect * width));
        // map y from [-height/2,+height/2] to [-1, 1] -> divide by height/2. Aspect is already done in the x coordinate
        mat.put(1,1, 2f/height);

        // Map z from [near,far] to [0,1] -> subtract near, so that z goes from [0,far-near]
        // then divide by (far-near), so that z goes from [0, 1]
        // This means: (z-near) / (far-near)
        // But we need it in the form of z*someConst1 + someConst2
        // Luckily we can rewrite this equation to be:
        // z* (1/(far-near)) - (near)/(far-near)
        mat.put(2, 2, 1f/(far-near));
        mat.put(2, 3, -(near)/(far-near));

        if(perspective) {
            // It is common practise (for example in OpenGL), that the xy-coordinate will be
            // divided by the w(index=3) component of the vector after the projection matrix
            // was applied.
            // We can introduce a perspective by dividing x and y by (1.0+z*someFactor) after(!)
            // the matrix should set the w component of the vector to:
            // someFactor * (z*/(far-near) - (near)/(far-near)) + 1.0
            // which is the same as:
            // z * ( someFactor / (far-near))  -  (someFactor * near ) / (far-near) + 1.0
            mat.put(3, 2, fudgeFactor / (far-near));
            mat.put(3, 3,  (-fudgeFactor * near) / (far-near) + 1f);
        } else {
            // keep the w comp of the vector
            mat.put(3, 3, 1f);
        }

        return mat;
    }

    /**
     * Creates a projection matrix, which maps from {@code [-width,width]}, {@code [-height,height]} and {@code [near,far]}
     * to {@code [-1,1]}, {@code [-1,1]} and {@code [0,1]}.<br>
     * The matrix can optionally also store perspective data in the last component of the vector.
     * <br><br>
     * For a code explanation see comments in {@link #projectionMatrixExplained(float, float, float, float, float, boolean, float)}.
     * @param aspect aspect ratio to extend width by.
     * @param width width of screen (2f for 45°)
     * @param height height of screen (2f for 45°)
     * @param near near clipping
     * @param far far clipping
     * @param invertY whether the projection matrix should invert the y-axis.
     * @param perspective whether to store perspective in w
     * @param fudgeFactor fudge factor for perspective
     * @return projection matrix
     */
    public static <M extends MinFloat4x4> M projectionMatrix(
            float aspect,
            float width,
            float height,
            float near,
            float far,
            boolean invertY,
            boolean perspective,
            float fudgeFactor,
            @NotNull M store
    ) {

        float dfn = 1f/(far-near); // replace /(far-near) with *dfn
        float nDfn = -near * dfn;

        store.put(0,0, 2f/(aspect * width));
        if(invertY) store.put(1,1, -2f/height);
        else store.put(1,1, 2f/height);

        store.put(2, 2, dfn);
        store.put(2, 3, nDfn);

        if(perspective) {
            store.put(3, 2, fudgeFactor * dfn);
            store.put(3, 3, fudgeFactor * nDfn + 1f);
        } else {
            store.put(3, 3, 1f);
        }


        return store;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *                    Vector-Matrix Functions                    *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Contract("_, _, _ -> param3")
    public static @NotNull Float4 multiply(@NotNull Float4x4 left, @NotNull Float4 right,  @NotNull Float4 store) {
        float storeX, storeY, storeZ, storeW;

        storeX = 0
                + right.get(0) * left.get(0, 0)
                + right.get(1) * left.get(0, 1)
                + right.get(2) * left.get(0, 2)
                + right.get(3) * left.get(0, 3);

        storeY = 0
                + right.get(0) * left.get(1, 0)
                + right.get(1) * left.get(1, 1)
                + right.get(2) * left.get(1, 2)
                + right.get(3) * left.get(1, 3);

        storeZ = 0
                + right.get(0) * left.get(2, 0)
                + right.get(1) * left.get(2, 1)
                + right.get(2) * left.get(2, 2)
                + right.get(3) * left.get(2, 3);

        storeW = 0
                + right.get(0) * left.get(3, 0)
                + right.get(1) * left.get(3, 1)
                + right.get(2) * left.get(3, 2)
                + right.get(3) * left.get(3, 3);

        store.xyzw(storeX, storeY, storeZ, storeW);

        return store;
    }

    @Contract("_, _, _ -> param3")
    public static @NotNull Float3 multiply(@NotNull Float3x3 left, @NotNull Float3 right, @NotNull Float3 store) {
        float storeX, storeY, storeZ;

        storeX = 0
                + right.get(0) * left.get(0, 0)
                + right.get(1) * left.get(0, 1)
                + right.get(2) * left.get(0, 2);

        storeY = 0
                + right.get(0) * left.get(1, 0)
                + right.get(1) * left.get(1, 1)
                + right.get(2) * left.get(1, 2);

        storeZ = 0
                + right.get(0) * left.get(2, 0)
                + right.get(1) * left.get(2, 1)
                + right.get(2) * left.get(2, 2);

        store.xyz(storeX, storeY, storeZ);

        return store;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *                        Assertion-Methods                      *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Checks if all {@link Vector}s in {@code vectors} have the same dimension.
     * @param vectors {@link Vector} array
     * @return {@code true} if the {@link Vector#getMemberCount()} is the same for all given {@code vectors}
     */
    private static boolean matchingDimensions(@NotNull Vector @NotNull ... vectors) {
        int dim = vectors[0].getMemberCount();

        for(int i = 1; i < vectors.length; i++) {
            if(vectors[i].getMemberCount() != dim) return false;
        }

        return true;
    }

    /**
     * Checks if all {@link Matrix matrices} in {@code vectors} have the same {@link Matrix#getWidth() width}
     * and {@link Matrix#getHeight() height}.
     * @param matrices {@link Matrix} array
     * @return {@code true} if the {@link Matrix#getWidth() width} and {@link Matrix#getHeight() height}
     * is the same for all given {@code vectors}
     */
    private static boolean matchingDimensions(@NotNull Matrix @NotNull ... matrices) {
        int width = matrices[0].getWidth();
        int height = matrices[0].getHeight();

        for(int i = 1; i < matrices.length; i++) {
            if(matrices[i].getWidth() != width || matrices[i].getHeight() != height)
                return false;
        }

        return true;
    }

    /**
     * Checks if no {@link Vector} in {@code vectors} is the same as {@code unique}.
     * Meaning, that no vector in {@code vectors} is the same as or a {@link Vector#isView() view} on {@code unique}.
     * @param vectors array of {@link Vector vectors}
     * @param unique vector which should be checked if it is unique
     * @return {@code true} if no vector in {@code vectors} is the same as or a {@link Vector#isView() view} on {@code unique}
     */
    @SuppressWarnings("unused")
    private static boolean uniqueVector(@NotNull Vector unique, @NotNull Vector @NotNull ... vectors) {
        Vector original = unique.isView() ? unique.getAsView().getOriginal() : unique;

        for(int i = 0; i < vectors.length; i++) {
            if(original == (vectors[i].isView() ? vectors[i].getAsView().getOriginal() : vectors[i]))
                return false;
        }

        return true;
    }

    /**
     * Checks if no {@link Vector} in {@code vectors} is a {@link Vector#isView() view} on {@code unique}. Or if {@code unique}
     * itself is a view, that {@code unique} is not a view on any vector in {@code vectors}.<br>
     * Views with a non-{@link Vector.View#isMappingSpecial(Vector) special} {@link Vector.View#getMapping() mapping} are allowed.<br>
     * If {@code unique} is a view, views whose mapping do not {@link Vector.View#doesMappingCollide(int[], int[]) collide}
     * with the mapping of {@code unique} are allowed.
     * @param vectors array of {@link Vector vectors}
     * @param unique vector which should be checked if it is unique (view wise)
     * @return {@code true} if no vector in {@code vectors} is a {@link Vector#isView() view} on {@code unique}
     */
    protected static boolean uniqueViewVector(@NotNull Vector unique, @NotNull Vector @NotNull ... vectors) {

        if(unique.isView()) {
            Vector original = unique.getAsView().getOriginal();
            boolean isUniqueMappingSpecial = isMappingSpecial(unique);
            int[] defaultMapping = unique.getAsView().getMapping();

            for(int i = 0; i < vectors.length; i++) {
                if(
                        vectors[i].isView()
                     && original == vectors[i].getAsView().getOriginal()
                     && isMappingSpecial(defaultMapping, vectors[i])
                     && doesMappingCollide(defaultMapping, vectors[i].getAsView().getMapping())
                ) {
                    return false;
                } else if(!vectors[i].isView() && vectors[i] == original && isUniqueMappingSpecial) {
                    return false;
                }
            }

        } else {
            for(int i = 0; i < vectors.length; i++) {
                if(vectors[i].isView() && unique == vectors[i].getAsView().getOriginal() && isMappingSpecial(vectors[i])) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if no {@link Matrix} in {@code matrices} is the same as {@code unique}.
     * @param matrices array of {@link Matrix matrices}
     * @param unique matrix which should be checked if it is unique
     * @return {@code true} if no matrix in {@code matrices} is the same as {@code unique}
     */
    private static boolean uniqueMatrix(@NotNull Matrix unique, @NotNull Matrix @NotNull ... matrices) {
        for(int i = 0; i < matrices.length; i++) {
            if(unique == matrices[i])
                return false;
        }

        return true;
    }

    /**
     * Marks that no other parameter may be the same or a {@link Vector#isView() view} on this parameter.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    public @interface Unique {
    }

    /**
     * Marks that no other parameter may be a {@link Vector#isView() view} on this parameter. But they may
     * be the same parameter. If this parameter is a {@link Vector#isView() view}, it may not view on any other parameter.<br>
     * Views with a non-{@link Vector.View#isMappingSpecial(Vector) special} {@link Vector.View#getMapping() mapping} are allowed.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    public @interface UniqueView {
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *                      private constructor                      *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private VMath() {}

}
