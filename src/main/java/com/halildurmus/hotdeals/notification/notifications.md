Docs Firebase:  https://firebase.google.com/docs/cloud-messaging/send-message#send-messages-to-multiple-devices   



Some error message you're seeing:

```
Resolved [org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST ""; nested exception is com.google.firebase.messaging.FirebaseMessagingException: Unexpected HTTP response with status: 404<EOL><!DOCTYPE html><EOL>...<EOL>  <p><b>404.</b> <ins>That’s an error.</ins><EOL>  <p>The requested URL <code>/batch</code> was not found on this server.   <ins>That’s all we know.</ins><EOL>]
```

This indicates that your application successfully sent a request to the Firebase Cloud Messaging (FCM) API, but FCM itself responded with an HTTP 404 Not Found error for the `/batch` endpoint.

Here's a breakdown of what's happening and common causes:

1.  **Your application sent the request:** Your `NotificationServiceImpl` is making a call to `firebaseMessaging.sendMulticast(message)`. This method, behind the scenes, constructs an HTTP request to the FCM endpoint.
2.  **FCM responded with 404:** The Firebase server reported that the `/batch` URL (which is part of the FCM API for sending multiple messages) was not found. This is a critical error coming *from Firebase*, not from your Spring Boot application's internal API.
3.  **The `ResponseStatusException`:** Your `NotificationServiceImpl` catches the `FirebaseMessagingException` and re-throws it as a Spring `ResponseStatusException` (HTTP 400 Bad Request). This is just how your application handles the error that originated from Firebase.

### Common Causes for FCM `404 Not Found` on `/batch`

Based on the provided information and common Firebase issues, here are the most likely culprits:

1.  **Outdated Firebase Admin SDK Version (Most Likely Cause):**

    * **Problem:** The `sendMulticast` method (and `sendAll` in some older SDKs) was deprecated and eventually removed/changed by Firebase. They transitioned to a different API version (v1) and updated the SDKs to reflect this. If your `firebase-admin` dependency in your `pom.xml` or `build.gradle` is too old, it might be trying to hit an endpoint (`/batch`) that no longer exists or is no longer the correct endpoint for batch messages in the current FCM API.
    * **Solution:** **Update your Firebase Admin SDK dependency to the latest stable version.**
        * Check the official Firebase Admin SDK documentation for Java to find the latest version.
        * In your `pom.xml` (if you're using Maven) or `build.gradle` (if you're using Gradle), update the `com.google.firebase:firebase-admin` dependency.
        * After updating, rebuild your project.

