/* Copyright (c) 2015, The Linux Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of The Linux Foundation nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.evan.incall.incallui;

import android.os.Bundle;
import com.evan.incall.incallui.InCallPresenter.InCallDetailsListener;
import com.google.common.base.Preconditions;
import org.codeaurora.QtiVideoCallConstants;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class listens to incoming events from the {@class InCallDetailsListener}.
 * When call details change, this class is notified and we parse the extras from the details to
 * figure out if session modification cause has been sent when a call upgrades/downgrades and
 * notify the {@class InCallMessageController} to display the indication on UI.
 */
public class SessionModificationCauseNotifier implements InCallDetailsListener{

    private final List<InCallSessionModificationCauseListener> mSessionModificationCauseListeners
            = new CopyOnWriteArrayList<>();

    private static SessionModificationCauseNotifier sSessionModificationCauseNotifier;

    /**
     * Returns a singleton instance of {@class SessionModificationCauseNotifier}
     */
    public static synchronized SessionModificationCauseNotifier getInstance() {
        if (sSessionModificationCauseNotifier == null) {
            sSessionModificationCauseNotifier = new SessionModificationCauseNotifier();
        }
        return sSessionModificationCauseNotifier;
    }

    /**
     * Adds a new session modification cause listener. Users interested in this cause
     * should add a listener of type {@class InCallSessionModificationCauseListener}
     */
    public void addListener(InCallSessionModificationCauseListener listener) {
        Preconditions.checkNotNull(listener);
        mSessionModificationCauseListeners.add(listener);
    }

    /**
     * Removes an existing session modification cause listener. Users listening to any cause
     * changes when not interested any more can de-register an existing listener of type
     * {@class InCallSessionModificationCauseListener}
     */
    public void removeListener(InCallSessionModificationCauseListener listener) {
        if (listener != null) {
            mSessionModificationCauseListeners.remove(listener);
        } else {
            Log.e(this, "Can't remove null listener");
        }
    }

    /**
     * Private constructor. Must use getInstance() to get this singleton.
     */
    private SessionModificationCauseNotifier() {
    }

    /**
     * Overrides onDetailsChanged method of {@class InCallDetailsListener}. We are
     * notified when call details change and extract the session modification cause from the
     * extras, detect if the cause has changed and notify all registered listeners.
     */
    @Override
    public void onDetailsChanged(Call call, android.telecom.Call.Details details) {
        Log.d(this, "onDetailsChanged: - call: " + call + "details: " + details);
        final Bundle extras =  (call != null && details != null) ? details.getExtras() : null;
        final int sessionModificationCause = (extras != null) ? extras.getInt(
                QtiVideoCallConstants.SESSION_MODIFICATION_CAUSE_EXTRA_KEY,
                QtiVideoCallConstants.CAUSE_CODE_UNSPECIFIED) :
                QtiVideoCallConstants.CAUSE_CODE_UNSPECIFIED;

        if (sessionModificationCause != QtiVideoCallConstants.CAUSE_CODE_UNSPECIFIED) {
            Preconditions.checkNotNull(mSessionModificationCauseListeners);
            for (InCallSessionModificationCauseListener listener :
                    mSessionModificationCauseListeners) {
                listener.onSessionModificationCauseChanged(call, sessionModificationCause);
            }
        }
    }
}
