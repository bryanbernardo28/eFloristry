package com.example.eflorisity.login.data

import android.media.Image
import com.google.gson.JsonObject
import com.paypal.checkout.createorder.*
//import com.paypal.checkout.createorder.*
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
data class Code(val code:String,val email:String)
data class CodeResponse(val success: Boolean?,val is_verified:Boolean?,val code_success:Boolean,val errors: JsonObject)
//
//data class PayPalBody(val grant_type:String)
//data class PayPalAccessTokenResponse(
//    val access_token:String,
//)
//
//data class PayPalOrderBody(
//    val intent:String,
//    val purchase_units:ArrayList<PayPalPurchaseUnits>
//)
//
//data class PayPalPurchaseUnits(
//    val amount:PayPalAmounts
//)
//
//data class PayPalAmounts(
//    val currency_code:String,
//    val value:String,
//)
//
//
//data class PayPalOrderResponse(
//    val id:String,
//    val status:String,
//    val links:ArrayList<CreatedOrderLinks>
//)
//
//data class CreatedOrderLinks(
//    val href:String,
//    val rel:String,
//    val method:String
//)

