# Adyen Google Wallet Provisioning Example App (early access)

## Getting Started

A physical (un-rooted) Android device is required for access to the Google Tap and Pay API. The example app will run on an emulator but will display the error:
`15009: Calling package not verified` instead of showing the 

### Get the Adyen Provisioning SDK

This is not yet publicly available but can be requested from your Adyen account manager.

Once you have the SDK, the dependency must be made available to your development environment. This can be done via Maven Local by adding the files under your `~/.m2` directory or they can be hosted in an internal Maven repository if you have one. 

The files should not be stored in such a way that they are made publicly available.

### Get the Google Push Provisioning API

The Adyen Google Wallet Provisioning SDK has a transitive dependency on the Google Push Provisioning API (version 18.3.3).

The Google Push Provisioning API is not available publicly but can be provided by Adyen (ask your Adyen account manager) or [requested from Google](https://support.google.com/faqs/contact/pp_api_allowlist).

Once you have the API, the dependency must be made available to your development environment. This can be done via Maven Local by adding the files under your `~/.m2` directory or they can be hosted in an internal Maven repository if you have one. 

The files should not be stored in such a way that they are made publicly available.