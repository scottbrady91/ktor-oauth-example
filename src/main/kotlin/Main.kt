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

val clientSettings = OAuthServerSettings.OAuth2ServerSettings(
    name = "IdentityServer4",
    authorizeUrl = "http://localhost:5000/connect/authorize", // OAuth authorization endpoint
    accessTokenUrl = "http://localhost:5000/connect/token", // OAuth token endpoint
    clientId = "ktor_app",
    clientSecret = "super_secret",
    accessTokenRequiresBasicAuth = false, // basic auth implementation is not "OAuth style" so falling back to post body
    requestMethod = HttpMethod.Post, // must POST to token endpoint
    defaultScopes = listOf("api1.read", "api1.write") // what scopes to request
)

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {

        install(Authentication) {
            oauth("IdentityServer4") {
                client = HttpClient(Apache)
                providerLookup = { clientSettings }
                urlProvider = { "http://localhost:8080/oauth" }
            }
        }

        routing {
            get("/") {
                call.respondText("""Click <a href="/oauth">here</a> to get tokens""", ContentType.Text.Html)
            }
            authenticate("IdentityServer4") {
                get("/oauth") {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

                    call.respondText("Access Token = ${principal?.accessToken}")
                }
            }
        }
    }.start(wait = true)
}

