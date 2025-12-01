package com.sesac.data.source.remote

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class SimpleSdpObserver : SdpObserver {

    private var continuation: kotlin.coroutines.Continuation<SessionDescription>? = null

    suspend fun await(): SessionDescription = suspendCoroutine {
        continuation = it
    }

    override fun onCreateSuccess(sdp: SessionDescription) {
        continuation?.resume(sdp)
    }

    override fun onSetSuccess() {
        // This is not used for createOffer/createAnswer but for setLocal/RemoteDescription
        // For simplicity, we can consider this as a success with a dummy SDP or handle it differently.
        // Or better, create a different observer for set actions.
        // For now, let's assume this observer is only for create actions.
    }

    override fun onCreateFailure(error: String) {
        continuation?.resumeWithException(Exception("Sdp creation failed: $error"))
    }

    override fun onSetFailure(error: String) {
        continuation?.resumeWithException(Exception("Sdp setting failed: $error"))
    }
}
