package com.halildurmus.hotdeals.notification;

import com.google.firebase.messaging.*;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.halildurmus.hotdeals.security.SecurityService;
import com.halildurmus.hotdeals.user.FCMTokenParams;
import com.halildurmus.hotdeals.user.UserService;

import java.util.ArrayList;
import java.util.List;
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
  private MulticastMessage  createMessagesForTokens(Notification notification) {
    var user = securityService.getUser();
    Map<String, String> data = notification.getData();
    data.put("actor", user.getId());

    // Build the common notification payload
    var firebaseNotification =
        com.google.firebase.messaging.Notification.builder()
            .setTitle(notification.getTitle())
            .setBody(notification.getBody())
            .build();

    // Build the common Android-specific configuration

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
    BatchResponse batchResponse;
    // Create individual messages for each token
    MulticastMessage  messagesToSend = createMessagesForTokens(notification);

    try {
      batchResponse = firebaseMessaging.sendEachForMulticast(messagesToSend);
      log.debug(batchResponse.getSuccessCount() + " messages were sent successfully");
    } catch (FirebaseMessagingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "", e);
    }

    // Remove invalid FCM tokens from the authenticated user
    for (int i = 0; i < batchResponse.getResponses().size(); i++) {
      SendResponse sendResponse = batchResponse.getResponses().get(i);
      if (!sendResponse.isSuccessful() && sendResponse.getException() != null) {
        // It's good to check if getMessagingErrorCode() is not null before calling name()

        FirebaseMessagingException fme = sendResponse.getException();
        String errorCode = (fme.getMessagingErrorCode() != null) ?
                fme.getMessagingErrorCode().name() : "UNKNOWN_ERROR";

        fme.printStackTrace();
        // Log the failure for debugging
        log.warn("Failed to send message to token {}: ErrorCode={}, Message={}", notification.getTokens().get(i), errorCode, fme.getMessage());

        if (errorCode.equals("INVALID_ARGUMENT") || errorCode.equals("UNREGISTERED")) {
          var userUid = notification.getData().get("uid");
          var fcmToken = notification.getTokens().get(i);
          // Ensure userUid is not null before attempting to delete
          if (userUid != null) {
            var fcmTokenParams = FCMTokenParams.builder().token(fcmToken).build();
            userService.deleteFCMToken(userUid, fcmTokenParams);
              log.debug("{} was removed successfully from user {}", fcmToken, userUid);
          } else {
            log.warn("Could not remove FCM token {} as 'uid' was missing from notification data.", fcmToken);
          }
        }
      }
    }


    return batchResponse.getSuccessCount();
  }
}
