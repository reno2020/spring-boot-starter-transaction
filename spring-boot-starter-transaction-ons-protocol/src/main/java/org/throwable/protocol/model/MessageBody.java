package org.throwable.protocol.model;

import lombok.NonNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 18:54
 */
public final class MessageBody {

    private MessageProperties messageProperties;
    private Map<String, Object> staticAttributes;
    private Object body;

    public MessageBody(@NonNull Message message){
        this.messageProperties = message.getMessageProperties();
        this.body = new String(message.getBody());
    }

    public MessageBody addAttribute(String key,Object value){
        staticAttributes.putIfAbsent(key, value);
        return this;
    }

    public Object getAttribute(String key){
        return staticAttributes.get(key);
    }

    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }

    public Map<String, Object> getStaticAttributes() {
        return staticAttributes;
    }

    public void setStaticAttributes(Map<String, Object> staticAttributes) {
        this.staticAttributes = staticAttributes;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
