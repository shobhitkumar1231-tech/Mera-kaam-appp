package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ComplaintEntity
import com.example.data.model.EmployerEntity
import com.example.data.model.JobEntity
import com.example.data.model.SavedJobEntity
import com.example.data.model.WorkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeraKaamDao {
    // Jobs
    @Query("SELECT * FROM jobs ORDER BY createdAt DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE isClosed = 0 AND isApproved = 1 ORDER BY createdAt DESC")
    fun getActiveApprovedJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE employerId = :employerId ORDER BY createdAt DESC")
    fun getJobsByEmployer(employerId: String): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :jobId LIMIT 1")
    suspend fun getJobById(jobId: String): JobEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobs: List<JobEntity>)

    @Update
    suspend fun updateJob(job: JobEntity)

    @Query("UPDATE jobs SET isClosed = :isClosed WHERE id = :jobId")
    suspend fun setJobClosedStatus(jobId: String, isClosed: Boolean)

    @Query("UPDATE jobs SET isApproved = :isApproved WHERE id = :jobId")
    suspend fun setJobApprovalStatus(jobId: String, isApproved: Boolean)

    @Query("DELETE FROM jobs WHERE id = :jobId")
    suspend fun deleteJobById(jobId: String)

    // Workers
    @Query("SELECT * FROM workers ORDER BY createdAt DESC")
    fun getAllWorkers(): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE id = :workerId LIMIT 1")
    suspend fun getWorkerById(workerId: String): WorkerEntity?

    @Query("SELECT * FROM workers WHERE phone = :phone LIMIT 1")
    suspend fun getWorkerByPhone(phone: String): WorkerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorker(worker: WorkerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkers(workers: List<WorkerEntity>)

    @Update
    suspend fun updateWorker(worker: WorkerEntity)

    @Delete
    suspend fun deleteWorker(worker: WorkerEntity)

    // Employers
    @Query("SELECT * FROM employers ORDER BY createdAt DESC")
    fun getAllEmployers(): Flow<List<EmployerEntity>>

    @Query("SELECT * FROM employers WHERE id = :employerId LIMIT 1")
    suspend fun getEmployerById(employerId: String): EmployerEntity?

    @Query("SELECT * FROM employers WHERE phone = :phone LIMIT 1")
    suspend fun getEmployerByPhone(phone: String): EmployerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployer(employer: EmployerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployers(employers: List<EmployerEntity>)

    @Update
    suspend fun updateEmployer(employer: EmployerEntity)

    @Query("UPDATE employers SET isSuspended = :isSuspended WHERE id = :employerId")
    suspend fun setEmployerSuspension(employerId: String, isSuspended: Boolean)

    // Saved Jobs
    @Query("SELECT jobId FROM saved_jobs")
    fun getSavedJobIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(savedJob: SavedJobEntity)

    @Query("DELETE FROM saved_jobs WHERE jobId = :jobId")
    suspend fun removeSavedJob(jobId: String)

    // Complaints
    @Query("SELECT * FROM complaints ORDER BY createdAt DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)

    @Query("UPDATE complaints SET status = :status WHERE id = :complaintId")
    suspend fun updateComplaintStatus(complaintId: String, status: String)

    @Query("DELETE FROM complaints WHERE id = :complaintId")
    suspend fun deleteComplaint(complaintId: String)
}
