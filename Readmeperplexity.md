Got it — you want a Java API that connects to ICICI Bank’s services.
Before we start, there’s an important distinction: ICICI doesn’t expose its core banking API publicly for anyone to just connect to — it’s only available to registered partners through their ICICI API Banking platform.

That means:

You need to sign up on their developer portal.

Get client_id, client_secret, API key, and endpoint details.

Use HTTPS with OAuth 2.0 or token-based authentication (depending on their API).

I’ll show you a generic Java API client structure that you can adapt once you have actual ICICI endpoint URLs and credentials.



Key Notes:
Replace https://apibankingonesandbox.icicibank.com with ICICI’s sandbox or production endpoint from their portal.

You’ll need to parse the JSON response (use Gson or Jackson instead of printing raw).

Each ICICI API (fund transfer, account statement, etc.) will have its own endpoint & request body format.

This code uses HttpURLConnection for simplicity — in production, you may prefer Apache HttpClient or OkHttp.

If you want, I can also give you a version with Spring Boot + RestTemplate so you can integrate it more cleanly into a microservice. That’s the preferred approach for banking APIs.

Do you want me to prepare that Spring Boot version?