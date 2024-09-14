package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.math.vector.buffer.byten.BBByte1;
import de.linusdev.lutils.math.vector.buffer.intn.BBUInt1;
import de.linusdev.lutils.nat.string.NullTerminatedUTF8String;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Pointer64Test {
    @Test
    void test() {
        Pointer64 pointer64 = Pointer64.of(null);
        assertTrue(pointer64.isNullPtr());
        assertEquals(Pointer64.NULL_POINTER, Pointer64.refL(null));

        NullTerminatedUTF8String str = NullTerminatedUTF8String.newAllocated("Test");
        pointer64.set(str.getPointer());
        assertFalse(pointer64.isNullPtr());
        assertEquals(str.getPointer(), pointer64.get());
        assertEquals(str.getPointer(), Pointer64.refL(str));


        str = NullTerminatedUTF8String.newAllocated("Test2");
        pointer64 = Pointer64.of(str);
        assertEquals(str.getPointer(), pointer64.get());

        TypedPointer64<NullTerminatedUTF8String> typedPointer64 = TypedPointer64.ref(str);
        assertEquals(str.getPointer(), typedPointer64.get());
        assertFalse(typedPointer64.isNullPtr());
        typedPointer64.set(null);
        assertTrue(typedPointer64.isNullPtr());

        TypedPointer64<BBByte1> pOther = TypedPointer64.ofOther(str);
        assertEquals(str.getPointer(), pOther.get());
        assertFalse(pOther.isNullPtr());
        pOther.set(null);
        assertTrue(pOther.isNullPtr());


        StructureArray<BBUInt1> array = StructureArray.newAllocated(2, BBUInt1.class, BBUInt1::newUnallocated);
        TypedPointer64<BBUInt1> pArray = TypedPointer64.ofArray(array);

        assertEquals(array.getPointer(), pArray.get());

        TypedPointer64<StructureArray<BBUInt1>> realPArray = pArray.cast();
        assertEquals(array.getPointer(), realPArray.get());

    }

    @Test
    void testOfArray() {
        var array = StructureArray.newAllocated(4, BBUInt1.class, BBUInt1::newUnallocated);
        TypedPointer64<BBUInt1> pointer = BBTypedPointer64.newAllocated1(null);
        pointer.setOfArray(array);

        assertEquals(array.getPointer(), array.get(0).getPointer());
    }

    @Test
    void testRNotNull() {
        assertTrue(Pointer64.requireNotNull(1L, "Test"));
        assertThrows(IllegalArgumentException.class, () -> Pointer64.requireNotNull(Pointer64.NULL_POINTER, ""));
    }

}