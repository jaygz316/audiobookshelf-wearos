package com.jaybit.audiobookshelf

import android.net.Uri
import net.openid.appauth.AuthorizationServiceConfiguration

object AuthConfig {
    const val CLIENT_ID = "audiobookshelf-wearos"
    const val REDIRECT_URI = "audiobookshelf-wearos://callback"
    
    val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse("https://auth.jaybit.com/application/o/authorize/"),
        Uri.parse("https://auth.jaybit.com/application/o/token/")
    )
}
