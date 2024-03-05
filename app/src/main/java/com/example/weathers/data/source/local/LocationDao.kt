package com.example.weathers.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.concurrent.Flow

@Dao
interface LocationDao {
    @Insert
    suspend fun inserts(vararg location: Location)

    @Update
    suspend fun updates(vararg location: Location)

    @Delete
    suspend fun deletes(vararg location: Location)

    @Query("SELECT * FROM location")
    suspend fun getAll(): List<Location>

    @Query("SELECT * FROM location")
    fun getAllStream(): kotlinx.coroutines.flow.Flow<List<Location>>

    @Query("SELECT * FROM location WHERE adress1 LIKE '%' || :adress || '%' OR adress2 LIKE '%' || :adress || '%' OR adress3 LIKE '%' || :adress || '%'")
    suspend fun getLocationsByAdress(adress: String): List<Location>

    @Query("SELECT * FROM location WHERE code IN (:codes)")
    suspend fun getLocationsByCode(codes: List<String>): List<Location>

    @Query("SELECT * FROM location WHERE dx = :x AND dy = :y")
    suspend fun getLocations(x: Int, y: Int): List<Location>

    @Query("SELECT * FROM location WHERE level <= :level")
    suspend fun getLocations(level: Int): List<Location>

    @Query("SELECT * FROM location WHERE dx >= :x - :distance AND dx <= :x + :distance AND dy >= :y - :distance AND dy <= :y + :distance")
    suspend fun getLocations(x: Int, y: Int, distance: Int): List<Location>

    @Query("SELECT * FROM location WHERE code = :code")
    suspend fun getByCode(code: String): Location
}