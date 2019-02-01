using IdentityServer4.Models;
using System.Collections.Generic;

namespace AuthorizationServer
{
    public static class Config
    {
        public static IEnumerable<IdentityResource> GetIdentityResources()
        {
            return new IdentityResource[]
            {
                new IdentityResources.OpenId(),
                new IdentityResources.Profile(),
            };
        }

        public static IEnumerable<ApiResource> GetApis()
        {
            return new ApiResource[]
            {
                new ApiResource("api1", "API #1")
                {
                    Scopes =
                    {
                        new Scope("api1.read"),
                        new Scope("api1.write")
                    }
                }
            };
        }

        public static IEnumerable<Client> GetClients()
        {
            return new Client[]
            {
                new Client
                {
                    ClientId = "ktor_app",
                    AllowedGrantTypes = GrantTypes.Code,
                    AllowedScopes = {"api1.read", "api1.write"},
                    ClientSecrets = {new Secret("super_secret".Sha256())},
                    RedirectUris = {"http://localhost:8080/oauth"}
                }
            };
        }
    }
}