package com.djavid.checkserver.model.entity

import javax.persistence.*

@Entity
public class Receipt(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
//        val buyerId: Long,
        val fiscalSign: Long,
        val fiscalDocumentNumber: Long,
        val ecashTotalSum: Long,
        val taxationType: Long,
        @OneToMany(targetEntity = Item::class, fetch = FetchType.EAGER) //TODO hz cho tut
        val items: List<Item>,
        val shiftNumber: Long,
        val user: String,
        val receiptCode: Long,
        val cashTotalSum: Long,
        val userInn: String,
        val nds10: Long,
        val requestNumber: Long,
        val nds18: Long,
        val operationType: Long,
        val fiscalDriveNumber: String,
        val dateTime: String,
        val totalSum: Long,
        val kktRegId: String,
        val operator: String
)