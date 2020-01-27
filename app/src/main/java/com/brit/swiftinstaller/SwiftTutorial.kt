/*
 *
 *  * Copyright (C) 2018 Griffin Millender
 *  * Copyright (C) 2018 Per Lycke
 *  * Copyright (C) 2018 Davide Lilli & Nishith Khanna
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.brit.swiftinstaller

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import com.brit.swiftinstaller.library.ui.activities.TutorialActivity
import com.google.android.vending.licensing.AESObfuscator
import com.google.android.vending.licensing.LicenseChecker
import com.google.android.vending.licensing.LicenseCheckerCallback
import com.google.android.vending.licensing.ServerManagedPolicy

class SwiftTutorial : TutorialActivity() {

    private var mChecker: LicenseChecker? = null

    companion object {
        private const val BASE64_PUBLIC_KEY = "Enter Key here"
        private val SALT = byteArrayOf(-46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32, -64, 89)
    }

    private fun piracyCheck() {

        val deviceId = Secure.getString(contentResolver, Secure.ANDROID_ID)

        mChecker = LicenseChecker(
                this, ServerManagedPolicy(this,
                AESObfuscator(SALT, packageName, deviceId)),
                BASE64_PUBLIC_KEY)

        mChecker?.checkAccess(object: LicenseCheckerCallback {
            override fun allow(policyReason: Int) {
                if (isFinishing) {
                    return
                }
                Log.i("Swift Installer", "SwiftCheck success")
            }

            override fun dontAllow(policyReason: Int) {
                if (isFinishing) {
                    return
                }
                Log.i("Swift Installer", "SwiftCheck failed")
                val intent = Intent(applicationContext, PiracyCheckDialogActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }

            override fun applicationError(errorCode: Int) {
                if (isFinishing) {
                    return
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!BuildConfig.DEBUG) {
            piracyCheck()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mChecker?.onDestroy()
    }
}
