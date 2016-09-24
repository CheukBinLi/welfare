--获取 广告轮播数据, json输出
--KEYS:getRedisLatestRecordkKey

local key=KEYS[1];
local list=redis.call('lrange',key,0,-1);
local result='[';
for i=1,#list,1 do
	if i~=1 then
		result=result..',';
	end
	result=result..list[i];
end
return result..']';