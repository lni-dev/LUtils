package de.linusdev.lutils.http.header.value;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BasicHeaderValueImpl implements BasicHeaderValue {

    private @Nullable List<@NotNull String> values;
    private @Nullable Map<@NotNull String, @NotNull String> parameters;

    public BasicHeaderValueImpl(
            @Nullable List<@NotNull String> values,
            @Nullable Map<@NotNull String, @NotNull String> parameters) {
        this.values = values;
        this.parameters = parameters;
    }

    public BasicHeaderValueImpl(
            @NotNull String value) {
        this.values = new ArrayList<>(1);
        this.values.add(value);
        this.parameters = null;
    }

    /**
     * get the values of this header-value
     * @return list of string values
     */
    public @NotNull List<@NotNull String> getValues() {
        if(values == null) values = new ArrayList<>(1);
        return values;
    }

    @Override
    public @NotNull Map<@NotNull String, @NotNull String> getParameters() {
        if(parameters == null) parameters = new HashMap<>(2);
        return parameters;
    }

    @Override
    public String asString() {
        return PARSER.parse(this);
    }
}
