package com.sesac.data.di

import android.content.Context
import com.sesac.data.source.remote.SignalingClient
import com.sesac.data.source.remote.WebRTCClient
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
        // WebRTCClient 내부에서 Encoder/Decoder Factory를 생성하므로, 여기서만 초기화하면 됩니다.
        return PeerConnectionFactory.builder()
            // .setVideoEncoderFactory(...) 및 .setVideoDecoderFactory(...)는 WebRTCClient에서
            // DefaultVideoEncoderFactory/DefaultVideoDecoderFactory를 사용하여 설정
            .createPeerConnectionFactory()
    }

    @Provides
    @Singleton
    fun provideSignalingClient(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): SignalingClient {
        return SignalingClient(okHttpClient, moshi)
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
