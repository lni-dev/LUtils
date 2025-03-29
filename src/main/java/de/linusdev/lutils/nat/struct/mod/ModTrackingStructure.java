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

package de.linusdev.lutils.nat.struct.mod;

import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ModTrackingStructure extends Structure {

    protected final boolean trackModifications;
    protected ReentrantLock modificationLock;
    protected final int modificationSplitOffset = 128;
    protected ModificationInfo modInfo = null;

    protected ModTrackingStructure(boolean trackModifications) {
        this.trackModifications = trackModifications;
    }

    @Override
    protected void useBuffer(
            @NotNull Structure mostParentStructure,
            int offset,
            @NotNull StructureInfo info
    ) {
        super.useBuffer(mostParentStructure, offset, info);
    }

    @Override
    public void claimBuffer(@NotNull ByteBuffer buffer) {
        super.claimBuffer(buffer);
        this.modificationLock = new ReentrantLock();
    }

    @Override
    protected void onModification(int offset, int size) {
        if(trackModifications) {
            modificationLock.lock();

            int offsetEnd = offset + size;

            if(modInfo == null) {
                modInfo = new ModificationInfo(offset, offsetEnd);
            } else {
                modInfo.add(offset, offsetEnd, modificationSplitOffset, null);
                if(modInfo.previous != null)
                    modInfo = modInfo.previous;
            }

            modificationLock.unlock();
        }

        super.onModification(offset, size);
    }

    /**
     * Get the first {@link ModificationInfo}. Only available if this structure was {@link #isModified() modified}
     * and {@link #tracksModifications() tracks modifications}.
     * @param clear {@code true} if the modifications should be cleared.
     * @return {@link ModificationInfo} or {@code null}
     */
    @ApiStatus.Internal
    public @Nullable ModificationInfo getFirstModificationInfo(boolean clear) {
        ModificationInfo ret = modInfo;
        if(clear) modInfo = null;
        return ret;
    }

    /**
     * Whether this structure tracks modifications using {@link ModificationInfo}.
     */
    public boolean tracksModifications() {
        return trackModifications;
    }

    /**
     * Calls {@link ModificationsHandler#handle(ModificationInfo)} for each {@link ModificationInfo}.
     * If this structure was not {@link #isModified() modified}, {@code false} will be returned.
     * @param handler {@link ModificationsHandler}
     * @return {@code true} if this structure was {@link #isModified() modified}.
     */
    @SuppressWarnings("unused")
    public boolean handleModifications(@NotNull ModificationsHandler handler) {
        if (!isModified())
            return false;

        //set structure to unmodified first. During copying there may be coming in new modifications,
        //that must be copied in the next handleModifications()...
        unmodified();

        if (!tracksModifications()) {
            //No info about the modifications given. Copy the complete buffer.
            handler.handle(new ModificationInfo(0, getRequiredSize()));
            return true;
        }

        //Acquire lock for this structure's modifications info.
        modificationLock.lock();
        try {
            ModificationInfo first = getFirstModificationInfo(true);

            while (first != null) {
                handler.handle(first);
                first = first.next;
            }
        } finally {
            modificationLock.unlock();
        }

        return true;
    }

    public interface ModificationsHandler {
        void handle(@NotNull ModificationInfo info);
    }
}
