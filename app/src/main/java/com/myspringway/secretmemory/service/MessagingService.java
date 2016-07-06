package com.myspringway.secretmemory.service;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by legab on 2016-06-19.
 * This is required if you wnat to do any message handling beyond receiving notifications
 * on apps in the background. To receive notifications in foregrounded apps,
 * to receive data payload, to send upstream messages, and so on, you must extend this service.
 */
public class MessagingService extends FirebaseMessagingService {
}
