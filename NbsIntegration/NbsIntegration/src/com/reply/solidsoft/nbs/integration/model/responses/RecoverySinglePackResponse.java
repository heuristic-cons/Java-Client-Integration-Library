/**
 * -----------------------------------------------------------------------------
 * File=RecoveryResultResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Recovery result record for a single pack.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

import com.reply.solidsoft.nbs.integration.extensions.OperationCodeExtensions;
import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.RequestedPackState;
import com.reply.solidsoft.nbs.integration.model.extensions.PropertyChangedEventArgs;
import com.reply.solidsoft.nbs.integration.model.extensions.PropertyChangedEventHandler;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import com.reply.solidsoft.nbs.integration.data.model.DataRecord;

/**
 * Recovery result record for a single pack.
 */
public class RecoverySinglePackResponse implements DataRecord {

    /**
     * Initializes a new instance of the RecoverySinglePackResponse class.
     */
    public RecoverySinglePackResponse() {
    }

    /**
     * Initializes a new instance of the RecoverySinglePackResponse class.
     *
     * @param request The pack request.
     * @param record The pack record.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public RecoverySinglePackResponse(DeferredRequest request, BulkSinglePackResponse record) {

        this.setRequiredState(RequestedPackState.get(request.getRequestedState()));
        this.setIsManual(request.getIsManual());
        this.setTimeStamp(record.getTimeStamp());
        this.setPack(record.getPack());
        this.setResult(record.getResult());
        this.setSeverity(
                OperationCodeExtensions.isSuccess(record.getResult().getOperationCode())
                ? OperationCodeExtensions.hasWarning(record.getResult().getOperationCode()) ? 1 : 0
                : com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(record.getResult().getUprc()) ? 2 : 3);
        this.setMessage(com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(record.getResult().getWarning()) ? record.getResult().getInformation() : record.getResult().getWarning());
    }

    /**
     * The Property Changed event.
     */
    public com.reply.solidsoft.nbs.integration.extensions.events.Event<PropertyChangedEventHandler> propertyChanged = new com.reply.solidsoft.nbs.integration.extensions.events.Event<>();

    /**
     * A value indicating whether the data entry is manual or non-manual.
     */
    private Boolean isManual = null;

    /**
     * Gets a value indicating whether the data entry is manual or non-manual.
     *
     * @return A value indicating whether the data entry is manual or
     * non-manual.
     */
    public Boolean getIsManual() {
        return isManual;
    }

    /**
     * Sets a value indicating whether the data entry is manual or non-manual.
     *
     * @param value A value indicating whether the data entry is manual or
     * non-manual.
     */
    public void setIsManual(Boolean value) {
        isManual = value;
    }

    /**
     * An information or warning message.
     */
    private String message;

    /**
     * Gets an information or warning message.
     *
     * @return An information or warning message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets an information or warning message.
     *
     * @param value An information or warning message.
     */
    public void setMessage(String value) {
        message = value;
    }

    /**
     * The pack to be verified.
     */
    private PackIdentifier pack;

    /**
     * Gets the pack to be verified.
     *
     * @return The pack to be verified.
     */
    public PackIdentifier getPack() {
        return pack;
    }

    /**
     * Sets the pack to be verified.
     *
     * @param value The pack to be verified.
     */
    public void setPack(PackIdentifier value) {
        pack = value;
    }

    /**
     * The required pack state.
     */
    private RequestedPackState requiredState;

    /**
     * Gets the required pack state.
     *
     * @return The required pack state.
     */
    public RequestedPackState getRequiredState() {
        return requiredState;
    }

    /**
     * Sets the required pack state.
     *
     * @param value The required pack state.
     */
    public void setRequiredState(RequestedPackState value) {
        requiredState = value;
    }

    /**
     * The record for the verification of the single pack.
     */
    private SinglePackResponse result;

    /**
     * Gets the record for the verification of the single pack.
     *
     * @return The record for the verification of the single pack.
     */
    public SinglePackResponse getResult() {
        return result;
    }

    /**
     * Sets the record for the verification of the single pack.
     *
     * @param value The record for the verification of the single pack.
     */
    public void setResult(SinglePackResponse value) {
        result = value;
    }

    /**
     * A value indicating whether the result is selected.
     */
    private boolean selected;

    /**
     * Gets a value indicating whether the result is selected.
     *
     * @return A value indicating whether the result is selected.
     */
    public final boolean getSelected() {
        return selected;
    }

    /**
     * Sets a value indicating whether the result is selected.
     *
     * @param value A value indicating whether the result is selected.
     */
    public final void setSelected(boolean value) {
        selected = value;
    }

    /**
     * The severity of the result. 0 means OK with no warnings.
     */
    private int severity;

    /**
     * Gets the severity of the result. 0 means OK with no warnings.
     *
     * @return The severity of the result. 0 means OK with no warnings.
     */
    public final int getSeverity() {
        return severity;
    }

    /**
     * Sets the severity of the result. 0 means OK with no warnings.
     *
     * @param value The severity of the result. 0 means OK with no warnings.
     */
    public final void setSeverity(int value) {
        severity = value;
    }

    /**
     * The timestamp value.
     */
    private long timeStamp;

    /**
     * Gets the timestamp value.
     */
    @Override
    public final long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the timestamp value.
     *
     * @param value The timestamp value.
     */
    public final void setTimeStamp(long value) {
        timeStamp = value;
    }

    /**
     * Returns a string representing the bulk results.
     *
     * @return A representation of the bulk results.
     */
    @Override
    public String toString() {
        return this.getResult() == null ? null : this.getResult().toString();
    }

    /**
     * Notify the change of property.
     */
    private void notifyPropertyChanged() {
        this.notifyPropertyChanged("");
    }

    /**
     * Notify the change of property.
     *
     * @param propertyName The name of the property that has been changed.
     */
    private void notifyPropertyChanged(String propertyName) {
        //invoke all listeners:
        this.propertyChanged.listeners().forEach((listener) -> {
            listener.invoke(this, new PropertyChangedEventArgs(propertyName));
        });
    }
}
