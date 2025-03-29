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

package de.linusdev.lutils.image.atlas;

import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.ImageSize;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ImageRef<ID> implements ImageSize {

     private final ID id;
     private boolean rotate = false;
     private final @NotNull ImageSize size;
     private final @NotNull Store store;

     public ImageRef(ID id, @NotNull ImageSize size, @NotNull Store store) {
         this.id = id;
         this.size = size;
          this.store = store;
     }

     static <ID> @NotNull ImageRef<ID> ofImage(@NotNull Image src, ID id) {
          return new ImageRef<>(id, src, new Store() {
               @Override
               public void storeTo(@NotNull Image image, int offsetX, int offsetY) {
                    Image.copy(src, 0, 0, image, offsetX, offsetY);
               }

               @Override
               public void storeRotatedTo(@NotNull Image image, int offsetX, int offsetY) {
                    Image.copy(src.createRotatedView(), 0, 0, image, offsetX, offsetY);
               }
          });
     }

     void storeTo(@NotNull Atlas<?, ID> atlas, int offsetX, int offsetY) throws IOException {
          if (rotate) store.storeRotatedTo(atlas, offsetX, offsetY);
          else store.storeTo(atlas, offsetX, offsetY);
          atlas.addImage(this, offsetX, offsetY);
     }

     protected void onAddedToAtlas(@SuppressWarnings("unused") @NotNull AtlasImage<ID> added) {}

     void rotate() {
          rotate = !rotate;
     }

     boolean isRotated() {
          return rotate;
     }

     public ID getId() {
          return id;
     }

     @Override
     public int getWidth() {
          return rotate ? size.getHeight() : size.getWidth();
     }

     @Override
     public int getHeight() {
          return rotate ? size.getWidth() : size.getHeight();
     }

     public interface Store {
          void storeTo(@NotNull Image image, int offsetX, int offsetY) throws IOException;

          void storeRotatedTo(@NotNull Image image, int offsetX, int offsetY) throws IOException;
     }
}
