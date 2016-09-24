--筹款统计和更新轮广告数据
--KEYS : getRedisStatisticalKey,getRedisLatestRecordkKey
--ARGV :{record:{record:广告数据,size:广告显示数据量},statistical:{totalAmount:总数}}

local statisticalKey=KEYS[1];
local latestRecordKey=KEYS[2];
local json=cjson.decode(ARGV[1]);

local totalAmountField='totalAmount';
local countsField='counts';
local totalAmount=json.statistical.totalAmount;
local latestRecord=cjson.encode(json.record.record);
latestRecord=string.gsub(latestRecord,'\/','\\');
local latestRecordViewSize=json.record.size;

redis.call('HINCRBYFLOAT',statisticalKey,totalAmountField,totalAmount);
redis.call('HINCRBY',statisticalKey,countsField,1);

local len=redis.call('LLEN',latestRecordKey);
redis.call('LPUSH',latestRecordKey,latestRecord);
if len>=tonumber(latestRecordViewSize) then
	redis.call('RPOP',latestRecordKey);
end
return 1;