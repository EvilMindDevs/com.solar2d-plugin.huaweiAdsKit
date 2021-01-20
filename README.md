# Huawei Ads Kit Solar2d Plugin

This plugin was created based on Huawei Ads Kit. Please [check](https://developer.huawei.com/consumer/en/hms/huawei-adskit/) for detailed information about Huawei Ads Kit. 

In order to use the Huawei Ads Kit, you must first create an account from developer.huawei.com. And after logging in with your account, and then you must create a project in the huawei console in order to use HMS kits.

## Project Setup

To use the plugin please add following to `build.settings`

```lua
{
    plugins = {
        ["plugin.huaweiAdsKit"] = {
            publisherId = "com.solar2d",
        },
    },
}
```

After you need to define the plugin in main.lua.

```lua
local adsKit = require "plugin.huaweiAdsKit"

local function listener( event )
    print(TAG, json.prettify(event))
end

adsKit.init( listener )
```

We should call all methods through adsKit object. And you can take result informations from listener.

## Ads Types

### Banner Ad
Banner ads are rectangular images that occupy a spot at the top, middle, or bottom within an app's layout. Banner ads refresh automatically at regular intervals. When a user clicks a banner ad, the user is usually redirected to the advertiser's page.
#### create 

```lua
    adsKit.create("Banner", {AdId="testw6vs28auh3", BannerSize="360x144", BannerRefresh=60})

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = create (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```
#### load 

```lua
    adsKit.load("Banner")

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = load (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```
#### show 

```lua
    adsKit.show("Banner")

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = load (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```
#### hide 

```lua
    adsKit.hide("Banner")

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = load (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```

### Interstitial Ad
Interstitial ads are full-screen ads that cover the interface of an app. Such an ad is displayed when a user starts, pauses, or exits an app, without disrupting the user's experience.

#### create 

```lua
    adsKit.create("Interstitial", {AdId="testb4znbuh3n2"})

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = create (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```
#### load 

```lua
    adsKit.load("Interstitial")

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = load (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```
#### show 

```lua
    adsKit.show("Interstitial")

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = load (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```


### Rewarded Ad
Rewarded ads are full-screen video ads that reward users for watching.

#### create 

```lua
    adsKit.create("Rewarded", {AdId="testw6vs28auh3"})

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = create (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```
#### load 

```lua
    adsKit.load("Rewarded")

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = load (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```
#### show 

```lua
    adsKit.show("Rewarded")

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = load (text)
              provider = Huawei Ads Kit (text)
        } 
    ]]--
```

The HUAWEI Ads SDK allows you to set the request ad content for different audiences. Please [check](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/publisher-service-advanced-settings-0000001050064972
) for detailed information about request options. 

```lua
    adsKit.init( listener, 
            {setTagForChildProtection= -1, 
            setTagForUnderAgeOfPromise=1, 
            setAdContentClassification="W", 
            setNonPersonalizedAd=0} )
            
    --[[
        TAG_FOR_CHILD_PROTECTION_UNSPECIFIED = -1;
        TAG_FOR_CHILD_PROTECTION_FALSE = 0;
        TAG_FOR_CHILD_PROTECTION_TRUE = 1;
        
        PROMISE_TRUE = 1;
        PROMISE_FALSE = 0;
        PROMISE_UNSPECIFIED = -1;
        
        AD_CONTENT_CLASSIFICATION_W = "W";
        AD_CONTENT_CLASSIFICATION_PI = "PI";
        AD_CONTENT_CLASSIFICATION_J = "J";
        AD_CONTENT_CLASSIFICATION_A = "A";
        AD_CONTENT_CLASSIFICATION_UNKOWN = "";
        
        ALLOW_NON_PERSONALIZED = 1;
        ALLOW_ALL = 0;
    ]]--
```



## References
HMS Ads Kit  https://developer.huawei.com/consumer/en/hms/huawei-adskit/

## License
MIT
