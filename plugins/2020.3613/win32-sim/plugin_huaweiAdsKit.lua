local Library = require "CoronaLibrary"

local lib = Library:new{ name='plugin.huaweiHuaweiKit', publisherId='com.solar2d' }

local placeholder = function()
	print( "WARNING: The '" .. lib.name .. "' library is not available on this platform." )
end


lib.init = placeholder
lib.create = placeholder
lib.load = placeholder
lib.show = placeholder
lib.hide = placeholder

-- Return an instance
return lib