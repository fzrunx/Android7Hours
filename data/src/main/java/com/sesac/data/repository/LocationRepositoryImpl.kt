package com.sesac.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.sesac.data.mapper.CoordMapper.toCoord
import com.sesac.domain.model.Coord
import com.sesac.domain.repository.LocationRepository
import com.sesac.domain.result.LocationException
import com.sesac.domain.result.LocationFlowResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import java.util.concurrent.TimeUnit

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): LocationRepository {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getCurrentCoord(): Flow<LocationFlowResult<Coord>> = callbackFlow {
        // 위치 서비스 활성화 확인
        if (!isLocationEnabled()) {
            trySend(LocationFlowResult.Error(LocationException.LocationDisabled))
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.SECONDS.toMillis(10)
        ).apply {
            setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(5))
            setMaxUpdateDelayMillis(TimeUnit.SECONDS.toMillis(15))
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                /* 가장 최신 위치 정보만을 갸져옴 */
                locationResult.lastLocation?.let { location ->
                    trySend(LocationFlowResult.Success(location.toCoord()))
                } ?: trySend(LocationFlowResult.Error(LocationException.NoLocation))
            }
        }
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            ).addOnFailureListener { exception ->
                val error = when (exception) {
                    is SecurityException -> LocationException.PermissionDenied
                    is ApiException -> LocationException.Timeout
                    else -> LocationException.Unknown(exception)
                }
                trySend(LocationFlowResult.Error(error))
            }
        } catch (e: SecurityException) {
            trySend(LocationFlowResult.Error(LocationException.PermissionDenied))
            close()
        } catch (e: Exception) {
            trySend(LocationFlowResult.Error(LocationException.Unknown(e)))
            close()
        }

        /**
         * Flow 가 취소(Coroutine Finish)될 때  위치 갱신 중지
         */
        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


}
