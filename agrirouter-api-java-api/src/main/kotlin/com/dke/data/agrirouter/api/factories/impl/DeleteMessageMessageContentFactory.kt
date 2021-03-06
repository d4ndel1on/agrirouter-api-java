package com.dke.data.agrirouter.api.factories.impl

import agrirouter.feed.request.FeedRequests
import com.dke.data.agrirouter.api.factories.impl.parameters.DeleteMessageMessageParameters
import com.dke.data.agrirouter.api.util.TimestampUtil
import com.google.protobuf.ByteString

/**
 * Implementation of a message content factory.
 */
class DeleteMessageMessageContentFactory {

    fun message(parameters: DeleteMessageMessageParameters): ByteString {
        parameters.validate()
        val messageContent = FeedRequests.MessageDelete.newBuilder()
        messageContent.addAllMessageIds(parameters.messageIds)
        messageContent.addAllSenders(parameters.senderIds)
        if (null != parameters.sentFromInSeconds || null != parameters.sentToInSeconds) {
            val validityPeriod = FeedRequests.ValidityPeriod.newBuilder()
            if (null != parameters.sentFromInSeconds) {
                validityPeriod.sentFrom = TimestampUtil().seconds(parameters.sentFromInSeconds!!)
            }
            if (null != parameters.sentToInSeconds) {
                validityPeriod.sentTo = TimestampUtil().seconds(parameters.sentToInSeconds!!)
            }
            messageContent.setValidityPeriod(validityPeriod)
        }
        return messageContent.build().toByteString()
    }

}