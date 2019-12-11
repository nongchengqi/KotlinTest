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
 * figure out if call substate has changed and notify the {@class InCallMessageController} to
 * display the indication on UI.
 *
 */
public class CallSubstateNotifier implements InCallDetailsListener {

    private final List<InCallSubstateListener> mCallSubstateListeners =
            new CopyOnWriteArrayList<>();

    private static CallSubstateNotifier sCallSubstateNotifier;
    private int mCallSubstate = QtiVideoCallConstants.CALL_SUBSTATE_NONE;

    /**
     * This method returns a singleton instance of {@class CallSubstateNotifier}
     */
    public static synchronized CallSubstateNotifier getInstance() {
        if (sCallSubstateNotifier == null) {
            sCallSubstateNotifier = new CallSubstateNotifier();
        }
        return sCallSubstateNotifier;
    }

    /**
     * This method adds a new call substate listener. Users interested in listening to call
     * substate changes should add a listener of type {@class InCallSubstateListener}
     */
    public void addListener(InCallSubstateListener listener) {
        Preconditions.checkNotNull(listener);
        mCallSubstateListeners.add(listener);
    }

    /**
     * This method removes an existing call substate listener. Users listening to call
     * substate changes when not interested any more can de-register an existing listener of type
     * {@class InCallSubstateListener}
     */
    public void removeListener(InCallSubstateListener listener) {
        if (listener != null) {
            mCallSubstateListeners.remove(listener);
        } else {
            Log.e(this, "Can't remove null listener");
        }
    }

    /**
     * Private constructor. Must use getInstance() to get this singleton.
     */
    private CallSubstateNotifier() {
    }

    /**
     * This method overrides onDetailsChanged method of {@class InCallDetailsListener}. We are
     * notified when call details change and extract the call substate from the extras, detect if
     * call substate changed and notify all registered listeners.
     */
    @Override
    public void onDetailsChanged(Call call, android.telecom.Call.Details details) {
        Log.d(this, "onDetailsChanged - call: " + call + "details: " + details);
        final Bundle extras =  (call != null && details != null) ? details.getExtras() : null;
        final int callSubstate = (extras != null) ? extras.getInt(
                QtiVideoCallConstants.CALL_SUBSTATE_EXTRA_KEY,
                QtiVideoCallConstants.CALL_SUBSTATE_NONE) :
                QtiVideoCallConstants.CALL_SUBSTATE_NONE;

        if (callSubstate != mCallSubstate) {
            mCallSubstate = callSubstate;
            Preconditions.checkNotNull(mCallSubstateListeners);
            for (InCallSubstateListener listener : mCallSubstateListeners) {
                listener.onCallSubstateChanged(call, mCallSubstate);
            }
        }
    }
}
