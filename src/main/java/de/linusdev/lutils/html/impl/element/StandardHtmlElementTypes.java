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

package de.linusdev.lutils.html.impl.element;

import de.linusdev.lutils.html.HtmlElementType;
import org.jetbrains.annotations.NotNull;

import static de.linusdev.lutils.html.impl.element.StandardHtmlElement.Type.*;

public class StandardHtmlElementTypes {
    public final static @NotNull StandardHtmlElement.Type HTML       =      newNormal("html");
    public final static @NotNull StandardHtmlElement.Type HEAD       =      newNormal("head");
    public final static @NotNull StandardHtmlElement.Type BODY       =      newNormal("body");
    public final static @NotNull StandardHtmlElement.Type DIV        =      newNormal("div");
    public final static @NotNull StandardHtmlElement.Type A          =      newNormal("a");
    public final static @NotNull StandardHtmlElement.Type BUTTON     =      newNormal("button");
    public final static @NotNull StandardHtmlElement.Type OL         =      newNormal("ol");
    public final static @NotNull StandardHtmlElement.Type UL         =      newNormal("ul");
    public final static @NotNull StandardHtmlElement.Type LI         =      newNormal("li");
    public final static @NotNull StandardHtmlElement.Type VIDEO      =      newNormal("video");

    public final static @NotNull StandardHtmlElement.Type TITLE      =      newInline("title");
    public final static @NotNull StandardHtmlElement.Type PARAGRAPH  =      newInline("p");
    public final static @NotNull StandardHtmlElement.Type SPAN       =      newInline("span");
    public final static @NotNull StandardHtmlElement.Type H1         =      newInline("h1");
    public final static @NotNull StandardHtmlElement.Type H2         =      newInline("h2");
    public final static @NotNull StandardHtmlElement.Type H3         =      newInline("h3");
    public final static @NotNull StandardHtmlElement.Type H4         =      newInline("h4");
    public final static @NotNull StandardHtmlElement.Type H5         =      newInline("h5");
    public final static @NotNull StandardHtmlElement.Type H6         =      newInline("h6");
    public final static @NotNull StandardHtmlElement.Type B          =      newInline("b");
    public final static @NotNull StandardHtmlElement.Type STRONG     =      newInline("strong");

    public final static @NotNull StandardHtmlElement.Type AREA       =      newVoid("area");
    public final static @NotNull StandardHtmlElement.Type BASE       =      newVoid("base");
    public final static @NotNull StandardHtmlElement.Type BR         =      newVoid("br");
    public final static @NotNull StandardHtmlElement.Type COL        =      newVoid("col");
    public final static @NotNull StandardHtmlElement.Type EMBED      =      newVoid("embed");
    public final static @NotNull StandardHtmlElement.Type HR         =      newVoid("hr");
    public final static @NotNull StandardHtmlElement.Type IMG        =      newVoid("img");
    public final static @NotNull StandardHtmlElement.Type INPUT      =      newVoid("input");
    public final static @NotNull StandardHtmlElement.Type LINK       =      newVoid("link");
    public final static @NotNull StandardHtmlElement.Type META       =      newVoid("meta");
    public final static @NotNull StandardHtmlElement.Type SOURCE     =      newVoid("source");
    public final static @NotNull StandardHtmlElement.Type TRACK      =      newVoid("track");
    public final static @NotNull StandardHtmlElement.Type WBR        =      newVoid("wbr");

    public final static @NotNull HtmlElementType<?> @NotNull [] VALUES = new HtmlElementType[] {
            HTML,
            HEAD,
            BODY,
            DIV,
            A,
            BUTTON,
            OL,
            UL,
            LI,
            VIDEO,

            TITLE,
            PARAGRAPH,
            SPAN,
            H1,
            H2,
            H3,
            H4,
            H5,
            H6,
            B,
            STRONG,

            AREA,
            BASE,
            BR,
            COL,
            EMBED,
            HR,
            IMG,
            INPUT,
            LINK,
            META,
            SOURCE,
            TRACK,
            WBR,
    };

    private StandardHtmlElementTypes() { }


}
