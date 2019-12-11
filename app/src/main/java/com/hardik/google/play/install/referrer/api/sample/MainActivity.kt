package com.hardik.google.play.install.referrer.api.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails

class MainActivity : AppCompatActivity() {

  private lateinit var referrerClient: InstallReferrerClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    referrerClient = InstallReferrerClient.newBuilder(this)
        .build()
    referrerClient.startConnection(object : InstallReferrerStateListener {

      override fun onInstallReferrerSetupFinished(responseCode: Int) {
        when (responseCode) {
          InstallReferrerResponse.OK -> {
            // Connection established.
            getReferrerData()
          }
          InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
            // API not available on the current Play Store app.
          }
          InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
            // Connection couldn't be established.
          }
        }
      }

      override fun onInstallReferrerServiceDisconnected() {
        // Try to restart the connection on the next request to
        // Google Play by calling the startConnection() method.
      }
    })
  }

  private fun getReferrerData() {

    val response: ReferrerDetails = referrerClient.installReferrer
    val referrerUrl: String = response.installReferrer
    val referrerClickTime: Long = response.referrerClickTimestampSeconds
    val appInstallTime: Long = response.installBeginTimestampSeconds
    val instantExperienceLaunched: Boolean = response.googlePlayInstantParam

    println(
        "Hardik --> getReferrerData $referrerUrl -- $referrerClickTime -- $appInstallTime" +
            " -- $instantExperienceLaunched"
    )

    referrerClient.endConnection()
  }
}
