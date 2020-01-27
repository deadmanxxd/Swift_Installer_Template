package com.brit.swiftinstaller


import com.brit.swiftinstaller.library.SwiftApplication
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SwiftInstaller : SwiftApplication() {

    override fun createCipher(): Cipher? {
        return try {
            val cip = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cip!!.init(Cipher.DECRYPT_MODE, SecretKeySpec(BuildConfig.DECRYPTION_KEY, "AES"),
                    IvParameterSpec(BuildConfig.IV_KEY))
            cip
        } catch (e: Exception) {
            null
        }
    }
    
}