package io.github.benkoff.tymofiivsky.config;

import io.github.benkoff.tymofiivsky.converter.ReservationEntityToReservationResponseConverter;
import io.github.benkoff.tymofiivsky.converter.ReservationRequestToReservationEntityConverter;
import io.github.benkoff.tymofiivsky.converter.RoomEntityToReservableRoomResponseConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ConversionConfig {
    @Bean
    public ConversionService conversionService() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(getConverters());
        bean.afterPropertiesSet();

        return bean.getObject();
    }

    private Set<Converter> getConverters() {
        Set<Converter> converters = new HashSet<>();
        converters.add(new RoomEntityToReservableRoomResponseConverter());
        converters.add(new ReservationEntityToReservationResponseConverter());
        converters.add(new ReservationRequestToReservationEntityConverter());

        return converters;
    }
}
