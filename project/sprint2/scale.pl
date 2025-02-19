%====================================================================================
% scale description   
%====================================================================================
dispatch( load_weight, load_weight(WEIGHT) ).
dispatch( unload_weight, unload_weight(WEIGHT) ).
dispatch( update_scale_count, update_scale_count(COUNT) ).
event( scale_data, scale_data(WEIGHT) ).
%====================================================================================
context(ctxscale, "localhost",  "TCP", "8130").
context(ctxwis24, "localhost",  "TCP", "8121").
 qactor( wis, ctxwis24, "external").
  qactor( scale, ctxscale, "it.unibo.scale.Scale").
 static(scale).
  qactor( scale_device, ctxwis24, "it.unibo.scale_device.Scale_device").
 static(scale_device).
