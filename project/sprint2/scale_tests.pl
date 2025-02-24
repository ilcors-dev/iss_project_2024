%====================================================================================
% scale_tests description   
%====================================================================================
dispatch( load_weight, load_weight(WEIGHT) ).
dispatch( unload_weight, unload_weight(WEIGHT) ).
dispatch( update_scale_count, update_scale_count(COUNT) ).
event( scale_data, scale_data(WEIGHT) ).
event( mqtt_info, mqtt_info(MSG) ).
%====================================================================================
context(ctxscale_tests, "localhost",  "TCP", "8123").
 qactor( scale, ctxscale_tests, "it.unibo.scale.Scale").
 static(scale).
  qactor( scale_device, ctxscale_tests, "it.unibo.scale_device.Scale_device").
 static(scale_device).
