# Audiobookshelf WearOS - Authentication (OIDC)

## Strategy
Since Audiobookshelf supports OIDC (OpenID Connect), the WearOS app should implement a standard OAuth 2.0 / OIDC flow.

### Flow
1. **Initiate Login:** The watch app opens a browser (or a simplified web view if possible on WearOS) to the OIDC provider (e.g., Authentik, Keycloak).
2. **Authorize:** The user logs in on the provider.
3. **Redirect:** The provider redirects back to a custom URI scheme (e.g., `audiobookshelf-wearos://callback`) with an authorization code.
4. **Token Exchange:** The watch app exchanges the code for ID and Access tokens.
5. **Session Initiation:** The watch app sends the ID token to the Audiobookshelf server to establish a session.

### Implementation Details
- **Library:** [AppAuth-Android](https://github.com/openid/AppAuth-Android) is the recommended library for OIDC on Android.
- **WearOS Constraints:** WearOS 4+ supports standard authentication flows, but the limited screen size makes browser-based login tricky. Alternative: "Login on Phone" flow where the watch triggers a login on the paired phone.

### Audiobookshelf OIDC Endpoints
- Configuration: `Settings > Authentication > OpenID Connect Authentication`
- SSO Redirect: `https://abs.yoursite.com/login/?autoLaunch=1` (standard web flow)
- For API-based OIDC, we may need to use the `/auth/openid/callback` endpoint or similar if it exists for mobile clients.

### Next Steps
- [ ] Initialize Android WearOS project.
- [ ] Add AppAuth dependency.
- [ ] Implement "Login on Phone" or "Direct Login" toggle.
