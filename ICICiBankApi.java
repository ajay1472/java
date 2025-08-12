// ICICI Banking API - Java Implementation
// Note: This is a sample implementation for educational purposes

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

// Main ICICI Banking API Client
public class ICICIBankingAPI {
    private static final String BASE_URL = "https://api.icicibank.com/v1";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String authToken;
    private String apiKey;
    
    public ICICIBankingAPI(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    
    // Authentication
    public AuthResponse authenticate(String username, String password) throws Exception {
        AuthRequest authRequest = new AuthRequest(username, password, apiKey);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/auth/login"))
            .header("Content-Type", "application/json")
            .header("X-API-Key", apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(
                objectMapper.writeValueAsString(authRequest)))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            AuthResponse authResponse = objectMapper.readValue(
                response.body(), AuthResponse.class);
            this.authToken = authResponse.getAccessToken();
            return authResponse;
        } else {
            throw new RuntimeException("Authentication failed: " + response.body());
        }
    }
    
    // Account Operations
    public AccountDetailsResponse getAccountDetails(String accountNumber) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/accounts/" + accountNumber))
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .GET()
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), AccountDetailsResponse.class);
        } else {
            throw new RuntimeException("Failed to get account details: " + response.body());
        }
    }
    
    public List<Account> getAllAccounts() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/accounts"))
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .GET()
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            AccountListResponse accountListResponse = objectMapper.readValue(
                response.body(), AccountListResponse.class);
            return accountListResponse.getAccounts();
        } else {
            throw new RuntimeException("Failed to get accounts: " + response.body());
        }
    }
    
    // Balance Inquiry
    public BalanceResponse getBalance(String accountNumber) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/accounts/" + accountNumber + "/balance"))
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .GET()
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), BalanceResponse.class);
        } else {
            throw new RuntimeException("Failed to get balance: " + response.body());
        }
    }
    
    // Transaction Operations
    public List<Transaction> getTransactionHistory(String accountNumber, 
            int limit, String fromDate, String toDate) throws Exception {
        
        String url = String.format("%s/accounts/%s/transactions?limit=%d&from=%s&to=%s",
            BASE_URL, accountNumber, limit, fromDate, toDate);
            
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .GET()
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            TransactionHistoryResponse transactionResponse = objectMapper.readValue(
                response.body(), TransactionHistoryResponse.class);
            return transactionResponse.getTransactions();
        } else {
            throw new RuntimeException("Failed to get transaction history: " + response.body());
        }
    }
    
    // Fund Transfer
    public TransferResponse transferFunds(TransferRequest transferRequest) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/transfers"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(
                objectMapper.writeValueAsString(transferRequest)))
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), TransferResponse.class);
        } else {
            throw new RuntimeException("Fund transfer failed: " + response.body());
        }
    }
    
    // Bill Payment
    public PaymentResponse payBill(BillPaymentRequest billPaymentRequest) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/payments/bills"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(
                objectMapper.writeValueAsString(billPaymentRequest)))
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), PaymentResponse.class);
        } else {
            throw new RuntimeException("Bill payment failed: " + response.body());
        }
    }
    
    // Beneficiary Management
    public BeneficiaryResponse addBeneficiary(BeneficiaryRequest beneficiaryRequest) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/beneficiaries"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(
                objectMapper.writeValueAsString(beneficiaryRequest)))
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), BeneficiaryResponse.class);
        } else {
            throw new RuntimeException("Failed to add beneficiary: " + response.body());
        }
    }
    
    public List<Beneficiary> getBeneficiaries() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/beneficiaries"))
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .GET()
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            BeneficiaryListResponse beneficiaryListResponse = objectMapper.readValue(
                response.body(), BeneficiaryListResponse.class);
            return beneficiaryListResponse.getBeneficiaries();
        } else {
            throw new RuntimeException("Failed to get beneficiaries: " + response.body());
        }
    }
    
    // Utility method to logout
    public void logout() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/auth/logout"))
            .header("Authorization", "Bearer " + authToken)
            .header("X-API-Key", apiKey)
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
            
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() != 200) {
            throw new RuntimeException("Logout failed: " + response.body());
        }
        
        this.authToken = null;
    }
}

// Data Transfer Objects (DTOs)

// Authentication DTOs
class AuthRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("api_key")
    private String apiKey;
    
    public AuthRequest(String username, String password, String apiKey) {
        this.username = username;
        this.password = password;
        this.apiKey = apiKey;
    }
    
    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
}

class AuthResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    // Getters and setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public int getExpiresIn() { return expiresIn; }
    public void setExpiresIn(int expiresIn) { this.expiresIn = expiresIn; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}

// Account DTOs
class Account {
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("account_type")
    private String accountType;
    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("branch_code")
    private String branchCode;
    @JsonProperty("ifsc_code")
    private String ifscCode;
    @JsonProperty("status")
    private String status;
    
    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class AccountDetailsResponse {
    @JsonProperty("account")
    private Account account;
    @JsonProperty("balance")
    private BigDecimal balance;
    @JsonProperty("available_balance")
    private BigDecimal availableBalance;
    
    // Getters and setters
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }
}

class AccountListResponse {
    @JsonProperty("accounts")
    private List<Account> accounts;
    
    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
}

