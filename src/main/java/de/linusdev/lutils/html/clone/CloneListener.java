/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.html.clone;

import de.linusdev.lutils.html.HtmlAttribute;
import de.linusdev.lutils.html.HtmlObject;
import org.jetbrains.annotations.NotNull;

public interface CloneListener {

    @NotNull CloneListener EMPTY = new CloneListener() {
        @Override
        public void onChildCloned(@NotNull HtmlObject clone) {}

        @Override
        public void onAttributeCloned(@NotNull HtmlAttribute attribute) {}
    };

    void onChildCloned(@NotNull HtmlObject clone);

    void onAttributeCloned(@NotNull HtmlAttribute attribute);

}
