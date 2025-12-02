package com.sesac.android7hours.di

import com.sesac.common.usecase.webrtc.*
import com.sesac.common.repository.WebRTCRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object WebRTCUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideWebRTCUseCase(repository: WebRTCRepository): WebRTCUseCase {
        return WebRTCUseCase(
            initializeWebRTC = InitializeWebRTCUseCase(repository),
            sendOffer = SendOfferUseCase(repository),
            sendAnswer = SendAnswerUseCase(repository),
            setRemoteDescription = SetRemoteDescriptionUseCase(repository),
            sendIceCandidate = SendIceCandidateUseCase(repository),
            observeSignalingEvents = ObserveSignalingEventsUseCase(repository),
            observeRemoteVideoTrack = ObserveRemoteVideoTrackUseCase(repository),
            observeLocalVideoTrack = ObserveLocalVideoTrackUseCase(repository),
            observeSessionState = ObserveSessionStateUseCase(repository),
            closeSession = CloseSessionUseCase(repository)
        )
    }
}
