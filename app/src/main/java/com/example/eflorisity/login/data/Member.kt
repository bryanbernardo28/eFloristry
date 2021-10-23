package com.example.eflorisity.login.data

import android.media.Image
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.File

data class MemberLoginDetails(val email:String?,val password:String?,val device_name:String?)
data class MemberResponse(val success:Boolean?,val token:String?,val member:MemberDetails?,val errors:String?)
data class RegisterMemberResponse(val success:Boolean?,val member:MemberDetails?,val errors:JsonObject)
data class MemberDetails(
                        val id:String?,
                        val first_name:String?,
                        val middle_name:String?,
                        val last_name:String?,
                        val email:String?,
                        val contact_number:String?,
                        val birthday:String?,
                        val address:String?,
                        val state:String?,
                        val city:String?,
                        val postal_code:String?,
                        val type:String?,
                        val photo_url:String?,
                        val photo:Image?,
                        val password:String?,
                        val password_confirmation:String,
                         )


data class Email(val email:String)
data class EmailResponse(val success: Boolean?,val is_verified:Boolean?,val errors: JsonObject)