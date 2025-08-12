import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// This is a conceptual example. Do not use this for production.
// You must obtain proper credentials and follow the official ICICI Bank
// API documentation for a real-world integration.

public class IciciBankingApiClient {

    // You would get these from the ICICI Bank Developer Portal
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_SECRET = "YOUR_API_SECRET";
    private static final String BASE_URL = "https://developer.icicibank.com/api/v1"; // Example URL

    private final OkHttpClient httpClient = new OkHttpClient();

    /**
     * Generates a signed payload for the API request.
     * The signing mechanism is specific to the bank's API. This is a common
     * but hypothetical example using HMAC-SHA256.
     * @param requestPayload The JSON payload of the request.
     * @return A Base64-encoded signature string.
     */
    private String generateSignature(String requestPayload) {
        try {
            // A real signature might include more parameters like timestamp or nonce
            String dataToSign = requestPayload + API_SECRET;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(API_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacSha256 = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacSha256);
        } catch (Exception e) {
            System.err.println("Error generating signature: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetches the account balance for a given account number.
     * This is a GET request with a signed header.
     * @param accountNumber The account number to query.
     * @return The API response as a string, or an error message.
     */
    public String getAccountBalance(String accountNumber) {
        try {
            String path = "/accounts/balance";
            String fullUrl = BASE_URL + path + "?accountNumber=" + URLEncoder.encode(accountNumber, StandardCharsets.UTF_8.toString());

            // A typical API will require a signed header, even for GET requests.
            // The signature for a GET request might be based on the URL and other headers.
            // For simplicity, we will omit the signature here as it's highly dependent on the API spec.
            // A real implementation would require careful reading of the official documentation.

            Request request = new Request.Builder()
                .url(fullUrl)
                .header("api-key", API_KEY)
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "Error: " + response.code() + " " + response.message();
                }
                return response.body().string();
            }
        } catch (IOException e) {
            return "IOException: " + e.getMessage();
        }
    }

    /**
     * Initiates a fund transfer. This is a POST request with a JSON body and a signature.
     * @param sourceAccount The source account number.
     * @param destinationAccount The destination account number.
     * @param amount The amount to transfer.
     * @return The API response as a string, or an error message.
     */
    public String transferFunds(String sourceAccount, String destinationAccount, double amount) {
        try {
            String path = "/payments/transfer";
            String fullUrl = BASE_URL + path;

            // Build the JSON payload for the request
            String requestPayload = String.format("{\"sourceAccount\":\"%s\",\"destinationAccount\":\"%s\",\"amount\":%.2f,\"uniqueId\":\"%s\"}",
                sourceAccount, destinationAccount, amount, UUID.randomUUID().toString());

            // Generate a signature based on the payload
            String signature = generateSignature(requestPayload);
            if (signature == null) {
                return "Error: Could not generate signature.";
            }
            
            RequestBody body = RequestBody.create(requestPayload, MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                .url(fullUrl)
                .post(body)
                .header("api-key", API_KEY)
                .header("signature", signature) // Add the signature to the header
                .header("Content-Type", "application/json")
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "Error: " + response.code() + " " + response.message() + "\n" + response.body().string();
                }
                return response.body().string();
            }

        } catch (IOException e) {
            return "IOException: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        IciciBankingApiClient client = new IciciBankingApiClient();

        // Example usage:
        System.out.println("Fetching account balance...");
        String balanceResponse = client.getAccountBalance("1234567890");
        System.out.println("Response: " + balanceResponse);

        System.out.println("\nTransferring funds...");
        String transferResponse = client.transferFunds("1234567890", "0987654321", 1000.50);
        System.out.println("Response: " + transferResponse);
    }
}