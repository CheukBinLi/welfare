--筹款统计和更新轮广告数据
--KEYS : getRedisStatisticalKey,getRedisLatestRecordkKey
--return :{record:[{广告数据}],statistical:{counts:显示记录数,totalAmount:总数}}

local statisticalKey=KEYS[1];
local latestRecordKey=KEYS[2];
--统计
local totalAmountField='totalAmount';
local totalAmount=redis.call('HGET',statisticalKey,totalAmountField);
local countsField='counts';
local counts=redis.call('HGET',statisticalKey,countsField);
local statistical='{\"counts\":\"'..counts..'\",\"totalAmount\":\"'..totalAmount..'\"}';

local list=redis.call('lrange',latestRecordKey,0,-1);
local record='[';
for i=1,#list,1 do
	if i~=1 then
		record=record..',';
	end
	record=record..list[i];
end
record=record..']';

return {statistical,record};
