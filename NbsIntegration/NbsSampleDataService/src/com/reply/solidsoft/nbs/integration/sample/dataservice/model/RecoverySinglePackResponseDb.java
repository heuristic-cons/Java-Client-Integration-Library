/**
 * -----------------------------------------------------------------------------
 * File=RecoverySinglePackResponseDb.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Result record for a single pack.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice.model;

import com.reply.solidsoft.nbs.integration.extensions.StringExtensions;
import com.reply.solidsoft.nbs.integration.extensions.OperationCodeExtensions;
import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.RequestedPackState;
import com.reply.solidsoft.nbs.integration.model.responses.RecoverySinglePackResponse;
import com.reply.solidsoft.nbs.integration.model.responses.SinglePackResponse;
import com.reply.solidsoft.nbs.integration.sample.dataservice.SampleStoreAndForwardService;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Recovery result record for a single pack.
 */
public class RecoverySinglePackResponseDb extends RecoverySinglePackResponse {

    /**
     * The pack to be verified.
     */
    private PackIdentifier pack;

    /**
     * The required pack state.
     */
    private RequestedPackState requiredState;

    /**
     * The record for the verification of the single pack.
     */
    private SinglePackResponse result;

    /**
     * Initializes a new instance of the RecoverySinglePackResponseDb class.
     */
    public RecoverySinglePackResponseDb() {
    }

    /**
     * Initializes a new instance of the RecoverySinglePackResponseDb class.
     *
     * @param recoverySinglePackResponse The pack record.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public RecoverySinglePackResponseDb(RecoverySinglePackResponse recoverySinglePackResponse) {
        this.setIsManual(recoverySinglePackResponse.getIsManual());
        this.setTimeStamp(recoverySinglePackResponse.getTimeStamp());
        this.setSeverity(
                OperationCodeExtensions.isSuccess(recoverySinglePackResponse.getResult().getOperationCode())
                ? (OperationCodeExtensions.hasWarning(recoverySinglePackResponse.getResult().getOperationCode()) ? 1 : 0)
                : StringExtensions.isNullOrWhiteSpace(recoverySinglePackResponse.getResult().getUprc()) ? 2 : 3);
        this.setMessage(StringExtensions.isNullOrWhiteSpace(recoverySinglePackResponse.getResult().getWarning()) ? recoverySinglePackResponse.getResult().getInformation() : recoverySinglePackResponse.getResult().getWarning());
    }

    /**
     * Gets the pack to be verified.
     *
     * @return The pack to be verified.
     */
    @Override
    public final PackIdentifier getPack() {
        if (this.pack != null) {
            return this.pack;
        }

        Optional<PackIdentifierDb> item = getFirstOrDefault(SampleStoreAndForwardService
                        .getIboxDb()
                        .select(
                                PackIdentifierDb.class,
                                String.format("from responses%1$s where TimeStamp==?", "PackIdentifier"),
                                this.getTimeStamp())
        );

        if (!item.isPresent()) {
            return null;
        }

        this.pack = (PackIdentifier) item.get();
        return this.pack;
    }

    /**
     * Sets the pack to be verified.
     *
     * @param value The pack to be verified.
     */
    @Override
    public final void setPack(PackIdentifier value) {
        this.pack = value;
    }

    /**
     * Gets the required pack state.
     *
     * @return The required pack state.
     */
    @Override
    public final RequestedPackState getRequiredState() {
        if (this.requiredState != null) {
            return this.requiredState;
        }

        Optional<RequestedPackStateDb> item = getFirstOrDefault(SampleStoreAndForwardService
                        .getIboxDb()
                        .select(
                                RequestedPackStateDb.class,
                                String.format("from responses%1$s where TimeStamp==?", "RequestedPackState"),
                                this.getTimeStamp())
        );

        if (!item.isPresent()) {
            return null;
        }

        this.requiredState = Enum.valueOf(RequestedPackState.class, item.get().getValue());
        return this.requiredState;
    }

    /**
     * Sets the required pack state.
     *
     * @param value The required pack state.
     */
    @Override
    public final void setRequiredState(RequestedPackState value) {
        this.requiredState = value;
    }

    /**
     * Gets the record for the verification of the single pack.
     *
     * @return The record for the verification of the single pack.
     */
    @Override
    public final SinglePackResponse getResult() {
        if (this.result != null) {
            return this.result;
        }

        Optional<SinglePackResponseDb> item = getFirstOrDefault(SampleStoreAndForwardService
                        .getIboxDb()
                        .select(
                                SinglePackResponseDb.class,
                                String.format("from responses%1$s where TimeStamp==?", "SinglePackResponse"),
                                this.getTimeStamp())
        );

        if (!item.isPresent()) {
            return null;
        }

        this.result = (SinglePackResponse) item.get();
        return this.result;
    }

    /**
     * Sets the record for the verification of the single pack.
     *
     * @param value The record for the verification of the single pack.
     */
    @Override
    public final void setResult(SinglePackResponse value) {
        this.result = value;
    }

    /**
     * Gets the first item in a collection or the default (null).
     *
     * @param <TObj> The class of item in the collection.
     * @param items The collection.
     * @return An Optional instance containing the first item or null.
     */
    private <TObj> Optional<TObj> getFirstOrDefault(Iterable<TObj> items) {
        Iterator<TObj> iterator = items.iterator();
        if (iterator.hasNext()) {
            try {
                return Optional.ofNullable(iterator.next());
            } catch (NoSuchElementException emptyEx) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
