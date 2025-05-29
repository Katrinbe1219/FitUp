package com.example.fitup.hilt

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class StorageViewModel @Inject constructor  (
    private val storage: StorageReference
) : ViewModel() {

        // for splitting_body.kt: big version of body parts
        suspend fun getImages( level: String ) : MutableMap<String, Uri>  = suspendCoroutine{ continuation ->

                val body_part = mutableMapOf<String, Uri>()

                storage.child("body_parts").child(level).listAll().addOnSuccessListener { listResult ->
                    val downloadTasks = listResult.items.map { item ->
                        item.downloadUrl.addOnSuccessListener { uri ->
                            synchronized(body_part){
                                body_part[item.name.split('.')[0]] = uri
                            }
                        }
                    }

                    Tasks.whenAllComplete(downloadTasks).addOnCompleteListener {
                        continuation.resume(body_part)
                    }



                }.addOnFailureListener{ exception ->
                    Log.e("StorageLogs", "problems with getting images for splitting_body - ${exception.message}")

                    continuation.resumeWithException(exception)
                }
        }

        suspend fun  getBackground (  ) : Uri = suspendCoroutine { continuation ->
            storage.child("settings").child("backo.png").downloadUrl
                .addOnSuccessListener { uri ->
                continuation.resume(uri)
                }

                .addOnFailureListener { e ->
                    Log.e("StorageLogs", "problema with getting background ${e.message}")
                    continuation.resumeWithException(e)
                }
        }

        suspend fun getProfileBodyParts( level: String ): MutableMap<String, Uri> = suspendCancellableCoroutine {  continuation ->

        val body_part = mutableMapOf<String, Uri>()

        storage.child("body_parts").child("profile_versions").child(level).listAll().addOnSuccessListener { listResult ->
            val downloadTasks = listResult.items.map {item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    synchronized(body_part){
                        body_part[item.name.split('.')[0]] = uri
                    }
                }
            }

            Tasks.whenAllComplete(downloadTasks).addOnCompleteListener {
                continuation.resume(body_part)
            }

        }.addOnFailureListener { ex ->
            Log.e("StorageLogs", "problems with getting body parts - ${ex.message}")
            continuation.resumeWithException(ex)
        }
    }

        suspend fun getTextFieldBackground(): Uri  = suspendCancellableCoroutine{ continuation ->
            storage.child("settings").child("wide_back.png").downloadUrl
                .addOnSuccessListener { uri ->
                continuation.resume(uri)
                }.addOnFailureListener {  ex ->
                    Log.e("StorageLogs", "problema with getting textField background ${ex.message}")
                    continuation.resumeWithException(ex)
                }
        }

        suspend fun getArmEnergyPictures(level: String, bodyLevel: String) : MutableMap<String, Uri> = suspendCancellableCoroutine { continuation ->
            val uris = mutableMapOf<String, Uri>()
            val refStorage = storage.child("energy").child(level).child(bodyLevel)

            val tasks = listOf(
                refStorage.child("right_arm.png").downloadUrl,
                refStorage.child("left_arm.png").downloadUrl
            )

            val resultCount = AtomicInteger(0)

            tasks.forEachIndexed{index, task ->
                task.addOnCompleteListener{ result ->
                    if (result.isSuccessful){
                        val key = if (index == 0) "right_arm" else "left_arm"
                        uris[key] = result.result
                    }

                    if(resultCount.incrementAndGet() == tasks.size){
                        continuation.resume(uris)
                    }
                }
            }

//
        }

        suspend fun getLegEnergyPictures(level: String, bodyLevel: String) : Uri = suspendCancellableCoroutine { continuation ->

            storage.child("energy").child(level).child(bodyLevel).child("leg.png").downloadUrl
                .addOnSuccessListener { uri ->

                    continuation.resume(uri)
                }
                .addOnFailureListener { ex ->
                    continuation.resumeWithException(ex)
                    Log.e("StorageLogs", "problems with getLegEnergy  - e ${ex.message}")
                }

        }

        suspend fun getHeadEnergyPictures( ) : Uri = suspendCancellableCoroutine { continuation ->

            storage.child("energy").child("head.png").downloadUrl
                .addOnSuccessListener { uri ->
                    continuation.resume(uri)
                }
                .addOnFailureListener { ex ->
                    continuation.resumeWithException(ex)
                    Log.e("StorageLogs", "problems with getHeadEnergy  - e ${ex.message}")
                }

        }


        suspend fun getBodyEnergyPictures(level: String, bodyLevel: String) : Uri = suspendCancellableCoroutine { continuation ->

            storage.child("energy").child(level).child(bodyLevel).child("body.png").downloadUrl
                .addOnSuccessListener { uri ->
                    continuation.resume(uri)
                }
                .addOnFailureListener { ex ->
                    continuation.resumeWithException(ex)
                    Log.e("StorageLogs", "problems with getHeadEnergy  - e ${ex.message}")
                }

        }









}