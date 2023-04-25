/*
 * Copyright (c) 2021-2022 Linus Andera
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

package de.linusdev.lutils.async.queue;

import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.consumer.ResultConsumer;
import de.linusdev.lutils.async.executable.ExecutableTask;
import de.linusdev.lutils.async.manager.AsyncQueue;
import de.linusdev.lutils.async.manager.HasAsyncManager;
import de.linusdev.lutils.async.manager.HasAsyncQueue;


/**
 * <p>
 *     A {@link Queueable} is special {@link ExecutableTask}, that can be queued and executed. Unlike other {@link Task Tasks},
 *     a Queueable will actually end up in some kind of queue after being {@link #queue() queued}.<br>
 * </p>
 *
 * <p>
 *     By invoking {@link #queue()}, a {@link Future} will be created and {@link AsyncQueue#queue(QueueableFuture) queued}.
 *     queueing order and simultaneous task count may depend on the specific implementation of the used {@link AsyncQueue}.
 * </p>
 *
 * <p>
 *     The result can be processed by using listeners like {@link Future#then(ResultConsumer)}. These can be set directly when queuing by
 *     calling {@link Queueable#queue(ResultConsumer)}. These listeners may be called from the thread used by the queue. That is why
 *     <b style="color:red">{@link Object#wait()}, {@link Thread#sleep(long)} or any other blocking tasks may never be called inside these listeners!</b>.
 *     This may delay the queue and could lead to deadlocks.
 * </p><br>
 *
 * <span style="margin-bottom:0;padding-bottom:0;font-size:10px;font-weight:'bold';">Example {@link Queueable} usage:</span>
 * <pre>{@code
 * //Retrieve a Message...
 * //This creates a Queueable
 * var msgRetriever = lApi.getRequestFactory()
 *                        .getChannelMessage("channelId", "messageId");
 *
 * //Queue and create a listener
 * //This creates a Future
 * msgRetriever.queue((result, response, error) -> {
 *             if(error != null) {
 *                 System.out.println("could not get message.");
 *                 return;
 *             }
 *
 *             System.out.println("Message content: " + result.getContent());
 *
 *         });
 * }</pre>
 *
 * @param <T> Type of the data that should be retrieved / the result of the Task
 * @see Future
 * @see Task
 */
public interface Queueable<T, R extends QResponse> extends ExecutableTask<T, R>, HasAsyncManager, HasAsyncQueue<R> {

}
