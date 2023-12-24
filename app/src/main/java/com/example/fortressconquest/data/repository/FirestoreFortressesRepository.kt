package com.example.fortressconquest.data.repository

import android.location.Location
import com.example.fortressconquest.di.FortressesCollectionReference
import com.example.fortressconquest.di.UsersCollectionReference
import com.example.fortressconquest.domain.model.Fortress
import com.example.fortressconquest.domain.model.User
import com.example.fortressconquest.domain.repository.FortressesRepository
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class FirestoreFortressesRepository @Inject constructor(
    @FortressesCollectionReference private val fortressesRef: CollectionReference,
    @UsersCollectionReference private val usersRef: CollectionReference,
    private val db: FirebaseFirestore
): FortressesRepository {
    override suspend fun setFortress(user: User, location: Location) {
        val userDoc = usersRef.document(user.id)
        val fortressDoc = fortressesRef.document()

        val fortress = Fortress(
            id = fortressDoc.id,
            originalOwnerId = user.id,
            currentOwnerId = user.id,
            ownedSince = Date(),
            latitude = location.latitude,
            longitude = location.longitude,
            geoHash = GeoFireUtils.getGeoHashForLocation(GeoLocation(location.latitude, location.longitude)),
        )

        db.runTransaction { transaction ->
            val userSnap = transaction.get(userDoc)
            val newFortressCount = userSnap.getLong("fortressCount")!! + 1

            transaction.update(userDoc, "fortressCount", newFortressCount)
            transaction.set(fortressDoc, fortress)

            null
        }.await()
    }

    override suspend fun getFortressesInRadius(location: Location, radius: Double): List<Fortress> {
        val userGeoLocation = GeoLocation(location.latitude, location.longitude)

        val bounds = GeoFireUtils.getGeoHashQueryBounds(userGeoLocation, radius)

        val tasks = bounds.map { bound ->
            fortressesRef
                .orderBy("geoHash")
                .startAt(bound.startHash)
                .endAt(bound.endHash)
                .get()
        }

        val fortresses = Tasks.whenAllComplete(tasks).await()
            .flatMap { task -> (task.result as QuerySnapshot).documents }
            .filter { doc ->
                val latitude = doc.getDouble("latitude")!!
                val longitude = doc.getDouble("longitude")!!
                val distance = GeoFireUtils.getDistanceBetween(
                    GeoLocation(latitude, longitude),
                    userGeoLocation
                )

                distance <= radius
            }
            .map { doc -> doc.toObject(Fortress::class.java)!! }

        return fortresses
    }
}