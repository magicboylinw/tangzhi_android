package com.ifanr.tangzhi.repository.baas.datasource

import com.ifanr.tangzhi.model.Message
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.repository.baas.Tables
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.query.Where

/**
 * 普通消息
 */
class MessageDataSource(
    private val repository: BaasRepository
): BaseDataSource<Message>(
    table = Tables.message,
    clz = Message::class.java,
    initQuery = {
        put(Where().apply {
            notEqualTo(Message.COL_TYPE, Message.TYPE_SYSTEM)
            equalTo(Record.CREATED_BY, repository.currentUserWithoutData()?.id)
        })
        expand(Message.COL_SENDER)
        orderBy("-${Record.CREATED_BY}")
    }
)