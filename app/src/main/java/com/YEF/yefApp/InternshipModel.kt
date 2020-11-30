package com.YEF.yefApp

data class InternshipModel(
        val internshipId:String = "",
        val title: String = "",
        val type: String = "",
        val responsibilities: ArrayList<String> = arrayListOf<String>(),
        val opportunities: String = "",
        val skills: String = "",
        val conditions: ArrayList<String> = arrayListOf<String>(),
        val women: Boolean = true,
        val duration: String = "",
        val perks: String = "")

//    companion object {
//        fun initInternshipEntryList(resources: Resources): List<InternshipModel> {
//            val inputStream = resources.openRawResource(R.raw.internship)
//            val jsonProductsString = inputStream.bufferedReader().use(BufferedReader::readText)
//            val gson = Gson()
//            val internshipListType = object : TypeToken<ArrayList<InternshipModel>>() {}.type
//            return gson.fromJson<ArrayList<InternshipModel>>(jsonProductsString, internshipListType)
//        }
//    }
//
//    class Responsibilities(private val r: String) {
//        override fun toString(): String {
//            return r
//        }
//    }
//
//    class Conditions(private val c: String) {
//        override fun toString(): String {
//            return c
//        }
//    }
