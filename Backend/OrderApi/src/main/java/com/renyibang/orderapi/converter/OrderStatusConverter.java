package com.renyibang.orderapi.converter;

import com.renyibang.orderapi.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Byte> {

	@Override
	public Byte convertToDatabaseColumn(OrderStatus status) {
		if (status == null) {
			return null;
		}
		return (byte) status.getCode();
	}

	@Override
	public OrderStatus convertToEntityAttribute(Byte code) {
		if (code == null) {
			return null;
		}
		return OrderStatus.fromCode(code);
	}
}