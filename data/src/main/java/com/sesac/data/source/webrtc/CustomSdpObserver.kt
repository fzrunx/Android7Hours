package com.sesac.data.source.webrtc

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// A more robust SdpObserver that can handle both create and set operations.
internal class CustomSdpObserver(
    private val continuation: Continuation<Any?> // Use Any? to handle both SessionDescription and Unit
) : SdpObserver {

    override fun onCreateSuccess(sdp: SessionDescription) {
        continuation.resume(sdp)
    }

    override fun onSetSuccess() {
        continuation.resume(Unit) // Resume with Unit for set operations
    }

    override fun onCreateFailure(error: String) {
        continuation.resumeWithException(Exception("Sdp creation failed: $error"))
    }

    override fun onSetFailure(error: String) {
        continuation.resumeWithException(Exception("Sdp setting failed: $error"))
    }
}
