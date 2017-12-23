package com.real.order.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.real.order.model.Cash;
import com.real.order.model.Cheque;
import com.real.order.model.OnlinePayment;
import com.real.order.model.PaymentMode;
import com.real.order.model.PaymentRequest;
import com.real.order.model.PaymentStatus;
import com.real.order.model.PaymentType;
import com.real.order.repository.PaymentRepository;

@Service
public class PaymentService {

	private static Logger logger = LoggerFactory.getLogger(PaymentService.class);
	@Autowired
	PaymentRepository paymentRepository;

	public PaymentRequest newPaymentRequest(PaymentRequest paymentRequest) {
		if (paymentRequest == null)
			throw new IllegalArgumentException("Can't proceed with NULL paymentRequest");
		paymentRequest.setPaymentStatus(PaymentStatus.CREATED.toString());
		paymentRequest.setRequestCreatedDate(new Date());
		if (logger.isDebugEnabled())
			logger.debug("Invoking Database to store order information..!");
		return paymentRepository.save(paymentRequest);
	}

	public String initiatePayment(PaymentType payment) {
		if (PaymentMode.CHEQUE.equals(payment.getType()))
			return processChequePayment((Cheque) payment).getPaymentStatus();
		if (PaymentMode.CASH.equals(payment.getType()))
			return processCashPayment((Cash) payment).getPaymentStatus();
		if (PaymentMode.ONLINE.equals(payment.getType()))
			return processOnlinePayment((OnlinePayment) payment).getPaymentStatus();
		return "Not supported payment option";
	}

	public PaymentRequest getPaymentRequest(String paymentRequestId) {
		return paymentRepository.findOne(paymentRequestId);
	}

	public String updatePaymentStatus(String paymentRequestId, String status) {
		PaymentRequest paymentRequest = paymentRepository.findOne(paymentRequestId);
		paymentRequest.setPaymentStatus(status);
		paymentRepository.save(paymentRequest);
		return constructRedirectUrl(paymentRequest.getCallbackURI());
	}

	public Page<PaymentRequest> getPendingOrders(String status) {
		if (!status.equals("none"))
			return paymentRepository.findByPaymentStatusIn(new String[] { status }, new PageRequest(0, 20));
		else
			return paymentRepository.findByPaymentStatusIn(new String[] { PaymentStatus.CREATED.toString(),
					PaymentStatus.INITIATED.toString(), PaymentStatus.IN_PROGRESS.toString() }, new PageRequest(0, 20));
	}

	public PaymentRequest checkPaymentStatus(String paymentRequestId) {
		return getPaymentRequest(paymentRequestId);
	}

	private PaymentRequest processChequePayment(Cheque cheque) {
		PaymentRequest paymentRequest = getPaymentRequest(cheque.getPaymentRequestId());
		paymentRequest.setPaymentType(cheque);
		paymentRequest.setPaymentStatus(PaymentStatus.INITIATED.toString());
		return paymentRepository.save(paymentRequest);
	}

	private PaymentRequest processCashPayment(Cash cash) {
		PaymentRequest paymentRequest = getPaymentRequest(cash.getPaymentRequestId());
		paymentRequest.setPaymentType(cash);
		paymentRequest.setPaymentStatus(PaymentStatus.INITIATED.toString());
		return paymentRepository.save(paymentRequest);
	}

	private PaymentRequest processOnlinePayment(OnlinePayment onlinePayment) {
		// redirect to payment gateway api
		return null;
	}

	private String constructRedirectUrl(String URI) {
		return "http://localhost:8080" + URI;
	}

}
