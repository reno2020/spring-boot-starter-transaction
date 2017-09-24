package org.throwable.protocol.model;

import org.throwable.protocol.utils.FastJsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 18:54
 */
public final class MessageBody {

	private Map<String, Object> staticAttributes;
	private List<DestinationMessagePair> contents;

	public MessageBody() {
		staticAttributes = new HashMap<>();
	}

	public MessageBody addAttribute(String key, Object value) {
		staticAttributes.putIfAbsent(key, value);
		return this;
	}

	public MessageBody setAttribute(String key, Object value) {
		staticAttributes.put(key, value);
		return this;
	}

	public Object getAttribute(String key) {
		return staticAttributes.get(key);
	}

	public Map<String, Object> getStaticAttributes() {
		return staticAttributes;
	}

	public void setStaticAttributes(Map<String, Object> staticAttributes) {
		this.staticAttributes = staticAttributes;
	}

	public List<DestinationMessagePair> getContents() {
		return contents;
	}

	public void setContents(List<DestinationMessagePair> contents) {
		this.contents = contents;
	}

	public byte[] toRabbitmqMessageBodyBytes() {
		return FastJsonUtils.toJsonString(this).getBytes();
	}
}
