
/*
 * SampleController.java 2022. 01. 10
 *
 * Copyright 2022 Naver Cloud Corp. All rights Reserved.
 * Naver Cloud Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package me.potato.springresilience4jbulkhead.expose.rest;

import java.util.concurrent.CompletionStage;

import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongju.paek
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class SampleController {

	private final BulkheadRegistry bulkheadRegistry;
	private final ThreadPoolBulkheadRegistry threadBulkheadRegistry;

	@GetMapping("/test01")
	public String test01() throws Throwable {
		var bulkhead = bulkheadRegistry.bulkhead("sampleBulkhead");

		var supplier = Bulkhead.decorateCheckedSupplier(bulkhead, () -> {
			Thread.sleep(10000L);
			return "Hello World";
		});

		return Try.of(supplier)
			.recover(error -> "Waiting...")
			.get();

	}


	@GetMapping("/test02")
	public CompletionStage<String> test02() throws Throwable {
		var temp = threadBulkheadRegistry.getAllBulkheads();
		var bulkhead = threadBulkheadRegistry.bulkhead("sampleThreadPoolBulkhead");

		return ThreadPoolBulkhead.decorateSupplier(bulkhead, () -> {
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "Hello World";
		}).get();

	}


	@EventListener
	public void eventLogger(Object event){
		log.info(event.toString());
	}


}