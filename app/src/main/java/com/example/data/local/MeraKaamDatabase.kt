package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ComplaintEntity
import com.example.data.model.EmployerEntity
import com.example.data.model.JobEntity
import com.example.data.model.SavedJobEntity
import com.example.data.model.WorkerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        JobEntity::class,
        WorkerEntity::class,
        EmployerEntity::class,
        SavedJobEntity::class,
        ComplaintEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MeraKaamDatabase : RoomDatabase() {
    abstract fun dao(): MeraKaamDao

    companion object {
        @Volatile
        private var INSTANCE: MeraKaamDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MeraKaamDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MeraKaamDatabase::class.java,
                    "mera_kaam_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.dao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: MeraKaamDao) {
            val now = System.currentTimeMillis()

            // Initial Employers
            val sampleEmployers = listOf(
                EmployerEntity(
                    id = "emp_1",
                    name = "रमेश शर्मा (ठेकेदार)",
                    phone = "9876543210",
                    district = "वाराणसी",
                    locality = "शिवपुर, जीटी रोड",
                    employerType = "ठेकेदार",
                    businessDetails = "शर्मा कंस्ट्रक्शन - भवन व सड़क निर्माण",
                    isVerified = true,
                    createdAt = now - 86400000L * 5
                ),
                EmployerEntity(
                    id = "emp_2",
                    name = "बलवंत सिंह (किसान)",
                    phone = "9823456781",
                    district = "पटना",
                    locality = "बिहटा, देहात क्षेत्र",
                    employerType = "किसान",
                    businessDetails = "कृषि फार्म - गेहूं व धान कटाई",
                    isVerified = true,
                    createdAt = now - 86400000L * 4
                ),
                EmployerEntity(
                    id = "emp_3",
                    name = "अशोक कुमार (दूकानदार)",
                    phone = "9711223344",
                    district = "जयपुर",
                    locality = "मानसरोवर, मेन मार्केट",
                    employerType = "दूकानदार",
                    businessDetails = "कुमार ट्रेडर्स - अनाज व सीमेंट गोदाम",
                    isVerified = true,
                    createdAt = now - 86400000L * 3
                ),
                EmployerEntity(
                    id = "emp_4",
                    name = "श्रीमती सुनीता देवी (मकान मालिक)",
                    phone = "9988776655",
                    district = "लखनऊ",
                    locality = "गोमती नगर विस्तार",
                    employerType = "घरेलू / मकान मालिक",
                    businessDetails = "मकान रंगाई व सफाई कार्य",
                    isVerified = true,
                    createdAt = now - 86400000L * 2
                )
            )
            dao.insertEmployers(sampleEmployers)

            // Initial Jobs
            val sampleJobs = listOf(
                JobEntity(
                    id = "job_1",
                    title = "भवन निर्माण हेतु 5 राजमिस्त्री और मजदूर",
                    category = "निर्माण (Construction)",
                    description = "2 मंजिला मकान की छत ढलाई और ईंट चिनाई का काम। रोजाना सुबह 8:30 से शाम 5:30 तक। दोपहर 1 घंटे का भोजन विश्राम और चाय उपलब्ध। काम खत्म होते ही हर शाम भुगतान।",
                    district = "वाराणसी",
                    locality = "शिवपुर, कबीर मठ के पास",
                    dailyWage = 650,
                    workersNeeded = 5,
                    startDate = "कल से शुरू",
                    duration = "7 दिन",
                    workingHours = "सुबह 8:30 - शाम 5:30",
                    employerName = "रमेश शर्मा",
                    employerPhone = "9876543210",
                    employerId = "emp_1",
                    isClosed = false,
                    isApproved = true,
                    createdAt = now - 3600000L * 4
                ),
                JobEntity(
                    id = "job_2",
                    title = "खेत में धान कटाई और बंडल बांधने हेतु मजदूर",
                    category = "कृषि (Farming)",
                    description = "10 बीघा खेत में धान कटाई और थ्रेसिंग का काम। मेहनती मजदूरों की आवश्यकता है। समय पर दोपहर का नाश्ता और पानी की व्यवस्था। तुरंत संपर्क करें।",
                    district = "पटना",
                    locality = "बिहटा, सिकंदरपुर मोड़",
                    dailyWage = 550,
                    workersNeeded = 8,
                    startDate = "तुरंत आवश्यकता",
                    duration = "4 दिन",
                    workingHours = "सुबह 7:00 - दोपहर 3:00",
                    employerName = "बलवंत सिंह",
                    employerPhone = "9823456781",
                    employerId = "emp_2",
                    isClosed = false,
                    isApproved = true,
                    createdAt = now - 3600000L * 7
                ),
                JobEntity(
                    id = "job_3",
                    title = "गोदाम में सीमेंट और खाद बोरियों की लोडिंग-अनलोडिंग",
                    category = "लोडिंग (Loading & Transport)",
                    description = "रेलवे रैक से गोदाम तक माल ट्रक से उतारने और लगाने का कार्य। प्रति मजदूर प्रतिदिन निश्चित ₹700 नकद दिहाड़ी। स्वस्थ और मजबूत पल्लेदार चाहिए।",
                    district = "जयपुर",
                    locality = "मानसरोवर इंडस्ट्रियल एरिया",
                    dailyWage = 700,
                    workersNeeded = 4,
                    startDate = "आज से",
                    duration = "10 दिन",
                    workingHours = "सुबह 9:00 - शाम 6:00",
                    employerName = "अशोक कुमार",
                    employerPhone = "9711223344",
                    employerId = "emp_3",
                    isClosed = false,
                    isApproved = true,
                    createdAt = now - 3600000L * 12
                ),
                JobEntity(
                    id = "job_4",
                    title = "कोठी में पुट्टी, प्राइमर व पेंटिंग का काम",
                    category = "पेंटिंग (Painting)",
                    description = "नवनिर्मित मकान में अंदर और बाहर डिस्टेंपर और रॉयल पेंटिंग कार्य। अनुभवी पेंटर व हेल्पर चाहिए। रोलर और ब्रश चलाने में दक्ष।",
                    district = "लखनऊ",
                    locality = "गोमती नगर, सेक्टर 4",
                    dailyWage = 800,
                    workersNeeded = 3,
                    startDate = "सोमवार से",
                    duration = "12 दिन",
                    workingHours = "सुबह 9:30 - शाम 6:00",
                    employerName = "श्रीमती सुनीता देवी",
                    employerPhone = "9988776655",
                    employerId = "emp_4",
                    isClosed = false,
                    isApproved = true,
                    createdAt = now - 3600000L * 24
                ),
                JobEntity(
                    id = "job_5",
                    title = "प्लंबिंग पाइपलाइन व सेनेटरी फिटिंग कारीगर",
                    category = "प्लंबिंग (Plumbing)",
                    description = "हॉस्टल बिल्डिंग में सीपीवीसी पाइप फिटिंग, नल और वेस्ट लाइन बिछाने का काम। औजार स्वयं के होने चाहिए। कुशल कारीगर हेतु अच्छा वेतन।",
                    district = "इंदौर",
                    locality = "भंवरकुआं मेन रोड",
                    dailyWage = 850,
                    workersNeeded = 2,
                    startDate = "परसों से",
                    duration = "5 दिन",
                    workingHours = "सुबह 10:00 - शाम 6:30",
                    employerName = "दिनेश पाटीदार",
                    employerPhone = "9412309876",
                    employerId = "emp_5",
                    isClosed = false,
                    isApproved = true,
                    createdAt = now - 3600000L * 30
                ),
                JobEntity(
                    id = "job_6",
                    title = "फर्नीचर दुकान में बढ़ई (कारपेंटर) व हेल्पर",
                    category = "कारपेंटर (Carpentry)",
                    description = "प्लाईवुड कटिंग, अलमारी और बेड असेंबलिंग का काम। फिनिशिंग का ज्ञान आवश्यक। चाय और दोपहर का भोजन दिया जाएगा।",
                    district = "दिल्ली NCR",
                    locality = "नरेला इंडस्ट्रियल कॉम्प्लेक्स",
                    dailyWage = 900,
                    workersNeeded = 2,
                    startDate = "तुरंत",
                    duration = "15 दिन",
                    workingHours = "सुबह 9:00 - शाम 7:00",
                    employerName = "गुरप्रीत सिंह",
                    employerPhone = "9811002233",
                    employerId = "emp_6",
                    isClosed = false,
                    isApproved = true,
                    createdAt = now - 3600000L * 48
                )
            )
            dao.insertJobs(sampleJobs)

            // Initial Workers
            val sampleWorkers = listOf(
                WorkerEntity(
                    id = "wrk_1",
                    name = "राजू कुमार पासवान",
                    phone = "9835123456",
                    district = "पटना",
                    locality = "दानापुर, आनंद बाजार",
                    skills = "राजमिस्त्री, निर्माण मजदूर",
                    experienceYears = 6,
                    expectedDailyWage = 650,
                    isAvailable = true,
                    preferredLocation = "पटना, बिहटा, दानापुर",
                    allowPhoneShare = true,
                    createdAt = now - 86400000L * 10
                ),
                WorkerEntity(
                    id = "wrk_2",
                    name = "सुरेश चंद्र बिंद",
                    phone = "9450987654",
                    district = "वाराणसी",
                    locality = "रामनगर, पड़ाव",
                    skills = "पेंटर, पुट्टी कारीगर",
                    experienceYears = 4,
                    expectedDailyWage = 700,
                    isAvailable = true,
                    preferredLocation = "वाराणसी शहर और 15 किमी दायरा",
                    allowPhoneShare = true,
                    createdAt = now - 86400000L * 8
                ),
                WorkerEntity(
                    id = "wrk_3",
                    name = "मुकेश यादव",
                    phone = "9784561230",
                    district = "जयपुर",
                    locality = "सांगानेर, सीतापुरा",
                    skills = "इलेक्ट्रीशियन, वायरिंग हेल्पर",
                    experienceYears = 5,
                    expectedDailyWage = 800,
                    isAvailable = true,
                    preferredLocation = "जयपुर और आसपास",
                    allowPhoneShare = true,
                    createdAt = now - 86400000L * 6
                ),
                WorkerEntity(
                    id = "wrk_4",
                    name = "मनोज कुमार साहू",
                    phone = "9123456780",
                    district = "लखनऊ",
                    locality = "चिनहट, फैजाबाद रोड",
                    skills = "कृषि मजदूर, कटाई-बुवाई, लोडिंग",
                    experienceYears = 7,
                    expectedDailyWage = 500,
                    isAvailable = true,
                    preferredLocation = "लखनऊ ग्रामीण व शहरी क्षेत्र",
                    allowPhoneShare = true,
                    createdAt = now - 86400000L * 5
                ),
                WorkerEntity(
                    id = "wrk_5",
                    name = "संतोष विश्वकर्मा",
                    phone = "9630124578",
                    district = "इंदौर",
                    locality = "राऊ, पीथमपुर बाईपास",
                    skills = "कारपेंटर, वेल्डिंग",
                    experienceYears = 8,
                    expectedDailyWage = 850,
                    isAvailable = false,
                    preferredLocation = "इंदौर और पीथमपुर",
                    allowPhoneShare = true,
                    createdAt = now - 86400000L * 3
                )
            )
            dao.insertWorkers(sampleWorkers)
        }
    }
}
