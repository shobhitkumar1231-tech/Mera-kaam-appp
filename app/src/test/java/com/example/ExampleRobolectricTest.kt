package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.MeraKaamDatabase
import com.example.data.repository.MeraKaamRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: MeraKaamDatabase
    private lateinit var repository: MeraKaamRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MeraKaamDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = MeraKaamRepository(database.dao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context verifies Mera Kaam app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Mera Kaam", appName)
    }

    @Test
    fun `test CUJ post job and retrieve in active jobs`() = runTest {
        val job = repository.postJob(
            title = "3 राजमिस्त्री और 2 मजदूर चाहिए",
            category = "निर्माण (Construction)",
            description = "भवन निर्माण कार्य",
            district = "वाराणसी",
            locality = "शिवपुर",
            dailyWage = 650,
            workersNeeded = 5,
            startDate = "कल से",
            duration = "7 दिन",
            workingHours = "सुबह 9 से शाम 5",
            employerName = "रमेश शर्मा",
            employerPhone = "9876543210",
            employerId = "emp_1"
        )

        assertNotNull(job.id)
        val activeJobs = repository.getActiveApprovedJobs().first()
        assertTrue(activeJobs.any { it.id == job.id })
    }

    @Test
    fun `test CUJ register worker`() = runTest {
        val worker = repository.registerWorker(
            name = "राजू कुमार",
            phone = "9835123456",
            district = "पटना",
            locality = "दानापुर",
            skills = "राजमिस्त्री",
            experienceYears = 5,
            expectedDailyWage = 600,
            isAvailable = true,
            preferredLocation = "पटना",
            allowPhoneShare = true
        )

        assertNotNull(worker.id)
        val allWorkers = repository.getAllWorkers().first()
        assertTrue(allWorkers.any { it.name == "राजू कुमार" })
    }

    @Test
    fun `test CUJ register employer`() = runTest {
        val employer = repository.registerEmployer(
            name = "शर्मा कंस्ट्रक्शन",
            phone = "9876543210",
            district = "वाराणसी",
            locality = "जीटी रोड",
            employerType = "ठेकेदार",
            businessDetails = "सिविल कंस्ट्रक्शन"
        )

        assertNotNull(employer.id)
        val allEmployers = repository.getAllEmployers().first()
        assertTrue(allEmployers.any { it.name == "शर्मा कंस्ट्रक्शन" })
    }

    @Test
    fun `test CUJ close job`() = runTest {
        val job = repository.postJob(
            title = "धान कटाई",
            category = "कृषि (Farming)",
            description = "धान कटाई",
            district = "पटना",
            locality = "बिहटा",
            dailyWage = 500,
            workersNeeded = 4,
            startDate = "आज से",
            duration = "3 दिन",
            workingHours = "सुबह 8 से शाम 4",
            employerName = "बलवंत सिंह",
            employerPhone = "9823456781",
            employerId = "emp_2"
        )

        repository.setJobClosedStatus(job.id, true)
        val updated = repository.getJobById(job.id)
        assertNotNull(updated)
        assertTrue(updated!!.isClosed)
    }

    @Test
    fun `test CUJ submit complaint`() = runTest {
        val complaint = repository.submitComplaint(
            targetType = "JOB",
            targetId = "job_1",
            targetTitle = "फर्जी काम",
            reason = "अग्रिम पैसे की मांग",
            details = "मालिक ने फोन पर 500 रुपये रजिस्ट्रेशन मांगा",
            reporterPhone = "9123456780"
        )

        assertNotNull(complaint.id)
        val complaints = repository.getAllComplaints().first()
        assertTrue(complaints.any { it.id == complaint.id })
    }

    @Test
    fun `test language toggle switches text strings between Hindi and English`() {
        val hindiFindWork = com.example.ui.strings.AppStrings.findWork(com.example.ui.strings.Language.HINDI)
        val englishFindWork = com.example.ui.strings.AppStrings.findWork(com.example.ui.strings.Language.ENGLISH)
        assertEquals("काम खोजें", hindiFindWork)
        assertEquals("Find Work", englishFindWork)

        val hindiAppName = com.example.ui.strings.AppStrings.appName(com.example.ui.strings.Language.HINDI)
        val englishAppName = com.example.ui.strings.AppStrings.appName(com.example.ui.strings.Language.ENGLISH)
        assertEquals("मेरा काम", hindiAppName)
        assertEquals("Mera Kaam", englishAppName)
    }
}
