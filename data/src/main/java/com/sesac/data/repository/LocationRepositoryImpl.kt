package com.sesac.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.sesac.data.mapper.CoordMapper.toCoord
import com.sesac.data.mapper.toDomain
import com.sesac.data.mapper.toRequestDTO
import com.sesac.data.source.api.PetsApi
import com.sesac.domain.model.Coord
import com.sesac.domain.model.PetLocation
import com.sesac.domain.repository.LocationRepository
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.LocationException
import com.sesac.domain.result.LocationFlowResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import android.location.Location
import com.sesac.common.utils.smoothLocation

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val petsApi: PetsApi,
) : LocationRepository {
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
        } catch (e: Exception) {
            trySend(LocationFlowResult.Error(LocationException.Unknown(e)))
        }
        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    @SuppressLint("MissingPermission")
    override fun getRealtimePathLocation(): Flow<LocationFlowResult<Coord>> = callbackFlow {
        if (!isLocationEnabled()) {
            trySend(LocationFlowResult.Error(LocationException.LocationDisabled))
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500L)
            .setMaxUpdateDelayMillis(1000L)
            .build()

        var lastSmoothedLocation: Location? = null
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { loc ->
                    if (loc.accuracy > 25f) {
                        Log.d("LocationRepository", "Ignored: accuracy=${loc.accuracy}")
                        return@forEach
                    }
                    val smoothLoc = smoothLocation(lastSmoothedLocation, loc)
                    lastSmoothedLocation = smoothLoc
                    trySend(LocationFlowResult.Success(smoothLoc.toCoord()))
                }
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
        } catch (e: Exception) {
            trySend(LocationFlowResult.Error(LocationException.Unknown(e)))
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    override suspend fun postPetLocation(
        token: String,
        location: PetLocation
    ): Flow<AuthResult<PetLocation>> = flow {
        emit(AuthResult.Loading)
        val response = petsApi.postPetLocation(
            token = "Bearer $token",
            location = location.toRequestDTO()
        )
        Log.d("TAG-LcationRepositoryImpl", "post pet location response : $response")
        emit(AuthResult.Success(response.toDomain()))
    }.catch { e ->
        Log.e("LocationRepositoryImpl", "postPetLocation failed", e)
        emit(AuthResult.NetworkError(e))
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}
