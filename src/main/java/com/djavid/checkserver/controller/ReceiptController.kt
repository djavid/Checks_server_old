package com.djavid.checkserver.controller

import com.djavid.checkserver.ChecksApplication
import com.djavid.checkserver.model.entity.Receipt
import com.djavid.checkserver.model.repository.ReceiptRepository
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["receipt"])
class ReceiptController constructor(
        private val receiptRepository: ReceiptRepository
) {

    @RequestMapping(method = [RequestMethod.GET], produces = ["application/json"])
    fun getReceipts(): Iterable<Receipt> = receiptRepository.findAll()

    @RequestMapping(method = [RequestMethod.POST], produces = ["application/json"])
    fun postReceipt(@RequestBody receipt: Receipt) : Receipt {

        val res = receiptRepository.save(receipt)
        ChecksApplication.log.info("Saved " + receipt.toString())

        return res
    }

}