2.  **Firebase Cloud Messaging API Not Enabled:**

    * **Problem:** For your Firebase project to interact with FCM, the "Firebase Cloud Messaging API" must be enabled in the Google Cloud Console. If it's disabled, requests to FCM endpoints will fail.
    * **Solution:**
        1.  Go to the [Google Cloud Console](https://console.cloud.google.com/).
        2.  Select your Firebase project.
        3.  Navigate to "APIs & Services" \> "Enabled APIs & services".
        4.  Search for "Firebase Cloud Messaging API" and ensure it is **Enabled**. If not, enable it.

3.  **Incorrect Service Account Credentials/Permissions:**

    * **Problem:** Your Firebase Admin SDK needs proper authentication to interact with Firebase services. This is typically done by providing a service account JSON key file during initialization. If the service account doesn't have the necessary permissions (e.g., "Firebase Cloud Messaging Admin" or "Editor" role), it might not be authorized to access the FCM API, leading to a 404 or 401 error. While a 401 (Unauthorized) is more common for permission issues, sometimes misconfigurations can manifest as a 404.
    * **Solution:**
        1.  Verify that your service account JSON file is correct and properly loaded during your Firebase Admin SDK initialization.
        2.  Go to the [Google Cloud Console](https://console.cloud.google.com/).
        3.  Select your Firebase project.
        4.  Navigate to "IAM & Admin" \> "Service accounts".
        5.  Find the service account associated with your JSON key file.
        6.  Check its roles. Ensure it has at least the "Firebase Cloud Messaging Admin" role or a broader role like "Editor".

4.  **Project ID Mismatch:**

    * **Problem:** Ensure the Firebase project ID associated with your service account credentials matches the project ID where you're trying to send notifications.
    * **Solution:** Double-check your Firebase project configuration and the service account details.

### How to Diagnose Further:

1.  **Check Firebase Admin SDK Version:** This is the *first thing* you should check. Look at your `build.gradle` or `pom.xml`.
2.  **Enable FCM API (if not already):** Go to Google Cloud Console as described above.
3.  **Examine Service Account:** Ensure your service account has the necessary permissions.
4.  **Add More Logging:** In your `NotificationServiceImpl`, you can add more detailed logging around the `sendMulticast` call, potentially logging the `FirebaseMessagingException` directly to see its full message, which might sometimes contain more specific details than what's being propagated as a `ResponseStatusException`.
5.  **Test with Firebase Console:** Can you send a test notification to the *exact same token* from the Firebase Console (Cloud Messaging section)? If that works, it strongly points to an issue with your backend's configuration or SDK version. If it doesn't work, the problem might be with the token itself or the client app setup.

Given the `/batch` endpoint specifically being reported as 404, an **outdated Firebase Admin SDK version** is highly probable.


## Not working when updating sp what's happen?

You've hit a very common dependency compatibility issue when upgrading Java libraries, especially those that rely on underlying HTTP client implementations.

The core of your new error, after upgrading Firebase Admin SDK to 9.5.0, is this:

```
Caused by: java.lang.NoClassDefFoundError: org/apache/hc/client5/http/config/ConnectionConfig
    at com.google.firebase.internal.ApacheHttp2Transport.defaultHttpAsyncClientBuilder(ApacheHttp2Transport.java:77) ~[firebase-admin-9.5.0.jar:na]
```

### Explanation of the Error

* **`java.lang.NoClassDefFoundError: org/apache/hc/client5/http/config/ConnectionConfig`**: This means that when the Firebase Admin SDK (version 9.5.0) tried to load a class it depends on, it couldn't find it in the classpath.
* **`org.apache.hc.client5.http.config.ConnectionConfig`**: This class belongs to Apache HttpClient, specifically `httpclient5` (version 5.x.x).
* **`ApacheHttp2Transport`**: The stack trace shows that Firebase Admin SDK 9.5.0 internally uses `ApacheHttp2Transport`, which in turn relies on `httpclient5` for its HTTP communication.

**The Problem:**

Your project's current dependencies are likely pulling in an older version of Apache HttpClient (e.g., `httpclient` 4.x.x) or no `httpclient` at all, or perhaps a different HTTP client library that doesn't include the classes from `httpclient5`. Firebase Admin SDK 9.x.x made a significant internal change, moving from `httpclient` 4.x.x to `httpclient5` 5.x.x for its HTTP communication.

When you upgraded `firebase-admin` to 9.5.0, it now expects `httpclient5` to be available on the classpath, but your build system (Maven/Gradle) isn't providing it.

### Solution: Add the `httpclient5` Dependency

You need to explicitly add the `httpclient5` dependency to your project.

**If you are using Maven (in your `pom.xml`):**

```xml
<dependencies>
    <dependency>
        <groupId>com.google.firebase</groupId>
        <artifactId>firebase-admin</artifactId>
        <version>9.5.0</version> </dependency>

    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
        <version>5.2.1</version> </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5-fluent</artifactId>
        <version>5.2.1</version> </dependency>
</dependencies>
```

**If you are using Gradle (in your `build.gradle`):**

```gradle
dependencies {
    // Your existing dependencies

    implementation 'com.google.firebase:firebase-admin:9.5.0' // Or the latest stable 9.x.x version

    // Add these Apache HttpClient 5 dependencies
    implementation 'org.apache.httpcomponents.client5:httpclient5:5.2.1' // Use a recent stable version like 5.2.1 or newer
    implementation 'org.apache.httpcomponents.client5:httpclient5-fluent:5.2.1' // Use the same version as httpclient5
}
```

**Important Notes:**

* **Version of `httpclient5`**: Always try to use a recent stable version of `httpclient5` that is compatible with your `firebase-admin` SDK version. `5.2.1` is a good starting point for `firebase-admin` 9.x.x. Check the Maven Central Repository for the latest versions if needed.
* **Rebuild**: After adding these dependencies, make sure to completely rebuild your project (e.g., `mvn clean install` or `gradle clean build`).
* **Dependency Conflicts**: If you still encounter issues, it might be due to other dependencies pulling in older (or conflicting) versions of `httpclient`. You might need to use Maven's `dependency:tree` or Gradle's `dependencies` task to inspect your full dependency tree and explicitly exclude older `httpclient` transitive dependencies if they are causing conflicts. However, simply adding `httpclient5` is usually enough.

By adding the correct `httpclient5` dependencies, you'll provide the missing classes that Firebase Admin SDK 9.5.0 requires, resolving the `NoClassDefFoundError`.


## The issue still there

You're encountering a similar `ClassNotFoundException`, but this time for `org.apache.hc.core5.reactor.ProtocolUpgradeHandler`. This class is part of the Apache HttpComponents **Core 5** library (`httpcore5`), which is a foundational dependency for `httpclient5`.

### Explanation of the Error

* **`java.lang.ClassNotFoundException: org.apache.hc.core5.reactor.ProtocolUpgradeHandler`**: This means that the JVM cannot find the specified class at runtime.
* **`org.apache.hc.core5.reactor.ProtocolUpgradeHandler`**: This class belongs to the `httpcore5` project, which provides core HTTP protocol components. `httpclient5` (the one you just added) *depends* on `httpcore5`.

**The Problem:**

While `httpclient5` itself depends on `httpcore5`, sometimes dependency management systems (especially if there are other conflicting dependencies or specific configurations) might not pull in `httpcore5` correctly, or an incompatible version is being pulled.

Firebase Admin SDK 9.x.x, when it moved to Apache HttpClient 5.x.x, now fundamentally relies on both `httpclient5` and its underlying `httpcore5`.

### Solution: Explicitly Add the `httpcore5` Dependency

To resolve this, you need to explicitly add the `httpcore5` dependency to your project, ensuring that the version matches or is compatible with your `httpclient5` version.

**If you are using Maven (in your `pom.xml`):**

```xml
<dependencies>
    <dependency>
        <groupId>com.google.firebase</groupId>
        <artifactId>firebase-admin</artifactId>
        <version>9.5.0</version> </dependency>

    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
        <version>5.2.1</version> </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5-fluent</artifactId>
        <version>5.2.1</version> </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents.core5</groupId>
        <artifactId>httpcore5</artifactId>
        <version>5.2.2</version> </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents.core5</groupId>
        <artifactId>httpcore5-h2</artifactId>
        <version>5.2.2</version> </dependency>
</dependencies>
```

**If you are using Gradle (in your `build.gradle`):**

```gradle
dependencies {
    // Your existing dependencies

    implementation 'com.google.firebase:firebase-admin:9.5.0' // Or the latest stable 9.x.x version

    // Add these Apache HttpClient 5 dependencies
    // Ensure these versions are recent and compatible
    implementation 'org.apache.httpcomponents.client5:httpclient5:5.2.1' // Use a recent stable version like 5.2.1 or newer
    implementation 'org.apache.httpcomponents.client5:httpclient5-fluent:5.2.1' // Use the same version as httpclient5
    // ADD THESE ONES
    implementation 'org.apache.httpcomponents.core5:httpcore5:5.2.2' // Use a recent stable version, often slightly higher than client5, like 5.2.2 or newer
    implementation 'org.apache.httpcomponents.core5:httpcore5-h2:5.2.2' // Use the same version as httpcore5
}
```

**Key Considerations:**

* **Version Harmony:** It's crucial that the versions of `httpclient5` and `httpcore5` are compatible. Often, `httpcore5` will have a slightly higher patch version or be released in tandem with `httpclient5`. I've suggested `5.2.1` for `httpclient5` and `5.2.2` for `httpcore5`, which are recent stable versions. Always check Maven Central for the absolute latest stable versions.
* **`httpcore5-h2`**: The `ProtocolUpgradeHandler` often comes into play with HTTP/2 connections, so including `httpcore5-h2` is also important as `ApacheHttp2Transport` likely leverages HTTP/2 features.
* **Rebuild**: As always, after modifying your build configuration, perform a clean build of your project (`mvn clean install` or `gradle clean build`).

By explicitly including `httpcore5` and `httpcore5-h2` in your dependencies, you should provide the missing classes that the Firebase Admin SDK 9.x.x now expects for its underlying HTTP communication.


## Still missing in the end this resolve the issue

You are absolutely right to point that out\! My apologies, I made an error in the previous refactoring.

The `firebaseMessaging.sendEachForMulticast()` method, despite its name, **still expects a `MulticastMessage` object as its *first* argument**, not a `List<Message>`. The key difference from the old `sendMulticast` is that `sendEachForMulticast` then processes each token within that `MulticastMessage` individually, giving you per-token success/failure responses.

The confusion often arises because the name `sendEachForMulticast` implies iterating over individual messages, but the Java SDK's signature for this method is designed to take a single `MulticastMessage` containing all the tokens, and it handles the "each" part internally.

Let's correct the `NotificationServiceImpl` code:

```java
package com.halildurmus.hotdeals.notification;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message; // Keep Message import as it's used internally
import com.google.firebase.messaging.MulticastMessage; // Re-import MulticastMessage
import com.google.firebase.messaging.SendResponse;
import com.halildurmus.hotdeals.security.SecurityService;
import com.halildurmus.hotdeals.user.FCMTokenParams;
import com.halildurmus.hotdeals.user.UserService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired private FirebaseMessaging firebaseMessaging;

    @Autowired private SecurityService securityService;

    @Autowired private UserService userService;

    // This method will now create a single MulticastMessage
    private MulticastMessage createMulticastMessage(Notification notification) {
        var user = securityService.getUser();
        Map<String, String> data = notification.getData();
        // Ensure data map is mutable before putting new values
        // If notification.getData() returns an immutable map, you'd need to copy it
        // For example: Map<String, String> mutableData = new HashMap<>(notification.getData());
        data.put("actor", user.getId());

        var firebaseNotification =
                com.google.firebase.messaging.Notification.builder()
                        .setTitle(notification.getTitle())
                        .setBody(notification.getBody())
                        .build();
        var androidNotification =
                AndroidNotification.builder()
                        .setImage(notification.getImage())
                        .setTitle(notification.getTitle())
                        .setBody(notification.getBody())
                        .setTitleLocalizationKey(notification.getTitleLocKey())
                        .addAllTitleLocalizationArgs(notification.getTitleLocArgs())
                        .setBodyLocalizationKey(notification.getBodyLocKey())
                        .addAllBodyLocalizationArgs(notification.getBodyLocArgs())
                        .setPriority(AndroidNotification.Priority.MAX)
                        .build();
        var androidConfig =
                AndroidConfig.builder()
                        .setNotification(androidNotification)
                        .setPriority(Priority.HIGH)
                        .build();

        return MulticastMessage.builder()
                .setNotification(firebaseNotification)
                .setAndroidConfig(androidConfig)
                .putAllData(notification.getData()) // Use the potentially modified data map
                .addAllTokens(notification.getTokens()) // Add all tokens to the single MulticastMessage
                .build();
    }

    @Override
    public int send(Notification notification) {
        // Create a single MulticastMessage object
        MulticastMessage message = createMulticastMessage(notification);

        BatchResponse batchResponse;
        try {
            // Call sendEachForMulticast with the MulticastMessage
            batchResponse = firebaseMessaging.sendEachForMulticast(message);
            log.debug(batchResponse.getSuccessCount() + " messages were sent successfully");
        } catch (FirebaseMessagingException e) {
            log.error("Error sending Firebase messages: " + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to send notification via FCM.", e);
        }

        // Remove invalid FCM tokens from the authenticated user
        // The loop here is correct as BatchResponse gives you responses in the order of input tokens
        for (int i = 0; i < batchResponse.getResponses().size(); i++) {
            SendResponse sendResponse = batchResponse.getResponses().get(i);
            if (!sendResponse.isSuccessful() && sendResponse.getException() != null) {
                String errorCode = (sendResponse.getException().getMessagingErrorCode() != null) ?
                        sendResponse.getException().getMessagingErrorCode().name() : "UNKNOWN_ERROR";

                log.warn("Failed to send message to token {}: ErrorCode={}", notification.getTokens().get(i), errorCode);

                if (errorCode.equals("INVALID_ARGUMENT") || errorCode.equals("UNREGISTERED")) {
                    var userUid = notification.getData().get("uid");
                    var fcmToken = notification.getTokens().get(i);
                    if (userUid != null) {
                        var fcmTokenParams = FCMTokenParams.builder().token(fcmToken).build();
                        userService.deleteFCMToken(userUid, fcmTokenParams);
                        log.debug(fcmToken + " was removed successfully from user " + userUid);
                    } else {
                        log.warn("Could not remove FCM token {} as 'uid' was missing from notification data.", fcmToken);
                    }
                }
            }
        }

        return batchResponse.getSuccessCount();
    }
}
```

### Explanation of the Correction:

* **`createMulticastMessage` is back to its original return type**: It now correctly builds and returns a single `MulticastMessage` object.
* **`MulticastMessage.builder().addAllTokens(notification.getTokens())`**: The `MulticastMessage` itself holds the list of recipient tokens. This is the crucial part that `sendEachForMulticast` uses.
* **`firebaseMessaging.sendEachForMulticast(message)`**: You pass this *single* `MulticastMessage` object to `sendEachForMulticast`. The Firebase Admin SDK then internally handles iterating through the tokens within that `MulticastMessage` and sends them efficiently in a batch, while still providing individual `SendResponse` objects in the `BatchResponse`.

This revised code should resolve the `Required type: MulticastMessage Provided: List<com.google.firebase.messaging.Message>` compilation error and align with how `sendEachForMulticast` is intended to be used in the Firebase Admin Java SDK.