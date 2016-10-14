package com.ws.crud.operations.exception.resolvers;

import static javax.xml.bind.JAXB.marshal;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.ws.soap.SoapFault;

import com.ws.crud.operations.types.MessageType;
import com.ws.crud.operations.types.MessagesType;
import com.ws.crud.operations.types.ServiceFault;

public class SoapFaultHelper {
	public static void addServiceFaultMessage(SoapFault fault, Map<String, String> messages) {
		ServiceFault serviceFault = new ServiceFault();
		MessagesType messagesList = new MessagesType();
		messages.entrySet().stream().map(SoapFaultHelper::toMessage).forEach(m -> messagesList.getMessage().add(m));
		serviceFault.setMessages(messagesList);

		marshal(serviceFault, fault.addFaultDetail().getResult());
	}

	@Deprecated
	public static void addServiceFaultMessage(SoapFault fault, List<Pair<String, String>> messages) {
		ServiceFault serviceFault = new ServiceFault();
		MessagesType messagesList = new MessagesType();
		messages.stream().map(SoapFaultHelper::toMessage).forEach(m -> messagesList.getMessage().add(m));
		serviceFault.setMessages(messagesList);
		marshal(serviceFault, fault.addFaultDetail().getResult());
	}

	@Deprecated
	private static MessageType toMessage(Pair<String, String> tuple) {
		MessageType message = new MessageType();
		message.setCode(tuple.getLeft());
		message.setText(tuple.getRight());
		return message;
	}

	private static MessageType toMessage(Map.Entry<String, String> entry) {
		MessageType message = new MessageType();
		message.setCode(entry.getKey());
		message.setText(entry.getValue());
		return message;
	}
}
