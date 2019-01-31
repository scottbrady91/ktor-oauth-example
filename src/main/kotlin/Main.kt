package com.scottbrady91.ktor.oauth

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.Netty

// TODO: what is the response mode?
val clientSettings = OAuthServerSettings.OAuth2ServerSettings(
    name = "IdentityServer4",
    authorizeUrl = "http://localhost:5000/connect/authorize", // OAuth authorization endpoint
    accessTokenUrl = "http://localhost:5000/connect/token", // OAuth token endpoint
    clientId = "ktor_app",
    clientSecret = "super_secret",
    accessTokenRequiresBasicAuth = false, // basic auth implementation is not "OAuth style"
    requestMethod = HttpMethod.Post, // must POST to token endpoint
    defaultScopes = listOf("api1.read", "api1.write")
)

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {

        install(Authentication) {
            oauth("IdentityServer4") {
                client = HttpCl ient(Apache)
                providerLookup = { clientSettings }
                urlProvider = { "http://localhost:8080/callback" }
            }
        }

        routing {
            get("/") {
                call.respondText("Hello from Ktor", ContentType.Text.Html)
            }
            authenticate("IdentityServer4") {
                get("/callback") {
                    // TODO: may require "handle"???
                    val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                    call.respondText("Access Token = ${principal?.accessToken}")
                }
            }

        }
    }.start(wait = true)
}

