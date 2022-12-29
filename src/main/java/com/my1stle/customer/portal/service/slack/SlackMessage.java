package com.my1stle.customer.portal.service.slack;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "text"
})
public class SlackMessage {

    @JsonProperty("text")
    private String text;

    public SlackMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof SlackMessage)) return false;

        SlackMessage that = (SlackMessage) o;

        return new EqualsBuilder()
                .append(text, that.text)
                .isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37)
                .append(text)
                .toHashCode();

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text", text)
                .toString();
    }
}