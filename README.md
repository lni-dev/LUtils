# LUtils
A Utils Library for my other Projects

## Installation
Add it as implementation to your build.gradle.
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'de.linusdev:lutils:1.1.3'
}
```

## Contents
- [LLinkedList](https://github.com/lni-dev/LUtils/blob/master/src/main/java/de/linusdev/lutils/llist/LLinkedList.java): A thread safe linked list
- [SGR](https://github.com/lni-dev/LUtils/blob/master/src/main/java/de/linusdev/lutils/ansi/sgr/SGR.java): ANSI "Select Graphic Rendition" control sequence
- [Color](https://github.com/lni-dev/LUtils/blob/master/src/main/java/de/linusdev/lutils/color/Color.java): Color class (RGBA and HSVA)
- [Bitfield](https://github.com/lni-dev/LUtils/blob/master/src/main/java/de/linusdev/lutils/bitfield/): Bitfield classes
- [Async Framework](https://github.com/lni-dev/LUtils/blob/master/src/main/java/de/linusdev/lutils/async/): Async classes
- [Structure](https://github.com/lni-dev/LUtils/tree/master/src/main/java/de/linusdev/lutils/struct): classes to create C-structures in Java
- [VMath](https://github.com/lni-dev/LUtils/tree/master/src/main/java/de/linusdev/lutils/math): Vector and matrix math. Buffer backed or array backed.
- [HTTP](https://github.com/lni-dev/LUtils/tree/master/src/main/java/de/linusdev/lutils/http): HTTPRequest, HTTPResponse, Header and more.
- [Code Generator](https://github.com/lni-dev/LUtils/tree/master/src/main/java/de/linusdev/lutils/codegen): Generate Java Code. WIP!

## Todo
- Html parser:
  - Skip leading spaces only if there was a \n before.
- Replacement for nio.DirectByteBuffer:
  - DirectByteBuffer.allocatedDirect() is slow and bad for GC: see https://blog.lwjgl.org/memory-management-in-lwjgl-3/
  - Replace it with a custom buffer, which uses Unsafe for allocation
  - Middleware interface between the buffer and Unsafe, so Unsafe could be easily replaced with native functions, that call malloc and free.