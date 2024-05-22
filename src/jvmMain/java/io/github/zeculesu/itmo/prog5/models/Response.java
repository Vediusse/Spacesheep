package io.github.zeculesu.itmo.prog5.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Ответ получаемый после выполнения команды
 */
public class Response implements Serializable {

    private int status = 0;
    private String message;
    private String error;
    private List<String> output;
    private List<SpaceMarine> outputElement;

    public Response() {
        this.output = new ArrayList<>();
        this.outputElement = new ArrayList<>();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    public List<SpaceMarine> getOutputElement() {
        return outputElement;
    }

    public void setOutputElement(List<SpaceMarine> outputElement) {
        this.outputElement = outputElement;
    }

    public void addLineOutput(String line) {
        this.output.add(line);
    }

    public void addElement(SpaceMarine elem) {
        this.outputElement.add(elem);
    }

    public boolean isMessage() {
        return (this.message != null) && !this.message.isEmpty();
    }

    public boolean isError() {
        return (this.error != null) && !this.error.isEmpty();
    }

    public boolean isOutput() {
        return !this.output.isEmpty();
    }

    public boolean isOutputElement() {
        return !this.outputElement.isEmpty();
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
