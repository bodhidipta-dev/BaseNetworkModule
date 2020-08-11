package com.bodhi.network.networklayer.model.orderDetailsResponse

data class OrderItem(
    val ItemName: String,
    val addon: List<Addon>?,
    val id: String,
    val itemCategory: String,
    val itemCategoryID: String,
    val metric: String,
    val quantity: Int,
    val billingInfo: OrderSplitUpBill
)

fun OrderItem.makeDisplayableText(): String {
    var displayText = ""
    displayText += "${this.quantity} x ${this.billingInfo.price} = ${this.billingInfo.factor_price}"
    if (this.billingInfo.discount != "0%") {
        displayText += "\nDiscount :${this.billingInfo.discount} = -${this.billingInfo.discountAmount}"
    }
    displayText += "\nEffective price: ${this.billingInfo.displayAmount}"
    return displayText
}

fun List<Addon>.makeAddonDisplayable(): String {
    var displayableString = "Add on:"
    this.forEach {
        displayableString += "\n" + it.ItemName + "-" + it.metric
    }
    return displayableString
}