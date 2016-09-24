--筹款统计和更新轮广告数据
--KEYS : getRedisStatisticalKey,getRedisLatestRecordkKey
--ARGV :{record:[{广告数据}],statistical:{counts:显示记录数,totalAmount:总数}}

local statisticalKey=KEYS[1];
local latestRecordKey=KEYS[2];
local json=cjson.decode(ARGV[1]);
local list=json.record;
local totalAmountField='totalAmount';
local totalAmount=json.statistical.totalAmount;
local countsField='counts';
local counts=json.statistical.counts;

redis.call('del',latestRecordKey);
if table.getn(list)>0 then
	for i=1,#list,1 do
		local item =string.gsub(cjson.encode(list[i]),'\/','\\');
		redis.call('RPUSH',latestRecordKey,item);
	end
else
	redis.call('RPUSH',latestRecordKey,'');
end

redis.call('del',statisticalKey);
redis.call('HINCRBYFLOAT',statisticalKey,totalAmountField,totalAmount);
redis.call('HINCRBY',statisticalKey,countsField,counts);

return 1;