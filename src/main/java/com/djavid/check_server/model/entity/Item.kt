package com.djavid.check_server.model.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Item(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        val quantity: Long,
        val price: Long,
        val sum: Long,
        val name: String,
        val nds18: Long,
        val nds10: Long
)