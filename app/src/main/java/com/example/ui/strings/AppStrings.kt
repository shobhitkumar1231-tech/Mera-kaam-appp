package com.example.ui.strings

enum class Language {
    HINDI,
    ENGLISH
}

object AppStrings {
    fun appName(lang: Language): String = when (lang) {
        Language.HINDI -> "मेरा काम"
        Language.ENGLISH -> "Mera Kaam"
    }

    fun tagline(lang: Language): String = when (lang) {
        Language.HINDI -> "दैनिक मजदूरों और काम देने वालों का भरोसेमंद साथी"
        Language.ENGLISH -> "India's trusted daily-wage worker & employer marketplace"
    }

    fun findWork(lang: Language): String = when (lang) {
        Language.HINDI -> "काम खोजें"
        Language.ENGLISH -> "Find Work"
    }

    fun findWorkers(lang: Language): String = when (lang) {
        Language.HINDI -> "मजदूर खोजें"
        Language.ENGLISH -> "Find Workers"
    }

    fun postJob(lang: Language): String = when (lang) {
        Language.HINDI -> "काम पोस्ट करें"
        Language.ENGLISH -> "Post a Job"
    }

    fun registerWorker(lang: Language): String = when (lang) {
        Language.HINDI -> "मजदूर रजिस्ट्रेशन"
        Language.ENGLISH -> "Worker Register"
    }

    fun registerEmployer(lang: Language): String = when (lang) {
        Language.HINDI -> "नियोक्ता रजिस्ट्रेशन"
        Language.ENGLISH -> "Employer Register"
    }

    fun myProfile(lang: Language): String = when (lang) {
        Language.HINDI -> "मेरा प्रोफाइल"
        Language.ENGLISH -> "My Profile"
    }

    fun adminPanel(lang: Language): String = when (lang) {
        Language.HINDI -> "एडमिन पैनल"
        Language.ENGLISH -> "Admin Panel"
    }

    fun home(lang: Language): String = when (lang) {
        Language.HINDI -> "होम"
        Language.ENGLISH -> "Home"
    }

    fun scamWarning(lang: Language): String = when (lang) {
        Language.HINDI -> "सावधानी: काम के बदले कभी भी किसी को पहले पैसे (एडवांस) न दें! मेरा काम मजदूरों के लिए 100% मुफ्त है।"
        Language.ENGLISH -> "Warning: Never pay advance fees for any job! Mera Kaam is 100% free for workers."
    }

    fun zeroFeeGuarantee(lang: Language): String = when (lang) {
        Language.HINDI -> "100% मुफ्त सेवा • कोई दलाली नहीं • सीधा संपर्क"
        Language.ENGLISH -> "100% Free • No Commission • Direct Contact"
    }

    fun recentJobs(lang: Language): String = when (lang) {
        Language.HINDI -> "हाल ही में पोस्ट किए गए काम"
        Language.ENGLISH -> "Recently Posted Jobs"
    }

    fun popularCategories(lang: Language): String = when (lang) {
        Language.HINDI -> "प्रमुख कार्य श्रेणियां"
        Language.ENGLISH -> "Popular Work Categories"
    }

    fun searchPlaceholder(lang: Language): String = when (lang) {
        Language.HINDI -> "काम या जगह खोजें (जैसे: राजमिस्त्री, पटना)..."
        Language.ENGLISH -> "Search jobs or places (e.g. Mason, Patna)..."
    }

    fun callEmployer(lang: Language): String = when (lang) {
        Language.HINDI -> "कॉल करें"
        Language.ENGLISH -> "Call"
    }

    fun viewDetails(lang: Language): String = when (lang) {
        Language.HINDI -> "विवरण देखें"
        Language.ENGLISH -> "View Details"
    }

    fun reportProblem(lang: Language): String = when (lang) {
        Language.HINDI -> "शिकायत या समस्या बताएं"
        Language.ENGLISH -> "Report / Complaint"
    }

    fun perDay(lang: Language): String = when (lang) {
        Language.HINDI -> "₹ / दिन"
        Language.ENGLISH -> "₹ / day"
    }

    fun workersNeeded(lang: Language, count: Int): String = when (lang) {
        Language.HINDI -> "$count मजदूर चाहिए"
        Language.ENGLISH -> "$count workers needed"
    }

    fun statusActive(lang: Language): String = when (lang) {
        Language.HINDI -> "सक्रिय काम"
        Language.ENGLISH -> "Active"
    }

    fun statusClosed(lang: Language): String = when (lang) {
        Language.HINDI -> "काम बंद (भर्ती पूरी)"
        Language.ENGLISH -> "Closed (Filled)"
    }

    fun savedJobs(lang: Language): String = when (lang) {
        Language.HINDI -> "सेव किए गए काम"
        Language.ENGLISH -> "Saved Jobs"
    }

    fun myPostedJobs(lang: Language): String = when (lang) {
        Language.HINDI -> "मेरे पोस्ट किए गए काम"
        Language.ENGLISH -> "My Posted Jobs"
    }

    fun closeJobBtn(lang: Language): String = when (lang) {
        Language.HINDI -> "काम बंद करें"
        Language.ENGLISH -> "Close Job"
    }

    fun deleteJobBtn(lang: Language): String = when (lang) {
        Language.HINDI -> "हटाएं"
        Language.ENGLISH -> "Delete"
    }

    fun allDistricts(lang: Language): String = when (lang) {
        Language.HINDI -> "सभी जिले"
        Language.ENGLISH -> "All Districts"
    }

    fun allCategories(lang: Language): String = when (lang) {
        Language.HINDI -> "सभी श्रेणियां"
        Language.ENGLISH -> "All Categories"
    }
}
