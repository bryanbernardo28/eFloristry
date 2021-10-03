package com.example.eflorisity.login.data

import org.json.JSONObject

data class MemberLoginDetails(val email:String?,val password:String?,val device_name:String?)
data class MemberResponse(val success:Boolean?,val token:String?,val member:MemberDetails?,val errors:String?)
data class MemberDetails(
                        val id:String,
                        val first_name:String,
                        val middle_name:String?,
                        val last_name:String,
                        val email:String,
                        val contact_number:String,
                        val birthday:String,
                        val photo_url:String,

                         )
