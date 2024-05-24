//
//  Plugin.swift
//  CardReaderSample
//
//  Created by 平山 裕也 on 2023/12/04.
//

import Foundation
import UIKit

@objc(Plugin)
class Plugin : CDVPlugin, OnPluginManagerDelegate {
    
    // MARK: - Member
    private var mPluginManager: PluginManager? = nil
    private var mOcrCallbackId: String? = nil
    
    // MARK: - OnPluginManagerDelegate
    func onResult(sender: PluginManager, code: Int, result: OcrResultInfo?) {
        stopOcr()
        if (mOcrCallbackId != nil) {
            if (code == PluginManager.CODE_SUCCESS) {
                
                let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: ["errorCode": 0,
                    "type": result?.mCardType ?? 0,
                    "name": result?.mName ?? "",
                    "gender": result?.mGender ??  "",
                    "address": result?.mAddress ?? "",
                    "birthdate": result?.mBirthdate ?? ""])
                pluginResult?.keepCallback = true
                commandDelegate.send(pluginResult, callbackId: mOcrCallbackId)
            } else if (code == PluginManager.CODE_AUTHROIZE) {
                let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: ["errorCode": -2] )
                pluginResult?.keepCallback = true
                commandDelegate.send(pluginResult, callbackId: mOcrCallbackId)
            } else {
                let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: ["errorCode": -1] )
                pluginResult?.keepCallback = true
                commandDelegate.send(pluginResult, callbackId: mOcrCallbackId)
            }
        }
    }
        
    // MARK: - Accesser
    @objc(startOCR:)
    func startOCR(command: CDVInvokedUrlCommand) {
        mOcrCallbackId = command.callbackId
        startOcr()
    }
    
    // MARK: - Function
    private func getPluginManager() -> PluginManager {
        if (mPluginManager == nil) {
            mPluginManager = PluginManager()
        }
        return mPluginManager!
    }
    
    private func topMostController() -> UIViewController? {
        return UIApplication.shared.keyWindow?.rootViewController?.presentedViewController
    }
    
    private func startOcr() {
        let viewController = topMostController()
        if (viewController == nil) {
            if (mOcrCallbackId != nil) {
                let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: ["errorCode": -1] )
                pluginResult?.keepCallback = true
                commandDelegate.send(pluginResult, callbackId: mOcrCallbackId)
            }
            return
        }
        getPluginManager().start(parent: viewController!, delegate: self)
    }
    
    private func stopOcr() {
        getPluginManager().stop()
        mPluginManager = nil
    }

}
