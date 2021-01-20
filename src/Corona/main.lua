local adsKit = require "plugin.huaweiAdsKit"
local widget = require( "widget" )
local json = require("json")

local TAG = "adsKit"

local header = display.newText( "Huawei Ads Kit", display.contentCenterX, 60, native.systemFont, 10 )
header:setFillColor( 255, 255, 255 )

local function listener( event )
    print(TAG, json.prettify(event))
end

adsKit.init( listener, {setTagForChildProtection= -1, setTagForUnderAgeOfPromise=1, setAdContentClassification="W", setNonPersonalizedAd=0} )

-- Interstitial Ad
local header = display.newText( "Interstitial Ad", display.contentCenterX, 100, native.systemFont, 10 )
header:setFillColor( 255, 255, 255 )

local InterstitialCreate = widget.newButton(
    {
        left = 65,
        top = 110,
        id = "create",
        label = "create",
        onPress = function()
            adsKit.create("Interstitial", {AdId="testb4znbuh3n2"})
        end,
        width = 90,
        height = 30
    }
)

local InterstitialLoad = widget.newButton(
    {
        left = 155,
        top = 110,
        id = "load",
        label = "load",
        onPress = function()
            adsKit.load("Interstitial")
        end,
        width = 90,
        height = 30
    }
)

local InterstitialShow = widget.newButton(
    {
        left = 65,
        top = 140,
        id = "show",
        label = "show",
        onPress = function()
            adsKit.show("Interstitial")
        end,
        width = 90,
        height = 30
    }
)



--Banner Ad
local header = display.newText( "Banner Ad", display.contentCenterX, 200, native.systemFont, 10 )
header:setFillColor( 255, 255, 255 )

local BannerCreate = widget.newButton(
    {
        left = 65,
        top = 210,
        id = "create",
        label = "create",
        onPress = function()
            adsKit.create("Banner", {AdId="testw6vs28auh3", BannerSize="360x144", BannerRefresh=60})
        end,
        width = 90,
        height = 30
    }
)

local BannerLoad = widget.newButton(
    {
        left = 155,
        top = 210,
        id = "load",
        label = "load",
        onPress = function()
            adsKit.load("Banner")
        end,
        width = 90,
        height = 30
    }
)

local BannerHide = widget.newButton(
    {
        left = 65,
        top = 240,
        id = "hide",
        label = "hide",
        onPress = function()
            adsKit.hide("Banner")
        end,
        width = 90,
        height = 30
    }
)

local BannerShow = widget.newButton(
    {
        left = 155,
        top = 240,
        id = "show",
        label = "show",
        onPress = function()
            adsKit.show("Banner")
        end,
        width = 90,
        height = 30
    }
)



--Rewarded Ad
local header = display.newText( "Rewarded Ad", display.contentCenterX, 300, native.systemFont, 10 )
header:setFillColor( 255, 255, 255 )

local RewardedCreate = widget.newButton(
    {
        left = 65,
        top = 310,
        id = "create",
        label = "create",
        onPress = function()
            adsKit.create("Rewarded", {AdId="testw6vs28auh3"})
        end,
        width = 90,
        height = 30
    }
)

local RewardedLoad = widget.newButton(
    {
        left = 155,
        top = 310,
        id = "load",
        label = "load",
        onPress = function()
            adsKit.load("Rewarded")
        end,
        width = 90,
        height = 30
    }
)

local show = widget.newButton(
    {
        left = 65,
        top = 340,
        id = "show",
        label = "show",
        onPress = function()
            adsKit.show("Rewarded")
        end,
        width = 90,
        height = 30
    }
)
