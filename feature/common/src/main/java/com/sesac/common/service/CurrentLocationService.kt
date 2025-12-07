package com.sesac.common.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sesac.common.R
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.LocationException
import com.sesac.domain.result.LocationFlowResult
import com.sesac.domain.usecase.location.LocationUseCase
import com.sesac.domain.usecase.session.SessionUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

//const val NOTIFICATION_ID = 12345
//private const val CHANNEL_ID = "location_service_channel"

@AndroidEntryPoint
class CurrentLocationService: Service() {
    @Inject
    lateinit var locationUseCase: LocationUseCase
    @Inject
    lateinit var sessionUseCase: SessionUseCase
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "location_notification_channel"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("TAG-LocationService", "Service Created")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundNotify()
        Log.e("TAG", "Service Start")

        serviceScope.launch { // Launch a coroutine to get token and collect location
            val token = sessionUseCase.getAccessToken().first()
            if (token.isNullOrEmpty()) {
                Log.e("TAG-CurrentLocationService", "No token found, stopping service.")
                stopSelf()
                return@launch
            }

            // 권한 체크
            if (!hasLocationPermissions()) {
                Log.e("TAG-CurrentLocationService", "Location permissions not granted")
                stopSelf()
                return@launch
            }
            val userInfo = sessionUseCase.getUserInfo().first()

            locationUseCase.getCurrentLocationUseCase()
                .onEach { locationFlowResult ->
                    when (locationFlowResult) {
                        is LocationFlowResult.Success -> {
                            val coord = locationFlowResult.coord
                            val petLocation = coord.toPetLocation() // Map Coord to PetLocation
                            Log.d("TAG-CurrentLocationService", "Location updated : $coord")

                            // Send location to backend
                            if (userInfo != null && userInfo.isPet ?: false) {
                                locationUseCase.postPetLocationUseCase(token, petLocation)
                                    .collectLatest { result ->
                                        when (result) {
                                            is AuthResult.Success -> {
                                                Log.d("TAG-CurrentLocationService", "Location sent to backend: ${result.resultData}")
                                            }
                                            is AuthResult.NetworkError -> {
                                                Log.e("TAG-CurrentLocationService", "Failed to send location to backend: ${result.exception.message}")
                                            }
                                            else -> { /* Loading or other states */ }
                                        }
                                    }
                            }
                        }
                        is LocationFlowResult.Error -> handleLocationError(locationFlowResult.exception)
                    }
                }
                .catch { e ->
                    when (e) {
                        is SecurityException -> {
                            Log.e("TAG-CurrentLocationService", "SecurityException: ${e.message}")
                            stopSelf()
                            return@catch
                        }
                        is Exception -> {
                            Log.e("TAG-CurrentLocationService", "Failed to start foreground: ${e.message}")
                            stopSelf()
                            return@catch
                        }
                    }
                }
                .launchIn(this) // Use the serviceScope
        }


        return START_STICKY
    }

    private fun hasLocationPermissions(): Boolean {
//        val context = Context
        val fineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocation || coarseLocation
    }

    private fun handleLocationError(exception: LocationException) {
        when (exception) {
            is LocationException.PermissionDenied -> {
                Log.e("LocationService", "권한 거부됨")
                // 사용자에게 알림 표시
                stopSelf()
            }
            is LocationException.LocationDisabled -> {
                Log.e("LocationService", "위치 서비스 꺼짐")
                // 위치 서비스 활성화 요청
            }
            is LocationException.Timeout -> {
                Log.e("LocationService", "위치 찾기 타임아웃")
                // 재시도 로직
            }
            is LocationException.NoLocation -> {
                Log.w("LocationService", "위치 정보 없음")
            }
            is LocationException.Unknown -> {
                Log.e("LocationService", "알 수 없는 오류", exception.cause)
            }
        }
    }

    private lateinit var notificationManager: NotificationManager

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundNotify() {
        val channelId = "location_notification_channel"
        notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Current Location Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("사용자 위치를 10초마다 추적 중")
            .setContentText("앱이 현재 위치를 추적하고 있습니다.")
            .setSmallIcon(R.drawable.image_7hours_foreground)
            /**
             * [사용자가 실수로 끄면 안 되는 중요한 작업에 대한 알림에 사용]
             * finish : stopForeground(true), NotificationManager.cancel(notificationId)
             * ex::
             * 위치 추적 앱: 현재 위치를 계속 추적 중일 때             *
             * 음악 플레이어: 음악이 재생 중일 때             *
             * 전화 통화: 통화가 진행 중일 때             *
             * 파일 다운로드: 파일 다운로드가 진행 중일 때
             */
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "위치 서비스",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "위치 추적 서비스 알림"
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Log.d("TAG-CurrentLocationService", "Service Destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}