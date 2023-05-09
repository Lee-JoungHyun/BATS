package com.cookandroid.bats;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // 데이터 메시지 처리 로직을 여기에 구현
        if (remoteMessage.getData().size() > 0) {
            // 데이터 메시지에서 필요한 데이터 추출
            String key1 = remoteMessage.getData().get("key1");
            String key2 = remoteMessage.getData().get("key2");

            // 수신된 데이터를 기반으로 필요한 작업 수행
            // ...
        }
    }
}

