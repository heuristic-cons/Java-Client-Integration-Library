/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reply.solidsoft.nbs.integration.sample.console;

import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.RequestedPackState;
import java.lang.FunctionalInterface;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;

/**
 *
 * @author ch.young
 */
public class BulkRequestThread extends Thread {
    
    @FunctionalInterface
    interface DecommissionConsumer
    {
        public void accept(
                Supplier<PackIdentifier[]> supplier, 
                RequestedPackState requestedPackState);
    }
    
    private DecommissionConsumer doBulkDecommissionRequest;
    private Consumer<Supplier<PackIdentifier[]>> doBulkRequest;
    private Supplier<PackIdentifier[]> getPacks;
    private RequestedPackState requestedPackState;
    private boolean isDecommission = false;

    public void run() {

        if (isDecommission)
        {
            this.doBulkDecommissionRequest = NbsConsole::doBulkRequest;
            
            if (null != this.getPacks) {
                this.doBulkDecommissionRequest.accept(
                        this.getPacks, 
                        this.requestedPackState);
            }            
        }
        else
        {
            this.doBulkRequest = NbsConsole::doBulkRequest;

            if (null != this.getPacks) {
                this.doBulkRequest.accept(this.getPacks);
            }
        }

    }

    public void StartBulkVerifyTestPacks() {
        this.isDecommission = false;
        this.getPacks = NbsConsole::bulkVerifyTestPacks;
        this.start();
    }

    public void StartBulkSupplyTestPacks() {
        this.isDecommission = false;
        this.getPacks = NbsConsole::bulkSupplyTestPacks;
        this.start();
    }
    
    public void StartBulkDecommissionTestPacks(RequestedPackState requestedPackState) {
        this.isDecommission = true;
        this.requestedPackState = requestedPackState;
        this.getPacks = NbsConsole::bulkDecommissionTestPacks;
        this.start();
    }
    
    public void StartBulkReactivateTestPacks() {
        this.isDecommission = false;
        this.getPacks = NbsConsole::bulkReactivateTestPacks;
        this.start();
    }
}
