package com.sesac.data.di

import android.content.Context
import com.sesac.data.repository.SessionRepositoryImpl
import com.sesac.data.source.webrtc.SignalingClient
import com.sesac.data.source.webrtc.WebRTCClient
import com.sesac.domain.repository.SessionRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.PeerConnectionFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebRTCDataModule {

    @Provides
    @Singleton
    fun provideEglBase(): EglBase {
        return EglBase.create()
    }

    @Provides
    @Singleton
    fun providePeerConnectionFactory(
        @ApplicationContext context: Context,
        eglBase: EglBase
    ): PeerConnectionFactory {
        // PeerConnectionFactory 초기화는 한 번만 수행
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .setFieldTrials("WebRTC-H264HighProfile/Enabled/") // H264 하드웨어 인코딩 활성화 등
                .createInitializationOptions()
        )

        val videoEncoderFactory = DefaultVideoEncoderFactory(eglBase.eglBaseContext, true, true)
        val videoDecoderFactory = DefaultVideoDecoderFactory(eglBase.eglBaseContext)

        // WebRTCClient 내부에서 Encoder/Decoder Factory를 생성하므로, 여기서만 초기화하면 됩니다.
        return PeerConnectionFactory.builder()
            .setVideoEncoderFactory(videoEncoderFactory)
            .setVideoDecoderFactory(videoDecoderFactory)
            .createPeerConnectionFactory()
    }

    @Provides
    @Singleton
    fun provideSignalingClient(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        sessionRepository: SessionRepository // SessionRepository 주입 추가
    ): SignalingClient {
        return SignalingClient(okHttpClient, moshi, sessionRepository) // 생성자에 전달
    }

    @Provides
    @Singleton
    fun provideWebRTCClient(
        @ApplicationContext context: Context,
        eglBase: EglBase,
        peerConnectionFactory: PeerConnectionFactory
    ): WebRTCClient {
        return WebRTCClient(context, eglBase, peerConnectionFactory)
    }
}
