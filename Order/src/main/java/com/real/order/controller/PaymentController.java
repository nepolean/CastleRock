package com.real.order.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.real.order.model.Cash;
import com.real.order.model.Cheque;
import com.real.order.model.PaymentRequest;
import com.real.order.repository.PaymentRepository;
import com.real.order.service.PaymentService;

@RestController
public class PaymentController {
	private static Logger logger = LoggerFactory.getLogger(PaymentController.class);
	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	PaymentService paymentService;

	@RequestMapping(path = { "/pay/cheque" }, method = { RequestMethod.POST })
	public String payByCheque(@Validated @RequestBody Cheque cheque, Principal loggedInUser) throws Exception {
		return paymentService.initiatePayment(cheque);
	}

	@RequestMapping(path = { "/pay/cash" }, method = { RequestMethod.POST })
	public String payByCash(@Validated @RequestBody Cash cash, Principal loggedInUser) throws Exception {
		return paymentService.initiatePayment(cash);
	}

	@RequestMapping(path = { "/pay/online" }, method = { RequestMethod.POST })
	public String payByOnline(@Validated @RequestBody Cash cash, Principal loggedInUser) throws Exception {
		return paymentService.initiatePayment(cash);
	}

	@RequestMapping(path = { "/pay/{paymentRequestId}/status" }, method = { RequestMethod.GET })
	public PaymentRequest getPaymentStatus(@PathVariable String paymentRequestId, Principal loggedInUser)
			throws Exception {
		return paymentService.checkPaymentStatus(paymentRequestId);
	}

	@RequestMapping(path = { "/pay/status/pending" }, method = { RequestMethod.GET })
	@Secured("ROLE_ADMIN")
	public Page<PaymentRequest> getPendingPayments(@RequestParam(value = "status", defaultValue = "none") String status,
			Principal loggedInUser) throws Exception {
		return paymentService.getPendingOrders(status);
	}

	@RequestMapping(path = { "/pay/{paymentRequestId}/approval" }, method = { RequestMethod.PUT })
	@Secured("ROLE_ADMIN")
	public void updateOrderApproval(HttpServletResponse httpServletResponse, @PathVariable String paymentRequestId,
			@RequestBody String status, Principal loggedInUser) throws Exception {
		httpServletResponse.sendRedirect(paymentService.updatePaymentStatus(paymentRequestId, status));
	}

	// Local testing purpose, please do consider this
	@RequestMapping(path = { "/test" }, method = { RequestMethod.POST })
	public PaymentRequest test(@Validated @RequestBody PaymentRequest paymentRequest) throws Exception {
		return paymentService.newPaymentRequest(paymentRequest);
	}

	@RequestMapping(path = { "/test/updatecallback" }, method = { RequestMethod.PUT })
	public Page<PaymentRequest> test2() throws Exception {
		return paymentService.getPendingOrders("none");
	}
}