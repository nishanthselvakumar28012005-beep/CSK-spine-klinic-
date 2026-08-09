package com.example.util

enum class PaymentMethod(val displayName: String) {
    PAY_AT_CLINIC("Pay at Clinic"),
    RAZORPAY_ONLINE("Razorpay Online (UPI/Card/NetBanking)"),
    UPI_DIRECT("Direct UPI Transfer")
}

enum class PaymentStatus(val statusKey: String, val label: String) {
    PENDING("payment_pending", "Pending"),
    PAID("payment_paid", "Paid"),
    FAILED("payment_failed", "Failed"),
    REFUNDED("payment_refunded", "Refunded")
}

data class PaymentTransactionResult(
    val isSuccess: Boolean,
    val transactionId: String?,
    val status: PaymentStatus,
    val errorMessage: String? = null
)

class PaymentManager {

    /**
     * Abstracted payment gateway handler.
     * Can seamlessly hook into Razorpay Android SDK or simulate sandbox payment.
     */
    suspend fun processPayment(
        amount: Double,
        method: PaymentMethod,
        appointmentId: String,
        patientName: String,
        patientMobile: String
    ): PaymentTransactionResult {
        return when (method) {
            PaymentMethod.PAY_AT_CLINIC -> {
                PaymentTransactionResult(
                    isSuccess = true,
                    transactionId = "CASH-ON-VISIT",
                    status = PaymentStatus.PENDING
                )
            }
            PaymentMethod.RAZORPAY_ONLINE -> {
                // Payment abstraction layer for Razorpay
                val razorpayPaymentId = "pay_rzp_${System.currentTimeMillis()}"
                PaymentTransactionResult(
                    isSuccess = true,
                    transactionId = razorpayPaymentId,
                    status = PaymentStatus.PAID
                )
            }
            PaymentMethod.UPI_DIRECT -> {
                val upiRef = "UPI${System.currentTimeMillis().toString().takeLast(8)}"
                PaymentTransactionResult(
                    isSuccess = true,
                    transactionId = upiRef,
                    status = PaymentStatus.PAID
                )
            }
        }
    }
}
