package de.linusdev.lutils.math;

import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.abstracts.floatn.Float3x3;
import de.linusdev.lutils.math.matrix.abstracts.floatn.Float4x4;
import de.linusdev.lutils.math.matrix.abstracts.floatn.FloatMxN;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat3x3;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import de.linusdev.lutils.math.matrix.buffer.floatn.BBFloat3x3;
import de.linusdev.lutils.math.matrix.buffer.floatn.BBFloat4x4;
import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float2;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float3;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float4;
import de.linusdev.lutils.math.vector.abstracts.floatn.FloatN;
import de.linusdev.lutils.math.vector.abstracts.intn.Int2;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat1;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat2;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat3;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat4;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat1;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat2;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat3;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat4;
import de.linusdev.lutils.math.vector.buffer.intn.BBInt2;
import de.linusdev.lutils.math.vector.buffer.intn.BBUInt2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class VMathTest {
    @Test
    void add() {
        BBFloat4 a = BBFloat4.newAllocated(null);
        a.xyzw(1f, 2f, 3f, 4f);

        BBFloat4 b = BBFloat4.newAllocated(null);
        b.xyzw(1f, 2f, 3f, 4f);

        VMath.add(a, b, a);

        assertEquals(2f, a.x());
        assertEquals(4f, a.y());
        assertEquals(6f, a.z());
        assertEquals(8f, a.w());
    }

    @Test
    void subtract() {
        BBFloat4 a = BBFloat4.newAllocated(null);
        a.xyzw(2f, 2f, 3f, 1f);

        BBFloat4 b = BBFloat4.newAllocated(null);
        b.xyzw(1f, 2f, 3f, 4f);

        VMath.subtract(a, b, a);

        assertEquals(1f, a.x());
        assertEquals(0f, a.y());
        assertEquals(0f, a.z());
        assertEquals(-3f, a.w());
    }

    @Test
    void multiply() {
        BBFloat4 a = BBFloat4.newAllocated(null);
        a.xyzw(2f, 2f, 3f, 1f);

        BBFloat4 b = BBFloat4.newAllocated(null);
        b.xyzw(1f, 2f, 3f, 4f);

        VMath.multiply(a, b, a);

        assertEquals(2f, a.x());
        assertEquals(4f, a.y());
        assertEquals(9f, a.z());
        assertEquals(4f, a.w());
    }

    @Test
    void divide() {
        BBFloat4 a = BBFloat4.newAllocated(null);
        a.xyzw(2f, 2f, 3f, 1f);

        BBFloat4 b = BBFloat4.newAllocated(null);
        b.xyzw(1f, 2f, 3f, 4f);

        VMath.divide(a, b, a);

        assertEquals(2f, a.x());
        assertEquals(1f, a.y());
        assertEquals(1f, a.z());
        assertEquals(1f/4f, a.w());
    }

    @Test
    void scale() {
        BBFloat4 a = BBFloat4.newAllocated(null);
        a.xyzw(2f, 2f, 3f, 1f);

        VMath.scale(a, 2.f, a);

        assertEquals(4f, a.x());
        assertEquals(4f, a.y());
        assertEquals(6f, a.z());
        assertEquals(2f, a.w());
    }

    @Test
    void dot() {
        BBFloat4 a = BBFloat4.newAllocated(null);
        a.xyzw(2f, 2f, 3f, 1f);

        BBFloat4 b = BBFloat4.newAllocated(null);
        b.xyzw(1f, 2f, 3f, 4f);

        assertEquals(2f+4f+9f+4f, VMath.dot(a, b));
    }

    @Test
    void cross() {
        BBFloat3 a = BBFloat3.newAllocated(null);
        a.xyz(2f, 2f, 3f);

        BBFloat3 b = BBFloat3.newAllocated(null);
        b.xyz(1f, 2f, 3f);

        VMath.cross(a, b, a);

        assertEquals(0f, a.x());
        assertEquals(-3f, a.y());
        assertEquals(2f, a.z());
    }

    @Test
    void normalize() {
        BBFloat3 a = BBFloat3.newAllocated(null);
        a.xyz(2f, 2f, 3f);

        VMath.normalize(a, a);

        assertEquals(0.48507127f, a.x());
        assertEquals(0.48507127f, a.y());
        assertEquals(0.7276069f, a.z());
    }

    @Test
    void absolute() {
        BBFloat3 a = BBFloat3.newAllocated(null);
        a.xyz(2f, -2f, -3f);

        VMath.absolute(a, a);

        assertEquals(2f, a.x());
        assertEquals(2f, a.y());
        assertEquals(3f, a.z());
    }

    @Test
    void determinantFloat4x4() {
        ABFloat4x4 mat = new ABFloat4x4();

        mat.put(0, 0, 1);
        mat.put(0, 1, -5.5f);
        mat.put(0, 2, 3);
        mat.put(0, 3, 7);

        mat.put(1, 0, 8);
        mat.put(1, 1, 2);
        mat.put(1, 2, -1.5f);
        mat.put(1, 3, 1);

        mat.put(2, 0, 3);
        mat.put(2, 1, 4);
        mat.put(2, 2, 7.67f);
        mat.put(2, 3, 5.5f);

        mat.put(3, 0, 3.2f);
        mat.put(3, 1, 2.1f);
        mat.put(3, 2, 5f);
        mat.put(3, 3, 6.6f);

        System.out.println("Matrix: " + mat);
        System.out.println("Determinant: " + VMath.determinant(mat));

        assertEquals(959.26385f, VMath.determinant(mat));
    }

    @Test
    void adjugateFloat4x4() {
        ABFloat4x4 res = new ABFloat4x4();
        ABFloat4x4 mat = new ABFloat4x4();

        mat.put(0, 0, 1);
        mat.put(0, 1, 5.5f);
        mat.put(0, 2, 3);
        mat.put(0, 3, 7);

        mat.put(1, 0, 8);
        mat.put(1, 1, 2);
        mat.put(1, 2, 1.5f);
        mat.put(1, 3, 1);

        mat.put(2, 0, 3);
        mat.put(2, 1, 4);
        mat.put(2, 2, 7);
        mat.put(2, 3, 5.5f);

        mat.put(3, 0, 3.2f);
        mat.put(3, 1, 2.1f);
        mat.put(3, 2, 5f);
        mat.put(3, 3, 6.6f);

        System.out.println("toAdjugate: " + mat);

        VMath.adjugate(mat, res);

        System.out.println("Adjugate: " + res);

        assertEquals(20.42501f,     res.get(0, 0));
        assertEquals(-95.40001f,    res.get(0, 1));
        assertEquals(41.60000f,     res.get(0, 2));
        assertEquals(-41.87500f,    res.get(0, 3));

        assertEquals(-138.89999f,   res.get(1, 0));
        assertEquals(-39.70000f,    res.get(1, 1));
        assertEquals(-102.50000f,   res.get(1, 2));
        assertEquals(238.75000f,    res.get(1, 3));

        assertEquals(107.899994f,   res.get(2, 0));
        assertEquals(42.749996f,    res.get(2, 1));
        assertEquals(-188.90001f,   res.get(2, 2));
        assertEquals(36.50000f,     res.get(2, 3));

        assertEquals(-47.450012f,   res.get(3, 0));
        assertEquals(26.500006f,    res.get(3, 1));
        assertEquals(155.54999f,    res.get(3, 2));
        assertEquals(-197.25000f,   res.get(3, 3));
    }

    @Test
    void inverseFloat4x4() {
        ABFloat4x4 res = new ABFloat4x4();
        ABFloat4x4 mat = new ABFloat4x4();

        mat.put(0, 0, 1);
        mat.put(0, 1, 5.5f);
        mat.put(0, 2, 3);
        mat.put(0, 3, 7);

        mat.put(1, 0, 8);
        mat.put(1, 1, 2);
        mat.put(1, 2, 1.5f);
        mat.put(1, 3, 1);

        mat.put(2, 0, 3);
        mat.put(2, 1, 4);
        mat.put(2, 2, 7);
        mat.put(2, 3, 5.5f);

        mat.put(3, 0, 3.2f);
        mat.put(3, 1, 2.1f);
        mat.put(3, 2, 5f);
        mat.put(3, 3, 6.6f);

        System.out.println("toInverse: " + mat);

        VMath.inverse(mat, res);

        System.out.println("Inverse: " + res);

        assertEquals(-0.027161822f,             res.get(0, 0));
        assertEquals(0.12686592f,               res.get(0, 1));
        assertEquals(-0.05532098561525345,      res.get(0, 2));
        assertEquals(0.055686691711825526106f,  res.get(0, 3));

        assertEquals(0.18471357f,               res.get(1, 0));
        assertEquals(0.052794308321420259988f,  res.get(1, 1));
        assertEquals(0.13630772299611024305f,   res.get(1, 2));
        assertEquals(-0.31749725722264702952f,  res.get(1, 3));

        assertEquals(-0.143488809466362f,       res.get(2, 0));
        assertEquals(-0.056850288f,             res.get(2, 1));
        assertEquals(0.25120518f,               res.get(2, 2));
        assertEquals(-0.04853884770105389139f,  res.get(2, 3));

        assertEquals(0.06310052f,               res.get(3, 0));
        assertEquals(-0.035240542f,             res.get(3, 1));
        assertEquals(-0.20685527f,              res.get(3, 2));
        assertEquals(0.26230925230227068722f,   res.get(3, 3));
    }

    private static Collection<Arguments> provideMultiplyFloat4TimesFloat4x4() {
        ArrayList<Arguments> list = new ArrayList<>();

        float[] left = new float[] {
                1.3f, 4.2f, 6.55f, 3f,
                1f, 1f, 5.4f, 3.3f,
                -1f, 22.45f, 6.6f, 0.1f,
                -5f, 2.35f, 3.6f, 0.7f
        };
        float[] right = new float[] {3.3f, 4.5f, -1f, 7.88f};

        Float4x4[] float4x4s = new Float4x4[]{
                new ABFloat4x4(),
                BBFloat4x4.newAllocated(null),
                new ABFloat4x4(),
                BBFloat4x4.newAllocated(null),
                BBFloat4x4.newAllocated(null),
                BBFloat4x4.newAllocated(null),
                BBFloat4x4.newAllocated(null),
                BBFloat4x4.newAllocated(null),
                new ABFloat4x4(),
                new ABFloat4x4(),
                new ABFloat4x4(),
                new ABFloat4x4(),
        };

        Float4[] float4s = new Float4[] {
                new ABFloat4(),
                BBFloat4.newAllocated(null),
                new ABFloat4().wzyx(),
                BBFloat4.newAllocated(null).wzyx(),
                new ABFloat4(),
                BBFloat4.newAllocated(null),
                new ABFloat4().wzyx(),
                BBFloat4.newAllocated(null).wzyx(),
                new ABFloat4(),
                BBFloat4.newAllocated(null),
                new ABFloat4().wzyx(),
                BBFloat4.newAllocated(null).wzyx(),
        };



        Float4[] stores = new Float4[] {
                new ABFloat4(),
                BBFloat4.newAllocated(null),
                new ABFloat4().wzyx(),
                BBFloat4.newAllocated(null).wzyx(),
                float4s[4],
                float4s[5],
                float4s[6],
                float4s[7],
                float4s[8].wzyx(),
                float4s[9].wzyx(),
                float4s[10].wzyx(),
                float4s[11].wzyx()
        };

        for(Float4 f : float4s) {
            f.xyzw(right[0], right[1], right[2], right[3]);
        }

        for(Float4x4 f : float4x4s) {
            f.fillFromArray(left);
        }

        for(int i = 0; i < float4s.length; i++) {
            list.add(Arguments.of(float4x4s[i], float4s[i], stores[i], 40.28f, 28.404f, 91.913f, -4.009f));
        }

        return list;
    }

    @ParameterizedTest
    @MethodSource("provideMultiplyFloat4TimesFloat4x4")
    void testMultiplyFloat4TimesFloat4x4(
            @NotNull Float4x4 left, @NotNull Float4 right,  @NotNull Float4 store,
            float resX, float resY, float resZ, float resW
    ) {

        System.out.println("left: " + left);
        System.out.println("right: " + right);

        VMath.multiply(left, right, store);

        assertTrue(Vector.equals(store, new float[]{resX, resY, resZ, resW}, 0.0001f));
    }

    private static Collection<Arguments> provideMultiplyFloat3TimesFloat3x3() {
        ArrayList<Arguments> list = new ArrayList<>();

        float[] left = new float[] {
                1.3f, 4.2f, 6.55f,
                1f, 1f, 5.4f,
                -1f, 22.45f, 6.6f,
        };
        float[] right = new float[] {3.3f, 4.5f, -1f,};

        Float3x3[] float3x3s = new Float3x3[]{
                new ABFloat3x3(),
                BBFloat3x3.newAllocated(null),
                new ABFloat3x3(),
                BBFloat3x3.newAllocated(null),
                BBFloat3x3.newAllocated(null),
                BBFloat3x3.newAllocated(null),
                BBFloat3x3.newAllocated(null),
                BBFloat3x3.newAllocated(null),
                new ABFloat3x3(),
                new ABFloat3x3(),
                new ABFloat3x3(),
                new ABFloat3x3(),
        };

        Float3[] float3s = new Float3[] {
                new ABFloat3(),
                BBFloat3.newAllocated(null),
                new ABFloat3().zyx(),
                BBFloat3.newAllocated(null).zyx(),
                new ABFloat3(),
                BBFloat3.newAllocated(null),
                new ABFloat3().zyx(),
                BBFloat3.newAllocated(null).zyx(),
                new ABFloat3(),
                BBFloat3.newAllocated(null),
                new ABFloat3().zyx(),
                BBFloat3.newAllocated(null).zyx(),
        };



        Float3[] stores = new Float3[] {
                new ABFloat3(),
                BBFloat3.newAllocated(null),
                new ABFloat3().zyx(),
                BBFloat3.newAllocated(null).zyx(),
                float3s[4],
                float3s[5],
                float3s[6],
                float3s[7],
                float3s[8].zyx(),
                float3s[9].zyx(),
                float3s[10].zyx(),
                float3s[11].zyx()
        };

        for(Float3 f : float3s) {
            f.fillFromArray(right);
        }

        for(Float3x3 f : float3x3s) {
            f.fillFromArray(left);
        }

        for(int i = 0; i < float3s.length; i++) {
            list.add(Arguments.of(float3x3s[i], float3s[i], stores[i], 16.64f, 2.4f, 91.125f));
        }

        return list;
    }

    @ParameterizedTest
    @MethodSource("provideMultiplyFloat3TimesFloat3x3")
    void testMultiplyFloat3TimesFloat3x3(
            @NotNull Float3x3 left, @NotNull Float3 right, @NotNull Float3 store,
            float resX, float resY, float resZ
    ) {

        System.out.println("left: " + left);
        System.out.println("right: " + right);

        VMath.multiply(left, right, store);

        System.out.println("result: " + store);

        assertTrue(Vector.equals(store, new float[]{resX, resY, resZ}, 0.0001f));
    }


    private static @NotNull Collection<Arguments> provideVectorEqualsArgs() {
        ArrayList<Arguments> list = new ArrayList<>();

        FloatN[] vectorsA = new FloatN[] {
                new ABFloat1(),
                new ABFloat2(),
                new ABFloat3(),
                new ABFloat4(),
                new ABFloat1(),
                new ABFloat2(),
                new ABFloat3(),
                new ABFloat4(),
                BBFloat1.newAllocated(null),
                BBFloat2.newAllocated(null),
                BBFloat3.newAllocated(null),
                BBFloat4.newAllocated(null),
                BBFloat1.newAllocated(null),
                BBFloat2.newAllocated(null),
                BBFloat3.newAllocated(null),
                BBFloat4.newAllocated(null),
        };

        FloatN[] vectorsB = new FloatN[] {
                new ABFloat1(),
                new ABFloat2(),
                new ABFloat3(),
                new ABFloat4(),
                BBFloat1.newAllocated(null),
                BBFloat2.newAllocated(null),
                BBFloat3.newAllocated(null),
                BBFloat4.newAllocated(null),
                new ABFloat1(),
                new ABFloat2(),
                new ABFloat3(),
                new ABFloat4(),
                BBFloat1.newAllocated(null),
                BBFloat2.newAllocated(null),
                BBFloat3.newAllocated(null),
                BBFloat4.newAllocated(null),
        };

        float[][] datasA = new float[][] {
                {1.f},
                {1.f, 3.45f},
                {1.f, 3.45f, 7.23f},
                {1.f, 3.45f, 7.23f, 99.9f},
                {1.f},
                {1.f, 3.45f},
                {1.f, 3.45f, 7.23f},
                {1.f, 3.45f, 7.23f, 99.9f},
                {1.f},
                {1.f, 3.45f},
                {1.f, 3.45f, 7.23f},
                {1.f, 3.45f, 7.23f, 99.9f},
                {1.f},
                {1.f, 3.462f},
                {1.f, 3.46f, 7.23f},
                {1.f, 1.45f, 7.23f, 99.9f},
        };

        float[][] datasB = new float[][] {
                {1.f},
                {1.f, 3.45f},
                {1.f, 3.45f, 7.23f},
                {1.f, 3.45f, 7.23f, 99.9f},
                {1.f},
                {1.f, 3.46f},
                {1.f, 3.45f, 77.23f},
                {1.f, 3.45f, 7.23f, 99.9f},
                {1.f},
                {1.f, 3.45f},
                {1.f, 3.45f, 7.23f},
                {1.f, 3.45f, 7.23f, 99.9f},
                {10.f},
                {1.f, 3.45f},
                {1.f, 3.45f, 7.23f},
                {1.f, 4.45f, 7.23f, 99.9f},
        };

        float[] epsilons = new float[] {
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.01f, 5.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.01f, 0.01f, 0.0f,
        };

        boolean[] equals = new boolean[] {
                true, true, true, true,
                true, true, false, true,
                true, true, true, true,
                false, false, true, false,
        };

        for(int i = 0; i < equals.length; i++) {
            vectorsA[i].fillFromArray(datasA[i]);
            vectorsB[i].fillFromArray(datasB[i]);
            list.add(Arguments.of(vectorsA[i], vectorsB[i], datasB[i], epsilons[i], equals[i]));
        }


        return list;
    }

    @ParameterizedTest
    @MethodSource("provideVectorEqualsArgs")
    void testEqualsVector(@NotNull FloatN vector, @NotNull FloatN other, float @NotNull [] data, float epsilon, boolean result) {
        assertEquals(result, Vector.equals(vector, data, epsilon));
        assertEquals(result, Vector.equals(vector, other, epsilon));
    }

    @Test
    void testEqualsIntVector() {

        BBInt2 int2 = BBInt2.newAllocated(null);
        BBInt2 other = BBInt2.newAllocated(null);

        int2.xy(10, 12);
        other.xy(10, 12);

        assertTrue(Vector.equals(int2, other));

        int2.xy(123, 231);
        other.xy(123, 12);

        assertFalse(Vector.equals(int2, other));
    }

    private static @NotNull Collection<Arguments> provideMatrixEqualsArgs() {
        ArrayList<Arguments> list = new ArrayList<>();

        FloatMxN[] matricesA = new FloatMxN[]{
                new ABFloat4x4(),
                new ABFloat3x3(),
                BBFloat4x4.newAllocated(null),
                BBFloat3x3.newAllocated(null)
        };

        FloatMxN[] matricesB = new FloatMxN[]{
                new ABFloat4x4(),
                BBFloat3x3.newAllocated(null),
                BBFloat4x4.newAllocated(null),
                new ABFloat3x3(),
        };

        float[][] datasA = new float[][]{
                {1.f, 4.3f, 5.55f, 46.4f, 1.f, 4.44f, 8.5f, 436.4f, 11.2f, 9.23f, 5.515f, 436.4f, 14.f, 4.23f, 5.455f, 465.4f,},
                {1.f, 4.3f, 5.75f, 46.4f, 1.f, 4.44f, 8.5f, 434.4f, 11.2f,},
                {1.f, 4.3f, 5.55f, 46.4f, 1.f, 4.44f, 8.5f, 436.4f, 11.2f, 9.23f, 5.515f, 436.4f, 14.f, 4.23f, 5.455f, 465.4f,},
                {1.f, 4.3f, 5.55f, 46.4f, 1.f, 4.44f, 8.5f, 436.4f, 11.2f,},
        };

        float[][] datasB = new float[][]{
                {1.f, 4.3f, 5.55f, 46.4f, 1.f, 4.44f, 8.5f, 436.4f, 11.2f, 9.23f, 5.515f, 436.4f, 14.f, 4.23f, 5.455f, 465.4f,},
                {1.f, 4.3f, 5.55f, 46.4f, 1.f, 4.44f, 8.5f, 436.4f, 11.2f,},
                {1.f, 4.3f, 5.55f, 46.4f, 1.f, 4.46f, 8.5f, 436.4f, 11.2f, 9.23f, 5.515f, 436.4f, 14.f, 4.23f, 5.455f, 465.4f,},
                {1.f, 4.3f, 5.56f, 46.4f, 1.f, 4.43f, 8.51f, 436.4f, 11.2f,},
        };

        float[] epsilons = new float[]{
                0.0f, 10.0f, 0.0f, 0.011f,
        };

        boolean[] equals = new boolean[]{
                true, true, false, true,
        };


        for (int i = 0; i < equals.length; i++) {
            matricesA[i].fillFromArray(datasA[i]);
            matricesB[i].fillFromArray(datasB[i]);
            list.add(Arguments.of(matricesA[i], matricesB[i], datasB[i], epsilons[i], equals[i]));
        }

        return list;
    }

    @ParameterizedTest
    @MethodSource("provideMatrixEqualsArgs")
    void testEqualsMatrix(@NotNull FloatMxN matrix, @NotNull FloatMxN other, float @NotNull [] data, float epsilon, boolean result) {
        assertEquals(result, Matrix.equals(matrix, data, epsilon));
        assertEquals(result, Matrix.equals(matrix, other, epsilon));
    }

    @Test
    void testIdentityMatrix() {
        ABFloat3x3 mat = new ABFloat3x3();
        VMath.diagonalMatrix(1f, false, mat);
        System.out.println(mat);
        assertTrue(Matrix.isIdentity(mat, 0.0f));

        mat.put(0,0,1.2f);
        assertFalse(Matrix.isIdentity(mat, 0.0f));

        VMath.diagonalMatrix(1f, false, mat);
        assertTrue(Matrix.isIdentity(mat, 0.0f));

        mat.put(0,1,1f);
        assertFalse(Matrix.isIdentity(mat, 0.0f));
    }

    @Test
    void uniqueViewVector() {
        assertTrue(VMath.uniqueViewVector(new ABFloat4(), new ABFloat4()));

        Float4 a = new ABFloat4();
        Float4 b = new ABFloat4();

        assertFalse(VMath.uniqueViewVector(a, a.wzyx()));
        assertFalse(VMath.uniqueViewVector(a.wzyx(), a));

        assertTrue(VMath.uniqueViewVector(b, a.wzyx()));
        assertTrue(VMath.uniqueViewVector(a.wzyx(), b));

        assertTrue(VMath.uniqueViewVector(a.wzyx(), a.wzyx()));

    }

    @Test
    public void minInt() {
        Int2 a = BBInt2.newAllocated(null);
        Int2 b = BBInt2.newAllocated(null);

        a.xy(1, 5);
        b.xy(3, 4);

        VMath.min(a, b, a);

        assertEquals(1, a.x());
        assertEquals(4, a.y());
    }

    @Test
    public void minFloat() {
        Float2 a = BBFloat2.newAllocated(null);
        Float2 b = BBFloat2.newAllocated(null);

        a.xy(1, 5);
        b.xy(3, 4);

        VMath.min(a, b, a);

        assertEquals(1, a.x());
        assertEquals(4, a.y());
    }

    @Test
    public void maxInt() {
        Int2 a = BBInt2.newAllocated(null);
        Int2 b = BBInt2.newAllocated(null);

        a.xy(1, 5);
        b.xy(3, 4);

        VMath.max(a, b, a);

        assertEquals(3, a.x());
        assertEquals(5, a.y());
    }

    @Test
    public void maxFloat() {
        Float2 a = BBFloat2.newAllocated(null);
        Float2 b = BBFloat2.newAllocated(null);

        a.xy(1, 5);
        b.xy(3, 4);

        VMath.max(a, b, a);

        assertEquals(3, a.x());
        assertEquals(5, a.y());
    }

    @Test
    public void clampInt() {
        Int2 a = BBInt2.newAllocated(null);
        Int2 min = BBInt2.newAllocated(null);
        Int2 max = BBInt2.newAllocated(null);

        a.xy(1, 5);
        min.xy(-1, 6);
        max.xy(0, 10);

        VMath.clamp(a, min, max, a);

        assertEquals(0, a.x());
        assertEquals(6, a.y());
    }

    @Test
    public void clampFloat() {
        Float2 a = BBFloat2.newAllocated(null);
        Float2 min = BBFloat2.newAllocated(null);
        Float2 max = BBFloat2.newAllocated(null);

        a.xy(1, 5);
        min.xy(-1, 6);
        max.xy(0, 10);

        VMath.clamp(a, min, max, a);

        assertEquals(0, a.x());
        assertEquals(6, a.y());
    }

    @Test
    public void minUnsigned() {
        BBUInt2 a = BBUInt2.newAllocated(null);
        BBUInt2 b = BBUInt2.newAllocated(null);

        a.xy(1, 5);
        b.xy(-3, 4);

        VMath.minUnsigned(a, b, a);

        assertEquals(1, a.x());
        assertEquals(4, a.y());
    }

    @Test
    public void maxUnsigned() {
        BBUInt2 a = BBUInt2.newAllocated(null);
        BBUInt2 b = BBUInt2.newAllocated(null);

        a.xy(1, 5);
        b.xy(3, 4);

        VMath.maxUnsigned(a, b, a);

        assertEquals(3, a.x());
        assertEquals(5, a.y());
    }

    @Test
    public void clampUnsigned() {
        BBUInt2 a = BBUInt2.newAllocated(null);
        BBUInt2 min = BBUInt2.newAllocated(null);
        BBUInt2 max = BBUInt2.newAllocated(null);

        a.xy(1, 5);
        min.xy(-1, 6);
        max.xy(0, 10);

        VMath.clampUnsigned(a, min, max, a);

        assertEquals(0, a.x());
        assertEquals(6, a.y());
    }


    @Test
    void rotationMatrix() {

        ABFloat3x3 mat = new ABFloat3x3();

        VMath.rotationMatrix(3.141592653589793238462643383279502884197f, new ABFloat3(0, 0, 1), mat);
        assertTrue(Matrix.equals(mat, new float[] {-1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f}, 0.00001f));

        VMath.rotationMatrix(1.334f, VMath.normalize(new ABFloat3(0.2f, 0.9f, 0.4f), new ABFloat3()) , mat);
        assertTrue(Matrix.equals(mat, new float[] {0.2649029f, -0.2504983f, 0.9311697f, 0.5233179f, 0.8484336f, 0.0793655f, -0.8099166f, 0.4662736f, 0.3558427f}, 0.00001f));

    }

    @Test
    void translationMatrix() {
        ABFloat4x4 mat = new ABFloat4x4();
        mat.fillFromArray(new float[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});

        VMath.translationMatrix(new ABFloat3(30,40, 50), mat);

        assertTrue(Matrix.equals(mat, new float[] {1,2,3,30,5,6,7,40,9,10,11,50,13,14,15,16}, 0.0f));

    }

    @Test
    void diagonalMatrix() {
        ABFloat4x4 mat = new ABFloat4x4();
        mat.fillFromArray(new float[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});

        VMath.diagonalMatrix(50f, false, mat);

        assertTrue(Matrix.equals(mat, new float[] {
                50,2 ,3 ,4 ,
                5 ,50,7 ,8 ,
                9 ,10,50,12,
                13,14,15,50
        }, 0.0f));

        mat.fillFromArray(new float[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});

        VMath.diagonalMatrix(50f, true, mat);

        assertTrue(Matrix.equals(mat, new float[] {
                50,0 ,0 ,0 ,
                0 ,50,0 ,0 ,
                0 ,0 ,50,0 ,
                0 ,0 ,0 ,50
        }, 0.0f));
    }

    @Test
    void testRotationMatrix() {

        ABFloat3x3 mat = new ABFloat3x3();

        VMath.rotationMatrix(0.34f, 2.23f, 4.33f, mat);
        assertTrue(Matrix.equals(mat, new float[] {-0.577426f, -0.566969f, -0.587474f, -0.204257f, -0.596353f, 0.7763f, -0.79048f, 0.568251f, 0.228542f}, 0.00001f));

    }

    @Test
    void transpose3x33X3() {

        ABFloat3x3 mat = new ABFloat3x3();
        mat.fillFromArray(new float[] {
                1,2,3,
                4,5,6,
                7,8,9
        });

        VMath.transpose3x3(mat, mat);
        assertTrue(Matrix.equals(mat, new float[] {
                1,4,7,
                2,5,8,
                3,6,9
        }, 0f));

        ABFloat4x4 mat4x4 = new ABFloat4x4();
        mat4x4.fillFromArray(new float[] {
                1 ,2 ,3 ,4 ,
                5 ,6 ,7 ,8 ,
                9 ,10,11,12,
                13,14,15,16,
        });
        var mat2 = VMath.transpose3x3(mat4x4, new ABFloat4x4());
        System.out.println(mat2);
        assertTrue(Matrix.equals(mat2, new float[] {
                1 ,5 ,9 ,0 ,
                2 ,6 ,10,0 ,
                3 ,7 ,11,0 ,
                0 ,0 ,0 ,0 ,
        }, 0.01f));

    }

    @Test
    void projectionMatrix() {

        float width = 2f;
        float height = 2f;
        float near = 1f;
        float far = 10f;

        var mat = VMath.projectionMatrix(2f, width, height, near, far, false, false, 1.01f, new ABFloat4x4());



        ABFloat4 vector = new ABFloat4(width, height/4f, far, 1f);
        VMath.multiply(mat, vector, vector);

        System.out.println(vector);

    }

    @Test
    void projectionMatrixExplained() {
        var mat = VMath.projectionMatrix(16f/9f, 2.3f, 2.1f, 1f, 10f, false, true, 1.01f, new ABFloat4x4());
        var mat2 = VMath.projectionMatrixExplained(16f/9f, 2.3f, 2.1f, 1f, 10f, true, 1.01f);



        assertTrue(Matrix.equals(mat, mat2, 0.0001f));
    }
}