class BalanceResponse {
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("balance")
    private BigDecimal balance;
    @JsonProperty("available_balance")
    private BigDecimal availableBalance;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("as_of_date")
    private String asOfDate;
    
    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getAsOfDate() { return asOfDate; }
    public void setAsOfDate(String asOfDate) { this.asOfDate = asOfDate; }
}

// Transaction DTOs
class Transaction {
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("transaction_type")
    private String transactionType;
    @JsonProperty("description")
    private String description;
    @JsonProperty("date")
    private String date;
    @JsonProperty("balance")
    private BigDecimal balance;
    @JsonProperty("reference_number")
    private String referenceNumber;
    
    // Getters and setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
}

class TransactionHistoryResponse {
    @JsonProperty("transactions")
    private List<Transaction> transactions;
    @JsonProperty("total_count")
    private int totalCount;
    
    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
}

// Transfer DTOs
class TransferRequest {
    @JsonProperty("from_account")
    private String fromAccount;
    @JsonProperty("to_account")
    private String toAccount;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("description")
    private String description;
    @JsonProperty("transfer_type")
    private String transferType; // IMPS, NEFT, RTGS
    
    public TransferRequest(String fromAccount, String toAccount, BigDecimal amount, 
                          String currency, String description, String transferType) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.transferType = transferType;
    }
    
    // Getters and setters
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTransferType() { return transferType; }
    public void setTransferType(String transferType) { this.transferType = transferType; }
}

class TransferResponse {
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("reference_number")
    private String referenceNumber;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("charges")
    private BigDecimal charges;
    
    // Getters and setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getCharges() { return charges; }
    public void setCharges(BigDecimal charges) { this.charges = charges; }
}

// Bill Payment DTOs
class BillPaymentRequest {
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("biller_id")
    private String billerId;
    @JsonProperty("consumer_number")
    private String consumerNumber;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("bill_type")
    private String billType; // ELECTRICITY, MOBILE, DTH, etc.
    
    public BillPaymentRequest(String accountNumber, String billerId, 
                             String consumerNumber, BigDecimal amount, String billType) {
        this.accountNumber = accountNumber;
        this.billerId = billerId;
        this.consumerNumber = consumerNumber;
        this.amount = amount;
        this.billType = billType;
    }
    
    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getBillerId() { return billerId; }
    public void setBillerId(String billerId) { this.billerId = billerId; }
    public String getConsumerNumber() { return consumerNumber; }
    public void setConsumerNumber(String consumerNumber) { this.consumerNumber = consumerNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
}

class PaymentResponse {
    @JsonProperty("payment_id")
    private String paymentId;
    @JsonProperty("reference_number")
    private String referenceNumber;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("transaction_date")
    private String transactionDate;
    
    // Getters and setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }
}

// Beneficiary DTOs
class Beneficiary {
    @JsonProperty("beneficiary_id")
    private String beneficiaryId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("ifsc_code")
    private String ifscCode;
    @JsonProperty("bank_name")
    private String bankName;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("status")
    private String status;
    
    // Getters and setters
    public String getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(String beneficiaryId) { this.beneficiaryId = beneficiaryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class BeneficiaryRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("ifsc_code")
    private String ifscCode;
    @JsonProperty("nickname")
    private String nickname;
    
    public BeneficiaryRequest(String name, String accountNumber, String ifscCode, String nickname) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.nickname = nickname;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}

class BeneficiaryResponse {
    @JsonProperty("beneficiary")
    private Beneficiary beneficiary;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    
    // Getters and setters
    public Beneficiary getBeneficiary() { return beneficiary; }
    public void setBeneficiary(Beneficiary beneficiary) { this.beneficiary = beneficiary; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

class BeneficiaryListResponse {
    @JsonProperty("beneficiaries")
    private List<Beneficiary> beneficiaries;
    
    public List<Beneficiary> getBeneficiaries() { return beneficiaries; }
    public void setBeneficiaries(List<Beneficiary> beneficiaries) { this.beneficiaries = beneficiaries; }
}

// Usage Example Class
class ICICIBankingAPIExample {
    public static void main(String[] args) {
        try {
            // Initialize API client
            ICICIBankingAPI api = new ICICIBankingAPI("your-api-key");
            
            // Authenticate
            AuthResponse authResponse = api.authenticate("username", "password");
            System.out.println("Authentication successful. Token: " + authResponse.getAccessToken());
            
            // Get account balance
            BalanceResponse balance = api.getBalance("1234567890");
            System.out.println("Balance: " + balance.getBalance());
            
            // Get transaction history
            List<Transaction> transactions = api.getTransactionHistory(
                "1234567890", 10, "2024-01-01", "2024-08-12");
            System.out.println("Found " + transactions.size() + " transactions");
            
            // Transfer funds
            TransferRequest transferRequest = new TransferRequest(
                "1234567890", "0987654321", new BigDecimal("1000.00"), 
                "INR", "Test transfer", "IMPS");
            TransferResponse transferResponse = api.transferFunds(transferRequest);
            System.out.println("Transfer status: " + transferResponse.getStatus());
            
            // Pay bill
            BillPaymentRequest billRequest = new BillPaymentRequest(
                "1234567890", "MSEB001", "123456789", new BigDecimal("500.00"), "ELECTRICITY");
            PaymentResponse paymentResponse = api.payBill(billRequest);
            System.out.println("Bill payment status: " + paymentResponse.getStatus());
            
            // Logout
            api.logout();
            System.out.println("Logged out successfully");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}