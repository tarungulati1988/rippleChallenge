package com.ripple.service.v1.impl;

import static org.junit.Assert.*;

import com.ripple.event.EventTypes;
import com.ripple.event.TransactionEvent;
import com.ripple.model.data.TrustLine;
import com.ripple.model.request.Transaction;
import com.ripple.model.response.TransactionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@PrepareForTest({})
public class TrustLineServiceTest {

  @InjectMocks
  @Spy
  private TrustLineService service;

  @Mock
  private ApplicationEventPublisher publisher;

  @Captor
  protected ArgumentCaptor<TransactionEvent> publishEventCaptor;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    PowerMockito.spy(TrustLineService.class);
  }

  @Test
  public void sendMoney_positiveCase1() throws Exception {
    Transaction transaction = Transaction
        .builder()
        .amount(new BigDecimal(1234))
        .destinationAccount("123")
        .source("321")
        .build();
    ResponseEntity<String> responseEntity = ResponseEntity.status(200).body("success");
    Mockito
        .doReturn(responseEntity)
        .when(service).sendToRecepient(Matchers.any());
    PowerMockito
        .when(service, "buildResponse", responseEntity, transaction)
        .thenCallRealMethod();
    ResponseEntity<TransactionResponse> response = service.sendMoney(transaction);
    Mockito
        .verify(publisher, Mockito.times(2))
        .publishEvent(publishEventCaptor.capture());
    List<TransactionEvent> capturedEvents = publishEventCaptor.getAllValues();
    assertTrue(response != null);
    assertTrue(response.getBody().getMessage().equals("success"));
    assertTrue(!CollectionUtils.isEmpty(response.getBody().getTrustLines()));
    assertTrue(capturedEvents.get(0) instanceof TransactionEvent);
    assertTrue(capturedEvents.get(0).getEventType() == EventTypes.AMOUNT_SENT);
    assertTrue(capturedEvents.get(1) instanceof TransactionEvent);
    assertTrue(capturedEvents.get(1).getEventType() == EventTypes.BALANCE);
  }


  @Test(expected = Exception.class)
  public void sendMoney_negativeCase1() throws Exception {
    Transaction transaction = Transaction
        .builder()
        .amount(new BigDecimal(1234))
        .destinationAccount("123")
        .source("321")
        .build();
    Mockito.doThrow(new SocketTimeoutException()).when(service).sendToRecepient(Matchers.any());
    service.sendMoney(transaction);
  }

  @Test
  public void receiveMoney_positiveCase1() throws Exception {
    Transaction transaction = Transaction
        .builder()
        .amount(new BigDecimal(1234))
        .destinationAccount("123")
        .source("321")
        .build();
    ResponseEntity<TransactionResponse> response = service.receiveMoney(transaction);
    assertTrue(response != null);
    assertTrue(response.getBody().getMessage().equals("success"));
    assertTrue(!CollectionUtils.isEmpty(response.getBody().getTrustLines()));
    Mockito
        .verify(publisher, Mockito.times(2))
        .publishEvent(publishEventCaptor.capture());
    List<TransactionEvent> capturedEvents = publishEventCaptor.getAllValues();
    assertTrue(capturedEvents.get(0) instanceof TransactionEvent);
    assertTrue(capturedEvents.get(0).getEventType() == EventTypes.AMOUNT_RECEIVED);
    assertTrue(capturedEvents.get(1) instanceof TransactionEvent);
    assertTrue(capturedEvents.get(1).getEventType() == EventTypes.BALANCE);
  }

}