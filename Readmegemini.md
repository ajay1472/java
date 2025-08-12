I can provide you with an example of an API connection for ICICI Bank in Java, but you must first understand a few critical points. ICICI Bank, like all major financial institutions, uses a secure API portal for developers. You cannot simply "connect" to their banking system without proper authorization.

Key Steps for API Access
Sign Up for the Developer Portal: You need to visit the official ICICI Bank API portal at https://developer.icicibank.com/ and create a developer account.

Select and Test APIs: The portal has a wide variety of APIs for different services (payments, accounts, loans, etc.). You must select the specific APIs you need, then use their sandbox environment to test your integration.

Get API Credentials: After successful testing in the sandbox, you will need to sign a Non-Disclosure Agreement (NDA) with the bank to get access to the UAT (User Acceptance Testing) and live production environments. This process will provide you with the necessary API keys and secrets.

Use a Client Library/SDK: While you can make raw HTTP requests, the bank often provides SDKs (Software Development Kits) or client libraries for popular programming languages, including Java. These libraries handle the complexities of authentication (like OAuth 2.0) and request signing, making integration much easier.

Conceptual Java API Connection
Since I cannot provide a live, runnable API for ICICI Bank, I'll provide a conceptual Java class. This example illustrates how you might structure a Java client using a popular library like OkHttp or Apache HttpClient to interact with a REST API, which is the standard for banking APIs. This code assumes you have already obtained your API key and secret from the developer portal.

I've included extensive comments to explain each part of the process.
ICICI Bank API Client (Conceptual Java Code)
12 Aug, 21:16

 Try again without Canvas 

This code shows the general structure of an API client in Java. It includes placeholders for your API key and secret, and demonstrates how to handle GET and POST requests, including the conceptual idea of adding a signature for security.

The next steps would be for you to sign up on the ICICI Bank developer portal, find the specific APIs you need, read their official documentation for the exact request/response formats and authentication method, and then adapt this conceptual code to match their specifications.

Watch the video to know how ICICI Bank's API solutions can benefit your business. #APIBanking #API - YouTube

This video provides an overview of ICICI Bank's API banking portal and its benefits for businesses.